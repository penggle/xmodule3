package com.penglecode.xmodule.samples.product.api.controller;

import com.penglecode.xmodule.common.dto.Result;
import com.penglecode.xmodule.common.util.SpringUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
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

    @GetMapping(value="/info", produces=MediaType.APPLICATION_JSON_VALUE)
    public Result<Map<String,Object>> getAppInfo() {
        Environment environment = SpringUtils.getEnvironment();
        Map<String,Object> appInfo = new LinkedHashMap<>();
        appInfo.put("app.name", environment.getProperty("spring.application.name"));
        return Result.success().data(appInfo).build();
    }

    @GetMapping(value="/ping")
    public Result<Boolean> ping()

}
