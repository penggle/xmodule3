package com.penglecode.xmodule.common.support;

/**
 * 全局错误码枚举
 *
 * 此处是全局错误码枚举，基于HTTP状态码之上。对于整个系统中的各个微服务的特定化错误码定义约定如下：
 *  1、自定义的错误码都是以某个HTTP状态码为前缀，后补三位数字凑成6位数字的错误码，也就是说，错误码分两种：
 *      (1)、原始的3位HTTP状态码错误码，例如ERR403("403", "请求未授权")
 *      (2)、基于原始的3位HTTP状态码扩展的错误码，例如ERR500001("500001", "用户账户状态异常")
 *  2、参数校验错误的自定义错误码必须归属在ERR40X(即HTTP-40X)下面，即参数校验错误码必须以ERR40X开头命名，code值必须以40X开头。
 *    考虑到若为每个参数校验都定义专有的校验错误码那太多了并不现实，我们可以为核心数据定义专有错误码，
 *    例如：ERR400001("400001", "用户ID不能为空"),ERR404002("404002", "用户账户不存在"),ERR409001("409001", "用户名已存在");
 *    其余一般性校验或者干脆都以ERR400代替一切参数校验类错误码也是可以的
 *  3、业务类错误码，都统一归属到ERR500下，即以HTTP-500作为前缀，例如ERR500001("500001", "用户账户余额不足")
 *  4、登录认证校验类归属到ERR401下
 *  5、操作权限校验类归属到ERR403下
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/20 0:12
 */
public enum GlobalErrorCode implements ErrorCode {

    /** 默认成功 */
    OK("200", "请求成功"),
    /** 默认失败 */
    ERR("500", "请求失败"),
    /** 204成功 */
    OK204("204", "请求成功"),

    /** 失败4xx */
    ERR400("400", "请求参数错误"),
    ERR401("401", "请求未认证"),
    ERR403("403", "请求未授权"),
    ERR404("404", "资源不存在"),
    ERR405("405", "请求方法不允许"),
    ERR406("406", "请求头不可接受"),
    ERR408("408", "请求超时"),
    ERR409("409", "请求冲突"),
    ERR413("413", "请求体过大"),
    ERR415("415", "请求媒体类型不支持"),

    /** 失败5xx */
    ERR502("502", "网关错误"),
    ERR503("503", "服务不可用"),
    ERR504("504", "网关超时");

    private final String code;

    private final String message;

    GlobalErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 错误信息
     * @param overrides   - 如果不为空，则使用该消息
     * @return
     */
    @Override
    public String getMessage(String... overrides) {
        if(overrides != null && overrides.length == 1 && overrides[0] != null && !"".equals(overrides[0].trim())) {
            return overrides[0].trim();
        }
        return getMessage();
    }

    public static ErrorCode getErrorCode(String code, ErrorCode defaultErrorCode) {
        for(ErrorCode errorCode : values()) {
            if(errorCode.getCode().equals(code)) {
                return errorCode;
            }
        }
        return defaultErrorCode;
    }

}
