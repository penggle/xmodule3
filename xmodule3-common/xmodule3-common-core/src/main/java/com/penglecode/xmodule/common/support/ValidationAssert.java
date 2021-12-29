package com.penglecode.xmodule.common.support;

import com.penglecode.xmodule.common.consts.ApplicationConstants;
import com.penglecode.xmodule.common.exception.ApplicationValidationException;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * 数据验证的Assert,所有抛出异常均为{@code ApplicationValidationException}
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/19 20:18
 */
public class ValidationAssert {

	private ValidationAssert() {}

	/**
	 * <p>对一个boolean表达式进行断言,如果表达式的值为false则抛出DataValidationException</p>
	 * 
	 * @param expression
	 * @param message
	 */
	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			throwApplicationValidationException(message);
		}
	}
	
	/**
	 * <p>对一个boolean表达式进行断言,如果表达式的值为false则抛出DataValidationException</p>
	 * 
	 * @param expression
	 * @param messageSupplier
	 */
	public static void isTrue(boolean expression, MessageSupplier messageSupplier) {
		if (!expression) {
			throwApplicationValidationException(messageSupplier);
		}
	}
	
	/**
	 * <p>如果object不为null则抛出DataValidationException</p>
	 * 
	 * @param object
	 * @param message
	 */
	public static void isNull(Object object, String message) {
		if (object != null) {
			throwApplicationValidationException(message);
		}
	}
	
	/**
	 * <p>如果object不为null则抛出DataValidationException</p>
	 * 
	 * @param object
	 * @param messageSupplier
	 */
	public static void isNull(Object object, MessageSupplier messageSupplier) {
		if (object != null) {
			throwApplicationValidationException(messageSupplier);
		}
	}
	
	/**
	 * <p>如果object为null则抛出DataValidationException</p>
	 * 
	 * @param object
	 * @param message
	 */
	public static void notNull(Object object, String message) {
		if (object == null) {
			throwApplicationValidationException(message);
		}
	}
	
	/**
	 * <p>如果object为null则抛出DataValidationException</p>
	 * 
	 * @param object
	 * @param messageSupplier
	 */
	public static void notNull(Object object, MessageSupplier messageSupplier) {
		if (object == null) {
			throwApplicationValidationException(messageSupplier);
		}
	}
	
	/**
	 * <p>如果object不为空值(null, "", " ", "null", empty collection, empty map, empty array)则抛出DataValidationException</p>
	 * 
	 * @param object
	 * @param message
	 */
	public static void isEmpty(Object object, String message) {
		if (!isEmptyObject(object)) {
			throwApplicationValidationException(message);
		}
	}
	
	/**
	 * <p>如果object不为空值(null, "", " ", "null", empty collection, empty map, empty array)则抛出DataValidationException</p>
	 * 
	 * @param object
	 * @param messageSupplier
	 */
	public static void isEmpty(Object object, MessageSupplier messageSupplier) {
		if (!isEmptyObject(object)) {
			throwApplicationValidationException(messageSupplier);
		}
	}
	
	/**
	 * <p>如果object为空值(null, "", " ", "null", empty collection, empty map, empty array)则抛出DataValidationException</p>
	 * 
	 * @param object
	 * @param message
	 */
	public static void notEmpty(Object object, String message) {
		if (isEmptyObject(object)) {
			throwApplicationValidationException(message);
		}
	}
	
	/**
	 * <p>如果object为空值(null, "", " ", "null", empty collection, empty map, empty array)则抛出DataValidationException</p>
	 * 
	 * @param object
	 * @param messageSupplier
	 */
	public static void notEmpty(Object object, MessageSupplier messageSupplier) {
		if (isEmptyObject(object)) {
			throwApplicationValidationException(messageSupplier);
		}
	}
	
	private static boolean isEmptyObject(Object object){
		if(object == null){
			return true;
		}else if(object instanceof String){
			return ((String)object).trim().equals("") || ((String)object).trim().equals("null");
		}else if(object instanceof Collection<?>){
			return ((Collection<?>)object).isEmpty();
		}else if(object instanceof Map<?,?>){
			return ((Map<?,?>)object).isEmpty();
		}else if(object.getClass().isArray()){
			return Array.getLength(object) == 0;
		}else{
			return false;
		}
	}
	
	/**
	 * <p>如果数组array中的元素存在null值,则抛出DataValidationException</p>
	 * 
	 * @param array
	 * @param message
	 */
	public static void noNullElements(Object[] array, String message) {
		if (array != null) {
			for (Object element : array) {
				if (element == null) {
					throwApplicationValidationException(message);
				}
			}
		}
	}
	
	/**
	 * <p>如果数组array中的元素存在null值,则抛出DataValidationException</p>
	 * 
	 * @param array
	 * @param messageSupplier
	 */
	public static void noNullElements(Object[] array, MessageSupplier messageSupplier) {
		if (array != null) {
			for (Object element : array) {
				if (element == null) {
					throwApplicationValidationException(messageSupplier);
				}
			}
		}
	}
	
	/**
	 * <p>如果集合collection中的元素存在null值,则抛出DataValidationException</p>
	 * 
	 * @param collection
	 * @param message
	 */
	public static void noNullElements(Collection<?> collection, String message) {
		if (collection != null) {
			for (Object element : collection) {
				if (element == null) {
					throwApplicationValidationException(message);
				}
			}
		}
	}
	
	/**
	 * <p>如果集合collection中的元素存在null值,则抛出DataValidationException</p>
	 * 
	 * @param collection
	 * @param messageSupplier
	 */
	public static void noNullElements(Collection<?> collection, MessageSupplier messageSupplier) {
		if (collection != null) {
			for (Object element : collection) {
				if (element == null) {
					throwApplicationValidationException(messageSupplier);
				}
			}
		}
	}
	
	/**
	 * <p>如果集合map中的元素存在value=null值,则抛出DataValidationException</p>
	 * 
	 * @param map
	 * @param message
	 */
	public static void noNullValues(Map<?,?> map, String message) {
		if (map != null) {
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				if (entry.getValue() == null) {
					throwApplicationValidationException(message);
				}
			}
		}
	}
	
	/**
	 * <p>如果集合map中的元素存在value=null值,则抛出DataValidationException</p>
	 * 
	 * @param map
	 * @param messageSupplier
	 */
	public static void noNullValues(Map<?,?> map, MessageSupplier messageSupplier) {
		if (map != null) {
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				if (entry.getValue() == null) {
					throwApplicationValidationException(messageSupplier);
				}
			}
		}
	}
	
	/**
	 * <p>如果集合map中的元素存在key=null值,则抛出DataValidationException</p>
	 * 
	 * @param map
	 * @param message
	 */
	public static void noNullKeys(Map<?,?> map, String message) {
		if (map != null) {
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				if (entry.getKey() == null) {
					throwApplicationValidationException(message);
				}
			}
		}
	}
	
	/**
	 * <p>如果集合map中的元素存在key=null值,则抛出DataValidationException</p>
	 * 
	 * @param map
	 * @param messageSupplier
	 */
	public static void noNullKeys(Map<?,?> map, MessageSupplier messageSupplier) {
		if (map != null) {
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				if (entry.getKey() == null) {
					throwApplicationValidationException(messageSupplier);
				}
			}
		}
	}
	
	/**
	 * <p>如果对象obj不是指定的类型type,则抛出DataValidationException</p>
	 * 
	 * @param type
	 * @param obj
	 * @param message
	 */
	public static void isInstanceOf(Class<?> type, Object obj, String message) {
		notNull(type, "Type to check against must not be null");
		if (!type.isInstance(obj)) {
			throwApplicationValidationException(message);
		}
	}
	
	/**
	 * <p>如果对象obj不是指定的类型type,则抛出DataValidationException</p>
	 * 
	 * @param type
	 * @param obj
	 * @param messageSupplier
	 */
	public static void isInstanceOf(Class<?> type, Object obj, MessageSupplier messageSupplier) {
		notNull(type, "Type to check against must not be null");
		if (!type.isInstance(obj)) {
			throwApplicationValidationException(messageSupplier);
		}
	}
	
	/**
	 * <p>如果subType的父类不是superType,则抛出DataValidationException</p>
	 * 
	 * @param superType
	 * @param subType
	 * @param message
	 */
	public static void isAssignable(Class<?> superType, Class<?> subType, String message) {
		notNull(superType, "Type to check against must not be null");
		if (subType == null || !superType.isAssignableFrom(subType)) {
			throwApplicationValidationException(message);
		}
	}
	
	/**
	 * <p>如果subType的父类不是superType,则抛出DataValidationException</p>
	 * 
	 * @param superType
	 * @param subType
	 * @param messageSupplier
	 */
	public static void isAssignable(Class<?> superType, Class<?> subType, MessageSupplier messageSupplier) {
		notNull(superType, "Type to check against must not be null");
		if (subType == null || !superType.isAssignableFrom(subType)) {
			throwApplicationValidationException(messageSupplier);
		}
	}

	protected static void throwApplicationValidationException(String message) {
		ErrorCode errorCode = GlobalErrorCode.ERR400; //参数校验类错误统一归属到HTTP(400)下面
		throw new ApplicationValidationException(errorCode.getCode(), errorCode.getMessage(message));
	}

	protected static void throwApplicationValidationException(MessageSupplier messageSupplier) {
		throwApplicationValidationException(messageSupplier.apply(ApplicationConstants.DEFAULT_MESSAGE_SOURCE_ACCESSOR.get()));
	}

}
