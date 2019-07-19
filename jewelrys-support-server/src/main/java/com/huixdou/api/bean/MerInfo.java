package com.huixdou.api.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.assertj.core.util.Lists;
import static com.huixdou.common.utils.Constant.REGEX_MOBILE;

public class MerInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	@NotBlank(message="请填写珠宝商全称")
	@Size(max= 60,message="不能超过60个字符")
	private String name;
	
	@NotBlank(message="请填写珠宝商简称")
	@Size(max= 20,message="不能超过20个字符")
	private String shortName;
	private String contact;
	
	@NotBlank(message="请填写联系电话")
	@Pattern(regexp=REGEX_MOBILE,message="请输入正确的手机号")
	private String contactNumber;
	private String provinceName;
	private Integer province;
	private String cityName;
	private Integer city;
	private String areaName;
	private Integer area;
	
	@Size(max= 60,message="不能超过60个字符")
	private String addr;
	
	@NotBlank(message="logo不能为空")
	private String logo;
	
	@Size(max= 250,message="不能超过250个字符")
	private String brief;
	
	@Size(max= 20,message="不能超过20个字符")
	private String username;
	private String password;
	private String salt;
	@Size(max= 10,message="不能超过10个字符")
	private String nickname;
	private String phone;
	private Integer status;
	private Integer parentId;
	private Integer deleteFlag;
	private String deleteTime;
	private String deleteId;
	private Integer vendorNum = 0;
	private Integer cerNum = 0;
	
	// 宣传片
	@JsonIgnore
	private String publicity;
	private String publicityStatus;
	private Integer publicityPerm = 0;
	private String publicityDate;
	private String publicityReject;
	private List<MerInfo> children = Lists.newArrayList();
	private String createDate;
	
	
	
	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Integer getVendorNum() {
		return vendorNum;
	}

	public void setVendorNum(Integer vendorNum) {
		this.vendorNum = vendorNum;
	}

	public Integer getCerNum() {
		return cerNum;
	}

	public void setCerNum(Integer cerNum) {
		this.cerNum = cerNum;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public List<MerInfo> getChildren() {
		return children;
	}

	public void setChildren(List<MerInfo> children) {
		this.children = children;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getDeleteId() {
		return deleteId;
	}

	public void setDeleteId(String deleteId) {
		this.deleteId = deleteId;
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

	public String getPublicityStatus() {
		return publicityStatus;
	}

	public void setPublicityStatus(String publicityStatus) {
		this.publicityStatus = publicityStatus;
	}

	public String getPublicityReject() {
		return publicityReject;
	}

	public void setPublicityReject(String publicityReject) {
		this.publicityReject = publicityReject;
	}

	public String getPublicity() {
		return publicity;
	}

	public void setPublicity(String publicity) {
		this.publicity = publicity;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(String deleteTime) {
		this.deleteTime = deleteTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getPublicityDate() {
		return publicityDate;
	}

	public void setPublicityDate(String publicityDate) {
		this.publicityDate = publicityDate;
	}

	public Integer getPublicityPerm() {
		return publicityPerm;
	}

	public void setPublicityPerm(Integer publicityPerm) {
		this.publicityPerm = publicityPerm;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

}
