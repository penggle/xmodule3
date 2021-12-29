package org.springframework.boot.autoconfigure.dal;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 自动注册多库情况下的数据库访问层(DAL)组件(基于Mybatis)
 * 数据源(连接池)默认使用SpringBoot
 *
 * 使用示例：
 * <br/>
 * application.yml:
 * <br/>
 * <code>
 * spring:
 *     autoconfigure:
 *         #排除SpringBoot默认的数据源和mybatis自动配置
 *         exclude:
 *             - org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
 *             - org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration
 *     #MyBatis配置
 *     mybatis:
 *         config-location: classpath:config/mybatis/mybatis-config.xml
 *         mapper-locations: classpath*:com/penglecode/xmodule/**\\/*Mapper.xml
 *         type-aliases-package:com.penglecode.xmodule
 *     #数据源连接池配置(若只有一个数据库则下面只保留default部分即可)
 *     datasource:
 *         #默认库
 *         default:
 *             username: root
 *             password: 123456
 *             url: jdbc:mysql://127.0.0.1:3306/xmodule?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=GMT%2B8&useSSL=false&rewriteBatchedStatements=true
 *         #商品库
 *         product:
 *             username: root
 *             password: 123456
 *             url: jdbc:mysql://127.0.0.1:3306/dt_product?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=GMT%2B8&useSSL=false&rewriteBatchedStatements=true
 * </code>
 *
 * 项目启动后默认动态注册以database库名前缀的beanName，诸如以下bean：
 * {database}DataSourceProperties、{database}MybatisProperties、{database}DataSource、{database}JdbcTemplate、{database}TransactionManager、{database}SqlSessionFactory、{database}SqlSessionTemplate、{database}XxxMapper
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 22:48
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({DalJdbcComponentConfigurerConfiguration.class, DalMybatisComponentConfigurerConfiguration.class, DalComponentAutoConfiguration.class})
public @interface EnableDalAutoConfigure {

    /**
     * 激活的数据库
     * @return
     */
    NamedDatabase[] value();

}
