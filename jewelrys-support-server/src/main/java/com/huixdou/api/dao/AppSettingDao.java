package com.huixdou.api.dao;

import com.huixdou.api.bean.AppSetting;
import com.huixdou.common.base.BaseDao;

import java.util.Map;

/**
 * Created by jinxin on 2018/12/27 10:10.
 */
public interface AppSettingDao extends BaseDao<AppSetting>{


    Integer updateReleases(Map<String, Object> params);

    Integer deleteByIds(Map<String, Object> params);
    
    Integer selectNumber(Map<String, Object> params);

    Integer selectReleases(Map<String, Object> params);
}
