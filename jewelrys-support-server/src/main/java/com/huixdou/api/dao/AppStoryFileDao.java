package com.huixdou.api.dao;

import java.util.List;
import java.util.Map;

import com.huixdou.api.bean.AppStoryFile;
import com.huixdou.common.base.BaseDao;

public interface AppStoryFileDao extends BaseDao<AppStoryFile> {
	
	public List<String> selectFileIdListByStroyId(Map<String, Object> params);
	
}
