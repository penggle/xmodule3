package com.penglecode.xmodule.common.codegen.support;

/**
 * 自动生成代码的传统三层架构模块
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/7/17 22:24
 */
public enum CodegenModule {

	DOMAIN("Domain", "Domain领域对象代码"), MYBATIS("Mybatis", "Mybatis代码"), SERVICE("Service", "Service代码"), CONTROLLER("Api", "Api接口代码");
	
	private final String name;

	private final String desc;

	CodegenModule(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

}
