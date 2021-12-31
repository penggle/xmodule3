package com.penglecode.xmodule.common.mybatis.mapper;

import com.penglecode.xmodule.common.domain.DomainObject;

/**
 * CRUD类操作Mybatis-Mapper基类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/10 20:02
 */
public interface BaseMybatisMapper<T extends DomainObject> extends BaseInsertMapper<T>, BaseUpdateMapper<T>, BaseDeleteMapper<T>, BaseQueryMapper<T> {

}
