package com.taobao.top.xbox.framework.http.exception;


public class FileSizeInvalidException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1093139935663157928L;
	public FileSizeInvalidException(){
		
	}
	public FileSizeInvalidException(String message,Throwable cause){
		super(message,cause);
	}
	public FileSizeInvalidException(String message){
		super(message);
	}
	public FileSizeInvalidException(Throwable cause){
		super(cause);
	}
}
