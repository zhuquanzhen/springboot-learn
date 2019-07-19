package com.huixdou.api.vo;

import java.io.Serializable;

public class MerInfoDetailVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private String shortName;
	private String contact;
	private String contactNumber;
	private String provinceName;
	private Integer province;
	private String cityName;
	private Integer city;
	private String areaName;
	private Integer area;
	private String addr;
	private String logo;
	private String imgUrl;
	private String brief;
	private String username;
	private String nickname;
	private String phone;
	private Integer publicityPerm;
	private String parentName;
	private String createDate;
	private Integer status;
	
	
	
	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
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

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public Integer getProvince() {
		return province;
	}

	public String getCityName() {
		return cityName;
	}

	public Integer getCity() {
		return city;
	}

	public String getAreaName() {
		return areaName;
	}

	public Integer getArea() {
		return area;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public void setProvince(Integer province) {
		this.province = province;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public void setCity(Integer city) {
		this.city = city;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public void setArea(Integer area) {
		this.area = area;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getPublicityPerm() {
		return publicityPerm;
	}

	public void setPublicityPerm(Integer publicityPerm) {
		this.publicityPerm = publicityPerm;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
}
