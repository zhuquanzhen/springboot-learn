/**
 * 
 */
package com.huixdou.api.vo;

import java.io.Serializable;

/**
 * @author jinxin 2019年1月22日下午2:15:13
 * 珠宝商配置列表VO类
 */
public class ConfigureListVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private String shortName;
	private String contact;
	private String contactNumber;
	private Integer vendorNum = 0;
	private Integer cerNum = 0;
	public Integer getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getShortName() {
		return shortName;
	}
	public String getContact() {
		return contact;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public Integer getVendorNum() {
		return vendorNum;
	}
	public Integer getCerNum() {
		return cerNum;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public void setVendorNum(Integer vendorNum) {
		this.vendorNum = vendorNum;
	}
	public void setCerNum(Integer cerNum) {
		this.cerNum = cerNum;
	}
	
	
}
