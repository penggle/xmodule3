package org.springframework.boot.autoconfigure.dal;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import javax.sql.DataSource;

/**
 * {@link SqlSessionFactoryBean}和{@link ClassPathMapperScanner}的Builder接口
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/7/16 13:41
 */
public interface DalMybatisComponentsBuilder {

    /**
     * 创建SqlSessionFactoryBean
     *
     * @param dataSource
     * @param mybatisProperties
     * @return
     */
    SqlSessionFactoryBean buildSqlSessionFactoryBean(DataSource dataSource, MybatisProperties mybatisProperties);

    /**
     * 创建ClassPathMapperScanner
     * 注意：此处不需要调用ClassPathMapperScanner#registerFilters()和scan()两个方法，build逻辑只需要设置相关属性即可
     *
     * @param database
     * @param registry
     * @return
     */
    ClassPathMapperScanner buildClassPathMapperScanner(String database, BeanDefinitionRegistry registry);

}
