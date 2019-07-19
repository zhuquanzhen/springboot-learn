package com.huixdou.api.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.huixdou.api.bean.CerInfo;
import com.huixdou.common.base.BaseDao;

public interface CerInfoDao extends BaseDao<CerInfo> {
	
	Integer selectCount();
	
	Integer selectNumByAccountId(Integer accountId);
	
	//查询这个送检厂商下所可以被关联的证书
	List<CerInfo> selectCerInfoByClintId(@Param("clintIds") List<String> clintIds);
	
	Integer updateMertId(@Param("mertId") Integer mertId,@Param("cerInfos") List<CerInfo> cerInfos);
	
	Integer deleteCerInfo(@Param("mertId") Integer mertId,@Param("clintIds") List<String>clintIds);
	
	Integer selectCerInfoByMertId(@Param("mertId") Integer mertId);
}
