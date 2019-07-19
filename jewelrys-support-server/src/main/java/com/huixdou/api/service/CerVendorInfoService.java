/**
 * 
 */
package com.huixdou.api.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.huixdou.api.bean.CerVendorInfo;
import com.huixdou.api.dao.CerVendorInfoDao;
import com.huixdou.common.base.BaseService;

/**
 * @author jinxin 2019年1月22日下午5:15:34
 *
 */
@Service
public class CerVendorInfoService extends BaseService<CerVendorInfoDao, CerVendorInfo> {
	
	/**
	 * 获取指定id的珠宝商所关联的送检厂商
	 * @param id
	 * @return
	 *
	 */
	public List<CerVendorInfo> relation(Map<String, Object> params){
		return dao.selectVendorByMertId(params);
	}
	
	/**
	 * 查询未关联的列表
	 * @param id
	 * @return
	 *
	 */
	public List<CerVendorInfo> selectVendorList(Map<String, Object> params){
		return dao.selectVendorList(params);
	}
}
