package com.penglecode.xmodule.common.exception;

/**
 * 应用启动初始化时异常
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/5/15 15:15
 */
public class ApplicationInitializationException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    public ApplicationInitializationException(String code, String message) {
        super(code, message);
    }

    public ApplicationInitializationException(Throwable cause) {
        super(cause);
    }

    public ApplicationInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationInitializationException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

}
