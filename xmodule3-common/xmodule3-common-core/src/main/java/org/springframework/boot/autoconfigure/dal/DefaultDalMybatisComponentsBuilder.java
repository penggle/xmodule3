package org.springframework.boot.autoconfigure.dal;

import com.penglecode.xmodule.common.domain.DomainObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import javax.sql.DataSource;

/**
 * 默认的DalMybatisComponentsBuilder实现
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/7/16 13:44
 */
public class DefaultDalMybatisComponentsBuilder extends AbstractDalMybatisComponentsBuilder {

    @Override
    public SqlSessionFactoryBean buildSqlSessionFactoryBean(DataSource dataSource, MybatisProperties properties) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfigLocation(resolveConfigLocation(properties.getConfigLocation()));
        sqlSessionFactoryBean.setTypeAliasesPackage(StringUtils.defaultString(properties.getTypeAliasesPackage(), DEFAULT_BASE_PACKAGE));
        sqlSessionFactoryBean.setTypeAliasesSuperType(DomainObject.class);
        sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
        //默认情况下未配置mapperLocations,则所有SqlSessionFactory实例共享全部的XxxMapper.xml。不过这不是事,因为下面的MapperScanner生成的XxxMapper接口代理是区分数据库的
        sqlSessionFactoryBean.setMapperLocations(resolveMapperLocations(properties.getMapperLocations()));
        return sqlSessionFactoryBean;
    }

    @Override
    public ClassPathMapperScanner buildClassPathMapperScanner(String database, BeanDefinitionRegistry registry) {
        String sqlSessionFactoryBeanName = DalComponentUtils.genBeanNameOfType(database, SqlSessionFactory.class);
        ClassPathMapperScanner classPathMapperScanner = new ClassPathMapperScanner(registry);
        classPathMapperScanner.setAddToConfig(true);
        classPathMapperScanner.setAnnotationClass(NamedDatabase.class);
        classPathMapperScanner.setSqlSessionFactoryBeanName(sqlSessionFactoryBeanName);
        classPathMapperScanner.setResourceLoader(resourceLoader);
        classPathMapperScanner.setEnvironment(environment);
        classPathMapperScanner.setBeanNameGenerator(new DatabaseDifferentiatedBeanNameGenerator(database));
        //通过XxxMapper接口上标记的@NamedDatabase("xxx")来仅为该库下的Mapper生成Mapper实现,实现了区分数据库的目的
        classPathMapperScanner.addExcludeFilter(new DatabaseDifferentiatedMapperExcludeFilter(database));
        return classPathMapperScanner;
    }
}
