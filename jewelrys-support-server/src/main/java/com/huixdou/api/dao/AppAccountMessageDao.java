/**
 * 
 */
package com.huixdou.api.dao;

import com.huixdou.api.bean.AppAccountMessage;
import com.huixdou.common.base.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author jinxin 2019年4月26日下午2:10:34
 *
 */
public interface AppAccountMessageDao extends BaseDao<AppAccountMessage>{

	List<AppAccountMessage> selectByAppMessageNewId(String appMessageNewId);

	Integer deleteListIds(@Param("appMessageNewIds") List<String> appMessageNewIds);

	List<AppAccountMessage> selectOrderByCreateDate(String appMessageNewId);
}
