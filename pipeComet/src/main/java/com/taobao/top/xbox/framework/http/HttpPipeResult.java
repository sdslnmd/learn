/**
 * taobao.com 2008 copyright
 */
package com.taobao.top.xbox.framework.http;

import com.taobao.top.xbox.framework.IPipeResult;


/**
 * 管道返回的结果
 * @author fangweng
 * @email fangweng@taobao.com
 * @date 2011-3-28
 *
 */
public class HttpPipeResult implements IPipeResult {

	private static final long serialVersionUID = 410179980412071183L;
    
	private Exception exception;
	
	private boolean breakPipeChain = false;
	
	private Object result;
	
	private int status = STATUS_UNDO;//
	
	private long executeTime;


	public void setException(Exception exception)
	{
		this.exception = exception;
	}

	@Override
	public boolean isBreakPipeChain()
	{
		return breakPipeChain;
	}

	@Override
	public void setBreakPipeChain(boolean breakPipeChain)
	{
		this.breakPipeChain = breakPipeChain;
	}
	
	public Exception getException() {
		return exception;
	}

	public void setExecuteTime(long time) {
		this.executeTime = time;
	}

	public long getExecuteTime() {
		return this.executeTime;
	}
	
	@Override
	public Object getResult() {
		// TODO Auto-generated method stub
		return result;
	}
	
	public void setResult(Object o)
	{
		result = o;
	}

	@Override
	public boolean isCancelled() {
		if (status == STATUS_CANCEL)
			return true;
		else
			return false;
	}

	@Override
	public boolean isDone() {
		if (status == STATUS_DONE)
			return true;
		else
			return false;
	}

	@Override
	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
 
