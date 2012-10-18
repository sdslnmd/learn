/**
 * 
 */
package com.taobao.top.xbox.framework.http;

import com.taobao.top.xbox.framework.IPipeContext;
import com.taobao.top.xbox.framework.IPipeData;


/**
 * 请求异步切换时现场数据结构
 * @author fangweng
 * @email fangweng@taobao.com
 * @date 2011-3-28
 *
 * @param <I>
 * @param <R>
 */
public class HttpPipeData<I extends HttpPipeInput, R extends HttpPipeResult> 
	implements IPipeData<I,R> {

	private IPipeContext pipeContext;
	private I pipeInput;
	private R pipeResult;
	
	
	
	public HttpPipeData(IPipeContext context,
			I pipeInput,R pipeResult)
	{
		pipeContext = context;
		this.pipeInput = pipeInput;
		this.pipeResult = pipeResult;
		
		//copy lazy context
		pipeContext.setAttachment(LazyParser.LAZY_CONTEXT, LazyParser.getLazyContext());
	}

	@Override
	public IPipeContext getPipeContext() {
		// TODO Auto-generated method stub
		return pipeContext;
	}


	@Override
	public I getPipeInput() {
		// TODO Auto-generated method stub
		return pipeInput;
	}


	@Override
	public R getPipeResult() {
		// TODO Auto-generated method stub
		return pipeResult;
	}


	@Override
	public void setPipeContext(IPipeContext pipeContext) {
		this.pipeContext = pipeContext;
	}


	@Override
	public void setPipeInput(I pipeInput) {
		this.pipeInput = pipeInput;
	}

	@Override
	public void setPipeResult(R pipeResult) {
		this.pipeResult = pipeResult;
	}

}
