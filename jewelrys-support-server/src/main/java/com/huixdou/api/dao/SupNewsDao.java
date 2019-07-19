package com.huixdou.api.dao;

import com.huixdou.api.bean.SupNews;
import com.huixdou.common.base.BaseDao;

import java.util.List;
import java.util.Map;

/**
 * Created by jinxin on 2018/12/21 15:34.
 */
public interface SupNewsDao extends BaseDao<SupNews> {

	void delete(Map<String, Object> params);

	void updateSupNews(SupNews supNews);

	List<SupNews> selectNumber(Map<String, Object> params);

	List<String> selectListByMap(Map<String, Object> params);
	
	 void updatePublishAmount(SupNews supNews);
}
