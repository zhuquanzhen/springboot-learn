package com.huixdou.api.web;

import com.huixdou.api.bean.SysConfig;
import com.huixdou.api.service.SysConfigService;
import com.huixdou.common.base.BaseController;
import com.huixdou.common.base.Result;

import cn.hutool.core.convert.Convert;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import javax.validation.Valid;

@RestController
@RequestMapping("/sup/sys-config")
public class SysConfigController extends BaseController {

    @Autowired
    private SysConfigService configService;
    
    /**
     * 获取配置列表
     * @param pageNum 	页码
     * @param pageSize 	每页数据量
     * @param val		搜索值
     * @return
     */
    @GetMapping("list")
    public Object selectList(@RequestParam Map<String, Object> params) {
        return success(configService.selectPage(params));
    }
    
    /**
     * 插入新的配置信息
     * @param config
     * @return
     */
    @PostMapping("insert")
    public Object insert(@RequestBody @Valid SysConfig config) {
        return configService.create(config);
    }
    
    /**
     * 更新配置信息
     * @param config
     * @return
     */
    @PostMapping("update")
    public Object update(@RequestBody @Valid SysConfig config) {
        return configService.updateConfig(config);
    }
    
    /**
     * 删除配置信息
     * @param id[] 主键ID数组
     * @return
     */
    @PostMapping("delete")
    public Object delete(@RequestBody Map<String, Object> params) {
    	params.put("deleteId", getUserId());
        return configService.delete(params);
    }
    
    /**
     * 获取某条配置的详细信息
     * @param id 主键ID
     * @return
     */
    @GetMapping("get")
    public Object selectById(@RequestParam("id") Integer id) {
    	if(StringUtils.isBlank(Convert.toStr(id))) {
    		return Result.failure("id 参数不能为空");
    	}
    	return success(configService.selectById(id));
    }
}
