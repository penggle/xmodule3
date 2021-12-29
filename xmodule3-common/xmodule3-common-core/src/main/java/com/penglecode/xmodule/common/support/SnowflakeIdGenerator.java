package com.penglecode.xmodule.common.support;

import org.springframework.core.NamedThreadLocal;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于Twitter/snowflake 算法的分布式全局id生成器
 * 64位ID (42(毫秒)+5(机器ID)+5(业务编码)+12(重复累加)) 
 * 
 * 整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞（由datacenter和机器ID作区分），
 * 并且效率较高，经测试，snowflake每秒能够产生26万ID左右，完全满足需要。
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/27 19:12
 */
public class SnowflakeIdGenerator implements IdGenerator<Long> {

	private static final long TWEPOCH = 1288834974657L;

	/**
	 * 机器标识位数
	 */
	private static final long WORKER_ID_BITS = 5L;

	/**
	 * 数据中心标识位数
	 */
	private static final long DATACENTER_ID_BITS = 5L;

	/**
	 * 机器ID最大值
	 */
	private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

	/**
	 * 数据中心ID最大值
	 */
	private static final long MAX_DATA_CENTER_ID = ~(-1L << DATACENTER_ID_BITS);

	/**
	 * 毫秒内自增位
	 */
	private static final long SEQUENCE_BITS = 12L;

	/**
	 * 机器ID偏左移12位
	 */
	private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

	/**
	 * 数据中心ID左移17位
	 */
	private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

	/**
	 * 时间毫秒左移22位
	 */
	private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;

	private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

	private long lastTimestamp = -1L;

	private long sequence = 0L;

	private final long datacenterId;

	private final long workerId;

	private final NamedThreadLocal<Long> clockDelta = new NamedThreadLocal<>("clock delta");

	/**
	 * @param datacenterId	- 数据中心id,范围[0,31],比如用户中心为0,订单中心为1等等
	 * @param workerId		- 机器ID,范围[0,31],比如用户中心有10台机器做集群,则依次分配0-9
	 */
	public SnowflakeIdGenerator(long datacenterId, long workerId) {
		Assert.isTrue(datacenterId <= MAX_DATA_CENTER_ID && datacenterId >= 0, "datacenter Id can't be greater than %d or less than 0");
		Assert.isTrue(workerId <= MAX_WORKER_ID && workerId >= 0, "worker Id can't be greater than %d or less than 0");
		this.workerId = workerId;
		this.datacenterId = datacenterId;
	}

	public synchronized Long nextId() {
		return getNextId();
	}
	
	public synchronized List<Long> nextIds(int batchSize) {
		Assert.isTrue(batchSize > 1, "worker Id can't be less than 1");
		List<Long> list = new ArrayList<>((int)(batchSize * 1.5));
		for(int i = 0; i < batchSize; i++){
			list.add(getNextId());
		}
		return list;
	}
	
	protected long getNextId() {
		long timestamp = timeGen();
		if (timestamp < lastTimestamp) {
			throw new KeyGeneratorException("Clock moved backwards.  Refusing to generate id for " + (lastTimestamp - timestamp) + " milliseconds");
		}

		if (lastTimestamp == timestamp) {
			// 当前毫秒内，则+1
			sequence = (sequence + 1) & SEQUENCE_MASK;
			if (sequence == 0) {
				// 当前毫秒内计数满了，则等待下一秒
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0;
		}
		lastTimestamp = timestamp;
		// ID偏移组合生成最终的ID，并返回ID

		return ((timestamp - TWEPOCH) << TIMESTAMP_LEFT_SHIFT)
				| (datacenterId << DATACENTER_ID_SHIFT)
				| (workerId << WORKER_ID_SHIFT) | sequence;
	}

	private long tilNextMillis(final long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	private long timeGen() {
		Long delta = clockDelta.get();
		return System.currentTimeMillis() + (delta == null ? 0 : delta);
	}

	public void setClockDelta(long delta) {
		clockDelta.set(delta);
	}

}