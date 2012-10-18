package com.taobao.top.xbox.threadpool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.taobao.top.xbox.util.NamedThreadFactory;

/**
 * 简单的任务分发类，支持根据业务资源分配模型来分配线程资源，当前支持两类
 *  1. 根据某一类key可以预留多少资源独享。
 *  2. 根据某一类key可以限制最多使用多少资源。
 * 
 * @author fangweng
 */
public class JobDispatcher{

	private static final Log log = LogFactory.getLog(JobDispatcher.class);

	public static final String DEFAULT_COUNTER = "defaultCounter";
	public static final String TOTAL_COUNTER = "totalCounter";
	public static final String DEFAULT_QUEUE_COUNTER = "defaultQueueCounter";
	public static final String THREAD_POOL_CORE_SISE = "coreSize"; //核心线程数
	public static final String THREAD_POOL_MAX_SIZE = "maxSize"; //允许的最大线程数
	public static final String THREAD_POOL_ACTIVE_COUNT = "activeCount"; //主动执行任务的近似线程数
	public static final String THREAD_POOL_COMPLETED_COUNT = "completedCount";//已完成执行的近似任务总数
	public static final String THREAD_POOL_LARGEST_SIZE = "largestSize";//曾经同时位于池中的最大线程数
	public static final String THREAD_POOL_CURRENT_SIZE = "currentSize"; //池中的当前线程数
	public static final String THREAD_POOL_QUEUE_SIZE = "queueSize"; //此执行程序使用的任务队列数量
	public static final String THREAD_WEIGHT_MODEL = "model";//资源分配线程池模型
	public static final String MODEL_SNAPSHOT = "modelSnapShot";//资源分配线程池当天的一个快照
	public static final String DEFAULT_JOBQUEUE = "_default_job_queue_";//默认没有key的job所在的Queue。
	

	private JobThreadPoolExecutor threadPool;//内部线程池
	private JobThreadWeightModel[] jobThreadWeightModel;//内置资源分配线程池模型，可运行期动态构建
	private Map<String, AtomicInteger> counterPool;//记录每一个设置了资源分配模型的资源所占用的私有线程数（如果是limit就和defaultCounterPool保持一致）
	private Map<String,JobQueue> jobQueuePool;//不同的资源分配模型配置key都有自己的队列结构，默认的模型采用default。
	private Map<String, AtomicInteger> defaultCounterPool;//用于记录每一个设置了资源分配模型的资源所占用的真实线程数。（包括私有和共有的）
	
	private JobThreshold jobThreshold;//任务阀值
	
	private AtomicInteger defaultCounter;//默认线程消耗数量，totalcounter-各个私有线程消耗
	private AtomicInteger totalCounter;//所有线程消耗数量
	private int maximumPoolSize = 200;
	private int corePoolSize = 50;
	private int maximumQueueSize = 1000;
	private int maxQueueDispatcherSize = 2;
	
	/**
	 * 保留型的资源配置，优先使用保留的资源
	 */
	private boolean privateUseFirst = true;
	
	
	public Map<String, JobQueue> getJobQueuePool() {
		return jobQueuePool;
	}


	public int getMaxQueueDispatcherSize() {
		return maxQueueDispatcherSize;
	}

	public void setMaxQueueDispatcherSize(int maxQueueDispatcherSize) {
		this.maxQueueDispatcherSize = maxQueueDispatcherSize;
	}

	public JobThreshold getJobThreshold() {
		return jobThreshold;
	}

	public void setJobThreshold(JobThreshold jobThreshold) {
		this.jobThreshold = jobThreshold;
	}

	public boolean isPrivateUseFirst() {
		return privateUseFirst;
	}

	public void setPrivateUseFirst(boolean privateUseFirst) {
		this.privateUseFirst = privateUseFirst;
	}
	

	public int getDefaultQueueCounter() {
		return jobQueuePool.get(DEFAULT_JOBQUEUE).size();
	}

	
	/**
	 * 获取资源分配线程池内部运行状态指标
	 * @return
	 */
	public Map<String, Object> getCurrentThreadStatus() 
	{
		Map<String, Object> status = new HashMap<String, Object>();
		
		status.put(THREAD_POOL_CORE_SISE, threadPool.getCorePoolSize());
		status.put(THREAD_POOL_MAX_SIZE, threadPool.getMaximumPoolSize());
		status.put(THREAD_POOL_ACTIVE_COUNT, threadPool.getActiveCount());
		status.put(THREAD_POOL_COMPLETED_COUNT, threadPool.getCompletedTaskCount());
		status.put(THREAD_POOL_LARGEST_SIZE, threadPool.getLargestPoolSize());
		status.put(THREAD_POOL_CURRENT_SIZE, threadPool.getPoolSize());
		status.put(THREAD_POOL_QUEUE_SIZE, threadPool.getQueue().size());
		
		status.put(DEFAULT_COUNTER, defaultCounter.get());
		status.put(TOTAL_COUNTER, totalCounter.get());
		status.put(DEFAULT_QUEUE_COUNTER, jobQueuePool.get(DEFAULT_JOBQUEUE).size());
		
		StringBuilder threadModel = new StringBuilder(); 
		if(jobThreadWeightModel != null){
			for (JobThreadWeightModel model : jobThreadWeightModel) {
				threadModel.append(model.getType()).append(":").append(model.getKey()).append(":").append(model.getValue()).append(",");
			}
		}
		if(threadModel.length() > 0){
			status.put(THREAD_WEIGHT_MODEL, threadModel.substring(0, threadModel.length() - 1));
		}
		
		StringBuilder detailModelStatus = new StringBuilder();
		Iterator<Entry<String, AtomicInteger>> entrys = counterPool.entrySet().iterator();
		while (entrys.hasNext()) {
			Entry<String, AtomicInteger> e = entrys.next();
			
			detailModelStatus.append(e.getKey()).append("=").append( 
					new StringBuilder("private:").append(e.getValue())
					.append(",total:").append(defaultCounterPool.get(e.getKey()).get())
						.append(",queue:").append(jobQueuePool.get(e.getKey()).size()).toString()).append(";");
			
		}
		if(detailModelStatus.length() > 0){
			status.put(MODEL_SNAPSHOT, detailModelStatus.substring(0, detailModelStatus.length() - 1));
		}
		return status;
	}
	

	public JobThreadPoolExecutor getThreadPool() {
		return threadPool;
	}

	public void setThreadPool(JobThreadPoolExecutor threadPool) {
		this.threadPool = threadPool;
	}


	public void init() {
		if (threadPool == null)
			threadPool = new JobThreadPoolExecutor(corePoolSize,maximumPoolSize, 0L, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(maximumQueueSize),
				new NamedThreadFactory("jobDispatcher_worker"), this);

		defaultCounter = new AtomicInteger(0);
		totalCounter = new AtomicInteger(0);
		counterPool = new ConcurrentHashMap<String, AtomicInteger>();
		defaultCounterPool = new ConcurrentHashMap<String, AtomicInteger>();
		jobQueuePool = new ConcurrentHashMap<String,JobQueue>();
		
		//默认无key的任务队列
		JobQueue q = new JobQueue(DEFAULT_JOBQUEUE,maximumQueueSize,this);
		jobQueuePool.put(DEFAULT_JOBQUEUE, q);
		
		jobThreshold = buildWeightModel(jobThreadWeightModel);

	}

	public void stopDispatcher() {
		if (threadPool != null)
			threadPool.shutdownNow();

		if (counterPool != null)
			counterPool.clear();

		if (jobThreshold.getThresholdPool() != null)
			jobThreshold.getThresholdPool().clear();
		
        if (jobQueuePool != null && jobQueuePool.size() > 0)
        {
        	Iterator<JobQueue> values = jobQueuePool.values().iterator();
        	
        	while(values.hasNext())
        	{
        		JobQueue q = values.next();
        		q.clean();
        	}
        	
        	jobQueuePool.clear();
        	
        }
		
	}
	
	/**
	 * 运行期修改模型
	 * @param newJobThreadWeightModel
	 */
	public JobThreshold buildWeightModel(JobThreadWeightModel[] newJobThreadWeightModel)
	{
		this.jobThreadWeightModel = newJobThreadWeightModel;
		JobThreshold newJobThreshold = new JobThreshold();
		newJobThreshold.setDefaultThreshold(maximumPoolSize);
		
		if (newJobThreadWeightModel != null && newJobThreadWeightModel.length > 0) 
		{
			for (JobThreadWeightModel j : newJobThreadWeightModel) 
			{
				try 
				{
					//构建设置资源分配模型所需要的附属对象
					if (counterPool.get(j.getKey()) == null)
					{
						counterPool.put(j.getKey(), new AtomicInteger(0));
						
						JobQueue q = new JobQueue(j.getKey(),maximumQueueSize,this);
						jobQueuePool.put(j.getKey(), q);
						
						defaultCounterPool.put(j.getKey(), new AtomicInteger(0));
					}
					
					if (j.getValue() == 0) 
						continue;

					if (j.getType().equals(JobThreadWeightModel.WEIGHT_MODEL_LIMIT)) 
					{
						newJobThreshold.getThresholdPool().put(j.getKey(), j.getValue());
					} 
					else 
						if (j.getType().equals(JobThreadWeightModel.WEIGHT_MODEL_LEAVE)) 
						{
							newJobThreshold.getThresholdPool().put(j.getKey(), j.getValue());
							newJobThreshold.setDefaultThreshold(newJobThreshold.getDefaultThreshold() - j.getValue());
						} 
						else
						{
							log.error(new StringBuilder(
								"thread weight config type:")
								.append(j.getType()).append(" key:")
								.append(j.getKey()).append(" value:")
								.append(j.getValue()).append(" not support!"));
						}
				} 
				catch (Exception ex) 
				{
					log.error("create jobWeightModels: " + j.getKey() + " error!", ex);
				}
			}

			if (newJobThreshold.getDefaultThreshold() <= 0)
				throw new RuntimeException("total leave resource > total resource...");

			for (JobThreadWeightModel j : newJobThreadWeightModel) 
			{
				try 
				{
					if (j.getValue() == 0)
						continue;

					if (j.getType().equals(JobThreadWeightModel.WEIGHT_MODEL_LIMIT)) 
					{
						if (newJobThreshold.getThresholdPool().get(j.getKey()) > newJobThreshold.getDefaultThreshold())
							newJobThreshold.getThresholdPool().put(j.getKey(), -newJobThreshold.getDefaultThreshold());
						else
							newJobThreshold.getThresholdPool().put(j.getKey(), -newJobThreshold.getThresholdPool().get(j.getKey()));
					}
				} 
				catch (Exception ex) 
				{
					log.error("create jobWeightModels: " + j.getKey() + " error!", ex);
				}
			}
		}
		
		return newJobThreshold;
	}
	
	/**
	 * 兼容普通的runnable的提交
	 * @param job
	 */
	public void execute(Runnable job)
	{
		if (job instanceof Job)
			execute((Job)job);
		else
			threadPool.execute(job);
	}
	
	
	/**
	 * 检查是否有资源可用，
	 * 注意检查过程已经有对资源数值作修改的动作，不可重复调用，避免资源泄露
	 * @param job
	 * @return
	 */
	public boolean checkJobResource(Job job)
	{
		boolean hasResource = false;
		
		// 第一层做总量判断，同时锁定总资源
		if (totalCounter.incrementAndGet() > this.maximumPoolSize) {
			totalCounter.decrementAndGet();
			return false;
		}

		String key = job.getKey();
		Integer threshold = null;
		if (key != null)
			threshold = jobThreshold.getThresholdPool().get(key);

		if (key == null || threshold == null) {
			//使用默认资源，计数器累加比较判断是否有资源
			if (defaultCounter.incrementAndGet() > jobThreshold.getDefaultThreshold()) {
				defaultCounter.decrementAndGet();
			} else {
				hasResource = true;
			}
		} else {
			AtomicInteger counter = counterPool.get(key);
			if (threshold > 0) {// leave mode
				//leave模式下，可以选择先用私有的资源
				if (privateUseFirst){
					if (counter.incrementAndGet() > threshold) {
						counter.decrementAndGet();
						
						//私有的用完了话，考虑用共有的
						if (defaultCounter.incrementAndGet() > jobThreshold.getDefaultThreshold()) {
							defaultCounter.decrementAndGet();
						} else {
							hasResource = true;
						}
					} else {
						hasResource = true;
					}
				}
				else
				{
					//先用公有的，如果没有资源在判断是否有私有的
					if (defaultCounter.incrementAndGet() > jobThreshold.getDefaultThreshold()) {
						defaultCounter.decrementAndGet();
						if (counter.incrementAndGet() > threshold) 
							counter.decrementAndGet();
						else
							hasResource = true;
					} else {
						hasResource = true;
					}
				}
				
			} else {//limit模式下，检查是否超过了阀值，limit得阀值设置为负数用于和leave区分
				if (counter.incrementAndGet() > -threshold) {
					counter.decrementAndGet();
				} else {
					if (defaultCounter.incrementAndGet() > jobThreshold.getDefaultThreshold()) {
						defaultCounter.decrementAndGet();
						counter.decrementAndGet();
					} else {
						hasResource = true;
					}
				}
			}
		}

		if (!hasResource) 
			totalCounter.decrementAndGet();
		
		return hasResource;
	}

	/**
	 * 提交任务
	 */
	public void execute(Job job) {
		
		boolean hasResource = checkJobResource(job);

		if (hasResource) 
		{
			threadPool.execute(job);
		} 
		else 
		{
			pushJob(job);	
		}
	}

	public JobThreadWeightModel[] getJobThreadWeightModel() {
		return jobThreadWeightModel;
	}

	public void setJobThreadWeightModel(JobThreadWeightModel[] jobThreadWeightModel) {
		this.jobThreadWeightModel = jobThreadWeightModel;
	}

	public void setJobThreadWeightModel(List<JobThreadWeightModel> jobThreadWeightModel) {
		this.jobThreadWeightModel = jobThreadWeightModel.toArray(new JobThreadWeightModel[0]);
	}

	public int getMaximumPoolSize() {
		return maximumPoolSize;
	}

	public void setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}

	public int getMaximumQueueSize() {
		return this.maximumQueueSize;
	}

	public void setMaximumQueueSize(int maximumQueueSize) {
		this.maximumQueueSize = maximumQueueSize;
	}

	public Map<String, AtomicInteger> getCounterPool() {
		return counterPool;
	}

	public Map<String, Integer> getThresholdPool() {
		return jobThreshold.getThresholdPool();
	}

	public int getDefaultThreshold() {
		return jobThreshold.getDefaultThreshold();
	}

	public AtomicInteger getDefaultCounter() {
		return defaultCounter;
	}

	public AtomicInteger getTotalCounter() {
		return totalCounter;
	}

	public Map<String, AtomicInteger> getDefaultCounterPool() {
		return defaultCounterPool;
	}
	
	public int getCorePoolSize() {
		return corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	/**
	 * 任务入队列
	 * @param job
	 */
	public void pushJob(Job job)
	{
		JobQueue jobQueue = getJobQueue(job);
					
		if (!jobQueue.offer(job)) {// 补偿job
			throw new RuntimeException("can't submit job, queue full...");
		}

	}
	
	/**
	 * 根据job类型获取对应的队列
	 * @param job
	 * @return
	 */
	public JobQueue getJobQueue(Job job)
	{
		JobQueue jobQueue;
		
		//采用默认的
		if (job.getKey() == null || 
				(job.getKey() != null && !jobQueuePool.containsKey(job.getKey())))
		{
			jobQueue = jobQueuePool.get(DEFAULT_JOBQUEUE);
		}
		else
		{
			jobQueue = jobQueuePool.get(job.getKey());
		}
		
		
		return jobQueue;
	}

	
	/**
	 * 线程执行前的操作
	 * @param job
	 */
	public void beforeExecuteJob(Job job)
	{
		//用于统计默认线程中不同的请求消耗的线程数
		if (job.getKey() != null && defaultCounterPool.containsKey(job.getKey()))
		{
			defaultCounterPool.get(job.getKey()).incrementAndGet();
		}
	}

	/**
	 * 释放线程时对于各种计数器做递减
	 * @param job
	 */
	public void releaseJob(Job job) {
		//需要增加notify的代码
		String key = job.getKey();
		
		this.getTotalCounter().decrementAndGet();
		
		if (this.getCounterPool().size() == 0 || key == null) 
		{
			this.getDefaultCounter().decrementAndGet();
		}
		else 
		{
			AtomicInteger counter = this.getCounterPool().get(key);
			
			if (counter != null) 
			{
				
				if(defaultCounterPool.get(key) != null)
				{
					defaultCounterPool.get(key).decrementAndGet();
				}
				
				//leave先还私有的
				if (counter.decrementAndGet() < 0) 
				{ 
					counter.incrementAndGet();
					this.getDefaultCounter().decrementAndGet();
				} 
				else 
				{
					Integer size = this.getThresholdPool().get(key);
					
					if (size == null || (size != null && size < 0)) 
					{ // limit mode (use default)
						// counter.decrementAndGet();
						this.getDefaultCounter().decrementAndGet();
					} 
					else 
					{ // leave mode (use itself)
						// nothing to do
					}
				}
			}
			else 
			{
				this.getDefaultCounter().decrementAndGet();
			}
		}
		
		//释放资源信号,必须放在最后
		JobQueue jobQueue = getJobQueue(job);
		jobQueue.notifyHasResource();
		
	}

}
