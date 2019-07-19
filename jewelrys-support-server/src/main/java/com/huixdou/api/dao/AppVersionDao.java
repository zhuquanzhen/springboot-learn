package com.huixdou.api.dao;

import com.huixdou.api.bean.AppVersion;
import com.huixdou.common.base.BaseDao;

import java.util.Map;

/**
 * Created by jinxin on 2018/12/27 18:21.
 */
public interface AppVersionDao extends BaseDao<AppVersion> {

    Integer deleteByIds(Map<String, Object> params);
    
    AppVersion selectByVersion(String version);

    Integer selectNumber(Map<String, Object> params);
}
