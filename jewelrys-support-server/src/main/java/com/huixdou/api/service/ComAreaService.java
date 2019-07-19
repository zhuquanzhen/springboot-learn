package com.huixdou.api.service;

import com.huixdou.api.bean.ComArea;
import com.huixdou.api.dao.ComAreaDao;
import com.huixdou.common.base.BaseService;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ComAreaService extends BaseService<ComAreaDao, ComArea> {
	
	public List<ComArea> selectByParentId(Integer areaParentId){
		return dao.selectByParentId(areaParentId);
	}
	
}
