/**
 * 
 */
package com.huixdou.api.vo;

import java.io.Serializable;

/**
 * @author jinxin 2019年1月4日下午3:05:07
 *
 */
public class AppTree implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private Integer id;
	
	private String code;
	
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
}
