package com.penglecode.xmodule.common.support;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 应用元信息
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/4/13 22:29
 */
@Schema(name="应用元信息")
public class AppMetaInfo {

    /** 应用代码 */
    @Schema(description="应用代码")
    private String appCode;

    /** 应用名称 */
    @Schema(description="应用名称")
    private String appName;

    /** JVM名称 */
    @Schema(description="JVM名称")
    private String jvmName;

    /** JVM进程ID */
    @Schema(description="JVM进程ID")
    private String jvmProcessId;

    /** JVM运行时间 */
    @Schema(description="JVM运行时间")
    private Long jvmUptime;

    /** JVM启动时间 */
    @Schema(description="JVM启动时间")
    private Long jvmStartTime;

    /** JAVA版本 */
    @Schema(description="JAVA版本")
    private String javaVersion;

    /** 操作系统名称 */
    @Schema(description="操作系统名称")
    private String osName;

    /** 操作系统架构 */
    @Schema(description="操作系统架构")
    private String osArch;

    /** 操作系统版本 */
    @Schema(description="操作系统版本")
    private String osVersion;

    /** 操作系统可用CPU核数 */
    @Schema(description="操作系统可用CPU核数")
    private Integer osCores;

    /** 初始化堆内存(单位字节) */
    @Schema(description="初始化堆内存(单位字节)")
    private Long jvmHeapInit;

    /** 已使用堆内存(单位字节) */
    @Schema(description="已使用堆内存(单位字节)")
    private Long jvmHeapUsed;

    /** 可使用堆内存(单位字节) */
    @Schema(description="可使用堆内存(单位字节)")
    private Long jvmHeapCommitted;

    /** 最大堆内存(单位字节) */
    @Schema(description="最大堆内存(单位字节)")
    private Long jvmHeapMax;

    /** 初始化非堆内存(单位字节) */
    @Schema(description="初始化非堆内存(单位字节)")
    private Long jvmNonHeapInit;

    /** 已使用非堆内存(单位字节) */
    @Schema(description="已使用非堆内存(单位字节)")
    private Long jvmNonHeapUsed;

    /** 可使用非堆内存(单位字节) */
    @Schema(description="可使用非堆内存(单位字节)")
    private Long jvmNonHeapCommitted;

    /** 最大非堆内存(单位字节) */
    @Schema(description="最大非堆内存(单位字节)")
    private Long jvmNonHeapMax;

    /** JVM当前使用的GC */
    @Schema(description="JVM当前使用的GC")
    private String jvmGcArguments;

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getJvmName() {
        return jvmName;
    }

    public void setJvmName(String jvmName) {
        this.jvmName = jvmName;
    }

    public String getJvmProcessId() {
        return jvmProcessId;
    }

    public void setJvmProcessId(String jvmProcessId) {
        this.jvmProcessId = jvmProcessId;
    }

    public Long getJvmUptime() {
        return jvmUptime;
    }

    public void setJvmUptime(Long jvmUptime) {
        this.jvmUptime = jvmUptime;
    }

    public Long getJvmStartTime() {
        return jvmStartTime;
    }

    public void setJvmStartTime(Long jvmStartTime) {
        this.jvmStartTime = jvmStartTime;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsArch() {
        return osArch;
    }

    public void setOsArch(String osArch) {
        this.osArch = osArch;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public Integer getOsCores() {
        return osCores;
    }

    public void setOsCores(Integer osCores) {
        this.osCores = osCores;
    }

    public Long getJvmHeapInit() {
        return jvmHeapInit;
    }

    public void setJvmHeapInit(Long jvmHeapInit) {
        this.jvmHeapInit = jvmHeapInit;
    }

    public Long getJvmHeapUsed() {
        return jvmHeapUsed;
    }

    public void setJvmHeapUsed(Long jvmHeapUsed) {
        this.jvmHeapUsed = jvmHeapUsed;
    }

    public Long getJvmHeapCommitted() {
        return jvmHeapCommitted;
    }

    public void setJvmHeapCommitted(Long jvmHeapCommitted) {
        this.jvmHeapCommitted = jvmHeapCommitted;
    }

    public Long getJvmHeapMax() {
        return jvmHeapMax;
    }

    public void setJvmHeapMax(Long jvmHeapMax) {
        this.jvmHeapMax = jvmHeapMax;
    }

    public Long getJvmNonHeapInit() {
        return jvmNonHeapInit;
    }

    public void setJvmNonHeapInit(Long jvmNonHeapInit) {
        this.jvmNonHeapInit = jvmNonHeapInit;
    }

    public Long getJvmNonHeapUsed() {
        return jvmNonHeapUsed;
    }

    public void setJvmNonHeapUsed(Long jvmNonHeapUsed) {
        this.jvmNonHeapUsed = jvmNonHeapUsed;
    }

    public Long getJvmNonHeapCommitted() {
        return jvmNonHeapCommitted;
    }

    public void setJvmNonHeapCommitted(Long jvmNonHeapCommitted) {
        this.jvmNonHeapCommitted = jvmNonHeapCommitted;
    }

    public Long getJvmNonHeapMax() {
        return jvmNonHeapMax;
    }

    public void setJvmNonHeapMax(Long jvmNonHeapMax) {
        this.jvmNonHeapMax = jvmNonHeapMax;
    }

    public String getJvmGcArguments() {
        return jvmGcArguments;
    }

    public void setJvmGcArguments(String jvmGcArguments) {
        this.jvmGcArguments = jvmGcArguments;
    }

    @Override
    public String toString() {
        return "AppMetaInfo{" +
                "appCode='" + appCode + '\'' +
                ", appName='" + appName + '\'' +
                ", jvmName='" + jvmName + '\'' +
                ", jvmProcessId='" + jvmProcessId + '\'' +
                ", jvmUptime=" + jvmUptime +
                ", jvmStartTime=" + jvmStartTime +
                ", javaVersion='" + javaVersion + '\'' +
                ", osName='" + osName + '\'' +
                ", osArch='" + osArch + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", osCores='" + osCores + '\'' +
                ", jvmHeapInit=" + jvmHeapInit +
                ", jvmHeapUsed=" + jvmHeapUsed +
                ", jvmHeapCommitted=" + jvmHeapCommitted +
                ", jvmHeapMax=" + jvmHeapMax +
                ", jvmNonHeapInit=" + jvmNonHeapInit +
                ", jvmNonHeapUsed=" + jvmNonHeapUsed +
                ", jvmNonHeapCommitted=" + jvmNonHeapCommitted +
                ", jvmNonHeapMax=" + jvmNonHeapMax +
                ", jvmGcArguments='" + jvmGcArguments + '\'' +
                '}';
    }
}