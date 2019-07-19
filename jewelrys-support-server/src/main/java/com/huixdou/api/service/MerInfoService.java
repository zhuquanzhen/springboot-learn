package com.huixdou.api.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.huixdou.api.bean.CerInfo;
import com.huixdou.api.bean.ComArea;
import com.huixdou.api.bean.MerInfo;
import com.huixdou.api.dao.MerInfoDao;
import com.huixdou.api.service.CerVendorInfoService;
import com.huixdou.api.service.OssService;
import com.huixdou.api.service.SysSmsService;
import com.huixdou.api.vo.ConfigureListVO;
import com.huixdou.api.vo.MerInfoDetailVO;
import com.huixdou.api.vo.MerInfoListVO;
import com.huixdou.api.vo.MerInfoTreeVO;
import com.huixdou.common.base.BaseService;
import com.huixdou.common.base.Query;
import com.huixdou.common.base.Result;
import com.huixdou.common.utils.DateUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import static com.huixdou.common.utils.Constant.REGEX_MOBILE;

@Service
public class MerInfoService extends BaseService<MerInfoDao, MerInfo> {

	
	@Autowired
	private OssService ossService;

	@Autowired
	private ComAreaService comAreaService;

	@Autowired
	private CerVendorInfoService cerVendorInfoService;
	
	@Autowired 
	private SysSmsService sysSmsService;

	@Autowired
	private CerInfoService cerInfoService;
	
	@Autowired
	private MerClerkService merClerkService;
	
	/**
	 * 通过管理员账号查询珠宝商信息
	 * @param username
	 * @return
	 *
	 */
	public MerInfo selectByUserName(String username) { 
		return dao.selectByUserName(username);
	}

	/**
	 * 通过手机号查询珠宝商信息
	 * @param phone
	 * @return
	 *
	 */
	public MerInfo selectByPhone(String phone) { 
		return dao.selectByPhone(phone);
	}
	
	/**
	 * 通过珠宝商全称查询珠宝商信息
	 * @param name
	 * @return
	 *
	 */
	public MerInfo selectByName(String name) { 
		return dao.selectByName(name);
	}
	/**
	 * 根据ID获取珠宝商详情
	 * 
	 * @param id
	 * @return
	 */
	public MerInfoDetailVO get(Integer id) { 
		MerInfoDetailVO detailVO = new MerInfoDetailVO();
		MerInfo merInfo = selectById(id);
		if (ObjectUtil.isNotNull(merInfo.getArea())) {
			 ComArea comArea = comAreaService.selectById(merInfo.getArea());
			if (ObjectUtil.isNotNull(comArea)) {
				merInfo.setAreaName(comArea.getAreaName());
			}
		}
		if (ObjectUtil.isNotNull(merInfo.getCity())) {
			 ComArea comArea = comAreaService.selectById(merInfo.getCity());
			if (ObjectUtil.isNotNull(comArea)) {
				merInfo.setCityName(comArea.getAreaName());
			}
		}
		if (ObjectUtil.isNotNull(merInfo.getProvince())) {
			 ComArea comArea = comAreaService.selectById(merInfo.getProvince());
			if (ObjectUtil.isNotNull(comArea)) {
				merInfo.setProvinceName(comArea.getAreaName());
			}
		}
		BeanUtil.copyProperties(merInfo, detailVO);

		if (StrUtil.isNotBlank(merInfo.getLogo())) {
			detailVO.setImgUrl(ossService.getAddress(merInfo.getLogo()));
		}

		if (merInfo.getParentId() != 0) {
			MerInfo parent = selectById(merInfo.getParentId());
			detailVO.setParentName(ObjectUtil.isNull(parent) ? "" : parent.getName());
		}
		// 进驻时间 f
		String createDate = merInfo.getCreateDate();
		if (!StringUtils.isEmpty(createDate)) {
			detailVO.setCreateDate(DateUtils.formatDate(new Date(Convert.toLong(createDate)), "yyyy-MM-dd"));
		}
		return detailVO;
	}
	
	/**
	 * 递归-获取珠宝商树形列表
	 * @param parentId
	 * @return
	 *
	 */
	public List<MerInfoTreeVO> getTreeVO(Integer parentId) {  
		List<MerInfoTreeVO> treeList = Lists.newArrayList();
		List<MerInfo> list = dao.selectByParentId(parentId);
		for (int i = 0, length = list.size(); i < length; i++) {
			MerInfo item = list.get(i);
			MerInfoTreeVO treeVO = new MerInfoTreeVO();
			BeanUtil.copyProperties(item, treeVO);
			treeVO.setChildren(getTreeVO(item.getId()));
			treeList.add(treeVO);
		}
		return treeList;
	}

	@Transactional(readOnly = false)
	public void updateState(Integer id, Integer status) { 
		dao.updateStatus(id, status);
	}

	
	/**
	 * 珠宝商信息新增
	 * 
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public Result create(MerInfo entity) { 
		if (StrUtil.isNotBlank(entity.getUsername())) {
			if (StrUtil.isBlank(entity.getPhone())) {
				return Result.failure("管理员帐号不为空，则管理员联系电话不可为空");
			}
		}
		if (StrUtil.isNotBlank(entity.getPhone())) {
			if (StrUtil.isBlank(entity.getUsername())) {
				return Result.failure("管理员联系电话不为空，则管理员帐号不可为空");
			}
		}
		if (StrUtil.isNotBlank(entity.getUsername())) {
			if (ObjectUtil.isNotNull(selectByUserName(entity.getUsername()))) {
				return Result.failure("当前管理员账号已存在");
			}
		}
		
		if (StrUtil.isNotBlank(entity.getPhone())) {
			if (!Pattern.matches(REGEX_MOBILE,entity.getPhone())) {
				return Result.failure("请输入正确的管理员手机号");
			}
		}
		
		if (StrUtil.isNotBlank(entity.getPhone())) {
			if (dao.findByPhone(entity.getPhone()).size() > 0) {
				return Result.failure("当前管理员手机号码已存在");
			}
		}
		
		MerInfo merInfo = dao.selectByName(entity.getName());
		if (ObjectUtil.isNotNull(merInfo)) {
			return Result.failure("当前珠宝商全称已存在");
		}
		entity.setCreateDate(new Date().getTime() + "");
		// 密码生成并且以短信形式发送
		Result result = passWordGeneration(entity);
		if (result.getCode() != 200) {
			 return result;
		}
		dao.insert(entity);
		return Result.success();
	}
	
	/**
	 * 珠宝商列表
	 * 
	 * @param params
	 * @return
	 */
	public Map<String, Object> selectPageVO(Map<String, Object> params) {  
		Query query = new Query(params);
		PageHelper.startPage(query.getPageNum(), query.getPageSize());
		Page<MerInfo> page = (Page<MerInfo>) selectList(query);

		List<MerInfo> list = page.getResult();
		List<MerInfoListVO> voList = Lists.newArrayList();

		for (int i = 0, length = list.size(); i < length; i++) {
			MerInfoListVO vo = new MerInfoListVO();
			MerInfo item = list.get(i);
			BeanUtil.copyProperties(item, vo);
			voList.add(vo);
		}

		Map<String, Object> result = Maps.newHashMap();
		result.put("total", page.getTotal());
		result.put("list", voList);

		return result;
	}

	
	/**
	 * 根据ID删除珠宝商
	 * @param params
	 * @return
	 *
	 */
	@Transactional(readOnly = false)
	public Result deleteMerInfo(Map<String, Object> params){
		dao.deleteByMap(params);
		dao.deleteChildrenByMap(params);
		merClerkService.deleteMerClerkByMertId(params);
		return Result.success();
	}
	
	
	
	
	/**
	 * 珠宝商信息更新
	 * 
	 * @param merInfo
	 * @return
	 */
	@Transactional(readOnly = false)
	public Result updateMerInfo(MerInfo newMerInfo){ 
		if (newMerInfo.getId() == null) {
			return Result.failure("请输入修改的主键id");
		}
		
		if (StrUtil.isNotBlank(newMerInfo.getPhone())) {
			if (StrUtil.isBlank(newMerInfo.getUsername())) {
				return Result.failure("管理员联系电话不为空，则管理员帐号不可为空");
			}
		}
		// 判断这个珠宝商之前有没有设置用户账号如果设置了就不给他更新账号
		MerInfo userMerInfo = dao.selectById(newMerInfo.getId());
		if (ObjectUtil.isNotNull(userMerInfo)) {
			if (StrUtil.isNotBlank(userMerInfo.getUsername())) {
				if (StrUtil.isBlank(newMerInfo.getPhone())) {
					return Result.failure("管理员帐号不为空，则管理员联系电话不可为空");
				}
				newMerInfo.setUsername(userMerInfo.getUsername());
			}
		}
		//珠宝管理修改校验管理员账号不允许重复
		if (StrUtil.isNotBlank(newMerInfo.getUsername())) {
			MerInfo mInfo = dao.selectByUserName(newMerInfo.getUsername());
			if (ObjectUtil.isNotNull(mInfo)) {
				if (newMerInfo.getId() != mInfo.getId()) {
					return Result.failure("当前管理员账号已存在");
				}
			}
		}
		
		if (StrUtil.isNotBlank(newMerInfo.getPhone())) {
			if (!Pattern.matches(REGEX_MOBILE,newMerInfo.getPhone())) {
				return Result.failure("请输入正确的管理员手机号");
			}
		}
		
		if (StrUtil.isNotBlank(newMerInfo.getName())) {
			MerInfo merInfo = dao.selectByName(newMerInfo.getName());
			  if (ObjectUtil.isNotNull(merInfo)) {
			    if (merInfo.getId() != newMerInfo.getId() ) {
			    	return Result.failure("当前珠宝商全称已存在");
					}
				}
			}
		
		if (StrUtil.isNotBlank(newMerInfo.getPhone())) {
			List<String> phone = dao.findByPhone(newMerInfo.getPhone());
			if (!phone.contains(newMerInfo.getPhone())) {
				return Result.failure("当前管理员手机号码已存在");
			}
		}
		// 判断手机号是否更改如果被更改则重新生成密码以短信方式发送 
		if (ObjectUtil.isNotNull(userMerInfo)) {
			String oldPhone = userMerInfo.getPhone();
			String newPhone = newMerInfo.getPhone();
			if (StrUtil.isBlank(oldPhone) && StrUtil.isNotBlank(newPhone)) {
				Result result = passWordGeneration(newMerInfo);
				if (result.getCode() != 200) {
					return result;
				}
			}
			if (StrUtil.isNotBlank(oldPhone) && StrUtil.isNotBlank(newPhone)) {
				if (!oldPhone.equals(newPhone)) {
					Result result = passWordGeneration(newMerInfo);
					if (result.getCode() != 200) {
						return result;
					}
				}
			}

		}
		dao.update(newMerInfo);
		return Result.success();
	}
	
	public List<MerInfo> selectPublicityList(Map<String, Object> params) {
		return dao.selectPublicityList(params);
	}
	@Transactional(readOnly = false)
	public void updatePublicityStatus(Map<String, Object> params) {
		super.updateByMap(params);
	}

	/**
	 * 获取的区域列表
	 * 
	 * @param params
	 * @return
	 *
	 */
	public Result area(Integer id) { 
		return Result.success(comAreaService.selectByParentId(id));
	}

	/**
	 * 获得珠宝商配置列表
	 * 
	 * @param params
	 * @return
	 *
	 */
	public Map<String, Object> selectConfigurePageVO(Map<String, Object> params) { 
		Query query = new Query(params);
		PageHelper.startPage(query.getPageNum(), query.getPageSize());
		Page<MerInfo> page = (Page<MerInfo>) selectList(query);
		List<MerInfo> list = page.getResult();
		List<ConfigureListVO> voList = Lists.newArrayList();
		for (int i = 0, length = list.size(); i < length; i++) {
			ConfigureListVO vo = new ConfigureListVO();
			MerInfo item = list.get(i);
			Integer certNum = cerInfoService.selectCerInfoByMertId(item.getId());
			item.setCerNum(certNum);
			BeanUtil.copyProperties(item, vo);
			voList.add(vo);
		}
		Map<String, Object> result = Maps.newHashMap();
		result.put("total", page.getTotal());
		result.put("list", voList);
		return result;
	}

	
	/**
	 * 通过id查寻珠宝商配置
	 * 
	 * @param params
	 * @return
	 *
	 */
	public Result details(Integer id) { 
	 	 MerInfo merInfo = dao.selectById(id);
	 	 //珠宝证书动态获取
	 	 merInfo.setCerNum(dao.selectCertificatesNum(id));
	 	 if (ObjectUtil.isNull(merInfo)) {
	 		return Result.failure("查询的参数详情不存在");
		 }
	 	 ConfigureListVO vo = new ConfigureListVO();
	 	 BeanUtil.copyProperties(merInfo, vo);
		 return Result.success(vo);
	}
	
	
	/**
	 * 获取指定id的珠宝商所关联的送检厂商
	 * 
	 * @param id
	 * @return
	 *
	 */
	public Result relation(Map<String, Object> params) { 
		Integer mertId = Convert.toInt(params.get("id"));
		if (ObjectUtil.isNull(mertId)) {
			return Result.failure("请输入合法的id参数");
		}
		return Result.success(cerVendorInfoService.relation(params));
	}

	/**
	 * 获取珠宝商未关联的送检关联厂商
	 * @param id
	 * @return
	 *
	 */
	public Result selectVendorList(Map<String, Object> params){ 
		return Result.success(cerVendorInfoService.selectVendorList(params));	
	}
    
	/**
	 * 保存关联送检厂商
	 * 				1，先解绑所有的证书
	 * 				2，再重新关联
	 * @param id
	 * @return
	 *
	 */
	@Transactional(readOnly = false)
	public Result saveRelation(Map<String, Object> params){ 
		if (ObjectUtil.isNull(params.get("Ids"))) {
			return Result.failure("请输入Ids参数");
		}
		if (ObjectUtil.isNull(params.get("mertId"))) {
			return Result.failure("请输入mertId参数");
		}
		Integer id= Convert.toInt(params.get("mertId"));
		List<Integer> vendorIds= Convert.toList(Integer.class,params.get("Ids"));
		//将此珠宝商关联的送检厂商的证书都清空
		cancelCerInfo(id);
		dao.deleteMerVendor(id);
		for(Integer vendorId:vendorIds) {
			if (dao.selectCountVendor(vendorId)>0) {
			throw new RuntimeException("数据异常，一个送检厂商仅可对应一个珠宝商");
			}
		}
		Integer cerNum = 0;
		if (vendorIds.size() != 0) {
			dao.insertVendor(params);
			List<String> clintIds= selectClintIdByCerVendor(vendorIds);
			//更新关联证书列表的关联
		    updateCerInfoMertId(clintIds,id);
			cerNum = dao.selectCertificatesNum(id);
		}
		//更新珠宝商表 关联送检厂商数
		dao.updateVendorNum(id, vendorIds.size());
		//更新珠宝商表 关联送检厂商的关联证书数
	    dao.updateCerNum(cerNum,id);
		return Result.success();
	}
	
	/**
	 * 珠宝商列表通过父类id删除子类
	 * @param params
	 * @return
	 *
	 */
	@Transactional(readOnly = false)
	public Integer deleteChildrenByMap(Map<String, Object> params){
		return dao.deleteChildrenByMap(params);
	}
	
	
	// 首页统计珠宝商数量
	public Integer getMerCount() {
		return dao.selectCount();
	}
	
	// 首页获取所有珠宝商信息
	public List<MerInfo> getMerInfo() {
		return dao.selectMerList();
	}
	
	/**
	 * 更新关联证书列表的关联
	 * @param vendorIds
	 * @param mertId
	 *
	 */
	private void updateCerInfoMertId(List<String> clintIds,Integer mertId) {
		if (clintIds.size() > 0) {
			// 1,查询这个送检厂商下所可以被关联的证书
			List<CerInfo> cerInfos = cerInfoService.selectCerInfoByClintId(clintIds);
			// 2,更新CerInfo表mertId字段（也就是所谓证书关联珠宝商）
			if (cerInfos.size() > 0) {
			cerInfoService.updateMertId(mertId, cerInfos);
			}
		}
	}
	
	/**
	 * 删除珠宝商所关联的证书（也就是将证书关联的珠宝商字段设为0）
	 * @param mertId
	 *
	 */
	private void cancelCerInfo(Integer mertId) {
		List<Integer> vendorIds = dao.selectVendor(mertId);
		List<String> clintIds = new ArrayList<>();
		if (vendorIds.size()!=0) {
		 clintIds = selectClintIdByCerVendor(vendorIds);
		}
		
		if (clintIds.size()>0) {
			cerInfoService.deleteCerInfo(mertId,clintIds);
		}
	}
	
	
	/**
	 * 珠宝商密码生成并且以短信形式发送
	 * @param entity
	 * @return
	 *
	 */
	private Result passWordGeneration(MerInfo entity) {
		if (StrUtil.isNotBlank(entity.getPhone()) && StrUtil.isNotBlank(entity.getUsername())) {
			entity.setSalt(RandomUtil.randomString(32));
			Integer random = (int) ((Math.random() * 9 + 1) * 100000);
			entity.setPassword(SecureUtil.md5(entity.getSalt() + random));
			// 已短信的形式发送初始密码
			Result result = sysSmsService.passWordGeneration(entity.getPhone(), entity.getUsername(), random.toString());
			if (result.getCode() != 200) {
				return result;
			}
		}
		return Result.success();
	}	
	
	/**
	 * 通过关联厂商的主键id查询客户ID（client_id）
	 * @return
	 *
	 */
	private List<String> selectClintIdByCerVendor(List<Integer> vendorIds){
		return dao.selectClintIdByCerVendor(vendorIds);
	}
}
