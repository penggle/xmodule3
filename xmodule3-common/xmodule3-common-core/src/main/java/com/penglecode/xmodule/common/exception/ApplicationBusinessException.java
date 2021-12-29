package com.penglecode.xmodule.common.exception;

/**
 * 应用业务异常
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 15:15
 */
public class ApplicationBusinessException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    public ApplicationBusinessException(String code, String message) {
        super(code, message);
    }

    public ApplicationBusinessException(Throwable cause) {
        super(cause);
    }

    public ApplicationBusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationBusinessException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

}
