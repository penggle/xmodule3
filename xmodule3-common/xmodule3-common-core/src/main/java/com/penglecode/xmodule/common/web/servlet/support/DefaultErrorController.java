package com.penglecode.xmodule.common.web.servlet.support;

import com.penglecode.xmodule.common.model.ResultMap;
import com.penglecode.xmodule.common.model.Result;
import com.penglecode.xmodule.common.support.DefaultErrorCode;
import com.penglecode.xmodule.common.support.ErrorCode;
import com.penglecode.xmodule.common.support.ErrorCodeResolver;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 默认全局请求出错处理器
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/5/15 14:02
 */
public class DefaultErrorController extends BasicErrorController {

	private final ErrorAttributes errorAttributes;
	
	public DefaultErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties,
                                  List<ErrorViewResolver> errorViewResolvers) {
		super(errorAttributes, errorProperties, errorViewResolvers);
		this.errorAttributes = errorAttributes;
	}

	public DefaultErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties) {
		super(errorAttributes, errorProperties);
		this.errorAttributes = errorAttributes;
	}

	@Override
	@RequestMapping
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
		Throwable exception = errorAttributes.getError(new ServletWebRequest(request));
		Map<String, Object> defaultErrorAttributes = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL) ? ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE) : ErrorAttributeOptions.defaults());
		HttpStatus status = getStatus(request);
		
		ErrorCode errorCode;
		if(exception != null) {
			errorCode = ErrorCodeResolver.resolveErrorCode(exception, status);
		} else {
			errorCode = new DefaultErrorCode(String.valueOf(status.value()), StringUtils.defaultIfEmpty((String) defaultErrorAttributes.get("error"), status.getReasonPhrase()));
		}
		//重新设置status,保持其与errorCode中的一致
		status = ObjectUtils.defaultIfNull(errorCode.getStatus(), status);
		Result<Object> result = Result.failure().code(errorCode.getCode()).message(errorCode.getMessage()).build();
		return new ResponseEntity<>(new ResultMap(result.asMap()), status);
	}

	protected ErrorAttributes getErrorAttributes() {
		return errorAttributes;
	}
	
}
