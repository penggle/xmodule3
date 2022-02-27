package com.penglecode.xmodule.common.exception;

/**
 * 应用API异常
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/5/15 15:15
 */
public class ApplicationApiException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    public ApplicationApiException(String code, String message) {
        super(code, message);
    }

    public ApplicationApiException(Throwable cause) {
        super(cause);
    }

    public ApplicationApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationApiException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

}
