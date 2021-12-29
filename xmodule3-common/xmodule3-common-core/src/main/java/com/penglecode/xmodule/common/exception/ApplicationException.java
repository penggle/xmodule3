package com.penglecode.xmodule.common.exception;

import com.penglecode.xmodule.common.support.GlobalErrorCode;
import com.penglecode.xmodule.common.support.ErrorCode;

/**
 * 应用异常基类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 14:21
 */
public abstract class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String code;

    protected ApplicationException(String code, String message) {
        super(message);
        this.code = code;
    }

    protected ApplicationException(Throwable cause) {
        super(cause);
        if(cause instanceof ApplicationException) {
            ApplicationException appException = (ApplicationException) cause;
            this.code = appException.code;
        } else {
            ErrorCode defaultError = GlobalErrorCode.ERR;
            this.code = defaultError.getCode();
        }
    }

    protected ApplicationException(String message, Throwable cause) {
        super(message, cause);
        if(cause instanceof ApplicationException) {
            ApplicationException appException = (ApplicationException) cause;
            this.code = appException.code;
        } else {
            ErrorCode defaultError = GlobalErrorCode.ERR;
            this.code = defaultError.getCode();
        }
    }

    protected ApplicationException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}