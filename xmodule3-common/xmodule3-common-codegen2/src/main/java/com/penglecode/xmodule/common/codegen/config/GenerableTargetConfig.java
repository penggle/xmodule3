package com.penglecode.xmodule.common.codegen.config;

/**
 * 能自动生成的目标配置
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/1/22 11:16
 */
public abstract class GenerableTargetConfig extends GenerableTargetLocation {

    /** 默认代码src目录：当前项目下的src/main/java */
    public static final String DEFAULT_TARGET_PROJECT = "src/main/java";

    /**
     * 根据指定领域对象名称，获取生成的目标名称
     *
     * 根据调用者不同生成的结果也是不同的，示例：
     *      1、(ProductInfo, true, true) => com.xxx.product.domain.model.ProductInfo.java
     *      2、(ProductInfo, true, false) => com.xxx.product.domain.model.ProductInfo
     *      3、(ProductInfo, true, true) => com.xxx.product.dal.mapper.ProductInfoMapper.java
     *      4、(ProductInfo, true, false) => com.xxx.product.domain.service.ProductInfoService
     *      5、(Product, false, false) => ProductAppService
     *
     * @param domainObjectName  - 领域对象名称
     * @param includePackage    - 是否包含包名
     * @param includeSuffix     - 是否包含后缀名
     * @return 返回指定领域对象的目标名称
     */
    public abstract String getGeneratedTargetName(String domainObjectName, boolean includePackage, boolean includeSuffix);

}
