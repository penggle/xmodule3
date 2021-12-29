package com.penglecode.xmodule.common.mybatis.executor;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * 自定义的SqlSessionFactoryBuilder
 * @author pengpeng
 * @version 1.0
 * @since 2021/7/24 14:53
 */
public class CustomSqlSessionFactoryBuilder extends SqlSessionFactoryBuilder {

    @Override
    public SqlSessionFactory build(Configuration config) {
        return super.build(new CustomConfiguration(config));
    }

}
