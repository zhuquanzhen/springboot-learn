/**
 * 
 */
package com.huixdou.api.vo;

import java.io.Serializable;

/**
 * @author jinxin 2019年1月21日下午8:45:39
 *
 */
public class AppAccountVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String phone;

	private String name;

	private Integer status;

	private String statusName;

	private String registerDate;

	private Integer num = 0;

	private Integer storyNum = 0;

	public Integer getId() {
		return id;
	}

	public String getPhone() {
		return phone;
	}

	public String getName() {
		return name;
	}

	public Integer getStatus() {
		return status;
	}

	public String getStatusName() {
		return statusName;
	}

	public String getRegisterDate() {
		return registerDate;
	}

	public Integer getNum() {
		return num;
	}

	public Integer getStoryNum() {
		return storyNum;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public void setStoryNum(Integer storyNum) {
		this.storyNum = storyNum;
	}

	

	
}
