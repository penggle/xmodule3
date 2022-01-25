package com.penglecode.xmodule.common.codegen;

import com.penglecode.xmodule.BasePackage;
import com.penglecode.xmodule.common.codegen.config.DomainObjectConfig;
import com.penglecode.xmodule.common.codegen.config.GenerableTargetConfig;
import com.penglecode.xmodule.common.codegen.config.ModuleCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.consts.CodegenConstants;
import com.penglecode.xmodule.common.codegen.exception.CodegenRuntimeException;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.codegen.support.CodegenFilter;
import com.penglecode.xmodule.common.codegen.support.CodegenModule;
import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;
import com.penglecode.xmodule.common.codegen.util.CodegenUtils;
import com.penglecode.xmodule.common.util.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.NamedThreadLocal;
import org.springframework.util.Assert;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;
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
		CodegenModule codeModule = getCodeModule();
		LOGGER.info("【{}】>>> 生成{}开始...", module, codeModule.getDesc());
		try {
			executeGenerate();
		} catch (Exception e) {
			LOGGER.error(String.format("【%s】>>> 生成%s代码出错：%s", module, codeModule.getDesc(), e.getMessage()), e);
		}
		LOGGER.info("【{}】<<< 生成{}结束...", module, codeModule.getDesc());
	}

	/**
	 * 执行代码生成的模板方法，留给子类去实现，
	 * 在其实现逻辑一般会调用方法
	 * @throws Exception
	 */
	protected abstract void executeGenerate() throws Exception;

	/**
	 * 返回代码层次模块
	 * @return
	 */
	protected abstract CodegenModule getCodeModule();

	/**
	 * 生成目标代码
	 */
	@SuppressWarnings("unchecked")
	protected <T extends GenerableTargetConfig, D extends DomainObjectConfig> void generateTarget(CodegenContext<C,T,D> codegenContext) throws Exception {
		if(codegenFilter == null || codegenFilter.filter(codegenContext)) { //如果未设代码生成过滤条件
			try {
				String generatedCodeFilePath = executeGenerateTarget(codegenContext);
				LOGGER.info("【{}】>>> 生成代码[{}]成功! (输出文件: {})", module, templateParameter.getTargetFileName(), generatedCodeFilePath);
			} catch (Exception e) {
				LOGGER.error("【{}】>>> 生成代码[{}]失败! (异常信息: {})", module, templateParameter.getTargetFileName(), e.getMessage());
				throw e;
			}
		}
	}

	/**
	 * 执行目标代码生成
	 * @param codegenConfig
	 * @param targetConfig
	 * @param templateParameter
	 * @return
	 * @throws Exception
	 */
	protected <T extends GenerableTargetConfig> String executeGenerateTarget(C codegenConfig, DomainBoundedTargetConfigProperties<T> targetConfig, TemplateParameter templateParameter) throws Exception {
		String targetProject = StringUtils.defaultIfBlank(targetConfig.getGeneratedTargetConfig().getTargetProject(), codegenConfig.getDomain().getDomainCommons().getTargetProject());
		String targetPackage = StringUtils.defaultIfBlank(targetConfig.getGeneratedTargetConfig().getTargetPackage(), codegenConfig.getDomain().getDomainCommons().getTargetPackage());
		String targetCodeFilePath = getTargetPackageDir(targetProject, targetPackage);
		String targetCodeFileName = CodegenUtils.calculateGeneratedCodeFileName(templateParameter.getTargetFileName(), new File(targetCodeFilePath));
		targetCodeFilePath = FileUtils.normalizePath(targetCodeFilePath + FileUtils.STANDARD_PATH_DELIMITER + targetCodeFileName);
		Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		Class<?> resourceLoadClass = getClass();
		configuration.setClassForTemplateLoading(resourceLoadClass, "/" + resourceLoadClass.getPackage().getName().replace(".", "/"));
		Template codeTemplate = configuration.getTemplate(templateParameter.getTemplateFileName());
		FileUtils.mkDirIfNecessary(targetCodeFilePath);
		try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(targetCodeFilePath))))) {
			codeTemplate.process(templateParameter, out);
		}
		return targetCodeFilePath;
	}

	/**
	 * 添加模板公共参数
	 * @param templateParameter
	 * @param codegenConfig
	 * @param targetConfig
	 * @param templateFileName
	 * @param <T>
	 */
	protected <T extends GenerableTargetConfig> void addCommonTemplateParameter(TemplateParameter templateParameter, ModuleCodegenConfigProperties codegenConfig, DomainBoundedTargetConfigProperties<T> targetConfig, String templateFileName) {
		templateParameter.setTemplateFileName(templateFileName);
		templateParameter.setTargetFileName(targetConfig.getGeneratedTargetConfig().getGeneratedTargetName(targetConfig, false, true));
		templateParameter.setTargetObjectName(targetConfig.getGeneratedTargetConfig().getGeneratedTargetName(targetConfig, false, false));
		templateParameter.setTargetPackage(targetConfig.getGeneratedTargetConfig().getTargetPackage());
		templateParameter.setCommentAuthor(codegenConfig.getDomain().getDomainCommons().getCommentAuthor());
		templateParameter.setGenerateTime(DateTimeUtils.formatNow("yyyy'年'MM'月'dd'日' a HH:mm"));
		if(targetConfig.getDomainObjectConfig() != null) {
			templateParameter.setDomainObjectTitle(targetConfig.getDomainObjectConfig().getDomainObjectTitle());
			templateParameter.setDomainObjectName(targetConfig.getDomainObjectConfig().getDomainObjectName());
			templateParameter.setDomainObjectAlias(targetConfig.getDomainObjectConfig().getDomainObjectAlias());
		}
	}

	/**
	 * 根据给定的targetProject和targetPackage计算目标Package的具体目录
	 * @param targetProject
	 * @param targetPackage
	 * @return
	 */
	protected String getTargetPackageDir(String targetProject, String targetPackage) {
		String codegenRuntimeProjectDir = getClass().getResource("/").getPath();
		codegenRuntimeProjectDir = codegenRuntimeProjectDir.substring(1, codegenRuntimeProjectDir.indexOf("/target/"));
		targetProject = Paths.get(codegenRuntimeProjectDir, targetProject).toAbsolutePath().toString();
		return FileUtils.normalizePath(targetProject + "/" + targetPackage.replace(".", "/"));
	}

	/**
	 * 计算所生成的类需要导入的import类列
	 * @param allImportedTypes
	 * @param templateParameter
	 */
	protected void calculateImportedTypes(Set<FullyQualifiedJavaType> allImportedTypes, Map<String, Object> templateParameter) {
		List<String> jdkImports = new ArrayList<>(); //JDK包中的import类
		List<String> thirdImports = new ArrayList<>(); //第三方jar包中的import类
		List<String> projectImports = new ArrayList<>(); //具有共同BasePackage的import类
		if(!CollectionUtils.isEmpty(allImportedTypes)) {
			allImportedTypes.stream().flatMap(importedType -> importedType.getImportList().stream()).forEach(importedType -> {
				if(importedType.startsWith("java.")) {
					jdkImports.add(importedType);
				} else if(importedType.startsWith(BasePackage.class.getPackage().getName())) {
					projectImports.add(importedType);
				} else {
					thirdImports.add(importedType);
				}
			});
		}
		Collections.sort(jdkImports);
		Collections.sort(thirdImports);
		Collections.sort(projectImports);
		templateParameter.put("jdkImports", jdkImports);
		templateParameter.put("thirdImports", thirdImports);
		templateParameter.put("projectImports", projectImports);
	}

}
