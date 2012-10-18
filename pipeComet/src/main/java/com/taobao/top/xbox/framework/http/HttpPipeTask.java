package com.taobao.top.xbox.framework.http;


import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.taobao.top.xbox.framework.IPipe;
import com.taobao.top.xbox.framework.IPipeTask;
import com.taobao.top.xbox.framework.PipeContextManager;
import com.taobao.top.xbox.framework.PipeMode;
import com.taobao.top.xbox.framework.event.AsynHttpTaskEvent;
import com.taobao.top.xbox.threadpool.Job;

/**
 * 管道事件中附加的需要执行的任务定义，支持多种类型的异步任务
 * @author fangweng
 *
 */
public class HttpPipeTask<I extends HttpPipeInput, R extends HttpPipeResult
			,D extends HttpPipeData<I,R>,E extends AsynHttpTaskEvent<I,R,D>> 
			implements IPipeTask,Job{

	private static final Log logger = LogFactory.getLog(HttpPipeTask.class);
			
	D data;
	IPipe<? super I, ? super R> pipe = null;
	AbstractHttpPipeManager<I,R,D,E> httpPipeManager = null;
	String key = null;
	
	public HttpPipeTask(D data,IPipe<? super I, ? super R> pipe
			,AbstractHttpPipeManager<I,R,D,E> httpPipeManager,String key)
	{
		this.data = data;
		this.pipe = pipe;
		this.httpPipeManager = httpPipeManager;
		this.key = key;
	}
	
	
	
	public D getData() {
		return data;
	}


	public void setData(D data) {
		this.data = data;
	}



	@SuppressWarnings("unchecked")
	@Override
	public void run()
	{
		try
		{
			//复制传递过来的被解析的参数到当前线程变量中
			if (data.getPipeContext().getAttachment(LazyParser.LAZY_CONTEXT) != null)
			{
				LazyParser.addParams2LazyContext(
						(Map<String,Object>)data.getPipeContext().getAttachment(LazyParser.LAZY_CONTEXT));
				
				data.getPipeContext().removeAttachment(LazyParser.LAZY_CONTEXT);
			}
			
			PipeContextManager.setContext(data.getPipeContext());
						
			if (pipe != null)
			{
				//如果是invoke在AsynTaskKeeper中被重置为未启动的循环任务
				if (pipe.getPipeMode().equals(PipeMode.CONDITION))
				{
					//condition被激发只执行当前管道，判断状态为0表示这个消息还要被持续保持，而非结束
					if (data.getPipeContext().getEvent(pipe.getPipeName()).getStatus() == 0)
					{
						pipe.doPipe(data.getPipeInput(), data.getPipeResult());
						return;
					}
				}
				else
					pipe.doPipe(data.getPipeInput(), data.getPipeResult());
			}
				
			httpPipeManager.doPipes(data,pipe);
		}
		catch(Exception ex)
		{
			logger.error("TopPipeTask error!",ex);
		}
		
	}

	@Override
	//为了防止使用异步request时被回收，强制要求在初始化的时候就设置key
	final public String getKey() {
		return key;
	}

}
