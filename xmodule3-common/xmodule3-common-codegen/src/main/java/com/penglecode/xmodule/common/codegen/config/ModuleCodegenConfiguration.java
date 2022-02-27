package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.common.codegen.mybatis.MySQLMybatisCodegenDialect;
import com.penglecode.xmodule.common.codegen.mybatis.MybatisCodegenDialect;
import com.penglecode.xmodule.common.codegen.mybatis.OracleMybatisCodegenDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 模块化代码生成配置类
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/9/20 0:34
 */
@Configuration
public class ModuleCodegenConfiguration {

    @Bean
    public MybatisCodegenDialect mysqlMybatisCodegenDialect() {
        return new MySQLMybatisCodegenDialect();
    }

    @Bean
    public MybatisCodegenDialect oracleMybatisCodegenDialect() {
        return new OracleMybatisCodegenDialect();
    }

}
