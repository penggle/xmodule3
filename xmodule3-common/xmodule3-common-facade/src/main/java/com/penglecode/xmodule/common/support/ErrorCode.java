package com.penglecode.xmodule.common.support;

/**
 * 错误码接口
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/20 0:18
 */
public interface ErrorCode {

    /**
     * 默认通用返回成功结果码
     */
    String DEFAULT_RESULT_CODE_OK = "200";

    /**
     * 默认通用返回错误结果码
     */
    String DEFAULT_RESULT_CODE_ERR = "500";

    /**
     * 错误码
     * @return
     */
    String getCode();

    /**
     * 错误信息
     * @param overrides   - 如果不为空，则使用该消息
     * @return
     */
    String getMessage(String... overrides);

    /**
     * HTTP状态码
     * @return
     */
    default int getStatus() {
        return Integer.parseInt(getCode().substring(0, 3));
    }

}
