package com.huixdou.api.dao;


import com.huixdou.api.bean.AppMessageNew;
import com.huixdou.common.base.BaseDao;

import java.util.Map;

/**
 * @author jinxin
 * @date 2019/4/25 9:48
 */
public interface AppMessageNewDao extends BaseDao<AppMessageNew> {

    Integer deleteByIds(Map<String, Object> params);

    Integer selectNumber(Map<String, Object> params);

    Integer sendUpdateAppAccountMessage(AppMessageNew appMessageNew);



}
