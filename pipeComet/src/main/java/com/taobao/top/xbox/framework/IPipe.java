/**
 * 
 */
package com.taobao.top.xbox.framework;


import com.taobao.top.xbox.framework.http.IHttpPipeTaskFactory;


/**
 * 管道接口
 * @author fangweng
 *
 */
public interface IPipe<I extends IPipeInput,R extends IPipeResult>
{
	public final static String PIPE_LOG = "_pipe_log_";
	
	/**
	 * 管道任务工厂类，用于在Pipe执行时主动创建需要执行的tasks附加于Event中
	 * @return
	 */
	public IHttpPipeTaskFactory getPipeTaskFactory();	
	
	/**
	 * @param pipeTaskFactory
	 */
	public void setPipeTaskFactory(IHttpPipeTaskFactory pipeTaskFactory);
	
	/**
	 * 获取pipe的名称
	 * @return
	 */
	public String getPipeName();
	
	/**
	 * 设置pipe的名称
	 * @param pipeName
	 */
	public void setPipeName(String pipeName);
	
	/**
	 * 执行管道具体逻辑
	 * @param pipeInput
	 * @param pipeResult
	 */
	public void doPipe(I pipeInput,
			R pipeResult);
	
	
	/**
	 * 获取当前处理上下文
	 * @return
	 */
	public IPipeContext getContext();
	
	/**
	 * 根据传入参数和上下文
	 * 判断是否忽略此管道的处理
	 * @param pipeInput
	 * @return
	 */
	public boolean ignoreIt(I pipeInput,
			R pipeResult);
	
	
	/**
	 * 获得管道属性
	 * @return
	 */
	public PipeMode getPipeMode();
	
	/**
	 * 在特定情况下可以不按照异步模式执行，避免一刀切带来的性能损耗
	 * @param pipeInput
	 * @param pipeResult
	 * @return
	 */
	public boolean ignoreAsynMode(I pipeInput,
			R pipeResult);
	
	
	/**
	 * 设置管道的模式
	 * @param mode
	 */
	public void setPipeMode(PipeMode mode);
	
	/**
	 * @return
	 */
	public long getPipeTimeOut();
	
	/**
	 * 如果是异步或者并行的Pipe，如果不设置超时时间，
	 * 则保持和jetty timeout时间一致，设置了以pipe为准
	 * @param timeout
	 */
	public void setPipeTimeOut(long timeout);
	
	
}
