package com.penglecode.xmodule.common.util;

import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/5/16 18:09
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static final Pattern CHINESE_CHAR_PATTERN = Pattern.compile("[\u4e00-\u9fa5]");

    private StringUtils() {}

    /**
     * 如果value为null则返回defaultValue所持有的值
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static String defaultString(String value, Supplier<String> defaultValue) {
        if(value == null) {
            return defaultValue != null ? defaultValue.get() : null;
        }
        return value;
    }

    /**
     * 如果value为空(null, "")则返回defaultValue所持有的值
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static String defaultIfEmpty(String value, Supplier<String> defaultValue) {
        if(isEmpty(value)) {
            return defaultValue != null ? defaultValue.get() : null;
        }
        return value;
    }

    /**
     * 如果value为空(null, "", "  ")则返回defaultValue所持有的值
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static String defaultIfBlank(String value, Supplier<String> defaultValue) {
        if(isBlank(value)) {
            return defaultValue != null ? defaultValue.get() : null;
        }
        return value;
    }

    /**
     * mobile-phone 转 mobilePhone
     *
     * @param paramName
     * @return
     */
    public static String kebabNamingToCamel(String paramName) {
        int fromIndex = 0;
        int targetIndex;
        StringBuilder sb = new StringBuilder(paramName);
        while((targetIndex = sb.indexOf("-", fromIndex)) != -1) {
            fromIndex = targetIndex + 1;
            sb.setCharAt(fromIndex, Character.toUpperCase(sb.charAt(fromIndex)));
        }
        return sb.toString().replace("-", "");
    }

    /**
     * mobile_phone 转 mobilePhone
     *
     * @param paramName
     * @return
     */
    public static String snakeNamingToCamel(String paramName) {
        int fromIndex = 0;
        int targetIndex;
        StringBuilder sb = new StringBuilder(paramName);
        while((targetIndex = sb.indexOf("_", fromIndex)) != -1) {
            fromIndex = targetIndex + 1;
            sb.setCharAt(fromIndex, Character.toUpperCase(sb.charAt(fromIndex)));
        }
        return sb.toString().replace("_", "");
    }

    /**
     * mobilePhone 转 mobile_phone
     *
     * @param paramName
     * @return
     */
    public static String camelNamingToSnake(String paramName) {
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toLowerCase(paramName.charAt(0)));
        for(int i = 1, len = paramName.length(); i < len; i++) {
            char ch = paramName.charAt(i);
            if(Character.isUpperCase(ch)) {
                ch = Character.toLowerCase(ch);
                sb.append("_");
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * mobilePhone 转 mobile-phone
     *
     * @param paramName
     * @return
     */
    public static String camelNamingToKebab(String paramName) {
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toLowerCase(paramName.charAt(0)));
        for(int i = 1, len = paramName.length(); i < len; i++) {
            char ch = paramName.charAt(i);
            if(Character.isUpperCase(ch)) {
                ch = Character.toLowerCase(ch);
                sb.append("-");
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * 将指定的paramName的第一个字符改为小写
     * @param paramName
     * @return
     */
    public static String lowerCaseFirstChar(String paramName) {
        return paramName != null ? Character.toLowerCase(paramName.charAt(0)) + paramName.substring(1) : null;
    }

    /**
     * 将指定的paramName的第一个字符改为大写
     * @param paramName
     * @return
     */
    public static String upperCaseFirstChar(String paramName) {
        return paramName != null ? Character.toUpperCase(paramName.charAt(0)) + paramName.substring(1) : null;
    }

    /**
     * 检测指定字符串中是否包含中文字符
     * @param str
     * @return
     */
    public static boolean containsChineseChar(String str){
        return str != null && CHINESE_CHAR_PATTERN.matcher(str).find();
    }

}
