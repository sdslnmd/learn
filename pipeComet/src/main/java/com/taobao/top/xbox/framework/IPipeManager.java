/**
 * 
 */
package com.taobao.top.xbox.framework;

import java.util.Collection;
import com.taobao.top.xbox.framework.event.AsynTaskEvent;




/**
 * 管道管理接口
 * @author fangweng
 *
 */
public interface IPipeManager <I extends IPipeInput,
			R extends IPipeResult,D extends IPipeData<I,R>,E extends AsynTaskEvent>
{
	/**
	 * 初始化资源
	 */
	public void init();
	
	/**
	 * 释放资源
	 */
	public void destory();
	
	/**
	 * 执行管道链，建立管道上下文，执行管道链调用，返回管道执行结果
	 * 执行顺序和注册的过程保持一致
	 * @param pipeInput
	 * @return
	 */
	public R doPipes(I pipeInput);
	
	/**
	 * 执行管道链，建立管道上下文，执行管道链调用，返回管道执行结果
	 * 执行顺序和注册的过程保持一致
	 * @param D
	 * @param pipe
	 * @return
	 */
	public R doPipes(D data,IPipe<? super I, ? super R> pipe);
	
	/**
	 * 执行管道链，建立管道上下文，执行管道链调用，返回管道执行结果
	 * 执行顺序和注册的过程保持一致
	 * @param pipeInput
	 * @param result
	 * @return
	 */
	public R doPipes(I pipeInput, R result,IPipe<? super I, ? super R> pipe);
	
	/**
	 * 获取注册管道
	 * @return
	 */
	public IPipe<? super I, ? super R>[] getPipes();
	
	/**
	 * 注册管道
	 * @param pipe
	 */
	public void register(IPipe<? super I, ? super R> pipe);
	
	/**
	 * 注销管道
	 * @param pipe
	 */
	public void unregister(IPipe<? super I, ? super R> pipe);
	
	/**
	 * 移除所有管道
	 */
	public void removeAll();
	
	/**
	 * 批量注册管道
	 * @param pipes
	 */
	public void addAll(Collection<IPipe<? super I, ? super R>> pipes);
	
	/**
	 * 批量注册管道
	 * @param pipes
	 */
	public void addAll(IPipe<? super I, ? super R>[] pipes);
	
	/**
	 * 批量替换
	 * @param pipes
	 */
	public void replaceAll(Collection<IPipe<? super I, ? super R>> pipes);
	
	
	/**
	 * 当执行的管道为异步模式（Asyn,Condition,Parallel）时，
	 * 创建事件，交由事件驱动模块执行
	 * @param data
	 */
	public void submitAsynTask(E event);
	
	/**
	 * 主动移除事件
	 * @param data
	 */
	public void removeAsynTask(E event);

	

	/**
	 * 创建异步任务
	 * @param data
	 * @param pipe
	 * @param executeNow
	 * @param timeout
	 * @return
	 */
	public E generateAsynTaskEvent(D data,IPipe<? super I, ? super R> pipe
			,boolean executeNow,long timeout);
	
}
