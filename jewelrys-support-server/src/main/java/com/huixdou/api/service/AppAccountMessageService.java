/**
 * 
 */
package com.huixdou.api.service;

import org.springframework.stereotype.Service;

import com.huixdou.api.bean.AppAccountMessage;
import com.huixdou.api.dao.AppAccountMessageDao;
import com.huixdou.common.base.BaseService;

import java.util.List;

/**
 * @author jinxin 2019年4月26日下午2:14:27
 *
 */
@Service
public class AppAccountMessageService extends BaseService<AppAccountMessageDao,AppAccountMessage> {

	
	
	
	/**
	 * 通过appMessageNew表的id查询AppAccountMessage
	 * @param appMessageNewId
	 * @return
	 *
	 */
	public List<AppAccountMessage> selectByAppMessageNewId(String appMessageNewId) {
		return dao.selectByAppMessageNewId(appMessageNewId);
	}


	/**
	 * 删除中间表数据
	 * @param appMessageNewId
	 * @return
	 */
	public Integer deleteListIds(List<String> appMessageNewId){
		return  dao.deleteListIds(appMessageNewId);
	}

	/**
	 * 查询中间表根据创建时间排序
	 * @param appMessageNewId
	 * @return
	 */
	public List<AppAccountMessage> selectOrderByCreateDate(String appMessageNewId){
		return dao.selectOrderByCreateDate(appMessageNewId);

	}
	
}

