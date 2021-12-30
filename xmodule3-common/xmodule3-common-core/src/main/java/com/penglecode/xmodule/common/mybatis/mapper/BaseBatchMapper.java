package com.penglecode.xmodule.common.mybatis.mapper;

import org.apache.ibatis.annotations.Flush;
import org.apache.ibatis.executor.BatchResult;

import java.util.List;

/**
 * JDBC批处理类操作Mybatis-Mapper基类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/10 20:02
 */
public interface BaseBatchMapper {

	/**
	 * 刷新(发送)批量语句到数据库Server端执行，并返回结果
	 * @return
	 */
	@Flush
	List<BatchResult> flushStatements();

}
