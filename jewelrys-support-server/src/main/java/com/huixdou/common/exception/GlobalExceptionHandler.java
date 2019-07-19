package com.huixdou.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import com.huixdou.common.base.Result;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@ExceptionHandler(ForbiddenException.class)
	public Object handlerForbiddenException(Exception e) {
		return new Result(HttpStatus.FORBIDDEN.value(), e.getMessage());
	}
	
	@ExceptionHandler(NoHandlerFoundException.class)
	public Object handlerNoFoundException(Exception e) {
		logger.error(e.getMessage(), e);
		return new Result(404, "路径不存在，请检查路径是否正确");
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public Object handleDuplicateKeyException(DuplicateKeyException e) {
		logger.error(e.getMessage(), e);
		return Result.error("数据库中已存在该记录");
	}
	
	@ExceptionHandler(BindException.class)
	public Object handleBindException(BindException e) {
		StringBuilder sb = new StringBuilder();
		for (ObjectError err : e.getAllErrors()) {
			sb.append(err.getDefaultMessage()).append("<br>");
		}
		return Result.failure(sb.toString());
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		StringBuilder sb = new StringBuilder();
		for (ObjectError err : e.getBindingResult().getAllErrors()) {
			sb.append(err.getDefaultMessage()).append("<br>");
		}
		return Result.failure(sb.toString());
	}
	
	@ExceptionHandler(Exception.class)
	public Object handleException(Exception e) {
		logger.error(e.getMessage(), e);
		
		if (e instanceof NullPointerException) {
			return Result.error("接口异常，请联系管理员");
		}
		
		return Result.error(e.getMessage());
	}

}
