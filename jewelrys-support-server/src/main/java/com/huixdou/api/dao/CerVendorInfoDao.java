/**
 * 
 */
package com.huixdou.api.dao;

import java.util.List;
import java.util.Map;

import com.huixdou.api.bean.CerVendorInfo;
import com.huixdou.common.base.BaseDao;

/**
 * @author jinxin 2019年1月22日下午5:14:34
 *
 */
public interface CerVendorInfoDao extends BaseDao<CerVendorInfo> {

	List<CerVendorInfo> selectVendorByMertId(Map<String, Object> params);
	
	List<CerVendorInfo> selectVendorList(Map<String, Object> params);
}
