package com.huixdou.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huixdou.api.bean.SysApiLog;
import com.huixdou.api.dao.SysApiLogDao;
import com.huixdou.common.base.BaseService;
import com.huixdou.common.base.Result;
import com.huixdou.common.utils.IdGen;

@Service
public class SysApiLogService extends BaseService<SysApiLogDao, SysApiLog>{
	
	@Transactional(readOnly = false)
	public Object create(SysApiLog apiLog) {
		apiLog.setId(IdGen.getObjectId());
		dao.insert(apiLog);
		return Result.success();
	}
	
	
}
