package com.penglecode.xmodule.common.codegen.service;

/**
 * 领域实体的领域服务实现代码生成参数
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/6 21:48
 */
public class DomainServiceImplementCodegenParameter extends AbstractDomainServiceCodegenParameter {

    /** 领域实体的Mybatis-Mapper的bean名称 */
    private String mapperBeanName;

    /** 领域实体的Mybatis-Mapper的接口名称 */
    private String mapperInterfaceName;

    /** 领域实体的Mybatis-Mapper的实例名称 */
    private String mapperInstanceName;

    /** 事务管理器的bean名称 */
    private String transactionManagerName;

    public DomainServiceImplementCodegenParameter(String targetTemplateName) {
        super(targetTemplateName);
    }

    public String getMapperBeanName() {
        return mapperBeanName;
    }

    public void setMapperBeanName(String mapperBeanName) {
        this.mapperBeanName = mapperBeanName;
    }

    public String getMapperInterfaceName() {
        return mapperInterfaceName;
    }

    public void setMapperInterfaceName(String mapperInterfaceName) {
        this.mapperInterfaceName = mapperInterfaceName;
    }

    public String getMapperInstanceName() {
        return mapperInstanceName;
    }

    public void setMapperInstanceName(String mapperInstanceName) {
        this.mapperInstanceName = mapperInstanceName;
    }

    public String getTransactionManagerName() {
        return transactionManagerName;
    }

    public void setTransactionManagerName(String transactionManagerName) {
        this.transactionManagerName = transactionManagerName;
    }

}