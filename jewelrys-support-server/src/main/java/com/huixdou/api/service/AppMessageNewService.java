package com.huixdou.api.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.huixdou.api.bean.*;
import com.huixdou.api.dao.AppMessageNewDao;
import com.huixdou.common.base.BaseService;
import com.huixdou.common.base.Result;
import com.huixdou.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import static com.huixdou.common.utils.Constant.REGEX_MOBILE;
import static com.huixdou.common.utils.IdGen.getObjectId;

/**
 * @author jinxin
 * @date 2019/4/25 10:22
 *
 *
 * 消息表添加消息记录
 *     （系统消息）单发消息类型：中间插入数据 状态为未读
 *     （系统消息）群发消息类型：中间表不插入数据
 *
 *
 *
 *
 */
@Service
public class AppMessageNewService extends BaseService<AppMessageNewDao, AppMessageNew> {
    @Autowired
    private PushMessageNew pushMessageNew;

    @Autowired
    private SysDictService dictService;

    @Autowired
    private AppAccountService appAccountService;

	@Autowired
	private AppAccountMessageService appAccountMessageService;

    /**
     * app消息中心添加
     * @param appMessageNew
     * @return
     */
    @Transactional(readOnly = false)
    public Result insertAppMessage(AppMessageNew appMessageNew) {
        appMessageNew.setId(getObjectId());
        appMessageNew.setCreateDate(String.valueOf(System.currentTimeMillis()));

        /**
         * 如果是单发就需要验证手机号码正确性
         */
        if (appMessageNew.getGroupFlag() == 0) {
            if (!Pattern.matches(REGEX_MOBILE, appMessageNew.getMember())) {
                return Result.failure("请输入正确的发送手机号");
            }
            /**
             * 如果为单发需要在子表中添加接收人并状态为未读
             */
            appAccountMessageService.insert(new AppAccountMessage(getObjectId(), appMessageNew.getMember(),
                    appMessageNew.getId(), 0,System.currentTimeMillis()+""));
        }
        dao.insert(appMessageNew);
        return Result.success();
    }



    
    
    /**
     * app消息中心更新
     *
     * @param appMessageNew
     * @return
     *
     */
    @Transactional(readOnly = false)
    public Result updateAppMessage(AppMessageNew appMessageNew) {
        /**
         * 如果是单发就需要验证手机号码正确性
         */
        if (appMessageNew.getGroupFlag() == 0) {
            if (!Pattern.matches(REGEX_MOBILE, appMessageNew.getMember())) {
                return Result.failure("请输入正确的发送手机号");
            }
            /**
             * 如果是单发则需要维护子表的字段
             */
            Result appAccountMessageResult = appAccountMessageUpdate(appMessageNew);
            if (appAccountMessageResult.getCode() != 200) {
                return appAccountMessageResult;
            }
        }
        dao.update(appMessageNew);
        return Result.success();
    }
    
    
	/**
	 * TODO:业务修改此方法已不用
	 * 
	 * app消息添加和更新的业务逻辑
	 * 
	 * @param appMessageNew
	 * @return
	 *
	 */
	@Transactional(readOnly = false)
	private Result commonLogic(AppMessageNew appMessageNew) {
		if (appMessageNew.getGroupFlag() == 0) {
			// 如果为单发，判断接收人是否是标准手机号，然后检测这个手机号是否存在状态是否启用，禁用就发送失败启用就正常发送
			if (!Pattern.matches(REGEX_MOBILE, appMessageNew.getMember())) {
				return Result.failure("请输入正确的发送手机号");
			} else {
				if (appAccountStatus(appMessageNew.getMember()) == 1) {
					/**
					 * appMessageNew的id为空则是添加操作 不为空则是更新操作
					 * 
					 * 类型（0：系统消息(显示在APP列表中)；1：推送消息（数据字典：app-message-type））
					 * 
					 * 如果为messageType 为 0，则为系统消息需要显示在app我的列表角标需要加1
					 */
                    if (StrUtil.isBlank(appMessageNew.getId())) {
/******************************************************************** 添加操作**********************************************************************/
                        appMessageNew.setId(getObjectId());
                        appMessageNew.setCreateDate(String.valueOf(System.currentTimeMillis()));
						if (appMessageNew.getMessageType() == 0) {
							appMessageNew.setAddOne(1);
						}
						appAccountMessageService.insert(new AppAccountMessage(getObjectId(), appMessageNew.getMember(),
								appMessageNew.getId(), 0,System.currentTimeMillis()+""));
/******************************************************************** 添加操作**********************************************************************/

					} else {
/******************************************************************** 更新操作**********************************************************************/

						Result appAccountMessageResult = appAccountMessageUpdate(appMessageNew);
						if (appAccountMessageResult.getCode() != 200) {
							return appAccountMessageResult;
						}
/******************************************************************** 更新操作**********************************************************************/

					}

					Result result = pushMessageNew.pushMessage(appMessageNew);
                    appMessageNew.setSendDate(System.currentTimeMillis() + "");
					if (result.getCode() == 200) {
						appMessageNew.setStatus(1);
					} else {
						appMessageNew.setStatus(0);
					}
				} else {
					appMessageNew.setStatus(0);
				}
			}

		} else {
/*******************************************************************新增群发************************************************************************/			
			 if (StrUtil.isBlank(appMessageNew.getId())) {
				   appMessageNew.setId(getObjectId());
                   appMessageNew.setCreateDate(String.valueOf(System.currentTimeMillis()));
                 if (appMessageNew.getMessageType() == 0) {
                     appMessageNew.setAddOne(1);
                 }
			 }
/*******************************************************************新增群发************************************************************************/			
			// 群发
			Result result = pushMessageNew.pushMessage(appMessageNew);
			if (result.getCode() == 200) {
				appMessageNew.setSendDate(System.currentTimeMillis() + "");
				appMessageNew.setStatus(1);
			} else {
				appMessageNew.setStatus(0);
			}
		}
		return Result.success();

	}
    
    
    /**
     * 查询
     *
     * @param params
     * @return
     */
    @Override
    public List<AppMessageNew> selectList(Map<String, Object> params) {
        List<AppMessageNew> appMessages = dao.selectList(params);
        for (AppMessageNew appMessageNew : appMessages) {
            SysDict dict = dictService.selectByTypeAndCode("app-message-status", appMessageNew.getStatus() + "");
            if (ObjectUtil.isNotNull(dict)) {
                appMessageNew.setStatusName(dict.getName());
            }
            SysDict labelName = dictService.selectByTypeAndCode("app-message-label", appMessageNew.getLabel());
            if (ObjectUtil.isNotNull(labelName)) {
                appMessageNew.setLabelName(labelName.getName());
            }
            if (appMessageNew.getCreateDate() != null && appMessageNew.getCreateDate() != "") {
                appMessageNew.setCreateDate(
                        DateUtils.formatDate(Convert.toLong(appMessageNew.getCreateDate()), "yyyy-MM-dd HH:mm"));
            } else {
                appMessageNew.setCreateDate("");
            }
            SysDict messageType = dictService.selectByTypeAndCode("app-message-type", appMessageNew.getMessageType() + "");
            if (ObjectUtil.isNotNull(messageType)) {
				appMessageNew.setMessageTypeName(messageType.getName());
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
        if (ObjectUtil.isNull(params.get("Ids"))) {
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
        params.put("deleteTime", System.currentTimeMillis()+"");
        appAccountMessageService.deleteListIds(list);
        dao.deleteByIds(params);
        return Result.success();
    }


    /**
     * @param id
     *            需要查询的主键id
     * @param id
     * @return
     *
     */
    public Result details(String id) {
        if (ObjectUtil.isNull(id)) {
            return Result.failure("请输入合法的id参数");
        }
        AppMessageNew appMessageNew = selectAppMessageNewById(id);
        if (ObjectUtil.isNull(appMessageNew)) {
            return Result.failure("没有查到此信息");
        }
        if (StrUtil.isNotBlank(appMessageNew.getCreateDate())) {
            appMessageNew.setCreateDate(
                    DateUtils.formatDate(Convert.toLong(appMessageNew.getCreateDate()), "yyyy-MM-dd HH:mm"));
        } else {
            appMessageNew.setCreateDate("");
        }
        SysDict labelName = dictService.selectByTypeAndCode("app-message-label", appMessageNew.getLabel());
        if (ObjectUtil.isNotNull(labelName)) {
            appMessageNew.setLabelName(labelName.getName());
        }
        return Result.success(appMessageNew);
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
        if (ObjectUtil.isNull(params.get("Ids"))) {
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
            AppMessageNew appMessageNew = selectAppMessageNewById(id);
            Result result = new Result();

            /**
             * 加一的情况:
             * 如果发送次数等于0 并且消息类型为系统消息addone为1。
             *
             * （0：系统消息(显示在APP列表中)；1：推送消息（数据字典：app-message-type））
             */
            if (StrUtil.isNotBlank(appMessageNew.getSendNum().toString())) {
                if (appMessageNew.getSendNum() == 0) {
                    if (appMessageNew.getMessageType() == 0) {
                        appMessageNew.setAddOne(1);
                    }
                }
            }


            // 过滤单发用户为禁用的
            if (appMessageNew.getGroupFlag() == 0 && ObjectUtil.isNotNull(appMessageNew.getMember())) {

                if (appAccountStatus(appMessageNew.getMember()) == 1) {
                    result = pushMessageNew.pushMessage(appMessageNew);
                } else {
                    result.setCode(400);
                }
            } else {
                result = pushMessageNew.pushMessage(appMessageNew);
            }

            AppMessageNew appMessageNewUpdate = new AppMessageNew();
            if (result.getCode() == 200) {
                appMessageNewUpdate.setId(appMessageNew.getId());
                appMessageNewUpdate.setSendDate(System.currentTimeMillis()+"");
                appMessageNewUpdate.setStatus(1);
                dao.sendUpdateAppAccountMessage(appMessageNewUpdate);
            } else {
                appMessageNewUpdate.setId(appMessageNew.getId());
                appMessageNewUpdate.setStatus(0);
                dao.sendUpdateAppAccountMessage(appMessageNewUpdate);
            }
        }
        return Result.success();
    }



    /**
     * 获取接收人列表
     *
     * @return
     *
     */
    public Result receiverList(Map<String, Object> params) {
        return Result.success(appAccountService.selectReceiver(params));
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
     * 更新消息列表的子表（AppAccountMessage）
     * @param appAccountMessage
     * @return
     *
     */
    private Result appAccountMessageUpdate(AppMessageNew appMessageNew){
        List<AppAccountMessage> appAccountMessages = appAccountMessageService.selectOrderByCreateDate(appMessageNew.getId());
        if (appAccountMessages.size() != 0){
             AppAccountMessage appAccountMessage = appAccountMessages.get(0);
            if (ObjectUtil.isNull(appAccountMessages)) {
                return Result.failure("此消息的子表不存在请联系管理员");
            }
            if (StrUtil.isNotBlank(appMessageNew.getMember())) {
                appAccountMessage.setAppAccountPhone(appMessageNew.getMember());
                appAccountMessageService.update(appAccountMessage);
            }
        }

		return Result.success();
    }

    /**
     * 根据AppMessageNew消息Id查询完成的消息
     * @param id
     * @return
     */
    public AppMessageNew selectAppMessageNewById(String id){
        AppMessageNew appMessageNew = dao.selectById(id);
        /**
         * （0：单发消息；1：群发系统）
         *
         * 如果为单发的话就去中间表中查询接收人
         */
        if (appMessageNew.getGroupFlag() == 0){
           List<AppAccountMessage> appAccountMessages = appAccountMessageService.selectOrderByCreateDate(appMessageNew.getId());
           if (appAccountMessages.size() != 0){
               appMessageNew.setMember(appAccountMessages.get(0).getAppAccountPhone());
           }
        }
        return appMessageNew;
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
    		AppMessageNew appMessageNew=new AppMessageNew();
    		String id = getObjectId();
    		appMessageNew.setTitle(title);
    		appMessageNew.setContent(content);
    		appMessageNew.setUrl(url);
    		appMessageNew.setId(id);
    		appMessageNew.setSendNum(1);
    		appMessageNew.setCreateDate(String.valueOf(new Date().getTime()));
    		appMessageNew.setGroupFlag(1);
    		Result result = pushMessageNew.pushMessage(appMessageNew);
    		if (result.getCode() == 200) {
    			appMessageNew.setSendDate(String.valueOf(new Date().getTime()));
    			appMessageNew.setStatus(1);
    		} else {
    			appMessageNew.setStatus(0);
    		}
    		// 插入表
    		dao.insert(appMessageNew);
    		return result;
    	}
}
