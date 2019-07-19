package com.huixdou.api.dao;

import com.huixdou.api.bean.AppMessage;
import com.huixdou.common.base.BaseDao;

import java.util.Map;

/**
 * Created by jinxin on 2018/12/27 18:31.
 */
public interface AppMessageDao extends BaseDao<AppMessage> {

    Integer deleteByIds(Map<String, Object> params);
    
    Integer selectNumber(Map<String, Object> params);

    Integer sendUpdate(AppMessage appMessage);
}
