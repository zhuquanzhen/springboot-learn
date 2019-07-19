package com.huixdou.common.exception;

public class ForbiddenException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String msg;
    
    public ForbiddenException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public ForbiddenException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
