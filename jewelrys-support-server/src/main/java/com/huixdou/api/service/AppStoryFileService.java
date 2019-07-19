package com.huixdou.api.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huixdou.api.bean.AppStoryFile;
import com.huixdou.api.dao.AppStoryFileDao;
import com.huixdou.common.base.BaseService;

@Service
public class AppStoryFileService extends BaseService<AppStoryFileDao, AppStoryFile> {
	
	@Transactional(readOnly = false)
	public void delete(Map<String, Object> params) {
		dao.deleteByMap(params);
	}
	
	public List<String> selectFileIdListByStroyId(Map<String, Object> params){
		return	dao.selectFileIdListByStroyId(params);
	}
	
	
}
