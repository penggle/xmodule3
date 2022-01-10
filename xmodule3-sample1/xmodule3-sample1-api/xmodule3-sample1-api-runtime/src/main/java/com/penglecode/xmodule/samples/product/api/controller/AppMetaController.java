package com.penglecode.xmodule.samples.product.api.controller;

import com.penglecode.xmodule.common.dto.Result;
import com.penglecode.xmodule.common.util.NetUtils;
import com.penglecode.xmodule.common.util.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用元信息Controller
 *
 * @author pengpeng
 * @version 1.0
 * @since 2022/1/8 21:14
 */
@RestController
@RequestMapping("/api/appmeta")
public class AppMetaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppMetaController.class);

    @GetMapping(value="/info", produces=MediaType.APPLICATION_JSON_VALUE)
    public Result<Map<String,Object>> info() {
        Environment environment = SpringUtils.getEnvironment();
        Map<String,Object> appMetaInfo = new LinkedHashMap<>();
        appMetaInfo.put("appName", environment.getProperty("spring.application.name"));
        //获取JVM的启动时间，版本及名称，当前进程ID，环境变量等
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        //操作系统及硬件信息：系统名称、版本，CPU内核，机器总内存、可用内存、可用内存占比
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        //获取JVM堆内存使用状况
        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        //获取JVM非堆内存使用状况
        MemoryUsage nonHeapMemoryUsage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
        appMetaInfo.put("processId", runtimeMXBean.getName().split("@")[0]);
        appMetaInfo.put("jvmName", runtimeMXBean.getVmName() + ", " + runtimeMXBean.getVmVendor());
        appMetaInfo.put("jvmUptime", runtimeMXBean.getUptime());
        appMetaInfo.put("jvmStartTime", runtimeMXBean.getStartTime());
        appMetaInfo.put("javaVersion", runtimeMXBean.getSpecVersion());
        appMetaInfo.put("osName", operatingSystemMXBean.getName());
        appMetaInfo.put("osArch", operatingSystemMXBean.getArch());
        appMetaInfo.put("osVersion", operatingSystemMXBean.getVersion());
        appMetaInfo.put("availableProcessors", operatingSystemMXBean.getAvailableProcessors());
        appMetaInfo.put("jvmHeapInit", heapMemoryUsage.getInit()); //初始化堆内存
        appMetaInfo.put("jvmHeapUsed", heapMemoryUsage.getUsed()); //已使用堆内存
        appMetaInfo.put("jvmHeapCommitted", heapMemoryUsage.getCommitted()); //可使用堆内存
        appMetaInfo.put("jvmHeapMax", heapMemoryUsage.getMax()); //最大堆内存
        appMetaInfo.put("jvmNonHeapInit", nonHeapMemoryUsage.getInit()); //初始化非堆内存
        appMetaInfo.put("jvmNonHeapUsed", nonHeapMemoryUsage.getUsed()); //已使用非堆内存
        appMetaInfo.put("jvmNonHeapCommitted", nonHeapMemoryUsage.getCommitted()); //可使用非堆内存
        appMetaInfo.put("jvmNonHeapMax", nonHeapMemoryUsage.getMax()); //最大非堆内存

        return Result.success().data(appMetaInfo).build();
    }

    @GetMapping(value="/ping/{host}")
    public Result<Boolean> ping(@PathVariable("host") String host) {
        boolean pong = NetUtils.ping(host, 6000);
        return Result.success().data(pong).build();
    }

    @GetMapping(value="/telnet/{host}/{port}")
    public Result<Boolean> telnet(@PathVariable("host") String host,@PathVariable("port") int port) {
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

    private static long bytesToMB(long bytes) {
        return bytes == -1 ? -1 : bytes / 1048576;
    }

    public static void main(String[] args) {
        try {
            List<GarbageCollectorMXBean> gcMxBeans = ManagementFactory.getGarbageCollectorMXBeans();

            for (GarbageCollectorMXBean gcMxBean : gcMxBeans) {
                System.out.println(gcMxBean.getName());
                System.out.println(gcMxBean.getObjectName());
            }

        } catch (RuntimeException re) {
            throw re;
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

}
