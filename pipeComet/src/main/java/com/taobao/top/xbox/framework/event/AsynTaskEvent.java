package com.taobao.top.xbox.framework.event;

import java.util.EventObject;
import com.taobao.top.xbox.framework.IPipeTask;

/**
 * 消息事件定义，用于事件框架
 * @author fangweng
 * @email fangweng@taobao.com
 * @date 2011-5-5
 *
 */
public abstract class AsynTaskEvent extends EventObject 
			implements java.lang.Runnable,java.lang.Comparable<AsynTaskEvent>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7882690518160071025L;
	
	long timeout;//如果设置0，表示永远不超时
	IPipeTask[] tasks;//事件处于执行状态时被执行的多个任务
	byte status = 0; //1:complete代表执行一次tasks，2代表invoke代表执行完毕后还可以由外部继续激发反复执行，4代表超时
	long timestamp;//用于标识创建时间
	IAsynTaskEventListener<? super AsynTaskEvent>[] eventListeners;
	
	public AsynTaskEvent(Object source,long timeout) {
		super(source);
		this.timeout = timeout;
		timestamp = System.currentTimeMillis();
	}
	
	
	public abstract AsynTaskEvent cloneEvent(boolean needStatus);

	//用于将Event按照timeout来排序
	@Override
	public int compareTo(AsynTaskEvent o) {
		if (this.timeout == 0)
			return 1;
		
		if (o.timeout == 0)
			return -1;
		
		
		return (int)(this.timeout - o.timeout);
	}
	
	
	public IAsynTaskEventListener<? super AsynTaskEvent>[] getEventListeners() {
		return eventListeners;
	}


	public void setEventListeners(
			IAsynTaskEventListener<? super AsynTaskEvent>[] eventListeners) {
		this.eventListeners = eventListeners;
	}


	public byte getStatus() {
		return status;
	}


	public void setStatus(byte status) {
		this.status = status;
	}
	
	protected void callListeners()
	{
		if (eventListeners != null && eventListeners.length > 0)
		{
			for(IAsynTaskEventListener<? super AsynTaskEvent> listener : eventListeners)
			{
				listener.onStatusChanged(this);
			}
		}
	}


	public void complete() 
	{
		status = (byte)(status | 1);
		
		callListeners();
	}
	
	public void invoke()
	{
		status = (byte)(status | 2);
		
		callListeners();
	}
	
	public void timeout()
	{
		status = (byte)(status | 4);
		callListeners();
	}
	
	
	public boolean isTimeOut()
	{
		//timeout小于等于0的时候，永不超时
		if (timeout <= 0)
			return false;
		
		if ((status & 4) == 4)
			return true;
		else
		{
			boolean flag = System.currentTimeMillis() > timeout;
			
			if (flag)
			{
				status = (byte)(status | 4);
			}
			
			return flag;
		}
	}
	
	public boolean isInvoke()
	{
		if ((status & 2) == 2)
			return true;
		else
			return false;
	}
	
	public boolean isComplete()
	{
		if ((status & 1) == 1)
			return true;
		else
			return false;
	}
	
	public boolean statusChanged()
	{
		if (status > 0)
			return true;
		else
		{
			return isTimeOut();
		}
	}
	
	public void clearStatus()
	{
		status = 0;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public IPipeTask[] getTasks() {
		return tasks;
	}

	public void setTasks(IPipeTask... tasks) {
		this.tasks = tasks;
	}
	
	public abstract void doTimeOut();
	
	@Override
	public void run()
	{
		if (isTimeOut())
			doTimeOut();
	}
	
	@Override
	public String toString()
	{
		return new StringBuilder().append("timeout: ").append(timeout)
					.append(" ,stauts: ").append(status)
					.append(" ,timestamp: ").append(timestamp)
					.append(" ,tasks count: ")
					.append( (tasks != null) ? tasks.length:0).toString();
	}
	

}

	
