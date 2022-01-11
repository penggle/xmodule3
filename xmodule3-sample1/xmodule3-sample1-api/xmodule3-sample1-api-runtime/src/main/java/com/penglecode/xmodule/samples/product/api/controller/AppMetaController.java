package com.penglecode.xmodule.samples.product.api.controller;

import com.penglecode.xmodule.common.dto.Result;
import com.penglecode.xmodule.common.enums.GarbageCollectorType;
import com.penglecode.xmodule.common.util.NetUtils;
import com.penglecode.xmodule.common.web.servlet.support.ServletHttpApiSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 应用元信息Controller
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/4/8 21:14
 */
@RestController
@RequestMapping("/api/appmeta")
public class AppMetaController extends ServletHttpApiSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppMetaController.class);

    /**
     * 获取应用元信息，包括JVM信息、操作系统信息、堆内内存、堆外内存、GC等信息
     *
     * @return
     */
    @GetMapping(value="/info", produces=MediaType.APPLICATION_JSON_VALUE)
    public Result<Map<String,Object>> info() {
        Map<String,Object> appMetaInfo = new LinkedHashMap<>();
        appMetaInfo.put("appName", getEnvironment().getProperty("spring.application.name"));
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
        appMetaInfo.put("processId", runtimeMXBean.getName().split("@")[0]);
        appMetaInfo.put("availableProcessors", operatingSystemMXBean.getAvailableProcessors());
        appMetaInfo.put("jvmName", runtimeMXBean.getVmName() + ", " + runtimeMXBean.getVmVendor());
        appMetaInfo.put("jvmUptime", runtimeMXBean.getUptime());
        appMetaInfo.put("jvmStartTime", runtimeMXBean.getStartTime());
        appMetaInfo.put("javaVersion", runtimeMXBean.getSpecVersion());
        appMetaInfo.put("osName", operatingSystemMXBean.getName());
        appMetaInfo.put("osArch", operatingSystemMXBean.getArch());
        appMetaInfo.put("osVersion", operatingSystemMXBean.getVersion());
        appMetaInfo.put("jvmHeapInit", heapMemoryUsage.getInit()); //初始化堆内存
        appMetaInfo.put("jvmHeapUsed", heapMemoryUsage.getUsed()); //已使用堆内存
        appMetaInfo.put("jvmHeapCommitted", heapMemoryUsage.getCommitted()); //可使用堆内存
        appMetaInfo.put("jvmHeapMax", heapMemoryUsage.getMax()); //最大堆内存
        appMetaInfo.put("jvmNonHeapInit", nonHeapMemoryUsage.getInit()); //初始化非堆内存
        appMetaInfo.put("jvmNonHeapUsed", nonHeapMemoryUsage.getUsed()); //已使用非堆内存
        appMetaInfo.put("jvmNonHeapCommitted", nonHeapMemoryUsage.getCommitted()); //可使用非堆内存
        appMetaInfo.put("jvmNonHeapMax", nonHeapMemoryUsage.getMax()); //最大非堆内存
        String jvmGcArguments = gcMxBeans.stream()
                .map(gcMxBean -> GarbageCollectorType.of(gcMxBean.getName()))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(GarbageCollectorType::getJvmGen).reversed())
                .map(GarbageCollectorType::getJvmArgs)
                .distinct()
                .collect(Collectors.joining(","));
        appMetaInfo.put("jvmGcArguments", jvmGcArguments);
        return Result.success().data(appMetaInfo).build();
    }

    /**
     * PING指定的主机。网络通达则返回true，否则返回false
     *
     * @param host
     * @return
     */
    @GetMapping(value="/ping/{host}")
    public Result<Boolean> ping(@PathVariable("host") String host) {
        boolean pong = NetUtils.ping(host, 6000);
        return Result.success().data(pong).build();
    }

    /**
     * PING指定的主机+端口。网络通达则返回true，否则返回false
     *
     * @param host
     * @param port
     * @return
     */
    @GetMapping(value="/ping/{host}/{port}")
    public Result<Boolean> ping(@PathVariable("host") String host,@PathVariable("port") int port) {
        boolean pong;
        String hostPort = host + ":" + port;
        try {
            NetUtils.netCat(host, port, "hello".getBytes(StandardCharsets.UTF_8));
            LOGGER.info(">>> Ping {} success!", hostPort);
            pong = true;
        } catch (Exception e) {
            LOGGER.error(String.format(">>> Ping %s failed: %s", hostPort, e.getMessage()), e);
            pong = false;
        }
        return Result.success().data(pong).build();
    }

    /**
     * 尝试触发错误，检查全局异常处理机制是否生效
     *
     * @param magic
     * @return
     */
    @GetMapping(value="/tryerror")
    public Result<Object> tryError(Integer magic) {
        return Result.success().data(System.currentTimeMillis() / magic).build();
    }

}
