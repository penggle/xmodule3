package com.penglecode.xmodule.common.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/28 20:25
 */
public class NestedApplicationException extends NestedRuntimeException {

    private static final long serialVersionUID = 1L;

    public NestedApplicationException(String msg) {
        super(msg);
    }

    public NestedApplicationException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
