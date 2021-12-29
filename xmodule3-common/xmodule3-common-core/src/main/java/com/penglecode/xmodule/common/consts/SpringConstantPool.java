package com.penglecode.xmodule.common.consts;

import com.penglecode.xmodule.common.util.ObjectUtils;
import com.penglecode.xmodule.common.util.SpringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于Spring的常量池
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 14:02
 */
public class SpringConstantPool<T> implements ConstantPool<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringConstantPool.class);
	
	@Override
	public T get(Constant<T> constant) {
		if (constant instanceof SpringBeanConstant) {
			T bean = null;
			try {
				if(StringUtils.isNotEmpty(constant.name)) {
					bean = SpringUtils.getBean(constant.name);
				} else {
					bean = SpringUtils.getBean(constant.type);
				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
			return ObjectUtils.defaultIfNull(bean, constant.defaultValue);
		} else {
			return SpringUtils.getEnvProperty(constant.name, constant.type, constant.defaultValue);
		}
	}
	
}
