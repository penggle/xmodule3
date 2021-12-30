package com.penglecode.xmodule.common.mybatis.mapper;

import com.penglecode.xmodule.common.domain.DomainObject;
import com.penglecode.xmodule.common.mybatis.dsl.QueryCriteria;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Map;

/**
 * 更新类操作Mybatis-Mapper基类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/10 20:02
 */
public interface BaseUpdateMapper<T extends DomainObject> extends BaseBatchMapper {

	/**
	 * 根据ID更新指定的实体字段
	 * @param id			- 主键ID
	 * @param columns		- 被更新的字段键值对
	 * @return 返回被更新条数
	 */
	int updateModelById(@Param("id") Serializable id, @Param("columns") Map<String,Object> columns);

	/**
	 * 根据指定的条件更新指定的实体字段
	 * @param criteria		- 更新范围条件(不能为null)
	 * @param columns		- 被更新的字段键值对
	 * @return 返回被更新条数
	 */
	int updateModelByCriteria(@Param("criteria") QueryCriteria<T> criteria, @Param("columns") Map<String,Object> columns);
	
}
