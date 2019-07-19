package com.huixdou.api.service;

import org.springframework.stereotype.Service;

import com.huixdou.api.bean.SysDictType;
import com.huixdou.api.dao.SysDictTypeDao;
import com.huixdou.common.base.BaseService;

@Service
public class SysDictTypeService extends BaseService<SysDictTypeDao, SysDictType> {
	
	public SysDictType getDictType(String code) {
		return dao.selectByCode(code);
	}
	
}
