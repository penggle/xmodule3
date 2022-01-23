package com.penglecode.xmodule.common.codegen.support;

/**
 * 查询Where条件运算符
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/23 12:24
 */
public enum QueryConditionOperator {

	EQ("eq", "等值查询条件"),
	LIKE("like", "全like查询条件"),
	LIKE_LEFT("likeLeft", "左like查询条件"),
	LIKE_RIGHT("likeLeft", "右like查询条件"),
	GT("gt", "大于查询条件"),
	LT("lt", "小于查询条件"),
	GTE("gte", "大于等于查询条件"),
	LTE("lte", "小于等于查询条件"),
	IN("in", "包含查询条件");
	
	private final String opName;
	
	private final String opDesc;

	QueryConditionOperator(String opName, String opDesc) {
		this.opName = opName;
		this.opDesc = opDesc;
	}

	public String getOpName() {
		return opName;
	}

	public String getOpDesc() {
		return opDesc;
	}

	public static QueryConditionOperator of(String opName) {
		for(QueryConditionOperator operator : values()) {
			if(operator.getOpName().equals(opName)) {
				return operator;
			}
		}
		return null;
	}

	/**
	 * 当前条件是否是in查询条件?
	 * @return
	 */
	public boolean isInQueryCondition() {
		return QueryConditionOperator.IN.equals(this);
	}

	/**
	 * 当前条件是否是区间查询条件?
	 * @return
	 */
	public boolean isRangeQueryCondition() {
		return QueryConditionOperator.GT.equals(this) || QueryConditionOperator.LT.equals(this) || QueryConditionOperator.GTE.equals(this) || QueryConditionOperator.LTE.equals(this);
	}

	/**
	 * 当前条件是否是>或>=区间查询条件?
	 * @return
	 */
	public boolean isGTRangeQueryCondition() {
		return QueryConditionOperator.GT.equals(this) || QueryConditionOperator.GTE.equals(this);
	}

	/**
	 * 当前条件是否是<或<=区间查询条件?
	 * @return
	 */
	public boolean isLTRangeQueryCondition() {
		return QueryConditionOperator.LT.equals(this) || QueryConditionOperator.LTE.equals(this);
	}

}
