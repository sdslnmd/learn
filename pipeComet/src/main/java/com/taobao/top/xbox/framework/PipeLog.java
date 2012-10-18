/**
 * 
 */
package com.taobao.top.xbox.framework;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 当前用于框架中记录管道执行时间，可扩展记录更多的信息
 * @author fangweng
 * @email fangweng@taobao.com
 * @date 2011-5-10
 *
 */
public class PipeLog implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3731826849596277239L;
	
	protected String className;
	protected Long logTime;
	
	/**
	 * 整个请求事务占用的时间，包括平台消耗时间和服务消耗时间
	 */
	private long transactionConsumeTime;
	
	/**
	 * 时间打点队列
	 */
	protected List<Long> timeStampQueue = new CopyOnWriteArrayList<Long>();

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
	
	public List<Long> getTimeStampQueue() {
		return timeStampQueue;
	}
	
	public long getTransactionConsumeTime() {
		return transactionConsumeTime;
	}


	public void setTransactionConsumeTime(long transactionConsumeTime) {
		this.transactionConsumeTime = transactionConsumeTime;
	}

	public void track(Long timestamp)
	{
		timeStampQueue.add(timestamp);
	}
	
	public void track()
	{		
		timeStampQueue.add(System.currentTimeMillis());
	}
	
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		
		result.append(getTransactionConsumeTime());
		
		int size = timeStampQueue.size();
	
		for(int i = 0 ; i < size; i++)
			result.append(",").append(timeStampQueue.get(i));
		
		return result.toString();
	}

}
