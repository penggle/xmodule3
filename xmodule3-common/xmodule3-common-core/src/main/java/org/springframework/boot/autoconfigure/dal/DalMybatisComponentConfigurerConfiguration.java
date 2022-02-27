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
 * @created 2021/5/16 14:58
 */
@Configuration
@ConditionalOnClass(name={"org.apache.ibatis.session.SqlSessionFactory", "org.mybatis.spring.SqlSessionFactoryBean", "org.mybatis.spring.boot.autoconfigure.MybatisProperties"})
public class DalMybatisComponentConfigurerConfiguration {

    /**
     * 如果需要自行创建SqlSessionFactoryBean和ClassPathMapperScanner两个bean，
     * 则继承AbstractDalMybatisComponentsBuilder重新自定义一个DalMybatisComponentsBuilder覆盖此处配置
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public DalMybatisComponentsBuilder dalMybatisComponentsBuilder() {
        return new DefaultDalMybatisComponentsBuilder();
    }

    /**
     * 动态向Spring上下文中注册多个数据源的SqlSessionFactory、SqlSessionTemplate、XxxMapper等bean
     *
     * @param dalMybatisComponentsBuilder
     * @return
     */
    @Bean
    public DalMybatisComponentAutoConfigurer dalMybatisComponentAutoConfigurer(DalMybatisComponentsBuilder dalMybatisComponentsBuilder) {
        return new DalMybatisComponentAutoConfigurer(dalMybatisComponentsBuilder);
    }

}
