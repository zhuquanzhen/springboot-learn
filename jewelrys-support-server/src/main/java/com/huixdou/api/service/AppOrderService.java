package com.huixdou.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.huixdou.api.bean.AppOrder;
import com.huixdou.api.dao.AppOrderDao;
import com.huixdou.common.base.BaseService;

@Service
public class AppOrderService extends BaseService<AppOrderDao, AppOrder> {
	
	// 首页获取销售记录
	public Integer getOrderCount() {
		return dao.selectCount();
	}
	
	// 首页统计珠宝月次销售
	public List<AppOrder> getMonthSell() {
		return dao.selectMonthCount();
	}
	
	// 首页统计珠宝商的销售记录
	public Integer getMerOrderCount(Integer id) {
		return dao.selectByCreateIdList(id);
	}
}
