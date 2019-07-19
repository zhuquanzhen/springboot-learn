package com.huixdou.api.service;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.huixdou.api.bean.SysMessage;
import com.huixdou.api.dao.SysMessageDao;
import com.huixdou.common.base.BaseService;
import com.huixdou.common.base.Result;
import com.huixdou.common.utils.DateUtils;
import com.huixdou.common.utils.IdGen;

@Service
public class SysMessageService extends BaseService<SysMessageDao, SysMessage> {

	/*
	 * 查看详情并且给予变更状态
	 */
	public Object details(Map<String, Object> params) {
		// 获取ID
		String id = (String) params.get("id");
		// 更改状态
		params.put("readDate", DateUtils.formatDateTime(new Date()));
		int key = dao.updateByMap(params);
		if (key > 0)
			return Result.success(dao.selectById(id));
		return Result.failure("状态变更和时间插入有误");
	}

	/*
	 * 查看详情并且给予变更状态
	 */
	public boolean insert(SysMessage message) {
		message.setCreateDate(DateUtils.formatDateTime(new Date()));
		message.setStatus(1);
		message.setId(IdGen.getObjectId());

		int key = dao.insert(message);

		return key > 0;

	}

}
