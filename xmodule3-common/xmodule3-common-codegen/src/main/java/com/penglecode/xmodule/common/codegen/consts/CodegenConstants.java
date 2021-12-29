package com.penglecode.xmodule.common.codegen.consts;

/**
 * 代码自动生成模块常量
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/7 22:24
 */
public class CodegenConstants {

    public static final String MODULE_CODEGEN_CONFIGURATION_PREFIX = "spring.codegen.config.";

    /**
     * 实体模型存在范围查询(最小值)的辅助Java字段的prefix，默认以'min'开头
     */
    public static final String RANGE_MIN_JAVA_FIELD_PREFIX = "min";

    /**
     * 实体模型存在范围查询(最大值)的辅助Java字段的prefix，默认以'max'开头
     */
    public static final String RANGE_MAX_JAVA_FIELD_PREFIX = "max";

    /**
     * 实体模型存在范围查询(最小值)的辅助Java字段的prefix，日期时间类以'start'开头
     */
    public static final String RANGE_START_JAVA_FIELD_PREFIX = "start";

    /**
     * 实体模型存在范围查询(最大值)的辅助Java字段的prefix，日期时间类以'end'开头
     */
    public static final String RANGE_END_JAVA_FIELD_PREFIX = "end";

    /**
     * 默认的代码生成注释作者
     */
    public static final String DEFAULT_CODEGEN_COMMENT_AUTHOR = "AutoCodeGenerator";

    private CodegenConstants() {}

}
