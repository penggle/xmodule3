package com.penglecode.xmodule.common.codegen.config;

/**
 * 自动生成的目标代码配置
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/6/7 22:24
 */
public abstract class GeneratedTargetConfigProperties {

	/**
	 * 默认代码src目录：当前项目下的src/main/java
	 */
	public static final String DEFAULT_TARGET_PROJECT = "src/main/java";

	/**
	 * 代码输出的项目位置
	 */
	private String targetProject;

	/**
	 * 代码输出的包路径
	 */
	private String targetPackage;

	public String getTargetProject() {
		return targetProject;
	}

	public void setTargetProject(String targetProject) {
		this.targetProject = targetProject;
	}

	public String getTargetPackage() {
		return targetPackage;
	}

	public void setTargetPackage(String targetPackage) {
		this.targetPackage = targetPackage;
	}

	/**
	 * 获取与指定领域对象相关的生成目标名称
	 * @param targetConfig
	 * @param includePackage
	 * @param includeSuffix
	 * @return
	 */
	public abstract <T extends GeneratedTargetConfigProperties> String getGeneratedTargetName(DomainBoundedTargetConfigProperties<T> targetConfig, boolean includePackage, boolean includeSuffix);

}
