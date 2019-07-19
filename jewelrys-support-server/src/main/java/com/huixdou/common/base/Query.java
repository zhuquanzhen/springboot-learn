package com.huixdou.common.base;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.hutool.core.convert.Convert;

public class Query extends LinkedHashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	private int pageNum = 1;
	private int pageSize = 5;

	public Query() {
		super();
	}

	public Query(Map<String, Object> params) {
		this.putAll(params);

		// 分页参数
		pageNum = Convert.toInt(params.get("pageNum"), this.pageNum);
		pageSize = Convert.toInt(params.get("pageSize"), this.pageSize);

		this.put("pageNum", pageNum);
		this.put("pageSize", pageSize);

	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
