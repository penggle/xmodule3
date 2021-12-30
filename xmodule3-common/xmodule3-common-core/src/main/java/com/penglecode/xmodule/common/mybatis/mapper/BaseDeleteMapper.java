package com.penglecode.xmodule.common.mybatis.mapper;

import com.penglecode.xmodule.common.domain.DomainObject;
import com.penglecode.xmodule.common.mybatis.dsl.QueryCriteria;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 删除类操作Mybatis-Mapper基类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/10 20:02
 */
public interface BaseDeleteMapper<T extends DomainObject> extends BaseBatchMapper {

	/**
	 * 根据ID删除实体
	 * @param id		- 主键ID
	 * @return 返回被删除条数
	 */
	int deleteModelById(@Param("id") Serializable id);
	
	/**
	 * 根据多个ID批量删除实体
	 * @param ids		- 主键ID列表
	 * @return 返回被删除条数
	 */
	int deleteModelByIds(@Param("ids") List<? extends Serializable> ids);

	/**
	 * 根据指定的条件删除实体数据
	 * @param criteria	- 删除范围条件(不能为null)
	 * @return 返回被删除条数
	 */
	int deleteModelByCriteria(@Param("criteria") QueryCriteria<T> criteria);
	
}
