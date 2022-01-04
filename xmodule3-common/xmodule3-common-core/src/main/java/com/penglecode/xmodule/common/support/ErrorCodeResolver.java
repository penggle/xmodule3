package com.penglecode.xmodule.common.support;

import com.penglecode.xmodule.common.consts.ApplicationConstants;
import com.penglecode.xmodule.common.exception.ApplicationException;
import com.penglecode.xmodule.common.util.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * ErrorCode解析器
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/29 21:57
 */
public class ErrorCodeResolver {

    private ErrorCodeResolver() {}

    /**
     * 根据异常解析其错误码
     * @param ex            - 异常对象
     * @return
     */
    public static ErrorCode resolveErrorCode(Throwable ex) {
        return resolveErrorCode(ex, "message.request.failure");
    }

    /**
     * 根据异常解析其错误码
     * @param ex            - 异常对象
     * @param messageCode   - 统一错误提示消息的i18n代码
     * @return
     */
    public static ErrorCode resolveErrorCode(Throwable ex, String messageCode) {
        boolean found = false;
        Throwable cause = ex;
        String code = GlobalErrorCode.ERR.getCode();
        String message = GlobalErrorCode.ERR.getMessage();
        while(cause != null){
            if (cause instanceof ApplicationException) { // 已知的异常信息
                code = ((ApplicationException) cause).getCode();
                message = cause.getMessage();
                if(StringUtils.containsChineseChar(message)){
                    found = true;
                    break;
                }
            }
            cause = cause.getCause();
        }
        if(!found){
            Throwable target = ExceptionUtils.getRootCause(ex);
            message = target.getMessage();
            if(!StringUtils.containsChineseChar(message)){
                message = ApplicationConstants.DEFAULT_MESSAGE_SOURCE_ACCESSOR.get().getMessage(messageCode, new Object[] {message}, message); // 未知的异常消息,需要转换成统一的,以增强用户体验
            }
        }
        return new DefaultErrorCode(code, message);
    }

}