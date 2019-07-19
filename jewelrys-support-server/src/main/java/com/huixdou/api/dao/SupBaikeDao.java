package com.huixdou.api.dao;


import com.huixdou.api.bean.SupBaike;
import com.huixdou.common.base.BaseDao;

import java.util.List;
import java.util.Map;

public interface SupBaikeDao extends BaseDao<SupBaike> {

    void updateByTypeId(Map<String, Object> map);

    Integer selectNumberByTypeId(String typeId);
    
    void  delete( Map<String, Object> params);
    
     void updateSupBaike(SupBaike supBaike);
     
     List<SupBaike> selectNumber(Map<String, Object> params);
     
    List<String> selectListByMap(Map<String, Object> params);
    
    void updatePublishAmount(SupBaike supBaike);

}
