package com.penglecode.xmodule.common.swagger;

import com.penglecode.xmodule.common.util.BeanUtils;
import com.penglecode.xmodule.common.util.ClassUtils;
import com.penglecode.xmodule.common.util.JsonUtils;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.apache.commons.lang3.ArrayUtils;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.core.CollectionFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.List;

/**
 * 默认的OperationCustomizer
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/4/12 20:51
 */
public class DefaultOperationCustomizer implements OperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
        List<Parameter> queryParameters = operation.getParameters();
        processQueryParameter(queryParameters, methodParameters);
        RequestBody bodyParameter = operation.getRequestBody();
        processBodyParameter(bodyParameter, methodParameters);
        System.out.println(handlerMethod + " ==========> " + operation);
        return operation;
    }

    protected void processBodyParameter(RequestBody bodyParameter, MethodParameter[] methodParameters) {
        if(bodyParameter != null && !ArrayUtils.isEmpty(methodParameters)) {
            //1、强制设置请求体必填
            bodyParameter.setRequired(true);
            bodyParameter.getContent().forEach((typeName, typeValue) -> {
                if(typeName.contains(MediaType.APPLICATION_JSON_VALUE)) { //如果是application/json
                    Object exampleValue = resolveBodyParameterDefaultValue(methodParameters);
                    typeValue.setExample(exampleValue);
                }
            });
        }
    }

    protected void processQueryParameter(List<Parameter> queryParameters, MethodParameter[] methodParameters) {
        if(!CollectionUtils.isEmpty(queryParameters) && !ArrayUtils.isEmpty(methodParameters)) {
            for(Parameter queryParameter : queryParameters) {
                //1、如果example未指定，则需要指定
                if(ObjectUtils.isEmpty(queryParameter.getExample())) {
                    Object defaultValue = queryParameter.getSchema().getDefault();
                    if(!ObjectUtils.isEmpty(defaultValue)) {
                        queryParameter.setExample(defaultValue);
                    } else {
                        for(MethodParameter methodParameter : methodParameters) {
                            if(queryParameter.getName().equals(methodParameter.getParameterName())) {
                                queryParameter.setExample(resolveQueryParameterDefaultValue(methodParameter));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    protected Object resolveBodyParameterDefaultValue(MethodParameter[] methodParameters) {
        if(methodParameters != null) {
            for(MethodParameter methodParameter : methodParameters) {
                org.springframework.web.bind.annotation.RequestBody requestBody = methodParameter.getParameterAnnotation(org.springframework.web.bind.annotation.RequestBody.class);
                if(requestBody != null) {
                    return createBodyParameterDefaultValue(methodParameter);
                }
            }
        }
        return null;
    }

    protected Object createBodyParameterDefaultValue(MethodParameter methodParameter) {
        try {
            TypeDescriptor requestBodyType = new TypeDescriptor(methodParameter);
            if(requestBodyType.isArray()) {
                Class<?> elementType = requestBodyType.getElementTypeDescriptor().getType();
                Object elementValue0 = createExampleObject(elementType);
                if(elementValue0 == null) {
                    return Array.newInstance(elementType, 0);
                } else {
                    Object[] elements = (Object[]) Array.newInstance(elementType, 1);
                    elements[0] = elementValue0;
                    return elements;
                }
            } else if(requestBodyType.isCollection()) {
                Class<?> elementType = requestBodyType.getElementTypeDescriptor().getType();
                Object elementValue0 = createExampleObject(elementType);
                if(elementValue0 == null) {
                    return CollectionFactory.createCollection(requestBodyType.getType(), 0);
                } else {
                    return CollectionFactory.createCollection(requestBodyType.getType(), elementType, 1);
                }
            } else if(requestBodyType.isMap()) {
                return Collections.emptyMap();
            }
            return BeanUtils.instantiateClass(requestBodyType.getType());
        } catch (Exception e) {
            //ignored
        }
        return null;
    }

    private Object createExampleObject(Class<?> exampleType) {
        if(!ClassUtils.isSimpleType(exampleType)) {
            return JsonUtils.json2Object("{}", exampleType);
        }
        return null;
    }

    protected Object resolveQueryParameterDefaultValue(MethodParameter methodParameter) {
        if(methodParameter != null) {
            RequestParam requestParam = methodParameter.getParameterAnnotation(RequestParam.class);
            if(requestParam != null && StringUtils.hasText(requestParam.defaultValue())) {
                return requestParam.defaultValue();
            } else {
                return ClassUtils.getDefaultValueOfPrimitive(methodParameter.getParameterType());
            }
        }
        return null;
    }

}
