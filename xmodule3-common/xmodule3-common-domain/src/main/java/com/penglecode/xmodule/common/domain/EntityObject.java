package com.penglecode.xmodule.common.domain;

import java.io.Serializable;

/**
 * 实体对象(Entity-Object)基类，实体对象直接对应着数据库的表
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 15:15
 */
public interface EntityObject extends DomainObject {

    /**
     * 实体对象的唯一标识
     *
     * @return
     */
    default Serializable identity() {
        return null;
    }

}
