package com.penglecode.xmodule.common.support;

import com.penglecode.xmodule.common.domain.DomainObject;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.ObjectUtils;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 应用服务辅助类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/7/23 19:18
 */
public class AppServiceHelper {

    private AppServiceHelper() {}

    /**
     * 对于客户端提交过来的待修改领域对象列表(transientDomainObjects)，通过数据库中的参照领域对象列表(persistentDomainObjects)来对其进行分组，
     * 分成待新增、待更新、待删除的三组，然后依次对其进行新增、修改、删除操作
     *
     * @param transientDomainObjects    - 待持久化领域对象列表
     * @param persistentDomainObjects   - 当前数据库中持久化的领域对象列表，用于参照
     * @param domainObjectIdGetter      - 获取领域对象的ID的方法引用
     * @param batchCreator              - 领域对象批量新增服务方法引用
     * @param batchModifier             - 领域对象批量更新服务方法引用
     * @param batchRemover              - 领域对象批量删除服务方法引用
     * @param <T>                       - 被保存领域对象
     * @param <K>                       - 领域对象的ID
     */
    public static <T extends DomainObject, K extends Serializable> void batchSaveDomainObjects(List<T> transientDomainObjects, List<T> persistentDomainObjects, Function<T,K> domainObjectIdGetter, Consumer<List<T>> batchCreator, Consumer<List<T>> batchModifier, Consumer<List<K>> batchRemover) {
        Map<OperationGroup,List<T>> groupedDomainObjects = groupByOperation(transientDomainObjects, persistentDomainObjects, domainObjectIdGetter);
        List<T> createDomainObjects = groupedDomainObjects.get(OperationGroup.CREATE);
        if(!CollectionUtils.isEmpty(createDomainObjects)) {
            batchCreator.accept(createDomainObjects);
        }
        List<T> modifyDomainObjects = groupedDomainObjects.get(OperationGroup.MODIFY);
        if(!CollectionUtils.isEmpty(modifyDomainObjects)) {
            batchModifier.accept(modifyDomainObjects);
        }
        List<T> removeDomainObjects = groupedDomainObjects.get(OperationGroup.REMOVE);
        if(!CollectionUtils.isEmpty(removeDomainObjects)) {
            batchRemover.accept(removeDomainObjects.stream().map(domainObjectIdGetter).collect(Collectors.toList()));
        }
    }
    
    /**
     * 对于客户端提交过来的待修改领域对象列表(transientDomainObjects)，通过数据库中的参照领域对象列表(persistentDomainObjects)来对其进行分组，
     * 分成待新增、待更新、待删除的三组
     *
     * @param transientDomainObjects    - 待持久化领域对象列表
     * @param persistentDomainObjects   - 当前数据库中持久化的领域对象列表，用于参照
     * @param domainObjectIdGetter      - 获取领域对象的ID的方法引用
     * @param <T>                       - 被保存领域对象
     * @param <K>                       - 领域对象的ID
     * @return
     */
    public static <T extends DomainObject, K extends Serializable> Map<OperationGroup,List<T>> groupByOperation(List<T> transientDomainObjects, List<T> persistentDomainObjects, Function<T,K> domainObjectIdGetter) {
        persistentDomainObjects = CollectionUtils.isEmpty(persistentDomainObjects) ? Collections.emptyList() : persistentDomainObjects;
        EnumMap<OperationGroup,List<T>> groupedDomainObjects = new EnumMap<>(OperationGroup.class);
        groupedDomainObjects.put(OperationGroup.CREATE, new ArrayList<>());
        groupedDomainObjects.put(OperationGroup.MODIFY, new ArrayList<>());
        groupedDomainObjects.put(OperationGroup.REMOVE, new ArrayList<>());
        groupByCreateAndModify(transientDomainObjects, persistentDomainObjects, domainObjectIdGetter, groupedDomainObjects);
        groupByRemove(transientDomainObjects, persistentDomainObjects, domainObjectIdGetter, groupedDomainObjects);
        return groupedDomainObjects;
    }

    /**
     * 处理新增和修改逻辑
     */
    private static <T extends DomainObject, K extends Serializable> void groupByCreateAndModify(List<T> transientDomainObjects, List<T> persistentDomainObjects, Function<T,K> domainObjectIdGetter, EnumMap<OperationGroup,List<T>> groupedDomainObjects) {
        for(T updateDomainObject : transientDomainObjects) {
            Serializable updateDomainObjectId = domainObjectIdGetter.apply(updateDomainObject);
            if(!ObjectUtils.isEmpty(updateDomainObjectId)) {
                for(T dbDomainObject : persistentDomainObjects) {
                    Serializable dbDomainObjectId = domainObjectIdGetter.apply(dbDomainObject);
                    if(updateDomainObjectId.equals(dbDomainObjectId)) { //客户端传来的领域对象的ID在数据库中存在，则表示更新
                        groupedDomainObjects.get(OperationGroup.MODIFY).add(updateDomainObject);
                        break;
                    }
                }
            } else { //客户端传来的领域对象的ID为空，则表示新增
                groupedDomainObjects.get(OperationGroup.CREATE).add(updateDomainObject);
            }
        }
    }

    /**
     * 处理删除逻辑
     */
    private static <T extends DomainObject, K extends Serializable> void groupByRemove(List<T> transientDomainObjects, List<T> persistentDomainObjects, Function<T,K> domainObjectIdGetter, EnumMap<OperationGroup,List<T>> groupedDomainObjects) {
        for(T dbDomainObject : persistentDomainObjects) {
            boolean remove = true;
            Serializable dbDomainObjectId = domainObjectIdGetter.apply(dbDomainObject);
            for(T updateDomainObject : transientDomainObjects) {
                Serializable updateDomainObjectId = domainObjectIdGetter.apply(updateDomainObject);
                if(!ObjectUtils.isEmpty(updateDomainObjectId) && updateDomainObjectId.equals(dbDomainObjectId)) {
                    remove = false;
                }
            }
            if(remove) { //数据库的领域对象ID在客户端传来的领域对象中不存在，则表示删除
                groupedDomainObjects.get(OperationGroup.REMOVE).add(dbDomainObject);
            }
        }
    }

    public enum OperationGroup {

        CREATE, MODIFY, REMOVE

    }
}
