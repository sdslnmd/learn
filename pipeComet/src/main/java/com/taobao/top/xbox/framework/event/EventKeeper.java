/**
 * 
 */
package com.taobao.top.xbox.framework.event;


import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.taobao.top.xbox.framework.IPipeTask;
import com.taobao.top.xbox.threadpool.JobDispatcher;


/**
 * 消息事件驱动模型
 * @author fangweng
 *
 */
public class EventKeeper<E extends AsynTaskEvent> extends Thread
{
	private static final Log logger = LogFactory.getLog(EventKeeper.class);
	
	private boolean _isMultiKeeper = false;//是否是多个消息事件驱动模型一起处理消息，用于负载分担
	private AtomicInteger _counter;//用于多个AsynTaskKeeper合作
	private int _maxTaskQueueLength;//用于多个AsynTaskKeeper合作
	ThreadPoolExecutor threadPool;//或者普通的线程池
	JobDispatcher jobDispatcher;//支持权重的线程池
	boolean flag = true;
	private LinkedBlockingQueue<E> executeEventQueue;//立即执行的消息队列
	private PriorityBlockingQueue<E> taskEventPool;//等待状态变更的消息池
	private TimeOutChecker timeOutChecker;//后台检查消息变更资源池的线程
	private Semaphore poolIsEmpty = new Semaphore(1);//防止空循环检查消息变更资源池
	private boolean useJobDispatcher = false;//是否使用权重线程池
	
	//这三个用于超时事件队列检查
	private ReentrantLock lock;
	private Condition checkCondition;
	private AtomicLong minTimeOutStamp;
	
	
	public EventKeeper(JobDispatcher jobDispatcher,boolean isMultiKeeper
			,AtomicInteger counter,int maxTaskQueueLength)
	{
		super("AsynTaskKeeper_Thread");
		
		if (jobDispatcher != null)
			this.jobDispatcher = jobDispatcher;
		else
			throw new java.lang.RuntimeException(" AsynTaskKeeper threadpool can't be null.");
		
		useJobDispatcher = true;
		
		init(isMultiKeeper,counter,maxTaskQueueLength);
	}
	
	public EventKeeper(ThreadPoolExecutor threadPool,boolean isMultiKeeper
			,AtomicInteger counter,int maxTaskQueueLength)
	{
		super("AsynTaskKeeper_Thread");
		
		if (threadPool != null)
			this.threadPool = threadPool;
		else
			throw new java.lang.RuntimeException(" AsynTaskKeeper threadpool can't be null.");
		
		useJobDispatcher = false;
		
		init(isMultiKeeper,counter,maxTaskQueueLength);
		
	}
	
	/**
	 * 返回消息事件驱动模型内部状态和线程池内部状态的方法
	 * @return
	 */
	public String snapShot()
	{
		StringBuilder snapshot = new StringBuilder();
		
		snapshot.append(_isMultiKeeper ? " isMultiKeeper":" isSingleKeeper").append(" ,\r\n <br/>");
		snapshot.append(" Current Counter : ").append(_counter.get()).append(" ,\r\n <br/>");
		snapshot.append(" Current execute event count :").append(executeEventQueue.size()).append(" ,\r\n <br/>");
		snapshot.append(" Current wait event count :").append(taskEventPool.size()).append(" ,\r\n <br/>");
		snapshot.append(" ----------- Detail event ----------- ,\r\n <br/>");
		
		Iterator<E> iter = taskEventPool.iterator();
		
		while(iter.hasNext())
		{
			snapshot.append("event : ").append(iter.next().toString()).append(" \r\n <br/>");
		}
		
		snapshot.append(" ------------   ----------------- ,\r\n <br/>");
		
		//权重线程池内部的统计信息
		if (useJobDispatcher)
		{
			Map<String, Object> current = jobDispatcher.getCurrentThreadStatus();
			
			snapshot.append(" Use Weight threadPool.").append(" ,\r\n <br/>");
			snapshot.append(" Current active thread count: ").append(jobDispatcher.getThreadPool().getActiveCount()).append(" ,\r\n <br/>");
			snapshot.append(" Current total thread count: ").append(jobDispatcher.getThreadPool().getPoolSize()).append(" ,\r\n <br/>");
			snapshot.append(" Used maximum thread : ").append(jobDispatcher.getThreadPool().getLargestPoolSize()).append(" ,\r\n <br/>");
			snapshot.append(" Current total task count: ").append(
					jobDispatcher.getThreadPool().getTaskCount() - jobDispatcher.getThreadPool().getCompletedTaskCount()).append(" ,\r\n <br/>");
			
			snapshot.append(" THREAD_WEIGHT_MODEL : ").append(current.get(JobDispatcher.THREAD_WEIGHT_MODEL)).append(" ,\r\n <br/>");
			snapshot.append(" MODEL_SNAPSHOT : ").append(current.get(JobDispatcher.MODEL_SNAPSHOT)).append(" ,\r\n <br/>");
			
		}
		else
		{
			snapshot.append(" Use Common threadPool.").append(" ,\r\n <br/>");
			snapshot.append(" Current active thread count: ").append(threadPool.getActiveCount()).append(" ,\r\n <br/>");
			snapshot.append(" Current total thread count: ").append(threadPool.getPoolSize()).append(" ,\r\n <br/>");
			snapshot.append(" Used maximum thread : ").append(threadPool.getLargestPoolSize()).append(" ,\r\n <br/>");
			snapshot.append(" Current total task count: ").append(threadPool.getTaskCount()).append(" ,\r\n <br/>");
		}
		
		return snapshot.toString();
	}
	
	/**
	 * 初始化
	 * @param isMultiKeeper
	 * @param counter
	 * @param maxTaskQueueLength
	 */
	public void init(boolean isMultiKeeper
			,AtomicInteger counter,int maxTaskQueueLength)
	{
		_isMultiKeeper = isMultiKeeper;
		
		if (_isMultiKeeper)
			_counter = counter;
		else
			_counter = new AtomicInteger(0);

		_maxTaskQueueLength = maxTaskQueueLength;
		
		lock = new ReentrantLock();
		checkCondition = lock.newCondition();
		minTimeOutStamp = new AtomicLong(0);
		
		executeEventQueue = new LinkedBlockingQueue<E>();
		taskEventPool = new PriorityBlockingQueue<E>(100);
		timeOutChecker = new TimeOutChecker();
		timeOutChecker.setDaemon(true);
		timeOutChecker.start();
	}
	
	public int getCurrentTaskCount()
	{
		return taskEventPool.size();
	}
	
	/**
	 * 当有新的事件加入状态变更等待队列，
	 * 判断最小的timeout是否发生改变，选择性唤醒checker
	 * @param timeout
	 */
	private void timeOutTrigger(long timeout)
	{
		if (timeout > 0 && (minTimeOutStamp.get() == 0 || timeout < minTimeOutStamp.get()))
		{
			//不做并发控制
			minTimeOutStamp.set(timeout);
			
			boolean flag = lock.tryLock();
		
			try
			{
				if (flag)
				{
					checkCondition.signalAll();
				}
			}
			catch(Exception ex)
			{
				logger.error(ex);
			}
			finally
			{
				if (flag)
					lock.unlock();
			}
			
		}
	}
	
	/**
	 * 存储异步请求的中间结果，构建任务
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	public void addAsynTaskEvent(E event)
	{
		
		//超过可以支持的中间结果的数量，抛出异常，只是log
		if (_counter.getAndIncrement() > _maxTaskQueueLength)
		{
			_counter.decrementAndGet();
			logger.error("PipeTaskQueue full ...");
			throw new java.lang.RuntimeException("PipeTaskQueue full ...");
		}
		else
		{
			//如果需要立即执行，则就不放入状态检查的资源池，直接投入执行
			if (event.isComplete())
			{
				_counter.decrementAndGet();
				executeEventQueue.offer(event);
			}
			else
			{
				//如果是Condition的invoke模式，则将原始消息清除状态
				//，放入消息变更等待池，另一方面产生一个立即执行的事件
				if (event.isInvoke())
				{
					executeEventQueue.offer((E)event.cloneEvent(true));
					event.clearStatus();
					
					event.setEventListeners(new DefaultTaskEventListener[]{new DefaultTaskEventListener<E>(this)});
					taskEventPool.put(event);
					
					timeOutTrigger(event.timeout);
					
				}
				else
				{
					event.setEventListeners(new DefaultTaskEventListener[]{new DefaultTaskEventListener<E>(this)});
					taskEventPool.put(event);
					
					timeOutTrigger(event.timeout);
				}
				
				if (poolIsEmpty.availablePermits() == 0)
					poolIsEmpty.release();
			}
				
		}
	}


	/* 
	 * 循环执行从队列中获取立即需要执行的任务，
	 * 并放入线程池直接执行
	 */
	@Override
	public void run()
	{
		try
		{
			while(flag)
			{
				E event = getAsynTaskEvent();
				
				if (event != null)
				{
					//如果超时，则执行该event的timeout的内容
					if (event.isTimeOut())
					{
						if (!useJobDispatcher)
							threadPool.execute(event);
						else
							jobDispatcher.execute(event);
					}
					else
						//如果是invoke或者complete，并且有task需要执行，则调用线程池执行任务
						if ((event.isComplete() || event.isInvoke()) && event.getTasks() != null
								&& event.getTasks().length > 0)
						{
							for(IPipeTask task : event.getTasks())
							{
								if (task == null)
									continue;
								
								if (!useJobDispatcher)
									threadPool.execute(task);
								else
									jobDispatcher.execute(task);
							}
								
						}	
				}
			}
		}
		catch(Exception ex)
		{
			logger.error("AsynTaskKeeper end...",ex);
		}
		finally
		{
			clean();
			if (timeOutChecker != null)
				timeOutChecker.stopThread();
		}
	}
	
	
	public void stopThread()
	{
		flag = false;
		interrupt();
	}
	
	public void clean()
	{

		if (executeEventQueue != null)
			executeEventQueue.clear();
		
		if (taskEventPool != null)
			taskEventPool.clear();
		

		minTimeOutStamp.set(0);
	}
	
	protected E getAsynTaskEvent()
	{
		E event = null;
		
		try 
		{
			event = executeEventQueue.poll(3, TimeUnit.SECONDS);
		}
		catch (InterruptedException e) 
		{
			//do nothing
		}
		catch(Exception ex)
		{
			logger.error("getAsynTaskEvent error.",ex);
		}
		
		return event;
	}
	
	@SuppressWarnings("unchecked")
	public void eventChanged(E e)
	{
		if (e == null)
			return;
		
		synchronized(e)
		{	
			
			//在有效期内的invoke，则clone一份作为立即执行，并将原始消息状态清除
			if (!e.isTimeOut() && e.isInvoke())
			{
				executeEventQueue.offer((E)e.cloneEvent(true));
				e.clearStatus();
			}
			else
			{
				//再次做一下保护检查，保护多线程例外状态一致
				if (e.getStatus() > 0)
				{
					//做好并发保护
					if (taskEventPool.remove(e))
					{
						executeEventQueue.offer(e);
						_counter.decrementAndGet();
					}
				}
			}
		}
	}
	
	/**
	 * 用于后台检查状态变更消息池,当前只负责Timeout状态检查
	 * @author fangweng
	 * @email fangweng@taobao.com
	 * @date 2011-5-17
	 *
	 */
	class TimeOutChecker extends Thread
	{
		
		public TimeOutChecker()
		{
			super("TimeOutChecker-thread");
		}
		
		boolean isRunning = true;
		
		public void run()
		{
			try
			{
				while(isRunning)
				{
					//checker没有竞争，所以这里还是可靠的，用于防止内部没有任何数据的空转
					poolIsEmpty.acquire();
					
					if (taskEventPool.isEmpty())
						continue;
					
					E node;
					long restTime = 0;
					
					while((node = taskEventPool.peek()) != null)
					{
						if (node.isTimeOut())
						{
							executeEventQueue.offer(node);
							taskEventPool.poll();
							_counter.decrementAndGet();
						}
						else
						{
							if (node.timeout == 0)
								restTime = 5 * 60 * 1000;//如果剩下的都没有超时事件了，则给5分钟
							else
								restTime = node.timeout - System.currentTimeMillis();
							
							break;
						}
					}
					
					//cpu time interval ,预估一下一个最小超时到来的情况，防止多次循环
					if (restTime > 10)
					{
						
						if (restTime > 5 * 60 * 1000)
						{
							if (logger.isInfoEnabled())
								logger.info("restTime : " + restTime + " so large.");
							
							restTime = 5 * 60 * 1000;
						}
						
						boolean flag = lock.tryLock();
						
						try
						{
							if (flag)
							{
								checkCondition.await(restTime, TimeUnit.MILLISECONDS);
							}
						}
						catch(Exception ex)
						{
							logger.error(ex);
						}
						finally
						{
							if (flag)
								lock.unlock();
						}
					}
					
					poolIsEmpty.release();
				}
			}
			catch (InterruptedException e) 
			{
				//do nothing
			}
			catch(Exception ex)
			{
				logger.error("TaskChecker end...",ex);
			}
		}
		
		public void stopThread()
		{
			isRunning = false;
			interrupt();
		}
	}

	public JobDispatcher getJobDispatcher() {
		return jobDispatcher;
	}

	public void setJobDispatcher(JobDispatcher jobDispatcher) {
		this.jobDispatcher = jobDispatcher;
	}

}
