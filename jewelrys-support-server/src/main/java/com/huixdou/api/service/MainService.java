package com.huixdou.api.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.huixdou.api.bean.AppOrder;
import com.huixdou.api.bean.CerVerify;
import com.huixdou.api.bean.MerInfo;
import com.huixdou.api.vo.MainMerVO;
import com.huixdou.api.vo.MainVO;
import com.huixdou.api.vo.MainVerifyVO;
import com.huixdou.common.utils.DateUtils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;

@Service
public class MainService {
	
	@Autowired
	private MerInfoService merInfoService;
	
	@Autowired
	private AppAccountService appAccountService;
	
	@Autowired
	private AppOrderService appOrderService;
	
	@Autowired
	private CerInfoService cerInfoService;
	
	@Autowired
	private SysConfigService sysConfigService;
	
	@Autowired
	private CerVerifyService cerVerifyService;
	
	public MainVO index() {
		FutureTask<Map<String, Integer>> indexCount = new FutureTask<Map<String, Integer>>(getIndexCount);
		FutureTask<List<Integer>> monthSell = new FutureTask<List<Integer>>(getMonthSell);
		FutureTask<List<MainMerVO>> merSellNum = new FutureTask<List<MainMerVO>>(getMerSellNum);
		FutureTask<List<Integer>> monthVerify = new FutureTask<List<Integer>>(getMonthVerify);
		FutureTask<List<MainVerifyVO>> verifyChannel = new FutureTask<List<MainVerifyVO>>(getVerifyChannel);
		FutureTask<List<MainVerifyVO>> verifyType = new FutureTask<List<MainVerifyVO>>(getVerifyType);
		FutureTask<List<MainMerVO>> merVerifyNum = new FutureTask<List<MainMerVO>>(getMerVerifyNum);
		ExecutorService threadPool = Executors.newCachedThreadPool();
		threadPool.execute(indexCount);
		threadPool.execute(monthSell);
		threadPool.execute(merSellNum);
		threadPool.execute(monthVerify);
		threadPool.execute(verifyChannel);
		threadPool.execute(verifyType);
		threadPool.execute(merVerifyNum);
		MainVO entity = new MainVO();
		try {
			// 首页统计
			entity.setIndexCount(indexCount.get());
			// 珠宝商销售
			entity.setMonthSell(monthSell.get());
			// 珠宝商销售TOP5
			entity.setMerSellNum(merSellNum.get());
			// 月验证
			entity.setMonthVerify(monthVerify.get());
			// 验证渠道
			entity.setVerifyChannel(verifyChannel.get());
			// 验证方式
			entity.setVerifyType(verifyType.get());
			// 验证张数TOP5
			entity.setMerVerifyNum(merVerifyNum.get());
		} catch (Exception e) {
			// e.printStackTrace();
			throw new RuntimeException("获取数据失败，请联系管理员");
		} finally {
			threadPool.shutdown();
		}
		return entity;
	}
	
	Callable<Map<String, Integer>> getIndexCount = new Callable<Map<String, Integer>>() {
		
		@Override
		public Map<String, Integer> call() throws Exception {
			Map<String, Integer> result = Maps.newHashMap();
			// 珠宝商
			result.put("merNum", merInfoService.getMerCount());
			// 会员
			result.put("accountNum", appAccountService.getAccountCount());
			// 电子证书
			result.put("cerInfoNum", cerInfoService.getCerCount());
			// 累计销售
			// result.put("totalSell", appOrderService.getOrderCount());
			// App下载量
			result.put("AppDownLoadNum", sysConfigService.getDownLoadNum());
			return result;
		}
		
	};
	
	Callable<List<Integer>> getMonthSell = new Callable<List<Integer>>() {

		@Override
		public List<Integer> call() throws Exception {
			List<Integer> result = Lists.newArrayList();
			List<AppOrder> appOrder = appOrderService.getMonthSell();
			List<MainMerVO> merVO = Lists.newArrayList();
			for (AppOrder item : appOrder) {
				// 判断是否为今年
				if (DateUtils.getYear().equals(Convert.toStr(item.getMonth()).substring(0, 4))) {
					MainMerVO entity = new MainMerVO();
					entity.setNum(item.getMonth());
					entity.setSaleNum(item.getCount());
					merVO.add(entity);
				}
			}
			result = getMonthSallList(merVO);
			return result;
		}
		
	};

	Callable<List<MainMerVO>> getMerSellNum = new Callable<List<MainMerVO>>() {

		@Override
		public List<MainMerVO> call() throws Exception {
			List<MainMerVO> result = Lists.newArrayList();
			List<MerInfo> merInfo = merInfoService.getMerInfo();
			MainMerVO merVO = null;
			for (MerInfo item : merInfo) {
				merVO = new MainMerVO();
				merVO.setName(item.getName());
				merVO.setSaleNum(appOrderService.getMerOrderCount(item.getId()));
				// 销量为0不显示
				if (merVO.getSaleNum() > 0) {
					result.add(merVO);
				}
			}
			result = getTOPList(result);
			for (int i = 0, length = result.size(); i < length; i++) {
				MainMerVO item = result.get(i);
				item.setNum(i + 1);
			}
			return result;
		}
		
	};
	
	Callable<List<Integer>> getMonthVerify = new Callable<List<Integer>>() {

		@Override
		public List<Integer> call() throws Exception {
			List<Integer> result = Lists.newArrayList();
			List<CerVerify> cerVerify = cerVerifyService.getMonthCount();
			List<MainMerVO> merVO = Lists.newArrayList();
			MainMerVO entity = null;
			for (CerVerify item : cerVerify) {
				// 判断是否为今年
				if (DateUtils.getYear().equals(Convert.toStr(item.getMonth()).substring(0, 4))) {
					// 将对象存入，判断今年月份是否从01开始
					entity = new MainMerVO();
					entity.setNum(item.getMonth());
					entity.setSaleNum(item.getCount());
					merVO.add(entity);
				}
			}
			result = getMonthSallList(merVO);
			return result;
		}
		
	};
	
	Callable<List<MainVerifyVO>> getVerifyChannel = new Callable<List<MainVerifyVO>>() {

		@Override
		public List<MainVerifyVO> call() throws Exception {
			List<MainVerifyVO> result = Lists.newArrayList();
			final String[] channel = {"移动端APP", "第三方工具"};
			for (String str : channel) {
				MainVerifyVO verifyVO = new MainVerifyVO();
				verifyVO.setName(str);
				verifyVO.setValue(cerVerifyService.getVerifyChannel(str));
				result.add(verifyVO);
			}
			return result;
		}
		
	};
	
	Callable<List<MainVerifyVO>> getVerifyType = new Callable<List<MainVerifyVO>>() {

		@Override
		public List<MainVerifyVO> call() throws Exception {
			List<MainVerifyVO> result = Lists.newArrayList();
			List<CerVerify> cerVerify = cerVerifyService.getVerifyTypeCode();
			MainVerifyVO verifyVO = null;
			for (CerVerify item : cerVerify) {
				verifyVO = new MainVerifyVO();
				switch(item.getTypeCode()) {
				case "sm" :
					verifyVO.setName("扫码验证");
					break;
				case "nfc" :
					verifyVO.setName("NFC验证");
					break;
				default :
					verifyVO.setName("编号验证");
				}
				verifyVO.setValue(item.getCount());
				result.add(verifyVO);
			}
			return result;
		}
		
	};
	
	Callable<List<MainMerVO>> getMerVerifyNum = new Callable<List<MainMerVO>>() {

		@Override
		public List<MainMerVO> call() throws Exception {
			List<MainMerVO> result = Lists.newArrayList();
			List<CerVerify> cerVerify = cerVerifyService.getMerVerifyNum();
			MainMerVO merVO = null;
			for (CerVerify item : cerVerify) {
				merVO = new MainMerVO();
				// 暂时保存珠宝商Id
				merVO.setNum(item.getMertId());
				merVO.setSaleNum(item.getCount());
				result.add(merVO);
			}
			result = getTOPList(result);
			for (MainMerVO item : result) {
				MerInfo info = merInfoService.selectById(item.getNum());
				if (ObjectUtil.isNotNull(info)) {
					item.setName(info.getName());
				} else {
					item.setName("未知");
				}		
			}
			return result;
		}
		
	};
	
	// 得到TOP5集合
	private List<MainMerVO> getTOPList(List<MainMerVO> merVO) {
		if (merVO.size() == 0) {
			return merVO;
		}
		Collections.sort(merVO, new Comparator<MainMerVO>() {
			@Override
			public int compare(MainMerVO o1, MainMerVO o2) {
				if (o1.getSaleNum() > o2.getSaleNum()) {
                    return 1;
                }
                if (o1.getSaleNum() == o2.getSaleNum()) {
                    return 0;
                }
               
                return -1;
			}			
		});
		Collections.reverse(merVO);
		if (merVO.size() > 5)
			merVO = merVO.subList(0, 5);
		return merVO;
	}
	
	// 得到月份销售List
	private List<Integer> getMonthSallList(List<MainMerVO> merVO) {
		List<Integer> result = Lists.newArrayList();
		if (merVO.size() > 0) {	
			int month = Integer.valueOf(Convert.toStr(merVO.get(0).getNum()).substring(5, 6));
			// 如果不是从01月份开始，之前月份销售为0
			if (month > 1) {
				for (int i = month; i > 1; i--) {
					result.add(0);
				}
			}
			for (MainMerVO item : merVO) {
				result.add(item.getSaleNum());
			}
		}
		// 如果不满12个月,销售量为0
		if (result.size() < 12) {
			for (int i = 12 - result.size(); i > 0; i--) {
				result.add(0);
			}
		}
		return result;
	}
}
