package com.penglecode.xmodule.common.codegen.config;

/**
 * 可自动生成的目标
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/22 11:16
 */
public abstract class GenerableTarget {

    /** 默认代码src目录：当前项目下的src/main/java */
    public static final String DEFAULT_TARGET_PROJECT = "src/main/java";

    /** 代码输出的项目位置 */
    private String targetProject;

    /** 代码输出的包路径 */
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

}
