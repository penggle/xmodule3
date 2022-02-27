package com.penglecode.xmodule.common.support;

import com.penglecode.xmodule.common.enums.GarbageCollectorType;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.lang.management.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 应用元信息服务
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/4/13 22:29
 */
@Service("appMetaService")
public class AppMetaService implements EnvironmentAware {

    private Environment environment;

    public AppMetaInfo getAppMetaInfo() {
        //获取JVM的启动时间，版本及名称，当前进程ID，环境变量等
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        //操作系统及硬件信息：系统名称、版本，CPU内核，机器总内存、可用内存、可用内存占比
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        //获取JVM堆内存使用状况
        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        //获取JVM非堆内存使用状况
        MemoryUsage nonHeapMemoryUsage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
        //获取JVM垃圾回收器状况
        List<GarbageCollectorMXBean> gcMxBeans = ManagementFactory.getGarbageCollectorMXBeans();

        AppMetaInfo appMetaInfo = new AppMetaInfo();
        appMetaInfo.setAppCode(getEnvironment().getProperty("spring.application.code"));
        appMetaInfo.setAppName(getEnvironment().getProperty("spring.application.name"));
        appMetaInfo.setJvmName(runtimeMXBean.getVmName() + ", " + runtimeMXBean.getVmVendor());
        appMetaInfo.setJvmProcessId(runtimeMXBean.getName().split("@")[0]);
        appMetaInfo.setJvmUptime(runtimeMXBean.getUptime());
        appMetaInfo.setJvmStartTime(runtimeMXBean.getStartTime());
        appMetaInfo.setJavaVersion(runtimeMXBean.getSpecVersion());
        appMetaInfo.setOsName(operatingSystemMXBean.getName());
        appMetaInfo.setOsArch(operatingSystemMXBean.getArch());
        appMetaInfo.setOsVersion(operatingSystemMXBean.getVersion());
        appMetaInfo.setOsCores(operatingSystemMXBean.getAvailableProcessors());
        appMetaInfo.setJvmHeapInit(heapMemoryUsage.getInit()); //初始化堆内存
        appMetaInfo.setJvmHeapUsed(heapMemoryUsage.getUsed()); //已使用堆内存
        appMetaInfo.setJvmHeapCommitted(heapMemoryUsage.getCommitted()); //可使用堆内存
        appMetaInfo.setJvmHeapMax(heapMemoryUsage.getMax()); //最大堆内存
        appMetaInfo.setJvmNonHeapInit(nonHeapMemoryUsage.getInit()); //初始化非堆内存
        appMetaInfo.setJvmNonHeapUsed(nonHeapMemoryUsage.getUsed()); //已使用非堆内存
        appMetaInfo.setJvmNonHeapCommitted(nonHeapMemoryUsage.getCommitted()); //可使用非堆内存
        appMetaInfo.setJvmNonHeapMax(nonHeapMemoryUsage.getMax()); //最大非堆内存
        String jvmGcArguments = gcMxBeans.stream()
                .map(gcMxBean -> GarbageCollectorType.of(gcMxBean.getName()))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(GarbageCollectorType::getJvmGen).reversed())
                .map(GarbageCollectorType::getJvmArgs)
                .distinct()
                .collect(Collectors.joining(","));
        appMetaInfo.setJvmGcArguments(jvmGcArguments);
        return appMetaInfo;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    protected Environment getEnvironment() {
        return environment;
    }

}
