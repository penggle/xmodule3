package com.penglecode.xmodule.common.support;

import org.springframework.core.NestedRuntimeException;

import java.util.List;

/**
 * 全局唯一主键生成器
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/27 19:12
 */
public interface IdGenerator<T> {

	/**
	 * 生成一个主键
	 * @return
	 */
	T nextId();
	
	/**
	 * 一次生成多个主键
	 * @return
	 */
	List<T> nextIds(int batchSize);
	
	class KeyGeneratorException extends NestedRuntimeException {

		private static final long serialVersionUID = 1L;

		public KeyGeneratorException(String msg) {
			super(msg);
		}

		public KeyGeneratorException(String msg, Throwable cause) {
			super(msg, cause);
		}
		
	}
	
}
