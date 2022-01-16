package com.penglecode.xmodule.common.domain;

import com.penglecode.xmodule.common.support.ConvertibleObject;

/**
 * 领域对象(Domain-Object)基类，领域对象包括实体对象、聚合根、值对象等
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 15:15
 */
public interface DomainObject extends ConvertibleObject {

    /**
     * Domain-Object数据出站（从存储介质输出出去）时的加工处理
     * 例如根据statusCode(字典值)设置statusName(字典值的字面意思字段)
     *
     * @return 返回处理过得实体对象
     */
    default DomainObject processOutbound() {
        return this;
    }

    /**
     * Domain-Object数据入站（从外界进入当前应用）时的加工处理
     *
     * @return 返回处理过得实体对象
     */
    default DomainObject processInbound() {
        return this;
    }

}
