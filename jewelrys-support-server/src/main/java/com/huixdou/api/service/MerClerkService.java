package com.huixdou.api.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huixdou.api.bean.MerClerk;
import com.huixdou.api.dao.MerClerkDao;
import com.huixdou.common.base.BaseService;

@Service
public class MerClerkService extends BaseService<MerClerkDao, MerClerk> {
	
	public List<String> getClertPhone(Integer id) {
		return dao.selectByMertId(id);
	}
	
	/**
	 * 删除某个珠宝商下的所有店员
	 * @param params
	 * @return
	 *
	 */
	@Transactional(readOnly = false)
	public Integer deleteMerClerkByMertId(Map<String, Object> params) {
		return dao.deleteMerClerkByMertId(params);
	}
}
