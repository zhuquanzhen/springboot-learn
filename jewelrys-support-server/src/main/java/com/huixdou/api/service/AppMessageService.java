package com.huixdou.api.service;

import com.huixdou.api.bean.AppAccount;
import com.huixdou.api.bean.AppMessage;
import com.huixdou.api.bean.SysDict;
import com.huixdou.api.dao.AppMessageDao;
import com.huixdou.common.base.BaseService;
import com.huixdou.common.base.Result;
import com.huixdou.common.utils.DateUtils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.huixdou.common.utils.IdGen.getObjectId;
import static com.huixdou.common.utils.Constant.REGEX_MOBILE;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by jinxin on 2018/12/27 18:32.
 */
@Service
public class AppMessageService extends BaseService<AppMessageDao, AppMessage> {

	@Autowired
	private PushMessage pushMessage;

	@Autowired
	private SysDictService dictService;

	@Autowired
	private AppAccountService appAccountService;

	/**
	 * app消息中心添加 + 发送信息并保存发送状态
	 * 
	 * @param appMessage
	 * @return
	 *
	 */
	@Transactional(readOnly = false)
	public Result insertAppMessage(AppMessage appMessage) {
		appMessage.setId(getObjectId());
		appMessage.setCreateDate(String.valueOf(System.currentTimeMillis()));
		if (appMessage.getGroupFlag() == 0) {
			// 如果为单发，判断接收人是否是标准手机号，然后检测这个手机号是否存在状态是否启用，禁用就发送失败启用就正常发送
			if (!Pattern.matches(REGEX_MOBILE, appMessage.getMember())) {
				return Result.failure("请输入正确的发送手机号");
			} else {
				if (appAccountStatus(appMessage.getMember()) == 1) {
					Result result = pushMessage.pushMessage(appMessage);
					if (result.getCode() == 200) {
						appMessage.setSendDate(String.valueOf(new Date().getTime()));
						appMessage.setStatus(1);
					} else {
						appMessage.setStatus(0);
					}

				} else {
					appMessage.setStatus(0);
				}
			}

		} else {
			// 群发
			Result result = pushMessage.pushMessage(appMessage);
			if (result.getCode() == 200) {
				appMessage.setSendDate(String.valueOf(new Date().getTime()));
				appMessage.setStatus(1);
			} else {
				appMessage.setStatus(0);
			}
		}
		dao.insert(appMessage);
		return Result.success();
	}

	/**
	 * app消息中心更新 + 发送信息并保存发送状态
	 * 
	 * @param appMessage
	 * @return
	 *
	 */
	@Transactional(readOnly = false)
	public Result updateAppMessage(AppMessage appMessage) {
		if (appMessage.getGroupFlag() == 0) {
			// 如果为单发，判断接收人是否是标准手机号，然后检测这个手机号是否存在状态是否启用，禁用就发送失败启用就正常发送
			if (!Pattern.matches(REGEX_MOBILE, appMessage.getMember())) {
				return Result.failure("请输入正确的发送手机号");
			} else {
				if (appAccountStatus(appMessage.getMember()) == 1) {
					Result result = pushMessage.pushMessage(appMessage);
					if (result.getCode() == 200) {
						appMessage.setSendDate(String.valueOf(new Date().getTime()));
						appMessage.setStatus(1);
					} else {
						appMessage.setStatus(0);
					}

				} else {
					appMessage.setStatus(0);
				}
			}

		} else {
			// 群发
			Result result = pushMessage.pushMessage(appMessage);
			if (result.getCode() == 200) {
				appMessage.setSendDate(String.valueOf(new Date().getTime()));
				appMessage.setStatus(1);
			} else {
				appMessage.setStatus(0);
			}
		}
		dao.update(appMessage);
		return Result.success();
	}

	/**
	 * 查询
	 *
	 * @param params
	 * @return
	 */
	public List<AppMessage> selectList(Map<String, Object> params) {
		List<AppMessage> appMessages = dao.selectList(params);
		for (AppMessage appMessage : appMessages) {
			SysDict dict = dictService.selectByTypeAndCode("app-message-status", appMessage.getStatus() + "");
			if (ObjectUtil.isNotNull(dict)) {
				appMessage.setStatusName(dict.getName());
			}
			SysDict labelName = dictService.selectByTypeAndCode("app-message-label", appMessage.getLabel());
			if (ObjectUtil.isNotNull(labelName)) {
				appMessage.setLabelName(labelName.getName());
			}
			if (appMessage.getMember() != null && appMessage.getMember().equalsIgnoreCase("all")) {
				appMessage.setMember("所有人");
			}
			if (appMessage.getCreateDate() != null && appMessage.getCreateDate() != "") {
				appMessage.setCreateDate(
						DateUtils.formatDate(Convert.toLong(appMessage.getCreateDate()), "yyyy-MM-dd HH:mm"));
			} else {
				appMessage.setCreateDate("");
			}
		}
		return appMessages;
	}

	/**
	 * 批量删除
	 *
	 * @param params
	 * @return
	 */
	@Transactional(readOnly = false)
	public Result deleteByIds(Map<String, Object> params) {
		if (Integer.parseInt(params.get("deleteId").toString()) == -1) {
			return Result.failure("请先登陆再进行操作");
		}
		if (params.get("Ids") == null && params.get("Ids") == "") {
			return Result.failure("请选择要删除内容");
		}
		ArrayList<String> list = (ArrayList<String>) Convert.toList(String.class, params.get("Ids"));
		if (list.size() == 0) {
			return Result.failure("请选择要删除内容");
		}
		Integer number = dao.selectNumber(params);
		if (list.size() > number) {
			return Result.failure("有些参数已删除或不存在");
		}
		params.put("deleteTime", new Date().getTime());
		dao.deleteByIds(params);
		return Result.success();
	}

	/**
	 * 消息发送接口
	 *
	 * @param ids
	 *            发送的id （list）
	 * @return
	 */
	@Transactional(readOnly = false)
	public Result sendMessage(Map<String, Object> params) {
		if (params.get("Ids") == null && params.get("Ids") == "") {
			return Result.failure("请选择要发送的内容");
		}
		ArrayList<String> list = (ArrayList<String>) Convert.toList(String.class, params.get("Ids"));
		if (list.size() == 0) {
			return Result.failure("请选择要发送的内容");
		}
		Integer number = dao.selectNumber(params);
		if (list.size() > number) {
			return Result.failure("有些消息已删除或不存在");
		}

		// 发送
		for (String id : list) {
			AppMessage appMessage = dao.selectById(id);
			Result result = new Result();

			// 过滤单发用户为禁用的
			if (appMessage.getGroupFlag() == 0 && ObjectUtil.isNotNull(appMessage.getMember())) {
				if (appAccountStatus(appMessage.getMember()) == 1) {
					result = pushMessage.pushMessage(appMessage);
				} else {
					result.setCode(400);
				}
			} else {
				result = pushMessage.pushMessage(appMessage);
			}

			AppMessage appMessageUpdate = new AppMessage();
			if (result.getCode() == 200) {
				appMessageUpdate.setId(appMessage.getId());
				appMessageUpdate.setSendDate(String.valueOf(new Date().getTime()));
				appMessageUpdate.setStatus(1);
				dao.sendUpdate(appMessageUpdate);
			} else {
				appMessageUpdate.setId(appMessage.getId());
				appMessageUpdate.setStatus(0);
				dao.sendUpdate(appMessageUpdate);
			}
		}
		return Result.success();
	}

	/**
	 * @param id
	 *            需要修改的主键id
	 * @param id
	 * @return
	 *
	 */
	public Result details(String id) {
		if (ObjectUtil.isNull(id)) {
			return Result.failure("请输入合法的id参数");
		}
		AppMessage appMessage = dao.selectById(id);
		if (ObjectUtil.isNull(appMessage)) {
			return Result.failure("没有查到此信息");
		}
		if (appMessage.getCreateDate() != null && appMessage.getCreateDate() != "") {
			appMessage.setCreateDate(
					DateUtils.formatDate(Convert.toLong(appMessage.getCreateDate()), "yyyy-MM-dd HH:mm"));
		} else {
			appMessage.setCreateDate("");
		}
		SysDict labelName = dictService.selectByTypeAndCode("app-message-label", appMessage.getLabel());
		if (ObjectUtil.isNotNull(labelName)) {
			appMessage.setLabelName(labelName.getName());
		}
		return Result.success(appMessage);
	}



	/**
	 * 判断推送信息的用户是否存在或者是否被警用
	 * 
	 * @param phone
	 * @return
	 *
	 */
	private Integer appAccountStatus(String phone) {
		AppAccount account = appAccountService.selectByPhone(phone);
		if (ObjectUtil.isNotNull(account)) {
			return account.getStatus();
		}
		return 0;
	}

/**
 * fxb 2019-4-23 
 * 平台信息发送的方法
 * @param title 标题
 * @param content 内容
 * @param url 链接 
 * @return
 */
	public Result supMessageInsertAndSend(String title,String content,String url) {
		// 群发
		AppMessage appMessage=new AppMessage();
		String id = getObjectId();
		appMessage.setTitle(title);
		appMessage.setContent(content);
		appMessage.setUrl(url);
		appMessage.setId(id);
		appMessage.setMember("all");
		appMessage.setCreateDate(String.valueOf(new Date().getTime()));
		appMessage.setGroupFlag(1);
		Result result = pushMessage.pushMessage(appMessage);
		if (result.getCode() == 200) {
			appMessage.setSendDate(String.valueOf(new Date().getTime()));
			appMessage.setStatus(1);
		} else {
			appMessage.setStatus(0);
		}
		// 插入表
		dao.insert(appMessage);
		return result;
	}
}
