package com.penglecode.xmodule.samples.product.api.controller;

import com.penglecode.xmodule.common.model.Result;
import com.penglecode.xmodule.common.support.AppMetaInfo;
import com.penglecode.xmodule.common.support.AppMetaService;
import com.penglecode.xmodule.common.util.NetUtils;
import com.penglecode.xmodule.common.web.servlet.support.ServletHttpApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * 应用元信息Controller
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/4/8 21:14
 */
@RestController
@RequestMapping("/api/appmeta")
@Tag(name="AppMetaController", description="应用元信息接口")
public class AppMetaController extends ServletHttpApiSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppMetaController.class);

    @Resource(name="appMetaService")
    private AppMetaService appMetaService;

    /**
     * 获取应用元信息，包括JVM信息、操作系统信息、堆内内存、堆外内存、GC等信息
     *
     * @return
     */
    @Operation(summary="获取应用元信息")
    @GetMapping(value="/info", produces=MediaType.APPLICATION_JSON_VALUE)
    public Result<AppMetaInfo> info() {
        AppMetaInfo appMetaInfo = appMetaService.getAppMetaInfo();
        return Result.success().data(appMetaInfo).build();
    }

    /**
     * PING指定的主机。网络通达则返回true，否则返回false
     *
     * @param host
     * @return
     */
    @Operation(summary="PING指定的主机")
    @GetMapping(value="/ping/{host}")
    public Result<Boolean> ping(@PathVariable("host") String host) {
        boolean pong = NetUtils.ping(host, 6000);
        return Result.success().data(pong).build();
    }

    /**
     * PING指定的主机:端口。网络通达则返回true，否则返回false
     *
     * @param host
     * @param port
     * @return
     */
    @Operation(summary="PING指定的主机:端口")
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
