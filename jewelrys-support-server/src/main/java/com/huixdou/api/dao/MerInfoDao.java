package com.huixdou.api.dao;

import com.huixdou.api.bean.MerInfo;
import com.huixdou.common.base.BaseDao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface MerInfoDao extends BaseDao<MerInfo> {
	
	List<String> findByPhone(String phone);

	List<MerInfo> selectPublicityList(Map<String, Object> params);

	void updatePublicityStatus(Map<String, Object> params);

	List<MerInfo> selectByParentId(Integer parentId);
	
	MerInfo selectByUserName(String username);
	
	MerInfo selectByPhone(String phone);
	
	MerInfo selectByName(String name);

	void updateStatus(@Param("id") Integer id, @Param("status") Integer status);

	Integer deleteMerVendor(@Param("id") Integer id);
	
	Integer insertVendor(Map<String, Object> params);
	
	Integer selectCountVendor(Integer id);
	
	Integer updateVendorNum(@Param("id") Integer id, @Param("vendorNum") Integer vendorNum);
	
	Integer selectCount();
	
	List<MerInfo> selectMerList();
	
	Integer deleteChildrenByMap(Map<String, Object> params);
	
	Integer updateCerNum(@Param("cerNum") Integer cerNum,@Param("mertId") Integer mertId);
	
	Integer selectCertificatesNum(@Param("mertId") Integer mertId);
	
	List<Integer> selectVendor(@Param("mertId") Integer mertId);
	
	List<String> selectClintIdByCerVendor(@Param("vendorIds") List<Integer> vendorIds);
	
	Integer insertMerRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId);
}
