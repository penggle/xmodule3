package com.penglecode.xmodule.common.codegen.exception;

/**
 * 代码自动生成运行时异常
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/9/5 14:48
 */
public class CodegenRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CodegenRuntimeException(String message) {
        super(message);
    }

    public CodegenRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodegenRuntimeException(Throwable cause) {
        super(cause);
    }

}
