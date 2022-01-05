package com.penglecode.xmodule.common.util;

import com.penglecode.xmodule.common.domain.EntityObject;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

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
public class AppServiceUtils {

    private AppServiceUtils() {}

    /**
     * 批量Merge客户端传来的实体对象进入数据库
     *
     * 对于客户端提交过来的待修改实体对象列表(transientEntityObjects)，通过数据库中的参照实体对象列表(persistedEntityObjects)来对其进行分组，
     * 分成待新增、待更新、待删除的三组，然后依次对其进行新增、修改、删除操作
     *
     * @param transientEntityObjects   - 待持久化实体对象列表
     * @param persistedEntityObjects   - 当前数据库中持久化的实体对象列表，用于参照
     * @param entityObjectIdGetter     - 获取实体对象的ID的方法引用
     * @param batchCreator              - 实体对象批量新增服务方法引用
     * @param batchModifier             - 实体对象批量更新服务方法引用
     * @param batchRemover              - 实体对象批量删除服务方法引用
     * @param <T>                      - 被保存实体对象
     * @param <K>                      - 实体对象的ID
     */
    public static <T extends EntityObject, K extends Serializable> void batchMergeEntityObjects(List<T> transientEntityObjects, List<T> persistedEntityObjects, Function<T,K> entityObjectIdGetter, Consumer<List<T>> batchCreator, Consumer<List<T>> batchModifier, Consumer<List<K>> batchRemover) {
        Map<OperationGroup,List<T>> groupedEntityObjects = groupByOperation(transientEntityObjects, persistedEntityObjects, entityObjectIdGetter);
        List<T> createEntityObjects = groupedEntityObjects.get(OperationGroup.CREATE);
        if(!CollectionUtils.isEmpty(createEntityObjects)) {
            batchCreator.accept(createEntityObjects);
        }
        List<T> modifyEntityObjects = groupedEntityObjects.get(OperationGroup.MODIFY);
        if(!CollectionUtils.isEmpty(modifyEntityObjects)) {
            batchModifier.accept(modifyEntityObjects);
        }
        List<T> removeEntityObjects = groupedEntityObjects.get(OperationGroup.REMOVE);
        if(!CollectionUtils.isEmpty(removeEntityObjects)) {
            batchRemover.accept(removeEntityObjects.stream().map(entityObjectIdGetter).collect(Collectors.toList()));
        }
    }
    
    /**
     * 对于客户端提交过来的待修改实体对象列表(transientEntityObjects)，通过数据库中的参照实体对象列表(persistedEntityObjects)来对其进行分组，
     * 分成待新增、待更新、待删除的三组
     *
     * @param transientEntityObjects    - 待持久化实体对象列表
     * @param persistedEntityObjects    - 当前数据库中持久化的实体对象列表，用于参照
     * @param entityObjectIdGetter      - 获取实体对象的ID的方法引用
     * @param <T>                       - 被保存实体对象
     * @param <K>                       - 实体对象的ID
     * @return
     */
    public static <T extends EntityObject, K extends Serializable> Map<OperationGroup,List<T>> groupByOperation(List<T> transientEntityObjects, List<T> persistedEntityObjects, Function<T,K> entityObjectIdGetter) {
        persistedEntityObjects = CollectionUtils.isEmpty(persistedEntityObjects) ? Collections.emptyList() : persistedEntityObjects;
        EnumMap<OperationGroup,List<T>> groupedEntityObjects = new EnumMap<>(OperationGroup.class);
        groupedEntityObjects.put(OperationGroup.CREATE, new ArrayList<>());
        groupedEntityObjects.put(OperationGroup.MODIFY, new ArrayList<>());
        groupedEntityObjects.put(OperationGroup.REMOVE, new ArrayList<>());
        groupByCreateAndModify(transientEntityObjects, persistedEntityObjects, entityObjectIdGetter, groupedEntityObjects);
        groupByRemove(transientEntityObjects, persistedEntityObjects, entityObjectIdGetter, groupedEntityObjects);
        return groupedEntityObjects;
    }

    /**
     * 处理新增和修改逻辑
     */
    private static <T extends EntityObject, K extends Serializable> void groupByCreateAndModify(List<T> transientEntityObjects, List<T> persistedEntityObjects, Function<T,K> entityObjectIdGetter, EnumMap<OperationGroup,List<T>> groupedEntityObjects) {
        for(T transientEntityObject : transientEntityObjects) {
            Serializable transientEntityObjectId = entityObjectIdGetter.apply(transientEntityObject);
            if(!ObjectUtils.isEmpty(transientEntityObjectId)) {
                boolean existsed = false;
                for(T persistedEntityObject : persistedEntityObjects) {
                    Serializable persistedEntityObjectId = entityObjectIdGetter.apply(persistedEntityObject);
                    if(transientEntityObjectId.equals(persistedEntityObjectId)) {
                        existsed = true;
                        break;
                    }
                }
                if(existsed) { //客户端传来的实体对象的ID在数据库中存在，则表示修改
                    groupedEntityObjects.get(OperationGroup.MODIFY).add(transientEntityObject);
                } else { //客户端传来的实体对象的ID在数据库中不存在，则表示新增
                    groupedEntityObjects.get(OperationGroup.CREATE).add(transientEntityObject);
                }
            } else { //客户端传来的实体对象的ID为空，则表示新增
                groupedEntityObjects.get(OperationGroup.CREATE).add(transientEntityObject);
            }
        }
    }

    /**
     * 处理删除逻辑
     */
    private static <T extends EntityObject, K extends Serializable> void groupByRemove(List<T> transientEntityObjects, List<T> persistedEntityObjects, Function<T,K> entityObjectIdGetter, EnumMap<OperationGroup,List<T>> groupedEntityObjects) {
        for(T persistedEntityObject : persistedEntityObjects) {
            boolean remove = true;
            Serializable persistedEntityObjectId = entityObjectIdGetter.apply(persistedEntityObject);
            for(T transientEntityObject : transientEntityObjects) {
                Serializable transientEntityObjectId = entityObjectIdGetter.apply(transientEntityObject);
                if(!ObjectUtils.isEmpty(transientEntityObjectId) && transientEntityObjectId.equals(persistedEntityObjectId)) {
                    remove = false;
                }
            }
            if(remove) { //数据库的实体对象ID在客户端传来的实体对象中不存在，则表示删除
                groupedEntityObjects.get(OperationGroup.REMOVE).add(persistedEntityObject);
            }
        }
    }

    public enum OperationGroup {

        CREATE, MODIFY, REMOVE

    }
}
