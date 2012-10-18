package com.taobao.top.xbox.framework.http.exception;


public class FileTypeInvalidException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8135274904695801043L;
	
	public FileTypeInvalidException(){
		
	}
	public FileTypeInvalidException(String message,Throwable cause){
		super(message,cause);
	}
	public FileTypeInvalidException(String message){
		super(message);
	}
	public FileTypeInvalidException(Throwable cause){
		super(cause);
	}
}
