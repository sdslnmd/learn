/**
 * 
 */
package com.taobao.top.xbox.framework.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.taobao.top.xbox.framework.IPipe;
import com.taobao.top.xbox.framework.IPipeContext;
import com.taobao.top.xbox.framework.IPipeTask;
import com.taobao.top.xbox.framework.PipeLog;
import com.taobao.top.xbox.framework.event.AsynHttpTaskEvent;

/**
 * Http版本的PipeManager的实现
 * @author fangweng
 * @email fangweng@taobao.com
 * @date 2011-3-29
 * 
 */
public class DefaultHttpPipeManager
		extends
		AbstractHttpPipeManager<HttpPipeInput, HttpPipeResult, HttpPipeData<HttpPipeInput, HttpPipeResult>, AsynHttpTaskEvent<HttpPipeInput, HttpPipeResult, HttpPipeData<HttpPipeInput, HttpPipeResult>>> {

	private static final Log logger = LogFactory.getLog(DefaultHttpPipeManager.class);
	
	@Override
	public HttpPipeInput createPipeInput(HttpServletRequest request,
			HttpServletResponse response) {
		return new HttpPipeInput(request, response);
	}

	@Override
	public HttpPipeResult createPipeResult() {
		return new HttpPipeResult();
	}

	@Override
	public HttpPipeData<HttpPipeInput, HttpPipeResult> getPipeDataInstance(
			HttpPipeInput pipeInput, HttpPipeResult result, IPipeContext context) {
		return new HttpPipeData<HttpPipeInput, HttpPipeResult>(context,
				pipeInput, result);
	}
	
	
	@Override
	public void afterDoPipes(IPipeContext context) {
		
		if (logger.isInfoEnabled() && context.getAttachment(IPipe.PIPE_LOG) != null)
			logger.info(((PipeLog) context.getAttachment(IPipe.PIPE_LOG)).toString());
	}

	@Override
	public PipeLog createPipeLog() {
		return new PipeLog();
	}


	@Override
	public AsynHttpTaskEvent<HttpPipeInput, HttpPipeResult, HttpPipeData<HttpPipeInput, HttpPipeResult>> createTaskEvent(
			HttpPipeData<HttpPipeInput, HttpPipeResult> data, long timeout) {
		
		AsynHttpTaskEvent<HttpPipeInput, HttpPipeResult, HttpPipeData<HttpPipeInput, HttpPipeResult>> 
			event = new AsynHttpTaskEvent<HttpPipeInput, HttpPipeResult, 
				HttpPipeData<HttpPipeInput, HttpPipeResult>>(data, timeout);
	
		return event;
	}

	@Override
	public IPipeTask[] createTasks(
			HttpPipeData<HttpPipeInput, HttpPipeResult> data,
			IPipe<? super HttpPipeInput, ? super HttpPipeResult> pipe,
			AbstractHttpPipeManager<HttpPipeInput, HttpPipeResult, HttpPipeData<HttpPipeInput, HttpPipeResult>, 
				AsynHttpTaskEvent<HttpPipeInput, HttpPipeResult, HttpPipeData<HttpPipeInput, HttpPipeResult>>> pipeManager,String key) {
		return new HttpPipeTask[]{new HttpPipeTask<HttpPipeInput, HttpPipeResult, HttpPipeData<HttpPipeInput, HttpPipeResult>, 
				AsynHttpTaskEvent<HttpPipeInput, HttpPipeResult, HttpPipeData<HttpPipeInput, HttpPipeResult>>>(data, pipe, pipeManager,key)};
		
	}



}
