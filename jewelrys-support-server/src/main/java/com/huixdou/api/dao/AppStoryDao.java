package com.huixdou.api.dao;

import java.util.List;
import java.util.Map;

import com.huixdou.api.bean.AppStory;
import com.huixdou.common.base.BaseDao;

public interface AppStoryDao extends BaseDao<AppStory> {
	
	List<String> selectFile(String id);
	
	List<String> selectContentId(Map<String, Object> params);
	
    Integer updatStatusByAppStory(AppStory appStory);
	
    Integer selectStoryNum(Integer createId);
}
