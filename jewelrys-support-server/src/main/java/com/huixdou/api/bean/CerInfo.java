package com.huixdou.api.bean;

import java.io.Serializable;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class CerInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	@Excel(name = "珠宝证书编号")
	private String no;

	private Integer mertId;

	private String code;

	private String result;

	private String totalMass;

	private String shape;

	private String color;

	private String clarity;

	private String deskWidth;

	private String pavilionDepth;

	private String preciousMetal;

	private String inspector;

	private String reviewer;

	private String inspectTime;

	private String createDate;

	private Integer lastOwner;
	// 证书状态（0：未售；1：已售）
	private Integer status;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getTotalMass() {
		return totalMass;
	}

	public void setTotalMass(String totalMass) {
		this.totalMass = totalMass;
	}

	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getClarity() {
		return clarity;
	}

	public void setClarity(String clarity) {
		this.clarity = clarity;
	}

	public String getDeskWidth() {
		return deskWidth;
	}

	public void setDeskWidth(String deskWidth) {
		this.deskWidth = deskWidth;
	}

	public String getPavilionDepth() {
		return pavilionDepth;
	}

	public void setPavilionDepth(String pavilionDepth) {
		this.pavilionDepth = pavilionDepth;
	}

	public String getPreciousMetal() {
		return preciousMetal;
	}

	public void setPreciousMetal(String preciousMetal) {
		this.preciousMetal = preciousMetal;
	}

	public String getInspector() {
		return inspector;
	}

	public void setInspector(String inspector) {
		this.inspector = inspector;
	}

	public String getReviewer() {
		return reviewer;
	}

	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	public String getInspectTime() {
		return inspectTime;
	}

	public void setInspectTime(String inspectTime) {
		this.inspectTime = inspectTime;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public CerInfo() {

	}

	public CerInfo(String id, String no, String code) {
		this.id = id;
		this.no = no;
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public Integer getMertId() {
		return mertId;
	}

	public void setMertId(Integer mertId) {
		this.mertId = mertId;
	}

	public Integer getLastOwner() {
		return lastOwner;
	}

	public void setLastOwner(Integer lastOwner) {
		this.lastOwner = lastOwner;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}



}
