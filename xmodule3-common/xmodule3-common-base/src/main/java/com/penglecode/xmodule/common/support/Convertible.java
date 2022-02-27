package com.penglecode.xmodule.common.support;

import java.io.Serializable;

/**
 * 可转换的对象
 * 在应用内各种O都应该实现这个标记接口，用于实现应用内的XXO转XXO的需求，
 * 例如实现：PO -> DO、DO -> DTO、DTO -> VO等之间的转换
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/6/14 10:49
 */
public interface Convertible extends Serializable {
}
