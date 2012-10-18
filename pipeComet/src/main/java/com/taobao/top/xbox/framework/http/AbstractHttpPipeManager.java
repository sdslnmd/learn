/**
 * 
 */
package com.taobao.top.xbox.framework.http;


import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;
import org.eclipse.jetty.continuation.Servlet3Continuation;

import com.taobao.top.xbox.framework.AbstractPipeManager;
import com.taobao.top.xbox.framework.IPipe;
import com.taobao.top.xbox.framework.IPipeContext;
import com.taobao.top.xbox.framework.IPipeTask;
import com.taobao.top.xbox.framework.PipeContextManager;
import com.taobao.top.xbox.framework.PipeLog;
import com.taobao.top.xbox.framework.PipeMode;
import com.taobao.top.xbox.framework.event.AsynHttpTaskEvent;
import com.taobao.top.xbox.framework.event.EventKeeper;
import com.taobao.top.xbox.threadpool.JobDispatcher;

/**
 * 抽象Http容器管道管理类，支持jetty异步模式
 * @author fangweng
 * @email fangweng@taobao.com
 * @date 2011-3-28
 *
 */
public abstract class AbstractHttpPipeManager <I extends HttpPipeInput, 
	R extends HttpPipeResult,D extends HttpPipeData<I,R>,E extends AsynHttpTaskEvent<I,R,D>>
		extends AbstractPipeManager<I,R,D,E>
{
	private static final Log logger = LogFactory.getLog(AbstractHttpPipeManager.class);
	public final static String PIPE_TRANSACTION_BEG = "_transaction_beg_";
	
	protected JobDispatcher jobDispatcher;//支持权重的线程池
	protected int maxThreadCount = 500;//线程池最大线程数
	protected int coreThreadCount = 50;//线程池核心线程数
	protected int maxQueueLength = 2000;//线程池最大队列长度
	protected int supportAsynMode = -1;//-1表示还不确定，0表示支持异步模式，1表示不支持异步模式
	protected int maxResultQueueLength = 10000;//结果集队列最大长度
	protected int maxAsynTimeOut = 60 * 1 * 1000;//1分钟作为默认的异步超时时间
	
	protected int maxKeeperCount = 1;//最大的检查线程连接数
	
	protected AtomicInteger counter = new AtomicInteger(0);
	
	protected Random random = new Random();
	
	//事件驱动引擎
	protected EventKeeper<E>[] asynTaskKeepers;
	
	private boolean trackTimeStamp = false;//是否需要统计管道执行时间，有一定的消耗

	public AbstractHttpPipeManager()
	{
		jobDispatcher = new JobDispatcher();
	}
	
	/**
	 * 内部状态快照
	 * @return
	 */
	public String snapShot()
	{
		StringBuilder snapShot = new StringBuilder();
		
		if (asynTaskKeepers != null)
		{
			int i = 0;
			
			for(EventKeeper<E> keeper : asynTaskKeepers)
			{
				snapShot.append("AsynTaskKeeper").append(i++).append(": \r\n <br/>").append(keeper.snapShot());
			}
		}
		
		return snapShot.toString();
	}
	
	public void setSupportAsynMode(int supportAsynMode) {
		this.supportAsynMode = supportAsynMode;
	}



	public int getCoreThreadCount() {
		return coreThreadCount;
	}

	

	public void setCoreThreadCount(int coreThreadCount) {
		this.coreThreadCount = coreThreadCount;
	}



	public int getMaxThreadCount() {
		return maxThreadCount;
	}

	public void setMaxThreadCount(int maxThreadCount) {
		this.maxThreadCount = maxThreadCount;
	}

	public int getMaxQueueLength() {
		return maxQueueLength;
	}

	public void setMaxQueueLength(int maxQueueLength) {
		this.maxQueueLength = maxQueueLength;
	}

	public int getMaxResultQueueLength() {
		return maxResultQueueLength;
	}

	public void setMaxResultQueueLength(int maxResultQueueLength) {
		this.maxResultQueueLength = maxResultQueueLength;
	}

	public int getMaxAsynTimeOut() {
		return maxAsynTimeOut;
	}

	public void setMaxAsynTimeOut(int maxAsynTimeOut) {
		this.maxAsynTimeOut = maxAsynTimeOut;
	}

	public JobDispatcher getJobDispatcher() {
		return jobDispatcher;
	}

	public void setJobDispatcher(JobDispatcher jobDispatcher) {
		this.jobDispatcher = jobDispatcher;
	}

	/**
	 * 从虚拟机参数中获得参数配置
	 */
	public void getConfigFromJvmParams()
	{
		try
		{
			//get -DmaxThreadCount="xx" 从脚本获得内部最大线程池
			if (System.getProperty("maxThreadCount") != null)
			{
				maxThreadCount = Integer.valueOf(System.getProperty("maxThreadCount"));
				logger.warn("use maxThreadCount :" + maxThreadCount);
			}
			if (System.getProperty("coreThreadCount") != null)
			{
				coreThreadCount = Integer.valueOf(System.getProperty("coreThreadCount"));
				logger.warn("use coreThreadCount :" + coreThreadCount);
			}
			if (System.getProperty("maxQueueLength") != null)
			{
				maxQueueLength = Integer.valueOf(System.getProperty("maxQueueLength"));
				logger.warn("use maxQueueLength :" + maxQueueLength);
			}
			
			if (System.getProperty("maxResultQueueLength") != null)
			{
				maxResultQueueLength = Integer.valueOf(System.getProperty("maxResultQueueLength"));
				logger.warn("use maxResultQueueLength :" + maxResultQueueLength);
			}
			
			if (System.getProperty("maxAsynTimeOut") != null)
			{
				maxAsynTimeOut = Integer.valueOf(System.getProperty("maxAsynTimeOut"));
				logger.warn("use maxAsynTimeOut :" + maxAsynTimeOut);
			}
			
			if (System.getProperty("maxKeeperCount") != null)
			{
				maxKeeperCount = Integer.valueOf(System.getProperty("maxKeeperCount"));
				logger.warn("use maxKeeperCount :" + maxKeeperCount);
			}
			
			if (System.getProperty("trackTimeStamp") != null)
			{
				trackTimeStamp = Boolean.valueOf(System.getProperty("trackTimeStamp"));
				logger.warn("use trackTimeStamp :" + trackTimeStamp);
			}
			
		}
		catch(Exception ex)
		{
			logger.error(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void init()
	{
		getConfigFromJvmParams();
		
		jobDispatcher.setCorePoolSize(coreThreadCount);
		jobDispatcher.setMaximumPoolSize(maxThreadCount);
		jobDispatcher.setMaximumQueueSize(maxQueueLength);
		jobDispatcher.init();
		
		asynTaskKeepers = new EventKeeper[maxKeeperCount];
		
		for(int i =0 ; i < maxKeeperCount; i++)
		{
			asynTaskKeepers[i] = new EventKeeper<E>
										(jobDispatcher,true,counter,maxResultQueueLength);
			
			asynTaskKeepers[i].setDaemon(true);
			asynTaskKeepers[i].start();
		}
		
	}
	
	
	public void destory()
	{
		if (jobDispatcher != null)
		{
			jobDispatcher.stopDispatcher();
		}
		
		if (asynTaskKeepers != null && asynTaskKeepers.length > 0)
		{
			for(int i =0 ; i < maxKeeperCount; i++)
			{
				try
				{
					asynTaskKeepers[i].stopThread();
				}
				catch(Exception ex)
				{
					logger.error("manager destory error!",ex);
				}
			}
		}
	}
	
	/**
	 * 每个实现TopPipeManager的类必须实现这个方法，在执行doPipes方法之前准备数据
	 * 
	 */
	public void prepare() {
		
		IPipeContext context = PipeContextManager.getContext();
		
		if (context == null)
		{
			context = new HttpPipeContext();
			PipeContextManager.setContext(context);
		}
		
		if (context.getAttachment(IPipe.PIPE_LOG) == null) {
			PipeLog log = createPipeLog();
			context.setAttachment(IPipe.PIPE_LOG, log);
		}
		
	}
	
	
	
	/**
	 * 异步支持的检查
	 * @param request
	 */
	protected void asynSupportCheck(HttpServletRequest request)
	{
		//不用做并发保护
		if(supportAsynMode == -1)
		{
			Continuation continuation = null;
			
			try
			{
				continuation = ContinuationSupport.getContinuation(request);
			}
			catch(Exception ex)
			{
				supportAsynMode = 1;
			}
			
			if(continuation != null && !(continuation instanceof Servlet3Continuation))
				supportAsynMode = 0;
			else
				supportAsynMode = 1;
		}
	}
	
	protected void asynSupportSuspend(HttpServletRequest request,HttpServletResponse response
			,E event,long timeout,I pipeInput)
	{
		if (supportAsynMode == 0)
		{
			Continuation continuation = ContinuationSupport.getContinuation(request);
			
			continuation.addContinuationListener(new HttpAsynPipeEventListener<I,R,D,E>(this,event,pipeInput));
			
			if (!continuation.isSuspended())
			{
				continuation.suspend(response);
				continuation.setTimeout(timeout);
			}

		}
	}
	
	protected void asynSupportComplete(HttpServletRequest request)
	{
		try
		{
			if (supportAsynMode == 0)
			{
				Continuation continuation = ContinuationSupport.getContinuation(request);
				if (continuation.isSuspended())
					continuation.complete();
			}
		}
		catch(Exception ex)
		{
			logger.error("asynSupportComplete error!",ex);
		}
		
	}
	
	/**
	 * 异步重入的另一个管道执行接口方法，暂时先和普通的管道执行接口分开
	 * @param data
	 * @return
	 */
	@Override
	public R doPipes(D data,IPipe<? super I, ? super R> pipe)
	{
		R result = data.getPipeResult();
		PipeContextManager.setContext(data.getPipeContext());
		I pipeInput = data.getPipeInput();
		
		return doPipes(pipeInput,result,pipe);
	}
	
	/**
	 * 执行管道链，建立管道上下文，执行管道链调用，返回管道执行结果
	 * 执行顺序和注册的过程保持一致
	 * @return
	 */
	@Override
	public R doPipes(I pipeInput) 
	{
		asynSupportCheck(pipeInput.getRequest());
		R result = createPipeResult();
		prepare();
		
		return doPipes(pipeInput,result,null);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public R doPipes(I pipeInput,R result,IPipe<? super I, ? super R> currentPipe)
	{
		long begtime = System.currentTimeMillis();
		
		PipeLog pipeLog = null;
		IPipeContext context = null;
				
		try
		{
				
			//TODO 需要检查TopPipeInput和TopPipeResult是否已经准备好了。
			context = PipeContextManager.getContext();
			Iterator<IPipe<? super I, ? super R>> iters = pipeChain.iterator();
			int pipeCount = pipeChain.size();
			
			
			if (trackTimeStamp)
			{
				// 取到上下文中的日志对象
				pipeLog = (PipeLog) context.getAttachment(IPipe.PIPE_LOG);
			}
			
			//管道被异步执行后回来计算时间需要从上次上下文中获取
			if (context.getAttachment(PIPE_TRANSACTION_BEG) != null)
			{
				begtime = (Long)context.getAttachment(PIPE_TRANSACTION_BEG);
			}
			
			
			//Asyn,Condition,Parallel几种模式再次进入
			if (currentPipe != null)
			{	
				while(iters.hasNext())
				{
					IPipe<? super I, ? super R> pipe = iters.next();
					pipeCount -=  1;
					if (pipe.equals(currentPipe))
					{
						
						//记录管道执行时间
						if (trackTimeStamp && context.getAttachment(pipe.getPipeName()) != null)
						{
							pipeLog.track(System.currentTimeMillis() - (Long)context.getAttachment(pipe.getPipeName()));
						}
						
						//除了并行模式，其他都继续执行剩余管道
						if (pipe.getPipeMode().equals(PipeMode.PARALLEL))
							return result;
						else
							break;
					}
				}

			}
			else
			{
				//表示自己就是一个子任务执行，计数器累加，用于最后判断是否要结束请求返回
				pipeInput.getTaskCounter().incrementAndGet();
				
				if (trackTimeStamp)
					pipeLog.track(begtime);
			}
				
			//循环执行管道
			while(iters.hasNext())
			{
				IPipe<? super I, ? super R> pipe = iters.next();
				
				if (pipe == null)
				{
					logger.error("null pipe in pipe chain,please check your config");
					continue;
				}
				
				pipeCount -=  1;
				
				try
				{	
					//部分管道可以被略过		
					if (!pipe.ignoreIt(pipeInput, result))
					{
						long start = System.currentTimeMillis();
						
						//根据Pipe的类型判断是否异步执行（容器如果不支持异步，则supportAsynMode就会屏蔽掉任何异步管道）
						//Pipe可以根据当前请求的条件来判断是否要执行异步
						if (supportAsynMode == 1 || pipe.getPipeMode().equals(PipeMode.SYN) 
								|| (!pipe.getPipeMode().equals(PipeMode.SYN) && pipe.ignoreAsynMode(pipeInput, result)))
						{							
							pipe.doPipe(pipeInput, result);
							
							// 消耗时间打点
							if (trackTimeStamp)
								pipeLog.track(System.currentTimeMillis() - start);
						}
						else
						{
							if (logger.isInfoEnabled())
								logger.info("asyn pipe execute...");
							
							
							//切换出去的时候，管道时间戳
							context.setAttachment(pipe.getPipeName(), System.currentTimeMillis());
							
							
							//表示有一个子任务开始被并行执行
							pipeInput.getTaskCounter().incrementAndGet();
							
							context.setAttachment(PIPE_TRANSACTION_BEG, begtime);
							
							//默认是就是jetty的超时时间
							long timeout = maxAsynTimeOut;
							if (pipe.getPipeTimeOut() > 0)
								timeout = pipe.getPipeTimeOut();
							
							D tempData = getPipeDataInstance(pipeInput,result,context);
							
							if (logger.isWarnEnabled() && timeout == 0)
								logger.warn("asyn timeout set 0!");
								
							E event = generateAsynTaskEvent(tempData,pipe,true,timeout + System.currentTimeMillis());
							context.setEvent(pipe.getPipeName(), event);
							
							asynSupportSuspend(pipeInput.getRequest(), pipeInput.getResponse(),event,timeout,pipeInput);
							
							submitAsynTask(event);
							
							//如果是并行允许继续执行剩下的管道
							if (pipe.getPipeMode().equals(PipeMode.ASYN) 
									|| pipe.getPipeMode().equals(PipeMode.CONDITION))
								break;
						}
					}
					else
					{
						// 设置消耗时间为空
						if (trackTimeStamp)
							pipeLog.track((long) -1);
					}
				}
				catch(Exception ex)
				{
					logger.error("request url : " + pipeInput.getRequest().getQueryString());
					
					doPipeException(ex,pipe,result);
								
					result.setException(ex);
				
				}

				if (result != null && result.isBreakPipeChain())
				{	
					if (trackTimeStamp)
					{
						// 设置其他环消耗为空
						for (int i = 0; i < pipeCount; i++)
							pipeLog.track((long) -1);
					}
						
					
					break;
				}
					
			}//end loop do pipes
		}
		catch(Exception ex)
		{
			logger.error("doPipes error!",ex);
		}
		finally
		{
			//每个异步化出去后的任务回归后，都递减总计数器，当计数器为0时，结束请求
			if (pipeInput.getTaskCounter().decrementAndGet() == 0)
			{
				if (logger.isInfoEnabled())
					logger.info("release resource...");
				
				asynSupportComplete(pipeInput.getRequest());
				result.setExecuteTime(System.currentTimeMillis() - begtime);
				
				if (trackTimeStamp)
					pipeLog.setTransactionConsumeTime(System.currentTimeMillis()
						- begtime);
				
				afterDoPipes(context);
				
				PipeContextManager.removeContext(true);
			}
			else
				PipeContextManager.removeContext(false);

			LazyParser.release();		
			
		}		
			
		return result;
	}
	
	
	
	public void doPipeException(Exception ex,IPipe<? super I, ? super R> pipe,R result)
	{
		try
		{
			logger.error(new StringBuilder("pipe :")
				.append(pipe.getPipeName())
				.append(pipe.getClass().getSimpleName()).toString(),ex);
		}
		catch(Exception e)
		{
			logger.error(e);
		}
	}


	
	/**
	 * 提交异步事件
	 * @param data
	 */
	public void submitAsynTask(E event)
	{
		int c = 0;
		
		if (maxKeeperCount > 1)
		{
			c = random.nextInt() % maxKeeperCount;
			
			if ( c < 0 )
				c = -c;
		}
		
		asynTaskKeepers[c].addAsynTaskEvent(event);
	}
	
	/**
	 * 主动清除事件，将事件设置为超时
	 * @param data
	 */
	public void removeAsynTask(E event)
	{
		//设置失效
		event.timeout();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public E generateAsynTaskEvent(D data, IPipe<? super I, ? super R> pipe,
			boolean executeNow, long timeout) {
		// 不设置超时时间
		E event = createTaskEvent(data,timeout);
		
		//事件所附加的任务可以由Pipe的工厂类生成或者默认的创建
		IPipeTask[] tasks = null;
		
		if (pipe.getPipeTaskFactory() != null)
			tasks = pipe.getPipeTaskFactory().createPipeTasks(pipe,data,this);
		
		if (tasks == null)
			tasks = createTasks(data,pipe,this,null);
		
		event.setTasks(tasks);
		
		//执行任务
		if (executeNow)
		{
			if (pipe.getPipeMode().equals(PipeMode.CONDITION))
				event.invoke();
			else
				event.complete();
		}

		return event;
	}
	
	/**
	 * 创建多个任务接口，用于框架在生成事件时，内置需要执行的任务
	 * @param data
	 * @param pipe
	 * @param pipeManager
	 * @return
	 */
	public abstract IPipeTask[] createTasks(D data, IPipe<? super I, ? super R> pipe
			,AbstractHttpPipeManager<I,R,D,E> pipeManager,String key);
	
	/**
	 * 创建事件接口
	 * @param data
	 * @param timeout
	 * @return
	 */
	public abstract E createTaskEvent(D data,long timeout);
	
	/**
	 * 创建管道日志，可以支持对管道执行的日志记录扩展
	 * @return
	 */
	public abstract PipeLog createPipeLog();
	
	/**
	 * 在管道执行完后的回调接口
	 * @param context
	 */
	public abstract void afterDoPipes(IPipeContext context);

	/**
	 * 创建管道输入对象，基于request和response
	 * @param request
	 * @param response
	 * @return
	 */
	public abstract I createPipeInput(HttpServletRequest request,
			HttpServletResponse response);


	/**
	 * 创建管道结果对象
	 * @return
	 */
	public abstract R createPipeResult();
	
	/**
	 * 创建现场类
	 * @param pipeInput
	 * @param result
	 * @param context
	 * @return
	 */
	public abstract D getPipeDataInstance(I pipeInput, R result,IPipeContext context);
	
}
