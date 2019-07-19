package com.huixdou.api.web;

import com.huixdou.api.bean.AppSetting;
import com.huixdou.api.service.AppSettingService;
import com.huixdou.common.base.BaseController;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import static com.huixdou.common.utils.Constant.*;

/**
 * Created by jinxin on 2018/12/27 10:16.
 */
@RestController
@RequestMapping("/sup/app-setting")
public class AppSettingController extends BaseController {


    @Autowired
    private AppSettingService appSettingService;

    /**
     * 获取app设置列表
     *
	 * @param name
	 *            查询参数（名称）
	 * @param pageNum
	 *            页码 默认为第一页
	 * @param pageSize
	 *            每页显示数据 如果不传，默认为5
     * @return
     */
    @GetMapping("list")
    public Object list(@RequestParam Map<String, Object> params){
        return success(appSettingService.selectAppSettingPage(params));
    }

    
    
    /**
     * 详情接口
     * @param id
     * @return
     *
     */
    @GetMapping("details")
    public Object details(String id){
    	return appSettingService.details(id);
    }
    
    /**
     * 添加app设置
     * @param name (名称)
     * @param link (链接地址)
     * @param orderNum (排序（序号）)
     * @param remark (备注)
     * @param fileId (图片id)
     * @param dir (设置目录，数据字典 app-dir-setting 的值)
     * @return
     */
    @PostMapping("insert")
    public Object insert(@Valid @RequestBody AppSetting appSetting ){
    	appSettingService.insert(appSetting);
        return success();
    }


    /**
     * 批量发布
     * @param Ids （app设置表的主键集合）
     * @param type （改变发布的状态（发布：1，未发布：0））
     * @return
     */
    @PostMapping("release")
     public Object release(@RequestBody Map<String, Object> params){
        params.put("type", ENABLE);
        return appSettingService.updateReleases(params);
    }

    
    /**
     * 批量撤回
     * @param Ids （app设置表的主键集合）
     * @param type （改变发布的状态（发布：1，未发布：0））
     * @return
     */
    @PostMapping("recall")
    public Object recall(@RequestBody Map<String, Object> params){
       params.put("type", DISABLE);
       return appSettingService.updateReleases(params);
   }
    
    
    
    
    /**
     * 批量删除
     * @param Ids (app设置表被删除的主键id集合)
     * @return
     */
    @PostMapping("delete")
    public Object deleteByIds(@RequestBody Map<String, Object> params){
    	Integer delectId = getUserId();
		params.put("deleteId", delectId);
        return appSettingService.deleteByIds(params);
    }


    /**
     * 更新app设置
     * @param id (需要更新的id)
     * @param name (名称)
     * @param link (链接地址)
     * @param orderNum (排序（序号）)
     * @param remark (备注)
     * @param fileId (图片id)
     * @param dir (设置目录，数据字典 app-dir-setting 的值)
     * @return
     */
    @PostMapping("update")
    public Object update(@Valid @RequestBody AppSetting appSetting ){
    	Map<String, Object> params = new HashMap<>();
		params.put("id", appSetting.getId());
		if (appSettingService.selectList(params).size() == 0) {
			return failure("输入的id不存在");
		}
    	appSettingService.update(appSetting);
        return success();
    }

    /**
     * 获取app设置 设置项列表 
     * @param 
     * @return
     */
    @GetMapping("tree")
    public Object getTree(@RequestParam Map<String, Object> params){
        return appSettingService.getTree("app-setting");
    }

    /**
     * 单独更新排序接口
     * @param id 主键id
     * @param orderNum 排序结果
     * @return
     *
     */
    @PostMapping("update-sort")
    public Object updateSort(@RequestBody AppSetting appSetting ){
    	if (ObjectUtil.isNull(appSetting.getId())) {
    		return failure("id不能为空");
		}
		if (ObjectUtil.isNull(appSettingService.details(appSetting.getId().toString()))) {
			return failure("输入的id不存在");
		}
    	appSettingService.update(appSetting);
        return success();
    }
    
}
