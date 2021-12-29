package com.penglecode.xmodule.common.util;

import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

/**
 * 基于Spring SPEL的模板工具类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2020/6/1 12:08
 */
public class TemplateUtils {

    private static final ExpressionParser DEFAULT_TEMPLATE_PARSER = new SpelExpressionParser();

    public static final TemplateParserContext HASH_DELIMITED_PARSER_CONTEXT = new TemplateParserContext("#{", "}");

    public static final TemplateParserContext DOLLAR_DELIMITED_PARSER_CONTEXT = new TemplateParserContext("${", "}");

    private TemplateUtils() {}

    /**
     * 使用指定的parameter参数解析指定template模板
     *
     * @param template          - 模板表达式，例如: 手机号码: ${mobile}, 验证码: ${smsCode}
     * @param parameter         - 模板参数
     * @param parserContext     - 如果parserContext为null则使用#DOLLAR_DELIMITED_PARSER_CONTEXT
     * @return
     */
    public static String parseTemplate(String template, Map<String, Object> parameter, TemplateParserContext parserContext) {
        parserContext = parserContext == null ? DOLLAR_DELIMITED_PARSER_CONTEXT : parserContext;
        Expression expression = DEFAULT_TEMPLATE_PARSER.parseExpression(template, parserContext);
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        if(!CollectionUtils.isEmpty(parameter)) {
            evaluationContext.addPropertyAccessor(new MapAccessor());
            evaluationContext.setRootObject(parameter);
        }
        return expression.getValue(evaluationContext, String.class);
    }

    /**
     * 使用指定的parameter参数解析指定template模板
     * (该方法默认使用#DOLLAR_DELIMITED_PARSER_CONTEXT，即参数模板诸如：${paramName})
     *
     * @param template          - 模板表达式，例如: 手机号码: ${mobile}, 验证码: ${smsCode}
     * @param parameter         - 模板参数
     * @return
     */
    public static String parseTemplate(String template, Map<String, Object> parameter) {
        return parseTemplate(template, parameter, DOLLAR_DELIMITED_PARSER_CONTEXT);
    }


}
