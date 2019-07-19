package com.huixdou.api.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huixdou.api.bean.MerStoryCert;
import com.huixdou.api.dao.MerStoryCertDao;
import com.huixdou.common.base.BaseService;

@Service
public class MerStoryCertService extends BaseService<MerStoryCertDao, MerStoryCert> {
	
	@Transactional(readOnly = false)
	public void deleteByIdMap(Map<String, Object> params){
		dao.deleteByMap(params);
	}
	

}
