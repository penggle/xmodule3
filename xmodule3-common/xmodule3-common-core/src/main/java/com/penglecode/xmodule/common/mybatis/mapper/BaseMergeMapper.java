package com.penglecode.xmodule.common.mybatis.mapper;

import com.penglecode.xmodule.common.domain.DomainObject;

/**
 * 通过unique-key来动态判断到底是执行insert还是update的能力
 *      1、对于MySQL来说，则使用INSERT INTO ... ON DUPLICATE KEY UPDATE ...语句来实现
 *      2、对于ORACLE来说，则使用MERGE INTO ...语句来实现
 * @author pengpeng
 * @version 1.0
 * @since 2021/7/20 23:15
 */
public interface BaseMergeMapper<T extends DomainObject> extends BaseBatchMapper {

    /**
     * 根据唯一键来insert或update记录
     * (注意：此处的执行的insert或update都是全部字段更新！如果不想报错，请确保model参数中的NOT-NULL字段都得塞值)
     * @param model - 实体对象
     * @return 返回更新记录数
     */
    int mergeModelByUniqueKey(T model);

}
