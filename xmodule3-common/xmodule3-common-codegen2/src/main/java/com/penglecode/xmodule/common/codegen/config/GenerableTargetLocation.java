package com.penglecode.xmodule.common.codegen.config;

/**
 * 能自动生成的目标位置
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/1/22 11:16
 */
public class GenerableTargetLocation {

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
