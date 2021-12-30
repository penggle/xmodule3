package com.penglecode.xmodule.common.mybatis.mapper;

import com.penglecode.xmodule.common.domain.DomainObject;

/**
 * 插入类操作Mybatis-Mapper基类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/10 20:02
 */
public interface BaseInsertMapper<T extends DomainObject> extends BaseBatchMapper {

	/**
	 * 插入实体
	 * @param model	- 实体对象
	 * @return 返回被更新条数
	 */
	int insertModel(T model);

}
