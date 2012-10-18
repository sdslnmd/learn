/**
 * 
 */
package com.taobao.top.xbox.framework.http;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationListener;

import com.taobao.top.xbox.framework.PipeContextManager;
import com.taobao.top.xbox.framework.event.AsynHttpTaskEvent;


/**
 * 用于附加在Jetty continuation中的事件监听器，
 * 可以支持在Continuation 处于timeout和complete时处理制定的逻辑
 * @author fangweng
 * @email fangweng@taobao.com
 * @date 2011-5-17
 *
 * @param <I>
 * @param <R>
 * @param <D>
 * @param <E>
 */
public class HttpAsynPipeEventListener<I extends HttpPipeInput,
		R extends HttpPipeResult,D extends HttpPipeData<I,R>
		,E extends AsynHttpTaskEvent<I,R,D>>  implements ContinuationListener{

	private static final Log logger = LogFactory.getLog(HttpAsynPipeEventListener.class);
	AbstractHttpPipeManager<I,R,D,E> httpPipeManager;
	E event;
	I httpPipeInput;
	
	
	public HttpAsynPipeEventListener(AbstractHttpPipeManager<I,R,D,E> httpPipeManager,E event,I httpPipeInput)
	{
		this.httpPipeManager = httpPipeManager;
		this.event = event;
		this.httpPipeInput = httpPipeInput;
	}
	
	@Override
	public void onComplete(Continuation continuation) {
		//设置当前请求已经结束，防止此时还获取request和response
		httpPipeInput.setRecycle(true);
		event = null;
	}

	@Override
	public void onTimeout(Continuation continuation) {
		// TODO Auto-generated method stub
		logger.error("Asyn Web Request time out.");
		
		//清理上下文和事件消息队列中的事件
		httpPipeManager.removeAsynTask(event);
		PipeContextManager.removeContext(true);
		event = null;
		
		try
		{
			continuation.complete();
		}
		catch(Exception ex)
		{
			logger.error(ex);
		}
		
	}

}
