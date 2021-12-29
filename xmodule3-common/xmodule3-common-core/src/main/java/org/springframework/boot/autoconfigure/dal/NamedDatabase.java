package org.springframework.boot.autoconfigure.dal;

import org.apache.ibatis.annotations.Mapper;

import java.lang.annotation.*;

/**
 * 激活的基于命名的数据库注解
 * 这里的数据库名既可以是物理上的(单库)也可以是逻辑上的(分库)
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 14:02
 */
@Mapper
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface NamedDatabase {

    /**
     * 激活的数据库名
     * @return
     */
    String value();

}
