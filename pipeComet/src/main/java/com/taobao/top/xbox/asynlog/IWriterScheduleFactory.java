/**
 * 
 */
package com.taobao.top.xbox.asynlog;


/**
 * @author fangweng
 *
 */
public interface IWriterScheduleFactory<T>
{
	public void init();
	
	public WriteSchedule<T> createWriter();
	
	public void setLoggerClass(String loggerClass);
}
