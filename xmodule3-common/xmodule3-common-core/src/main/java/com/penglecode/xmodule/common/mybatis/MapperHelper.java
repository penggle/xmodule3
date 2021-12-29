package com.penglecode.xmodule.common.mybatis;


import com.penglecode.xmodule.common.mybatis.dsl.QueryColumns;
import com.penglecode.xmodule.common.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Mybatis-Mapper辅助类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 14:02
 */
public class MapperHelper {

    private MapperHelper() {}

    public static String getMapperKey(Class<?> domainClass, String key) {
        return domainClass.getName() + "Mapper." + key;
    }

    public static boolean isEmpty(Object paramObj) {
        return !isNotEmpty(paramObj);
    }

    public static boolean isNotEmpty(Object paramObj) {
        if (paramObj == null) {
            return false;
        }
        if (paramObj instanceof String) {
            String str = (String) paramObj;
            return StringUtils.isNotEmpty(str);
        }
        if (paramObj.getClass().isArray()) {
            return Array.getLength(paramObj) > 0;
        }
        if (paramObj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) paramObj;
            return !map.isEmpty();
        }
        if (paramObj instanceof Collection) {
            Collection<?> collection = (Collection<?>) paramObj;
            return !collection.isEmpty();
        }
        return true;
    }

    public static boolean isArrayOrCollection(Object paramObj) {
        if (paramObj == null) {
            return false;
        }
        return paramObj instanceof Collection || paramObj.getClass().isArray();
    }
    
    public static boolean containsColumn(Map<String,Object> columnNames, String columnName) {
    	if(columnNames != null) {
    		return columnNames.containsKey(columnName);
    	}
    	return false;
    }

    public static boolean containsColumn(QueryColumns[] queryColumnArray, String columnName) {
        boolean selected = true;
        QueryColumns queryColumns = (queryColumnArray != null && queryColumnArray.length > 0) ? queryColumnArray[0] : null;
        if(queryColumns != null) {
            Set<String> selectColumns = queryColumns.getSelectColumns();
            if(!CollectionUtils.isEmpty(selectColumns)) {
                for(String selectColumn : selectColumns) {
                    if(selectColumn.equals(columnName)) {
                        return true;
                    }
                }
                selected = false;
            }
            if(queryColumns.getSelectPredicate() != null) {
                return queryColumns.getSelectPredicate().test(columnName);
            }
        }
        return selected;
    }

}
