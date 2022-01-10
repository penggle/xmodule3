package com.penglecode.xmodule.common.enums;

/**
 * 常用JVM垃圾回收器类型枚举
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/4/10 17:34
 */
public enum GarbageCollectorType {

    COPY("Oracle JDK", "Copy", "Young", "-XX:+UseSerialGC"),
    MARK_SWEEP_COMPACT("Oracle JDK", "MarkSweepCompact", "Old", "-XX:+UseSerialGC"),
    CONCURRENT_MARK_SWEEP("Oracle JDK", "ConcurrentMarkSweep", "Old", "-XX:+UseConcMarkSweepGC"),
    PAR_NEW("Oracle JDK", "ParNew", "Young", "-XX:+UseParNewGC"),
    PS_SCAVENGE("Oracle JDK", "PS Scavenge", "Young", "-XX:+UseParallelGC"),
    PS_MARK_SWEEP("Oracle JDK", "PS MarkSweep", "Old", "-XX:+UseParallelGC"),
    G1_YOUNG_GENERATION("Oracle JDK", "G1 Young Generation", "Young", "-XX:+UseG1GC"),
    G1_OLD_GENERATION("Oracle JDK", "G1 Old Generation", "Old", "-XX:+UseG1GC");

    private final String jvmVendor;

    private final String jmxName;

    private final String jvmGen;

    private final String jvmArgs;

    GarbageCollectorType(String jvmVendor, String jmxName, String jvmGen, String jvmArgs) {
        this.jvmVendor = jvmVendor;
        this.jmxName = jmxName;
        this.jvmGen = jvmGen;
        this.jvmArgs = jvmArgs;
    }

    public String getJvmVendor() {
        return jvmVendor;
    }

    public String getJmxName() {
        return jmxName;
    }

    public String getJvmGen() {
        return jvmGen;
    }

    public String getJvmArgs() {
        return jvmArgs;
    }

    public static GarbageCollectorType of(String jmxName) {
        for(GarbageCollectorType gcType : values()) {
            if(gcType.getJmxName().equals(jmxName)) {
                return gcType;
            }
        }
        return null;
    }

}
