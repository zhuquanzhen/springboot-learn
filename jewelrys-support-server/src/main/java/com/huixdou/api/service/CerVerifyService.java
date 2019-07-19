package com.huixdou.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.huixdou.api.bean.CerVerify;
import com.huixdou.api.dao.CerVerifyDao;
import com.huixdou.common.base.BaseService;

@Service
public class CerVerifyService extends BaseService<CerVerifyDao, CerVerify> {
	
	// 首页统计月验证数据
	public List<CerVerify> getMonthCount() {
		return dao.selectMonthCount();
	}
	
	// 首页统计验证渠道
	public Integer getVerifyChannel(String channel) {
		if ("移动端APP".equals(channel))
			return dao.selectByAppChannel();
		return dao.selectByOtherChannel();
	}
	
	// 首页统计验证方式
	public List<CerVerify> getVerifyTypeCode() {
		return dao.selectTypeCodeCount();
	}
	
	// 首页统计珠宝商验证TOP
	public List<CerVerify> getMerVerifyNum() {
		return dao.selectMertCount();
	}
	
	
	/**
	 * 通过会员id查询这个会员验证证书的次数
	 * @param createId
	 * @return
	 *
	 */
	public Integer selectNum(Integer createId){
		return dao.selectNum(createId);
	}
}
