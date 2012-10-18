package com.taobao.top.xbox.asynlog;
/**
 * 默认任务接口定义
 * @author wenchu.cenwc
 *
 */
public interface ISchedule<T> extends Runnable
{
	/**
	 * 初始化
	 */
	public void init();
	
	
	/**
	 * 停止任务
	 */
	public void stopSchedule();
}
