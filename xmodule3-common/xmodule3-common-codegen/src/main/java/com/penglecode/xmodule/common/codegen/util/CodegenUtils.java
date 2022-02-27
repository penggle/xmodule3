package com.penglecode.xmodule.common.codegen.util;

import com.penglecode.xmodule.common.codegen.config.DomainConfigProperties;
import com.penglecode.xmodule.common.util.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 代码生成工具类
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/9/8 23:01
 */
public class CodegenUtils {

    /**
     * 需要双引号括着的类型
     */
    private static final List<Class<?>> QUOTED_TYPES = Arrays.asList(CharSequence.class, Character.class, char.class);

    private CodegenUtils() {}

    /**
     * 计算目标源代码文件名与目录的最终源代码文件名, 如果targetCodeFileName已经存在则使用自增版本进行重命名
     * @param targetCodeFileName			- 目标源代码文件名，例如UserService.java, UserServiceImpl.java, UserMapper.xml
     * @param targetCodeFileDir				- 目标源代码文件所在目录
     * @return 返回最终源代码文件名, 例如 UserService.java，UserService.java.1，UserServiceImpl.java，UserServiceImpl.java.1，UserMapper.xml.1
     */
    public static String calculateGeneratedCodeFileName(String targetCodeFileName, File targetCodeFileDir) {
        String fileSuffix = "";
        File[] childFiles = targetCodeFileDir.listFiles(f -> !f.isDirectory());
        if(childFiles != null && childFiles.length > 0) {
            Integer version = Stream.of(childFiles).filter(f -> f.getName().startsWith(targetCodeFileName)).map(f -> {
                String suffix = f.getName().replace(targetCodeFileName, "");
                if(StringUtils.isEmpty(suffix)) {
                    return 0;
                } else {
                    return Integer.parseInt(StringUtils.stripStart(suffix, "."));
                }
            }).max(Comparator.comparing(Function.identity())).orElse(null);
            if(version != null) {
                fileSuffix = "." + (version + 1);
            }
        }
        return targetCodeFileName + fileSuffix;
    }

    /**
     * 获取指定属性名的getter方法名
     * @param fieldName
     * @param fieldType
     * @return
     */
    public static String getGetterMethodName(String fieldName, String fieldType) {
        StringBuilder sb = new StringBuilder(fieldName);
        if (Character.isLowerCase(sb.charAt(0)) && (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1)))) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }
        if(boolean.class.getName().equals(fieldType)) {
            sb.insert(0, "is");
        } else {
            sb.insert(0, "get");
        }
        return sb.toString();
    }

    /**
     * 获取指定属性名的setter方法名
     * @param fieldName
     * @param fieldType
     * @return
     */
    public static String getSetterMethodName(String fieldName, String fieldType) {
        StringBuilder sb = new StringBuilder(fieldName);
        if(boolean.class.getName().equals(fieldType) && fieldName.startsWith("is") && Character.isUpperCase(sb.charAt(2))) {
            sb.delete(0, 2);
        } else {
            if (Character.isLowerCase(sb.charAt(0)) && (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1)))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }
        sb.insert(0, "set");
        return sb.toString();
    }

    public static String lowerCaseFirstChar(String fieldName) {
        return fieldName != null ? Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1) : null;
    }

    public static String upperCaseFirstChar(String fieldName) {
        return fieldName != null ? Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1) : null;
    }

    /**
     * 解析注解表达式
     * 例如：
     *      @NamedDatabase("default")   ==>   org.springframework.boot.autoconfigure.dal.NamedDatabase:@NamedDatabase("default")
     *      @javax.validation.constraints.Size(max=191, message="商品规格名称最大长度不超过{max}个字符")    ==>     javax.validation.constraints.Size:@Size(max=191, message="商品规格名称最大长度不超过{max}个字符")
     *
     * @param annotationExpression
     * @param domainConfigProperties
     * @return
     */
    public static String parseAnnotationExpression(String annotationExpression, DomainConfigProperties domainConfigProperties) {
        annotationExpression = StringUtils.stripStart(annotationExpression, "@");
        int sbStart = annotationExpression.indexOf("(");
        int sbEnd = annotationExpression.lastIndexOf(")");
        String typePart = "";
        String sbPart = "";
        if(sbStart > 0 && sbEnd > 0 && sbEnd == annotationExpression.length() - 1) { //存在()括号对
            typePart = annotationExpression.substring(0, sbStart);
            sbPart = annotationExpression.substring(sbStart);
        } else if(sbStart == -1) {
            typePart = annotationExpression;
        }
        if(StringUtils.isNotBlank(typePart) && !typePart.contains(".")) {
            typePart = domainConfigProperties.getDomainCommons().getGlobalTypes().get(typePart); //短名换长名
        }
        if(StringUtils.isNotBlank(typePart)) {
            return typePart + ":@" + typePart.substring(typePart.lastIndexOf(".") + 1) + sbPart;
        }
        return null;
    }

    /**
     * 根据需要对指定value值进行必要的双引号括着
     * @param value
     * @return
     */
    public static Object quotingValueIfNecessary(Object value) {
        if(value != null) {
            Class<?> valueType = value.getClass();
            for(Class<?> quotedType : QUOTED_TYPES) {
                if(quotedType.isAssignableFrom(valueType)) {
                    return "\"" + value + "\"";
                }
            }
        }
        return value;
    }

}
