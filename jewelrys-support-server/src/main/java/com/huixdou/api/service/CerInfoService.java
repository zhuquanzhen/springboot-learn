package com.huixdou.api.service;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.huixdou.api.bean.CerInfo;
import com.huixdou.api.dao.CerInfoDao;
import com.huixdou.common.base.BaseService;

@Service
public class CerInfoService extends BaseService<CerInfoDao, CerInfo> {
	
	// 运营平台平台获得珠宝证书列表
	public List<CerInfo> selectList(Map<String, Object> params) {
		return dao.selectList(params);
	}
	
	// 运营平台查看珠宝证书详细信息
	public CerInfo selectById(String id) {
		// return dao.selectById(id);
		CerInfo cerInfo = new CerInfo();
		cerInfo.setId(id);
		cerInfo.setNo("5c273412e80bc83682a213f1");
		cerInfo.setCode("5c273412e80bc83682a213f1");
		cerInfo.setResult("合格");
		cerInfo.setTotalMass("20");
		cerInfo.setShape("方形");
		cerInfo.setColor("黄色");
		cerInfo.setClarity("0.8");
		cerInfo.setDeskWidth("0.8");
		cerInfo.setPavilionDepth("0.9");
		cerInfo.setPreciousMetal("含有");
		cerInfo.setInspector("zhang");
		cerInfo.setReviewer("zhang");
		cerInfo.setInspectTime("2018-12-15 09:00:00");
		return cerInfo;
	}
	
	// 更新珠宝证书拥有人
	@Transactional(readOnly = false)
	public void updateMertId(Map<String, Object> params) {
		super.updateByMap(params);
	}
	
	// 首页统计证书数量
	public Integer getCerCount() {
		return dao.selectCount();
	}
	
	/**
	 * 通过会员id查询此会员证书持有数量
	 * @param accountId
	 * @return
	 *
	 */
	public Integer selectNumByAccountId(Integer accountId) {
		return dao.selectNumByAccountId(accountId);
	}
	
	/**
	 * 查询这个送检厂商下所可以被关联的证书
	 * @param vendorIds
	 * @return
	 *
	 */
	public List<CerInfo> selectCerInfoByClintId(List<String> clintIds){
		return dao.selectCerInfoByClintId(clintIds);
	}
	
	/**
	 * 珠宝商关联送检厂商的同时修改证书所归属的珠宝商
	 * @param MertId
	 * @param cerInfos
	 *
	 */
	public Integer updateMertId(Integer mertId,List<CerInfo> cerInfos) {
		return dao.updateMertId(mertId, cerInfos);
	}
	
	/**
	 * 删除珠宝商所关联的证书（也就是将证书关联的珠宝商字段设为0）
	 * @param mertId
	 * @param vendorIds
	 * @return
	 *
	 */
	public Integer deleteCerInfo(Integer mertId,List<String>clintIds) {
		return dao.deleteCerInfo(mertId, clintIds);	
	}
	
	/**
	 * 获取指定珠宝商下的证书数量
	 * @param mertId
	 * @return
	 *
	 */
	public Integer selectCerInfoByMertId(Integer mertId){
		return dao.selectCerInfoByMertId(mertId);
	}
}
