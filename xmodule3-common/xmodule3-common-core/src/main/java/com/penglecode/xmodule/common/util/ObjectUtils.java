package com.penglecode.xmodule.common.util;

import java.util.function.Supplier;

/**
 * 有关object的工具类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 14:02
 */
public class ObjectUtils extends org.springframework.util.ObjectUtils {

	private ObjectUtils() {}

	/**
	 * <p>如果object为null则返回defaultValue</p>
	 * 
	 * @param object
	 * @param defaultValue
	 * @return
	 */
	public static <T> T defaultIfNull(T object, T defaultValue) {
        return object != null ? object : defaultValue;
    }

	/**
	 * <p>如果object为null则返回defaultValue</p>
	 *
	 * @param object
	 * @param defaultValue
	 * @return
	 */
	public static <T> T defaultIfNull(T object, Supplier<T> defaultValue) {
		if(object == null) {
			return defaultValue != null ? defaultValue.get() : null;
		}
		return object;
	}
	
	/**
	 * <p>通过==或equals方法来比较object1与object2是否相等</p>
	 * 
	 * @param object1
	 * @param object2
	 * @return
	 */
	public static boolean equals(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }
        if (object1 == null || object2 == null) {
            return false;
        }
        return object1.equals(object2);
    }
	
	/**
	 * <p>Null-safe 调用Object.toString()方法</p>
	 * 
	 * @param object
	 * @return
	 */
	public static String toString(Object object) {
        return object == null ? "" : object.toString();
    }
	
	/**
	 * <p>Null-safe 找出一组对象中的最小值</p>
	 * 
	 * @param values
	 * @return	如果所有元素都不为空则返回不为null的&最小的那个元素,否则返回null
	 */
	@SafeVarargs
	public static <T extends Comparable<? super T>> T min(T... values) {
        T result = null;
        if (values != null) {
            for (T value : values) {
                if (compare(value, result, true) < 0) {
                    result = value;
                }
            }
        }
        return result;
    }
	
	/**
	 * <p>Null-safe 找出一组对象中的最大值</p>
	 * 
	 * @param values
	 * @return 如果所有元素都不为空则返回不为null的&最大的那个元素,否则返回null
	 */
	@SafeVarargs
	public static <T extends Comparable<? super T>> T max(T... values) {
        T result = null;
        if (values != null) {
            for (T value : values) {
                if (compare(value, result, false) > 0) {
                    result = value;
                }
            }
        }
        return result;
    }
	
	/**
	 * <p>Null-safe 比较两个对象
	 * (如果c1、c2中存在null值,则认为其为最小值)</p>
	 * 
	 * @param c1			- 第一个比较参数,可能为空
	 * @param c2			- 第二个比较参数,可能为空
	 * @return				- 负数：c1 > c2,0：c1 = c2,正数c1 > c2
	 */
	public static <T extends Comparable<? super T>> int compare(T c1, T c2) {
        return compare(c1, c2, false);
    }
	
	/**
	 * <p>Null-safe 比较两个对象</p>
	 * 
	 * @param c1			- 第一个比较参数,可能为空
	 * @param c2			- 第二个比较参数,可能为空 
	 * @param nullGreater	- 如果为true则认为null是较大的
	 * @return				- 负数：c1 > c2,0：c1 = c2,正数c1 > c2
	 */
	public static <T extends Comparable<? super T>> int compare(T c1, T c2, boolean nullGreater) {
        if (c1 == c2) {
            return 0;
        } else if (c1 == null) {
            return nullGreater ? 1 : -1;
        } else if (c2 == null) {
            return nullGreater ? -1 : 1;
        }
        return c1.compareTo(c2);
    }
	
}
