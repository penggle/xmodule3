package com.penglecode.xmodule.common.domain;

import com.penglecode.xmodule.common.support.Convertible;

/**
 * 领域对象(Domain-Object)基类，领域对象包括实体对象、聚合根、值对象等
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 15:15
 */
public interface DomainObject extends Convertible {

    /**
     * 领域对象数据出站（从存储介质输出出去）前的加工处理
     * 例如根据statusCode(字典值)设置statusName(字典值的字面意思字段)
     *
     * @return 返回处理过得实体对象
     */
    default DomainObject beforeOutbound() {
        return this;
    }

    /**
     * 领域对象数据入站（从外界进入当前应用）后的加工处理
     *
     * @return 返回处理过得实体对象
     */
    default DomainObject afterInbound() {
        return this;
    }

}
