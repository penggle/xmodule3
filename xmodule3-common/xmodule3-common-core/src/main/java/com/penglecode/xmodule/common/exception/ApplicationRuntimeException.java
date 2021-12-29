package com.penglecode.xmodule.common.exception;

/**
 * 应用运行时异常
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 15:15
 */
public class ApplicationRuntimeException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    public ApplicationRuntimeException(String code, String message) {
        super(code, message);
    }

    public ApplicationRuntimeException(Throwable cause) {
        super(cause);
    }

    public ApplicationRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationRuntimeException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

}
