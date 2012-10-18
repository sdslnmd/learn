package com.taobao.top.xbox.asynlog.data;


/**
 * 日志记录结构定义 
 * @author wenchu
 *
 */
public class TopLog implements java.io.Serializable
{
	private static final long serialVersionUID = -1553696467252437619L;
	
	String className;
	Long logTime;

	public String getClassName()
	{
		return className;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}

	public Long getLogTime() {
		return logTime;
	}

	public void setLogTime(Long logTime) {
		this.logTime = logTime;
	}

}
