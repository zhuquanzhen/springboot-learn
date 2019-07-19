package com.huixdou.api.dao;

import java.util.List;
import java.util.Map;

import com.huixdou.api.bean.MerStory;
import com.huixdou.common.base.BaseDao;

public interface MerStoryDao extends BaseDao<MerStory> {
	
	List<String> selectFile(String id);
	
	Integer selectCertNoCount(String id);
	
	List<String> selectCertNo(String id);
	
	List<String> selectContentId(Map<String, Object> params);
	
	Integer deleteStoryCert(Map<String, Object> params);
	
}
