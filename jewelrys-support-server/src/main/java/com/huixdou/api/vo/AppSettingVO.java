/**
 * 
 */
package com.huixdou.api.vo;

import java.io.Serializable;

/**
 * @author jinxin 2019年2月20日下午3:03:50
 *
 */
public class AppSettingVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private String name;

	private String link;

	private String showFlagName;
	
	private Integer showFlag;

	private Integer orderNum;

	
	
	
	public Integer getShowFlag() {
		return showFlag;
	}

	public void setShowFlag(Integer showFlag) {
		this.showFlag = showFlag;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getLink() {
		return link;
	}

	public String getShowFlagName() {
		return showFlagName;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setShowFlagName(String showFlagName) {
		this.showFlagName = showFlagName;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

}
