package com.penglecode.xmodule.common.codegen;

import com.penglecode.xmodule.BasePackage;
import com.penglecode.xmodule.common.codegen.config.*;
import com.penglecode.xmodule.common.codegen.exception.CodegenRuntimeException;
import com.penglecode.xmodule.common.codegen.support.*;
import com.penglecode.xmodule.common.codegen.util.CodegenUtils;
import com.penglecode.xmodule.common.util.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.NamedThreadLocal;
import org.springframework.util.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;

/**
 * 模块化的代码生成器基类
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/7/25 15:18
 */
@NotThreadSafe
public abstract class ModuleCodeGenerator<C extends ModuleCodegenConfigProperties> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ModuleCodeGenerator.class);

	/** 生成代码所在项目的项目根目录 */
	private final String codegenRuntimeProjectDir;

	/**
	 * 业务模块名或者开发者名字,以此解决分工协作问题，
	 * 需要在application.yaml中配置spring.codegen.config.{module}.*
	 */
	private final String bizModule;

	/**
	 * 代码生成配置类
	 */
	private final Class<C> moduleCodegenConfigClass;

	/**
	 * 最新生成的代码文件是否覆盖原来代码? (为防代码覆盖丢失，此处直接一棍子打死：永不覆盖！！！)
	 */
	private final boolean overwrite;

	/**
	 * 分模块的代码生成配置
	 */
	private final C moduleCodegenConfig;

	/**
	 * 当前代码生成动态过滤条件ThreadLocal
	 */
	private final NamedThreadLocal<CodegenFilter<C,? extends GeneratedTargetConfigProperties>> currentCodegenFilter;

	protected ModuleCodeGenerator(String bizModule) {
		super();
		Assert.hasText(bizModule, "业务模块名'bizModule'不能为空!");
		this.codegenRuntimeProjectDir = calculateCodegenRuntimeProjectDir();
		this.bizModule = bizModule;
		this.overwrite = false;
		this.moduleCodegenConfigClass = ClassUtils.getSuperClassGenericType(getClass(), ModuleCodeGenerator.class, 0);
		this.moduleCodegenConfig = loadCodegenConfigProperties(bizModule);
		this.currentCodegenFilter = new NamedThreadLocal<>("当前代码生成动态过滤条件ThreadLocal");
	}


	protected Class<C> getModuleCodegenConfigClass() {
		return moduleCodegenConfigClass;
	}

	/**
	 * 加载指定模块的代码生成配置
	 * @param bizModule
	 * @return
	 */
	protected C loadCodegenConfigProperties(String bizModule) {
		try {
			LOGGER.info("【{}】>>> 从配置文件中加载代码生成配置...", bizModule);
			String moduleConfigPrefix = ModuleCodegenConfigProperties.getModuleCodegenConfigPrefix(bizModule);
			String exampleConfigPath = moduleConfigPrefix + ".domain.domainCommons.runtimeDataSource"; //抽样检查
			Assert.hasText(SpringUtils.getEnvProperty(exampleConfigPath, String.class), String.format("Spring上下文环境中未发现模块[%s]的相关代码生成配置: %s", bizModule, moduleConfigPrefix + ".*"));
			Class<C> codegenConfigPropertiesType = getModuleCodegenConfigClass();
			C codegenConfigProperties = codegenConfigPropertiesType.getConstructor(String.class).newInstance(bizModule);
			BeanDefinition beanDefinition = SpringUtils.createBindableBeanDefinition(codegenConfigProperties, codegenConfigPropertiesType, moduleConfigPrefix);
			beanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
			String beanName = bizModule + codegenConfigPropertiesType.getSimpleName();
			SpringUtils.registerBeanDefinition(beanName, beanDefinition);
			return SpringUtils.getBean(beanName, codegenConfigPropertiesType);
		} catch (Exception e) {
			throw new CodegenRuntimeException(e);
		}
	}

	/**
	 * 覆盖代码生成配置
	 * @param override
	 * @return
	 */
	public ModuleCodeGenerator<C> override(Consumer<C> override) {
		override.accept(moduleCodegenConfig);
		return this;
	}

	public <T extends GeneratedTargetConfigProperties> ModuleCodeGenerator<C> filter(CodegenFilter<C,T> codegenFilter) {
		currentCodegenFilter.set(codegenFilter);
		return this;
	}

	/**
	 * 执行代码生成
	 */
	public final void generate() {
		CodegenModule codeModule = getCodeModule();
		LOGGER.info("【{}】>>> 生成{}开始...", bizModule, codeModule.getDesc());
		try {
			initConfig();
			generateCodes();
		} catch (Exception e) {
			LOGGER.error(String.format("【%s】>>> 生成%s代码出错：%s", bizModule, codeModule.getDesc(), e.getMessage()), e);
		} finally {
			currentCodegenFilter.remove();
		}
		LOGGER.info("【{}】<<< 生成{}结束...", bizModule, codeModule.getDesc());
	}

	/**
	 * 初始化代码生成配置
	 * @throws Exception
	 */
	protected void initConfig() throws Exception {
		moduleCodegenConfig.initConfig();
	}

	/**
	 * 生成代码的模板方法，留给子类去实现，
	 * 在其实现逻辑一般会调用方法: generateCode(ModuleCodegenConfigProperties codegenConfig, T targetConfig, Map<String, Object> templateParameter)
	 * @throws Exception
	 */
	protected abstract void generateCodes() throws Exception;

	/**
	 * 返回代码层次模块
	 * @return
	 */
	protected abstract CodegenModule getCodeModule();

	/**
	 * 生成目标代码
	 * @param codegenConfig
	 * @param targetConfig
	 * @param templateParameter
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected <T extends GeneratedTargetConfigProperties> void generateCode(C codegenConfig, DomainBoundedTargetConfigProperties<T> targetConfig, TemplateParameter templateParameter) throws Exception {
		CodegenFilter<C,T> codegenFilter = (CodegenFilter<C,T>) currentCodegenFilter.get();
		if(codegenFilter == null || codegenFilter.filter(getCodeModule(), codegenConfig, targetConfig)) { //如果未设代码生成过滤条件
			try {
				String generatedCodeFilePath = generateCodeInternal(codegenConfig, targetConfig, templateParameter);
				LOGGER.info("【{}】>>> 生成代码[{}]成功! (输出文件: {})", bizModule, templateParameter.getTargetFileName(), generatedCodeFilePath);
			} catch (Exception e) {
				LOGGER.error("【{}】>>> 生成代码[{}]失败! (异常信息: {})", bizModule, templateParameter.getTargetFileName(), e.getMessage());
				throw e;
			}
		}
	}

	/**
	 * 生成目标代码
	 * @param codegenConfig
	 * @param targetConfig
	 * @param templateParameter
	 * @return
	 * @throws Exception
	 */
	protected <T extends GeneratedTargetConfigProperties> String generateCodeInternal(C codegenConfig, DomainBoundedTargetConfigProperties<T> targetConfig, TemplateParameter templateParameter) throws Exception {
		String targetProject = StringUtils.defaultIfBlank(targetConfig.getGeneratedTargetConfig().getTargetProject(), codegenConfig.getDomain().getDomainCommons().getTargetProject());
		String targetPackage = StringUtils.defaultIfBlank(targetConfig.getGeneratedTargetConfig().getTargetPackage(), codegenConfig.getDomain().getDomainCommons().getTargetPackage());
		String targetCodeFilePath = calculateTargetPackageDir(targetProject, targetPackage);
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
	protected <T extends GeneratedTargetConfigProperties> void addCommonTemplateParameter(TemplateParameter templateParameter, ModuleCodegenConfigProperties codegenConfig, DomainBoundedTargetConfigProperties<T> targetConfig, String templateFileName) {
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
	protected String calculateTargetPackageDir(String targetProject, String targetPackage) {
		/*File domainObjectTargetProjectDir = Paths.get(getCodegenRuntimeProjectDir(), targetProject).toFile();
		targetProject = domainObjectTargetProjectDir.getAbsolutePath();*/
		targetProject = Paths.get(getCodegenRuntimeProjectDir(), targetProject).toAbsolutePath().toString();
		return FileUtils.normalizePath(targetProject + "/" + targetPackage.replace(".", "/"));
	}

	private String calculateCodegenRuntimeProjectDir() {
		String rootDir = getClass().getResource("/").getPath();
		return rootDir.substring(1, rootDir.indexOf("/target/"));
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

	/**
	 * 解析decodeEnumType
	 * @param decodeEnumType
	 * @return
	 */
	protected DomainEnumTypeInfo resolveDecodeEnumTypeInfo(String decodeEnumType) {
		Set<DomainEnumConfigProperties> domainEnums = getModuleCodegenConfig().getDomain().getDomainCommons().getDomainEnums();
		for(DomainEnumConfigProperties domainEnum : domainEnums) { //1、优先从当前领域枚举中搜索
			String fullDomainEnumType = getFullDomainEnumType(domainEnum.getEnumName());
			String shortDomainEnumType = getShortDomainEnumType(domainEnum.getEnumName());
			if((decodeEnumType.contains(".") && decodeEnumType.equals(fullDomainEnumType)) ||
					(!decodeEnumType.contains(".") && decodeEnumType.equals(shortDomainEnumType))) {
				return new DomainEnumTypeInfo(new FullyQualifiedJavaType(fullDomainEnumType), domainEnum.getCodeField().getLeft(), domainEnum.getCodeField().getRight(), domainEnum.getNameField().getLeft(), domainEnum.getNameField().getRight());
			}
		}
		Map<String,String> globalTypes = getModuleCodegenConfig().getDomain().getDomainCommons().getGlobalTypes();
		try {
			for (Map.Entry<String, String> entry : globalTypes.entrySet()) { //2、其次从已注册的枚举中搜索
				String shortDomainEnumType = entry.getKey();
				String fullDomainEnumType = entry.getValue();
				if ((decodeEnumType.contains(".") && decodeEnumType.equals(fullDomainEnumType)) ||
						(!decodeEnumType.contains(".") && decodeEnumType.equals(shortDomainEnumType))) {
					ImmutablePair<Field,Field> decodeEnumFields = parseDecodeEnumFields(fullDomainEnumType);
					Field codeField = decodeEnumFields.getLeft();
					Field nameField = decodeEnumFields.getRight();
					if(codeField != null && nameField != null) {
						return new DomainEnumTypeInfo(new FullyQualifiedJavaType(fullDomainEnumType), codeField.getName(), new FullyQualifiedJavaType(codeField.getType().getName()), nameField.getName(), new FullyQualifiedJavaType(nameField.getType().getName()));
					}
				}
			}
		} catch (Exception e) {
			//ignore, try my best
		}
		return null;
	}

	private ImmutablePair<Field,Field> parseDecodeEnumFields(String fullDomainEnumType) {
		Class<?> domainEnumTypeClass = ClassUtils.resolveClassName(fullDomainEnumType, ClassUtils.getDefaultClassLoader());
		Set<Field> enumFields = ReflectionUtils.getAllFields(domainEnumTypeClass);
		Field codeField = null;
		Field nameField = null;
		Map<String,Field> prefixCodeFields = new HashMap<>();
		Map<String,Field> prefixNameFields = new HashMap<>();
		Set<String> prefixes = new HashSet<>();
		for(Field enumField : enumFields) {
			String fieldName = enumField.getName();
			if(fieldName.endsWith("Code")) {
				String prefix = fieldName.substring(0, fieldName.lastIndexOf("Code"));
				prefixCodeFields.put(prefix, enumField);
				prefixes.add(prefix);
			}
			if(fieldName.endsWith("Name")) {
				String prefix = fieldName.substring(0, fieldName.lastIndexOf("Name"));
				prefixNameFields.put(prefix, enumField);
				prefixes.add(prefix);
			}
		}
		for(String prefix : prefixes) {
			if(prefixCodeFields.containsKey(prefix) && prefixNameFields.containsKey(prefix)) {
				codeField = prefixCodeFields.get(prefix);
				nameField = prefixNameFields.get(prefix);
			}
		}
		return new ImmutablePair<>(codeField, nameField);
	}

	protected String getFullDomainEnumType(String domainEnumType) {
		if(!domainEnumType.contains(".")) {
			return getModuleCodegenConfig().getDomain().getDomainCommons().getTargetPackage() + ".enums." + domainEnumType;
		}
		return domainEnumType;
	}

	protected String getShortDomainEnumType(String domainEnumType) {
		if(domainEnumType.contains(".")) {
			return domainEnumType.substring(domainEnumType.lastIndexOf('.') + 1);
		}
		return domainEnumType;
	}

	protected DomainObjectColumnConfigProperties getDefaultCreateTimeColumn(ServiceCodegenConfigProperties codegenConfig, DomainObjectConfigProperties domainObjectConfig) {
		DomainObjectColumnConfigProperties createTimeColumn = domainObjectConfig.getDomainObjectColumns().get(codegenConfig.getDomain().getDomainCommons().getDefaultCreateTimeColumn());
		if(createTimeColumn != null && FullyQualifiedJavaType.getStringInstance().equals(createTimeColumn.getIntrospectedColumn().getJavaFieldType())) {
			return createTimeColumn;
		}
		return null;
	}

	protected DomainObjectColumnConfigProperties getDefaultUpdateTimeColumn(ServiceCodegenConfigProperties codegenConfig, DomainObjectConfigProperties domainObjectConfig) {
		DomainObjectColumnConfigProperties updateTimeColumn = domainObjectConfig.getDomainObjectColumns().get(codegenConfig.getDomain().getDomainCommons().getDefaultUpdateTimeColumn());
		if(updateTimeColumn != null && FullyQualifiedJavaType.getStringInstance().equals(updateTimeColumn.getIntrospectedColumn().getJavaFieldType())) {
			return updateTimeColumn;
		}
		return null;
	}

	public String getBizModule() {
		return bizModule;
	}

	public boolean isOverwrite() {
		return overwrite;
	}

	protected C getModuleCodegenConfig() {
		return moduleCodegenConfig;
	}

	protected String getCodegenRuntimeProjectDir() {
		return codegenRuntimeProjectDir;
	}
}
