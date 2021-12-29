package com.penglecode.xmodule.common.mybatis.executor;

import org.apache.ibatis.session.ExecutorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Mybatis Executor同步管理器
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/7/24 15:02
 */
public class ExecutorSynchronizationManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorSynchronizationManager.class);

    private static final NamedThreadLocal<ExecutorType> currentExecutorType = new NamedThreadLocal<>("The ExecutorType of current thread inuse");

    private ExecutorSynchronizationManager() {}

    /**
     * 设置当前线程上下文的ExecutorType
     * @param executorType
     */
    public static void setCurrentExecutorType(ExecutorType executorType) {
        ExecutorType currentType = currentExecutorType.get();
        LOGGER.debug("Set current ExecutorType from [{}] to [{}]", currentType == null ? "DEFAULT" : currentType, executorType);
        currentExecutorType.set(executorType);
        if(executorType == ExecutorType.BATCH && !TransactionSynchronizationManager.isActualTransactionActive()) {
            LOGGER.warn("There is no actual active transaction found, activating JDBC batches is also futile!");
        }
    }

    public static ExecutorType getCurrentExecutorType() {
        return currentExecutorType.get();
    }

    public static void resetCurrentExecutorType() {
        LOGGER.debug("Reset current ExecutorType from [{}] to [{}]", currentExecutorType.get(), "DEFAULT");
        currentExecutorType.remove();
    }

}
