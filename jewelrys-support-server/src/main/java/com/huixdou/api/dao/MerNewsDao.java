package com.huixdou.api.dao;

import com.huixdou.api.bean.MerNews;
import com.huixdou.common.base.BaseDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by jinxin on 2018/12/21 15:25.
 */
public interface MerNewsDao extends BaseDao<MerNews> {

    void delete(Map<String, Object> params);

    ArrayList<String> selectListByMap(Map<String, Object> params);
    
    List<MerNews>selectMerNewsListById(Map<String, Object> params);
    	
}
