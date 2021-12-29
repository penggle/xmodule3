package com.penglecode.xmodule.common.consts;

/**
 * 常量池
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 14:02
 */
public interface ConstantPool<T> {

	/**
	 * 从常量池中获取常量值
	 * @param constant
	 * @return
	 */
	T get(Constant<T> constant);
	
}