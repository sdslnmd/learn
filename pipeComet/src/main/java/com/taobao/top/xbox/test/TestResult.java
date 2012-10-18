package com.taobao.top.xbox.test;

/**
 * 测试结果
 * @author fangweng
 *
 */
public class TestResult
{
	boolean isSuccess;
	Exception exception;
	String desc;
	long timeConsume;
	
	public long getTimeConsume()
	{
		return timeConsume;
	}
	public void setTimeConsume(long timeConsume)
	{
		this.timeConsume = timeConsume;
	}
	public boolean isSuccess()
	{
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess)
	{
		this.isSuccess = isSuccess;
	}
	public Exception getException()
	{
		return exception;
	}
	public void setException(Exception exception)
	{
		this.exception = exception;
	}
	public String getDesc()
	{
		return desc;
	}
	public void setDesc(String desc)
	{
		this.desc = desc;
	}
	
	
}
