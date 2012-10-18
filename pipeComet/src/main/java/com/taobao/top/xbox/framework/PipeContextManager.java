package com.taobao.top.xbox.framework;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.taobao.top.xbox.framework.IPipeContext;

/**
 * 上下文管理类
 * @author fangweng
 *
 */
public class PipeContextManager
{
	private static final Log logger = LogFactory.getLog(PipeContextManager.class);
	
	/**
	 * 用于记录管道处理的上下文，线程独享
	 */
	static ThreadLocal<IPipeContext> _context =
		new ThreadLocal<IPipeContext>();
	
	/**
	 * 设置上下文
	 * @param context
	 */
	public static void setContext(IPipeContext context)
	{
		if (context == null)
			return;
		
		_context.set(context);
	}
	
	/**
	 * 获取上下文
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static IPipeContext getContext()
	{
		return _context.get();
	}
	
	/**
	 * 移除上下文
	 */
	public static void removeContext(boolean isDeepClear)
	{
		IPipeContext c = _context.get();
		
		try
		{
			if (c != null && isDeepClear)
			{
				c.removeAttachments();
			}
		}
		catch(Exception ex)
		{
			logger.error(ex,ex);
		}
		finally
		{
			_context.set(null);
			_context.remove();
		}
	}
}
