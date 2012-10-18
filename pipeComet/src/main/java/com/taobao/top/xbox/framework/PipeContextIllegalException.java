package com.taobao.top.xbox.framework;

/**
 * @author fangweng
 * @email fangweng@taobao.com
 * @date 2011-3-28
 *
 */
public class PipeContextIllegalException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4376779980050420855L;
	
	public PipeContextIllegalException(){
		
	}
	public PipeContextIllegalException(String message){
		super(message);
	}
	public PipeContextIllegalException(Throwable cause){
		super(cause);
	}
	public PipeContextIllegalException(String message, Throwable cause){
		super(message,cause);
	}
}
