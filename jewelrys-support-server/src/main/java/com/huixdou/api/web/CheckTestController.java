package com.huixdou.api.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huixdou.api.service.CheckService;
import com.huixdou.common.base.BaseController;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.convert.ConverterRegistry;

@RestController
@RequestMapping("/sup/check")
public class CheckTestController extends BaseController {

	@Autowired
	private CheckService checkService;
	// 文本
	@PostMapping("/text")
	public Object testText(@RequestBody Map<String, Object> params) throws Exception {
		Map<String, String> textCheckparam = new HashMap<>();
		String content = Convert.toStr(params.get("content"));
		if (StringUtils.isEmpty(content)) {
			return failure("传入参数不能为空");
		}
		textCheckparam.put("content", content);
		return checkService.textCheck(textCheckparam);

	}

	// 图片
	@PostMapping("/image")
	public Object testImage(@RequestBody Map<String, Object> params) throws Exception {
		List<String> imageList = Convert.toList(String.class, params.get("list"));
		if (StringUtils.isEmpty(imageList) || imageList.size() < 1) {
			return failure("传入参数不能为空");
		}

		return checkService.ImageCheck(imageList);

	}

	// 视频提交
	@PostMapping("/submit")
	public Object testVedioSubmit(@RequestBody Map<String, Object> params) throws Exception {
		Map<String, String> videoParams = new HashMap<>();
		String video = Convert.toStr(params.get("video"));
		if (StringUtils.isEmpty(video)) {
			return failure("传入参数不能为空");
		}
		videoParams.put("video", video);

		return checkService.videoSubmit(videoParams);

	}

	// 视频结果查询
	@PostMapping("/result")
	public Object testVedioResult(@RequestBody Map<String, Object> params) throws Exception {
		ConverterRegistry converterRegistry = ConverterRegistry.getInstance();

		Set<String> taskIds = converterRegistry.convert(Set.class, params.get("taskIds"));
		if (StringUtils.isEmpty(taskIds) || taskIds.size() < 1) {
			return failure("传入参数不能为空");
		}

		return checkService.videoGet(taskIds);

	}

	// 视频结果查询
	@PostMapping("/check-result")
	public void test1VedioResult(@RequestBody Map<String, String> params, HttpServletRequest request) throws Exception {
		
		checkService.checkNoVideo(params,request);
	}
}
