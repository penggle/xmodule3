package org.springframework.boot.autoconfigure.dal;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 实现按需加载必要的组件bean
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/16 14:58
 */
@Configuration
@ConditionalOnClass(name="com.zaxxer.hikari.HikariDataSource")
public class DalJdbcComponentConfigurerConfiguration {

    /**
     * 动态向Spring上下文中注册多个数据源的DataSource、JdbcTemplate、TransactionManager等bean
     * @return
     */
    @Bean
    public DalJdbcComponentAutoConfigurer dalJdbcComponentAutoConfigurer() {
        return new DalJdbcComponentAutoConfigurer();
    }

}
