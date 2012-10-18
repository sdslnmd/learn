/**
 * 
 */
package com.taobao.top.xbox.framework.http;


import com.taobao.top.xbox.framework.IPipe;
import com.taobao.top.xbox.framework.IPipeContext;
import com.taobao.top.xbox.framework.PipeContextManager;
import com.taobao.top.xbox.framework.PipeMode;


/**
 * 抽象Http管道基类
 * @author fangweng
 *
 */
public abstract class AbstractHttpPipe<I extends HttpPipeInput,R extends HttpPipeResult>
		implements IPipe<I,R> {

	private String pipeName;
	private PipeMode pipeMode = PipeMode.SYN;
	private long pipeTimeOut = 0;
	private IHttpPipeTaskFactory pipeTaskFactory = null;
	

	public IHttpPipeTaskFactory getPipeTaskFactory() {
		return pipeTaskFactory;
	}


	public void setPipeTaskFactory(
			IHttpPipeTaskFactory pipeTaskFactory) {
		this.pipeTaskFactory = pipeTaskFactory;
	}


	public long getPipeTimeOut() {
		return pipeTimeOut;
	}


	public void setPipeTimeOut(long pipeTimeOut) {
		this.pipeTimeOut = pipeTimeOut;
	}


	public AbstractHttpPipe()
	{
		this.setPipeName(this.getClass().getSimpleName());
	}
	

	@Override
	public PipeMode getPipeMode() {
		return pipeMode;
	}


	@Override
	public void setPipeMode(PipeMode pipeMode) {
		this.pipeMode = pipeMode;
	}



	@Override
	public IPipeContext getContext() {
		return PipeContextManager.getContext();
	}
	
	@Override
	public String getPipeName() {
		return pipeName;
	}
	
	@Override
	public void setPipeName(String pipeName) {
		this.pipeName = pipeName;
	}
	
	@Override
	public boolean ignoreIt(I pipeInput,
			R pipeResult) {
		return false;
	}
	
	@Override
	public boolean ignoreAsynMode(I pipeInput,
			R pipeResult) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
