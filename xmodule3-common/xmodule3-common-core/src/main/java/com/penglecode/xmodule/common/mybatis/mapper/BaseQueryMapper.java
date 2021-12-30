package com.penglecode.xmodule.common.mybatis.mapper;

import com.penglecode.xmodule.common.domain.DomainObject;
import com.penglecode.xmodule.common.mybatis.dsl.QueryColumns;
import com.penglecode.xmodule.common.mybatis.dsl.QueryCriteria;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;
import java.util.List;

/**
 * 查询类操作Mybatis-Mapper基类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/10 20:02
 */
public interface BaseQueryMapper<T extends DomainObject> {

	/**
	 * 根据ID查询单个结果集
	 * @param id		- 主键ID
	 * @param columns 	- 指定查询返回的列(这里的列指的是实体对象<T>中的字段)，这里使用JAVA可变参数特性的讨巧写法，实际只取columns[0]最为参数
	 * @return 返回单个结果集
	 */
	T selectModelById(@Param("id") Serializable id, @Param("columns") QueryColumns... columns);
	
	/**
	 * 根据条件获取查询单个结果集
	 * (注意：如果匹配到多个结果集将报错)
	 * @param criteria	- 查询条件(不能为null)
	 * @param columns 	- 指定查询返回的列(这里的列指的是实体对象<T>中的字段)，这里使用JAVA可变参数特性的讨巧写法，实际只取columns[0]最为参数
	 * @return 返回单个结果集
	 */
	T selectModelByCriteria(@Param("criteria") QueryCriteria<T> criteria, @Param("columns") QueryColumns... columns);

	/**
	 * 根据条件获取查询COUNT
	 * @param criteria	- 查询条件(不能为null)
	 * @return 返回单个结果集
	 */
	int selectModelCountByCriteria(@Param("criteria") QueryCriteria<T> criteria);
	
	/**
	 * 根据多个ID查询结果集
	 * @param ids		- 主键ID列表
	 * @param columns 	- 指定查询返回的列(这里的列指的是实体对象<T>中的字段)，这里使用JAVA可变参数特性的讨巧写法，实际只取columns[0]最为参数
	 * @return 返回结果集
	 */
	List<T> selectModelListByIds(@Param("ids") List<? extends Serializable> ids, @Param("columns") QueryColumns... columns);
	
	/**
	 * 查询所有结果集(数据量大时慎用)
	 * @param columns 	- 指定查询返回的列(这里的列指的是实体对象<T>中的字段)，这里使用JAVA可变参数特性的讨巧写法，实际只取columns[0]最为参数
	 * @return 返回所有结果集
	 */
	Cursor<T> selectAllModelList(@Param("columns") QueryColumns... columns);
	
	/**
	 * 查询所有结果集计数
	 * @return 返回所有记录数
	 */
	int selectAllModelCount();
	
	/**
	 * 根据条件查询结果集
	 * @param criteria	- 查询条件(为null则查询所有)
	 * @param columns 	- 指定查询返回的列(这里的列指的是实体对象<T>中的字段)，这里使用JAVA可变参数特性的讨巧写法，实际只取columns[0]最为参数
	 * @return 返回结果集
	 */
	List<T> selectModelListByCriteria(@Param("criteria") QueryCriteria<T> criteria, @Param("columns") QueryColumns... columns);
	
	/**
	 * 根据条件查询结果集(分页)
	 * @param criteria	- 查询条件(为null则查询所有)
	 * @param rowBounds	- 分页参数
	 * @param columns 	- 指定查询返回的列(这里的列指的是实体对象<T>中的字段)，这里使用JAVA可变参数特性的讨巧写法，实际只取columns[0]最为参数
	 * @return 返回结果集
	 */
	List<T> selectModelPageListByCriteria(@Param("criteria") QueryCriteria<T> criteria, RowBounds rowBounds, @Param("columns") QueryColumns... columns);
	
	/**
	 * 根据条件查询结果集计数
	 * @param criteria	- 查询条件(为null则查询所有)
	 * @return 返回记录数
	 */
	int selectModelPageCountByCriteria(@Param("criteria") QueryCriteria<T> criteria);

}
