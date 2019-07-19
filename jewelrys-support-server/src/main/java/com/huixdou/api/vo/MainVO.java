package com.huixdou.api.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Lists;

import com.google.common.collect.Maps;

public class MainVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// 首页数据统计
	private Map<String, Integer> indexCount = Maps.newHashMap();
	
	// 珠宝月次销售统计
	private List<Integer> monthSell = Lists.newArrayList();
	
	// 珠宝商销售排行榜TOP5
	private List<MainMerVO> merSellNum = Lists.newArrayList();
	
	// 电子证书验证月份统计
	private List<Integer> monthVerify = Lists.newArrayList();
	
	// 验证渠道统计
	private List<MainVerifyVO> verifyChannel = Lists.newArrayList();
	
	// 验证方式统计
	private List<MainVerifyVO> verifyType = Lists.newArrayList();
	
	// 验证张数(珠宝商)排行TOP5
	private List<MainMerVO> merVerifyNum = Lists.newArrayList();

	public List<Integer> getMonthSell() {
		return monthSell;
	}

	public void setMonthSell(List<Integer> monthSell) {
		this.monthSell = monthSell;
	}

	public List<MainMerVO> getMerSellNum() {
		return merSellNum;
	}

	public void setMerSellNum(List<MainMerVO> merSellNum) {
		this.merSellNum = merSellNum;
	}

	public Map<String, Integer> getIndexCount() {
		return indexCount;
	}

	public void setIndexCount(Map<String, Integer> indexCount) {
		this.indexCount = indexCount;
	}

	public List<Integer> getMonthVerify() {
		return monthVerify;
	}

	public void setMonthVerify(List<Integer> monthVerify) {
		this.monthVerify = monthVerify;
	}

	public List<MainVerifyVO> getVerifyChannel() {
		return verifyChannel;
	}

	public void setVerifyChannel(List<MainVerifyVO> verifyChannel) {
		this.verifyChannel = verifyChannel;
	}

	public List<MainVerifyVO> getVerifyType() {
		return verifyType;
	}

	public void setVerifyType(List<MainVerifyVO> verifyType) {
		this.verifyType = verifyType;
	}

	public List<MainMerVO> getMerVerifyNum() {
		return merVerifyNum;
	}

	public void setMerVerifyNum(List<MainMerVO> merVerifyNum) {
		this.merVerifyNum = merVerifyNum;
	}

}
