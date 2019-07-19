package com.huixdou.api.vo;

import java.util.List;

import com.google.common.collect.Lists;

public class MerInfoTreeVO {
	
	private Integer id;
	private String name;
	private List<MerInfoTreeVO> children = Lists.newArrayList();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MerInfoTreeVO> getChildren() {
		return children;
	}

	public void setChildren(List<MerInfoTreeVO> children) {
		this.children = children;
	}
	
}
