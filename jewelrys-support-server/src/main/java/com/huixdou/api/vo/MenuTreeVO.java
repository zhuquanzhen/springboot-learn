package com.huixdou.api.vo;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

public class MenuTreeVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private String name;
	
	private List<MenuTreeVO> children = Lists.newArrayList();

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

	public List<MenuTreeVO> getChildren() {
		return children;
	}

	public void setChildren(List<MenuTreeVO> children) {
		this.children = children;
	}

}
