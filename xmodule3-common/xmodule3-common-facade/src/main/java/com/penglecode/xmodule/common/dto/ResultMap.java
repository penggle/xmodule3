package com.penglecode.xmodule.common.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * 对应{@link Result}的Map形式，用于全局异常处理
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/4/11 15:08
 */
public final class ResultMap extends HashMap<String,Object> {

    private static final long serialVersionUID = 1L;

    public ResultMap() {
    }

    public ResultMap(Map<? extends String, ?> m) {
        super(m);
    }

    public Boolean getSuccess() {
        return (Boolean) get("success");
    }

    public void setSuccess(Boolean success) {
        put("success", success);
    }

    public String getApp() {
        return (String) get("app");
    }

    public void setApp(String app) {
        put("app", app);
    }

    public String getCode() {
        return (String) get("code");
    }

    public void setCode(String code) {
        put("code", code);
    }

    public String getMessage() {
        return (String) get("message");
    }

    public void setMessage(String message) {
        put("message", message);
    }

    public Object getData() {
        return get("data");
    }

    public void setData(Object data) {
        put("data", data);
    }

}
