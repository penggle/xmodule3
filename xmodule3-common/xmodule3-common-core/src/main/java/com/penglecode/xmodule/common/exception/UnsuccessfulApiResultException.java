package com.penglecode.xmodule.common.exception;

/**
 * 接口响应不成功(异常)异常
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 15:15
 */
public class UnsuccessfulApiResultException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    private final String app;

    public UnsuccessfulApiResultException(String app, String code, String message) {
        super(code, message);
        this.app = app;
    }

    public UnsuccessfulApiResultException(String app, String code, String message, Throwable cause) {
        super(code, message, cause);
        this.app = app;
    }

    public String getApp() {
        return app;
    }

}
