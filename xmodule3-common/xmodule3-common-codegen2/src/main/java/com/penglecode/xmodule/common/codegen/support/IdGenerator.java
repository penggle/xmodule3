package com.penglecode.xmodule.common.codegen.support;

import com.penglecode.xmodule.common.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 主键生成器
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/22 14:15
 */
public class IdGenerator {

    private final IdGenStrategy strategy;

    private final String parameter;

    IdGenerator(IdGenStrategy strategy, String parameter) {
        this.strategy = strategy;
        this.parameter = parameter;
    }

    public IdGenStrategy getStrategy() {
        return strategy;
    }

    public String getParameter() {
        return parameter;
    }

    public static IdGenerator parseGenerator(String idGenerator) {
        Pattern pattern = Pattern.compile("([0-9A-Z_]+)\\((\\w*)\\)");
        Matcher matcher = pattern.matcher(idGenerator);
        if(matcher.matches()) {
            String strategy = matcher.group(1);
            String parameter = StringUtils.defaultIfBlank(matcher.group(2), "").trim();
            if(IdGenStrategy.IDENTITY.name().equals(strategy)) {
                return new IdGenerator(IdGenStrategy.IDENTITY, null);
            } else if(IdGenStrategy.SEQUENCE.name().equals(strategy)) {
                return new IdGenerator(IdGenStrategy.SEQUENCE, parameter);
            }
        }
        throw new IllegalArgumentException("Unresolved idGenerator: " + idGenerator);
    }

}
