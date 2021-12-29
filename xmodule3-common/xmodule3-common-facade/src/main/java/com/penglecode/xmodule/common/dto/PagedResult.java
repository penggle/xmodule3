package com.penglecode.xmodule.common.dto;

import com.penglecode.xmodule.common.support.GlobalErrorCode;
import com.penglecode.xmodule.common.support.ErrorCode;

import java.util.Map;

/**
 * 通用返回结果类(针对分页)
 * 
 * @param <T>
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 15:15
 */
public class PagedResult<T> extends Result<T> {

	private static final long serialVersionUID = 1L;
	
	/** 当存在分页查询时此值为总记录数 */
	private int totalRowCount;

	PagedResult() {}

	PagedResult(Result<T> result, int totalRowCount) {
		super(result);
		this.totalRowCount = totalRowCount;
	}

	public int getTotalRowCount() {
		return totalRowCount;
	}

	protected void setTotalRowCount(int totalRowCount) {
		this.totalRowCount = totalRowCount;
	}

	public static PageBuilder success() {
		ErrorCode ok = GlobalErrorCode.OK;
		return new PageBuilder(Boolean.TRUE, null, ok.getCode(), ok.getMessage());
	}

	public static PageBuilder failure() {
		ErrorCode err = GlobalErrorCode.ERR;
		return new PageBuilder(Boolean.FALSE, null, err.getCode(), err.getMessage());
	}

	@Override
	public String toString() {
		return "PagedResult [success=" + isSuccess() + ", app=" + getApp() + ", code=" + getCode() + ", message="
				+ getMessage() + ", data=" + getData() + ", totalRowCount=" + getTotalRowCount() + "]";
	}

	@Override
	public Map<String, Object> asMap() {
		Map<String, Object> map = super.asMap();
		map.put("totalRowCount", totalRowCount);
		return map;
	}

	public static class PageBuilder extends Builder {

		private int totalRowCount = 0;

		PageBuilder(boolean success, String app, String code, String message) {
			super(success, app, code, message);
		}

		public PageBuilder totalRowCount(int totalRowCount) {
			this.totalRowCount = totalRowCount;
			return this;
		}

		@Override
		public PageBuilder app(String app) {
			return (PageBuilder) super.app(app);
		}

		@Override
		public PageBuilder code(String code) {
			return (PageBuilder) super.code(code);
		}

		@Override
		public PageBuilder message(String message) {
			return (PageBuilder) super.message(message);
		}

		@Override
		public PageBuilder data(Object data) {
			return (PageBuilder) super.data(data);
		}

		@Override
		public <T> PagedResult<T> build() {
			return new PagedResult<>(super.build(), totalRowCount);
		}
		
	}
	
}
