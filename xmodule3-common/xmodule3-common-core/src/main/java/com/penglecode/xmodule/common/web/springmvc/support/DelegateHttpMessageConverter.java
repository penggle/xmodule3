package com.penglecode.xmodule.common.web.springmvc.support;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.model.ResultMap;
import com.penglecode.xmodule.common.model.Result;
import com.penglecode.xmodule.common.support.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;

/**
 * 所有HttpMessageConverter的代理Wrapper
 * 用于统一处理响应结果：
 *      1、保证HTTP的状态码与Result.code一致
 *      2、如果Result.app为空则为其设置app字段值
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/4/11 12:31
 */
public class DelegateHttpMessageConverter implements HttpMessageConverter<Object> {

    private final HttpMessageConverter<Object> delegate;

    public DelegateHttpMessageConverter(HttpMessageConverter<Object> delegate) {
        super();
        Assert.notNull(delegate, "Parameter 'delegate' can not be null!");
        this.delegate = delegate;
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return delegate.canRead(clazz, mediaType);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return delegate.canWrite(clazz, mediaType);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return delegate.getSupportedMediaTypes();
    }

    @Override
    public Object read(Class<?> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        return delegate.read(clazz, inputMessage);
    }

    @Override
    public void write(Object t, MediaType contentType, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        handleOutputMessage(t, outputMessage);
        delegate.write(t, contentType, outputMessage);
    }

    @SuppressWarnings("unchecked")
    protected void handleOutputMessage(Object t, HttpOutputMessage outputMessage) {
        if(outputMessage instanceof ServerHttpResponse) {
            ServerHttpResponse serverHttpResponse = (ServerHttpResponse) outputMessage;
            HttpStatus status;
            if(t instanceof Result) {
                Result<Object> result = (Result<Object>) t;
                status = ErrorCode.defaultStatus(result.getCode());
                if(status != null) { //1、保证HTTP的状态码与Result.code一致
                    serverHttpResponse.setStatusCode(status);
                }
                if(StringUtils.isBlank(result.getApp())) { //2、如果Result.app为空则为其设置app字段值
                    result.setApp(GlobalConstants.APP_CODE.get());
                }
            } else if(t instanceof ResultMap) {
                ResultMap result = (ResultMap) t;
                status = ErrorCode.defaultStatus(result.getCode());
                if(status != null) { //1、保证HTTP的状态码与Result.code一致
                    serverHttpResponse.setStatusCode(status);
                }
                if(StringUtils.isBlank(result.getApp())) { //2、如果Result.app为空则为其设置app字段值
                    result.setApp(GlobalConstants.APP_CODE.get());
                }
            }

        }
    }

}
