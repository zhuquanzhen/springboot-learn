package com.huixdou.api.web;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.huixdou.api.bean.Oss;
import com.huixdou.api.oss.OSSFactory;
import com.huixdou.api.service.OssService;
import com.huixdou.common.base.BaseController;
import com.huixdou.common.utils.StringUtils;

import cn.hutool.core.convert.Convert;


@RestController
@RequestMapping("/oss")
public class OssController extends BaseController {
	
	@Autowired
	private OssService ossService;
	
	@GetMapping("/{id}")
	public void get(@PathVariable("id") String id, HttpServletResponse response) {
		if (StringUtils.isBlank(id)) {
			throw new RuntimeException("参数id值为空.");
		}

		Oss oss = ossService.selectById(id);
		if (oss == null) {
			throw new RuntimeException("该文件不存在.");
		}

		try {
			String fileName = oss.getName();
			// 判断文件类型
			response.setContentType(getContentType(fileName));
			if (!ossService.isImage(fileName) && !ossService.isVedio(fileName)) {
				response.addHeader("Content-Disposition", "attachment; filename=" + new String(oss.getName().getBytes(), "iso-8859-1"));
			}
			
//			String prefix = storageConfig.getLocal().getPrefix();
//			
//			if (prefix.endsWith("/")) {
//				prefix = StrUtil.removeSuffix(prefix, "/");
//			}
			
			IOUtils.copy(new FileInputStream(oss.getPath()), response.getOutputStream());
			response.flushBuffer();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("该文件路径不存在.");
		} catch (IOException e) {
			throw new RuntimeException("读取文件异常.");
		}

	}
	
	@PostMapping("/post")
	public Object post(@RequestParam("file") MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			throw new RuntimeException("上传文件不能为空");
		}
		// 上传文件
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		String result = OSSFactory.build().uploadSuffix(file.getBytes(), suffix);
		
		String[] arr = StringUtils.splitByWholeSeparator(result, "|");
		String url = arr[0];
		String path = arr[1];

		// 保存文件信息
		Oss oss = new Oss();
		oss.setName(file.getOriginalFilename());
		oss.setUrl(url);
		oss.setPath(path);
		oss.setSuffix(FilenameUtils.getExtension(oss.getName()));
//		oss.setThumbpath(thumbpath);
		oss.setCreateDate(Convert.toStr(new Date().getTime()));

		ossService.insert(oss);
		
		return success(oss);
	}

//	@PostMapping
//	public Object list(@RequestBody Map<String, Object> params) {
//		return success(ossService.selectPage(params));
//	}

//	@GetMapping("/delete")
//	public Object delete(String id) {
//		ossService.deleteById(id);
//		return success();
//	}
	
	public String getContentType(String returnFileName) {
		String contentType = "application/octet-stream";
		if (returnFileName.lastIndexOf(".") < 0)
			return contentType;
		returnFileName = returnFileName.toLowerCase();
		returnFileName = returnFileName.substring(returnFileName.lastIndexOf(".") + 1);
		if (returnFileName.equals("html") || returnFileName.equals("htm") || returnFileName.equals("shtml")) {
			contentType = "text/html";
		} else if (returnFileName.equals("apk")) {
			contentType = "application/vnd.android.package-archive";
		} else if (returnFileName.equals("sis")) {
			contentType = "application/vnd.symbian.install";
		} else if (returnFileName.equals("sisx")) {
			contentType = "application/vnd.symbian.install";
		} else if (returnFileName.equals("exe")) {
			contentType = "application/x-msdownload";
		} else if (returnFileName.equals("msi")) {
			contentType = "application/x-msdownload";
		} else if (returnFileName.equals("css")) {
			contentType = "text/css";
		} else if (returnFileName.equals("xml")) {
			contentType = "text/xml";
		} else if (returnFileName.equals("gif")) {
			contentType = "image/gif";
		} else if (returnFileName.equals("jpeg") || returnFileName.equals("jpg")) {
			contentType = "image/jpeg";
		} else if (returnFileName.equals("js")) {
			contentType = "application/x-javascript";
		} else if (returnFileName.equals("atom")) {
			contentType = "application/atom+xml";
		} else if (returnFileName.equals("rss")) {
			contentType = "application/rss+xml";
		} else if (returnFileName.equals("mml")) {
			contentType = "text/mathml";
		} else if (returnFileName.equals("txt")) {
			contentType = "text/plain";
		} else if (returnFileName.equals("jad")) {
			contentType = "text/vnd.sun.j2me.app-descriptor";
		} else if (returnFileName.equals("wml")) {
			contentType = "text/vnd.wap.wml";
		} else if (returnFileName.equals("htc")) {
			contentType = "text/x-component";
		} else if (returnFileName.equals("png")) {
			contentType = "image/png";
		} else if (returnFileName.equals("tif") || returnFileName.equals("tiff")) {
			contentType = "image/tiff";
		} else if (returnFileName.equals("wbmp")) {
			contentType = "image/vnd.wap.wbmp";
		} else if (returnFileName.equals("ico")) {
			contentType = "image/x-icon";
		} else if (returnFileName.equals("jng")) {
			contentType = "image/x-jng";
		} else if (returnFileName.equals("bmp")) {
			contentType = "image/x-ms-bmp";
		} else if (returnFileName.equals("svg")) {
			contentType = "image/svg+xml";
		} else if (returnFileName.equals("jar") || returnFileName.equals("var") || returnFileName.equals("ear")) {
			contentType = "application/java-archive";
		} else if (returnFileName.equals("doc")) {
			contentType = "application/msword";
		} else if (returnFileName.equals("pdf")) {
			contentType = "application/pdf";
		} else if (returnFileName.equals("rtf")) {
			contentType = "application/rtf";
		} else if (returnFileName.equals("xls")) {
			contentType = "application/vnd.ms-excel";
		} else if (returnFileName.equals("ppt")) {
			contentType = "application/vnd.ms-powerpoint";
		} else if (returnFileName.equals("7z")) {
			contentType = "application/x-7z-compressed";
		} else if (returnFileName.equals("rar")) {
			contentType = "application/x-rar-compressed";
		} else if (returnFileName.equals("swf")) {
			contentType = "application/x-shockwave-flash";
		} else if (returnFileName.equals("rpm")) {
			contentType = "application/x-redhat-package-manager";
		} else if (returnFileName.equals("der") || returnFileName.equals("pem") || returnFileName.equals("crt")) {
			contentType = "application/x-x509-ca-cert";
		} else if (returnFileName.equals("xhtml")) {
			contentType = "application/xhtml+xml";
		} else if (returnFileName.equals("zip")) {
			contentType = "application/zip";
		} else if (returnFileName.equals("mid") || returnFileName.equals("midi") || returnFileName.equals("kar")) {
			contentType = "audio/midi";
		} else if (returnFileName.equals("mp3")) {
			contentType = "audio/mpeg";
		} else if (returnFileName.equals("ogg")) {
			contentType = "audio/ogg";
		} else if (returnFileName.equals("m4a")) {
			contentType = "audio/x-m4a";
		} else if (returnFileName.equals("ra")) {
			contentType = "audio/x-realaudio";
		} else if (returnFileName.equals("3gpp") || returnFileName.equals("3gp")) {
			contentType = "video/3gpp";
		} else if (returnFileName.equals("mp4")) {
			contentType = "video/mp4";
		} else if (returnFileName.equals("mpeg") || returnFileName.equals("mpg")) {
			contentType = "video/mpeg";
		} else if (returnFileName.equals("mov")) {
			contentType = "video/quicktime";
		} else if (returnFileName.equals("flv")) {
			contentType = "video/x-flv";
		} else if (returnFileName.equals("m4v")) {
			contentType = "video/x-m4v";
		} else if (returnFileName.equals("mng")) {
			contentType = "video/x-mng";
		} else if (returnFileName.equals("asx") || returnFileName.equals("asf")) {
			contentType = "video/x-ms-asf";
		} else if (returnFileName.equals("wmv")) {
			contentType = "video/x-ms-wmv";
		} else if (returnFileName.equals("avi")) {
			contentType = "video/x-msvideo";
		}
		return contentType;
	}
	
}
