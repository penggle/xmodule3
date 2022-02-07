package com.penglecode.xmodule.common.codegen;

import com.penglecode.xmodule.common.codegen.config.DomainObjectConfig;
import com.penglecode.xmodule.common.codegen.config.GenerableTargetConfig;
import com.penglecode.xmodule.common.codegen.config.ModuleCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.consts.CodegenConstants;
import com.penglecode.xmodule.common.codegen.exception.CodegenRuntimeException;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.codegen.support.CodegenFilter;
import com.penglecode.xmodule.common.codegen.support.CodegenParameter;
import com.penglecode.xmodule.common.codegen.util.CodegenUtils;
import com.penglecode.xmodule.common.util.ClassUtils;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.FileUtils;
import com.penglecode.xmodule.common.util.SpringUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.util.Assert;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.*;
import java.nio.file.Paths;
import java.util.function.Consumer;

/**
 * 模块化的代码生成器基类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/25 15:18
 */
@NotThreadSafe
public abstract class ModuleCodeGenerator<C extends ModuleCodegenConfigProperties> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ModuleCodeGenerator.class);

	/**
	 * 业务/功能模块名称
	 * 需要在application.yaml中配置spring.codegen.config.{module}.*
	 */
	private final String module;

	/**
	 * 分模块的代码生成配置
	 */
	private final C codegenConfig;

	/**
	 * 代码生成过滤器
	 */
	private CodegenFilter codegenFilter;

	protected ModuleCodeGenerator(String module) {
		super();
		Assert.hasText(module, "业务模块名'module'不能为空!");
		this.module = module;
		this.codegenConfig = loadCodegenConfig();
	}

	/**
	 * 加载指定模块的代码生成配置
	 * @return
	 */
	protected C loadCodegenConfig() {
		try {
			LOGGER.info("【{}】>>> 从配置文件中加载代码生成配置...", module);
			String moduleConfigPrefix = CodegenConstants.MODULE_CODEGEN_CONFIGURATION_PREFIX + module;
			String exampleConfigPath = moduleConfigPrefix + ".domain.domainCommons.runtimeDataSource"; //抽样检查
			Assert.hasText(SpringUtils.getEnvProperty(exampleConfigPath, String.class), String.format("Spring上下文环境中未发现模块[%s]的相关代码生成配置: %s", module, moduleConfigPrefix + ".*"));
			Class<C> codegenConfigPropertiesType = ClassUtils.getSuperClassGenericType(getClass(), ModuleCodeGenerator.class, 0);
			C codegenConfigProperties = codegenConfigPropertiesType.getConstructor(String.class).newInstance(module);
			BeanDefinition beanDefinition = SpringUtils.createBindableBeanDefinition(codegenConfigProperties, codegenConfigPropertiesType, moduleConfigPrefix);
			beanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
			String beanName = module + codegenConfigPropertiesType.getSimpleName();
			SpringUtils.registerBeanDefinition(beanName, beanDefinition);
			return SpringUtils.getBean(beanName, codegenConfigPropertiesType);
		} catch (Exception e) {
			throw new CodegenRuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 覆盖代码生成配置
	 * @param override
	 * @return
	 */
	public ModuleCodeGenerator<C> override(Consumer<C> override) {
		override.accept(codegenConfig);
		return this;
	}

	public <T extends GenerableTargetConfig> ModuleCodeGenerator<C> filter(CodegenFilter codegenFilter) {
		this.codegenFilter = codegenFilter;
		return this;
	}

	/**
	 * 执行代码生成
	 */
	public final void generate() {
		LOGGER.info("【{}】>>> 生成{}开始...", module, getCodeName());
		try {
			executeGenerate();
		} catch (Exception e) {
			LOGGER.error(String.format("【%s】>>> 生成%s代码出错：%s", module, getCodeName(), e.getMessage()), e);
		}
		LOGGER.info("【{}】<<< 生成{}结束...", module, getCodeName());
	}

	/**
	 * 返回生成的代码模块名
	 * @return
	 */
	protected abstract String getCodeName();

	/**
	 * 执行代码生成的模板方法，留给子类去实现，
	 * 在其实现逻辑一般会调用方法generateTarget()
	 * @throws Exception
	 */
	protected abstract void executeGenerate() throws Exception;

	/**
	 * 生成目标代码
	 *
	 * @param codegenContext		- 代码生成上下文
	 * @param codegenParameter		- 代码生成参数
	 * @param <T> 当前生成目标配置
	 * @param <D> 当前生成目标绑定的领域对象
	 * @throws Exception
	 */
	protected <T extends GenerableTargetConfig, D extends DomainObjectConfig> void generateTarget(CodegenContext<C,T,D> codegenContext, CodegenParameter codegenParameter) throws Exception {
		if(codegenFilter == null || codegenFilter.filter(codegenContext)) { //如果未设代码生成过滤条件 || 通过过滤条件测试
			try {
				String targetFilePath = executeGenerateTarget(codegenContext, codegenParameter);
				LOGGER.info("【{}】>>> 生成代码[{}]成功! (输出文件: {})", module, codegenParameter.getTargetFileName(), targetFilePath);
			} catch (Exception e) {
				LOGGER.error("【{}】>>> 生成代码[{}]失败! (异常信息: {})", module, codegenParameter.getTargetFileName(), e.getMessage());
				throw e;
			}
		}
	}

	/**
	 * 执行目标代码生成
	 *
	 * @param codegenContext		- 代码生成上下文
	 * @param codegenParameter		- 代码生成参数
	 * @param <T> 当前生成目标配置
	 * @param <D> 当前生成目标绑定的领域对象
	 * @return 返回生成的目标代码文件路径
	 * @throws Exception
	 */
	protected <T extends GenerableTargetConfig, D extends DomainObjectConfig> String executeGenerateTarget(CodegenContext<C,T,D> codegenContext, CodegenParameter codegenParameter) throws Exception {
		String targetProject = codegenContext.getTargetConfig().getTargetProject();
		String targetPackage = codegenContext.getTargetConfig().getTargetPackage();
		String targetFilePath = getTargetPackageDir(targetProject, targetPackage);
		String targetCodeFileName = CodegenUtils.calculateGeneratedCodeFileName(codegenParameter.getTargetFileName(), new File(targetFilePath));
		targetFilePath = FileUtils.normalizePath(targetFilePath + FileUtils.STANDARD_PATH_DELIMITER + targetCodeFileName);
		Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		Class<?> resourceLoadClass = getClass();
		configuration.setClassForTemplateLoading(resourceLoadClass, "/" + resourceLoadClass.getPackage().getName().replace(".", "/"));
		Template codeTemplate = configuration.getTemplate(codegenParameter.getTemplateFileName());
		FileUtils.mkDirIfNecessary(targetFilePath);
		try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFilePath)))) {
			codegenParameter.calculateTargetImports(codegenContext.getTargetConfig().getTargetPackage()); //计算import列表
			codeTemplate.process(codegenParameter, out); //最后一步：生成代码
		}
		return targetFilePath;
	}

	/**
	 * 创建代码生成参数
	 *
	 * @param codegenContext		- 代码生成上下文
	 * @param templateFileName		- 模板文件名
	 * @param <T> 当前生成目标配置
	 * @param <D> 当前生成目标绑定的领域对象
	 * @return 返回代码生成参数
	 */
	protected <T extends GenerableTargetConfig, D extends DomainObjectConfig> CodegenParameter createCodegenParameter(CodegenContext<C,T,D> codegenContext, String templateFileName) {
		String domainObjectName = codegenContext.getDomainObjectConfig().getDomainObjectName();
		CodegenParameter codegenParameter = new CodegenParameter();
		codegenParameter.setTemplateFileName(templateFileName);
		codegenParameter.setTargetFileName(codegenContext.getTargetConfig().getGeneratedTargetName(domainObjectName, false, true));
		codegenParameter.setTargetPackage(codegenContext.getTargetConfig().getTargetPackage());
		codegenParameter.setTargetClass(codegenContext.getTargetConfig().getGeneratedTargetName(domainObjectName, false, false));
		codegenParameter.setTargetAuthor(codegenContext.getCodegenConfig().getDomain().getDomainCommons().getCommentAuthor());
		codegenParameter.setTargetVersion("1.0.0");
		codegenParameter.setTargetCreated(DateTimeUtils.formatNow("yyyy'年'MM'月'dd'日' a HH:mm"));
		return codegenParameter;
	}

	/**
	 * 根据给定的targetProject和targetPackage计算目标Package的具体目录
	 * @param targetProject		- 目标工程
	 * @param targetPackage		- 目标包名
	 * @return 源码包的具体目录路径
	 */
	protected String getTargetPackageDir(String targetProject, String targetPackage) {
		String codegenRuntimeProjectDir = getClass().getResource("/").getPath();
		codegenRuntimeProjectDir = codegenRuntimeProjectDir.substring(1, codegenRuntimeProjectDir.indexOf("/target/"));
		targetProject = Paths.get(codegenRuntimeProjectDir, targetProject).toAbsolutePath().toString();
		return FileUtils.normalizePath(targetProject + "/" + targetPackage.replace(".", "/"));
	}

	protected C getCodegenConfig() {
		return codegenConfig;
	}

}
