package com.huixdou.api.web;

import com.huixdou.api.bean.AppMessageNew;
import com.huixdou.api.service.AppMessageNewService;
import com.huixdou.common.base.BaseController;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import javax.validation.Valid;

/**
 * Created by jinxin on 2018/12/27 18:33.
 */
@RestController
@RequestMapping("/sup/app-message")
public class AppMessageController extends BaseController {


	@Autowired
	private AppMessageNewService appMessageNewService;

	/**
	 * 添加消息中心
	 *
	 * @param title
	 *            消息标题
	 * @param label
	 *            消息标签
	 * @param groupFlag
	 *            群发标志(0：单发 1：群发）
	 * @param url
	 *            链接
	 * @param content
	 *            内容
	 * @param member
	 *            单发为会员账号群发为all
	 * 
	 * @return
	 */
	@PostMapping("insert")
	public Object insert(@Valid @RequestBody AppMessageNew appMessageNew) {
		return appMessageNewService.insertAppMessage(appMessageNew);
	}

	/**
	 * 查询
	 *
	 * @param startDate
	 *            查询开始时间
	 * @param endDate
	 *            查询的结束时间
	 * @param querys
	 *            查询参数（消息标题）
	 * @param pageNum
	 *            页码 默认为第一页
	 * @param pageSize
	 *            每页显示数据 如果不传，默认为5
	 * @param status
	 *            启用状态
	 *            
	 * @return
	 */
	@GetMapping("list")
	public Object list(@RequestParam Map<String, Object> params) {
		return success(appMessageNewService.selectPage(params));
	}

	/**
	 * 批量删除
	 *
	 * @param AppMessageIds
	 *            app消息中心表主键id集合
	 * @return
	 */
	@PostMapping("delete")
	public Object deleteByIds(@RequestBody Map<String, Object> params) {
		Integer delectId = getUserId();
		params.put("deleteId", delectId);
		return appMessageNewService.deleteByIds(params);
	}

	/**
	 * 更新
	 * 
	 * @param title
	 *            消息标题
	 * @param label
	 *            消息标签
	 * @param groupFlag
	 *            群发标志(0：单发 1：群发）
	 * @param url
	 *            链接
	 * @param content
	 *            内容
	 * @param member
	 *            单发为会员账号群发为所有人
	 * @param id
	 *            需要修改的主键id
	 * @return
	 */
	@PostMapping("update")
	public Object update(@Valid @RequestBody AppMessageNew appMessageNew) {
		if (ObjectUtil.isNull(appMessageNewService.selectById(appMessageNew.getId()))) {
			return failure("输入的id不存在");
		}
		return appMessageNewService.updateAppMessage(appMessageNew);
	}

	/**
	 * @param id
	 *            需要修改的主键id
	 * @return
	 *
	 */
	 @GetMapping("details")
	 public Object details(String id) {
		return appMessageNewService.details(id);
	 }
	 
	/**
	 * 消息发送接口
	 *
	 * @param ids  发送的id （list）s
	 * @return
	 */
	@PostMapping("send-message")
	 public Object sendMessage(@RequestBody Map<String, Object> params) {
		Integer userId = getUserId();
		if (userId ==-1) {
			return failure("请先登陆再操作");
		}
		  return appMessageNewService.sendMessage(params);
	 }
	
	/**
	 * 获取接收人列表
	 *
	 * @param startDate
	 *            查询开始时间
	 * @param endDate
	 *            查询的结束时间
	 * @param querys
	 *            查询参数（消息标题）
	 * @param pageNum
	 *            页码 默认为第一页
	 * @param pageSize
	 *            每页显示数据 如果不传，默认为5
	 * @return
	 */
	@GetMapping("receiver-list")
	public Object receiverList(@RequestParam Map<String, Object> params){
		return appMessageNewService.receiverList(params);
	}
	
	
}
