package com.taobao.top.xbox.asynlog.data;

/**
 * 用于记录分散的日志内容，采用Thread Local的方式，
 * 注意使用后需要remove，防止内存泄露或者重用产生的异常
 * 如果所有日志内容在一个方法就可以创建或者很容易在几个方法获得
 * ，那么可以不使用这个类
 * @author wenchu
 *
 */
public class TopLogContext 
{
	private static ThreadLocal<TopLog> threadTopLog = new ThreadLocal<TopLog>();
	
	public static void setTopLog(TopLog topLog)
	{
		if (topLog == null)
			return;
		
		threadTopLog.set(topLog);
	}
	
	/**
	 * 提高性能采用固定写死的方式
	 * @return
	 */
	public static TopRequestLog getDefaultTopLog()
	{
		TopRequestLog topLog = (TopRequestLog)threadTopLog.get();
		
		if (topLog == null)
		{
			topLog = new TopRequestLog();
			threadTopLog.set(topLog);
		}
		
		return topLog;
	}
	
	/**
	 * 通用，但是性能不好
	 * @param <E>
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <E extends TopLog> E getTopLog(Class<E> c)
	{
		E topLog = (E)threadTopLog.get();
		
		if (topLog == null)
		{
			try
			{
				topLog = (E)Class.forName(c.getName()).newInstance();
			}
			catch(Exception ex)
			{
				throw new java.lang.RuntimeException("create top log instance fail!",ex);
			}
			
			threadTopLog.set(topLog);
		}
		
		return topLog;
	}
	
	public static void removeTopLog()
	{
		threadTopLog.remove();
	}
}
