/**
 * 
 */
package com.taobao.top.xbox.framework.http;

import com.taobao.top.xbox.framework.IPipe;
import com.taobao.top.xbox.framework.IPipeTask;
import com.taobao.top.xbox.framework.event.AsynHttpTaskEvent;

/**
 * 任务工厂，可附加于Pipe，用于在Pipe执行时创建需要执行的任务
 * ，如果Pipe没有设置，则用框架默认方式
 * @author fangweng
 * @email fangweng@taobao.com
 * @date 2011-5-16
 *
 */
public interface IHttpPipeTaskFactory<I extends HttpPipeInput
		,R extends HttpPipeResult,D extends HttpPipeData<I,R>,E extends AsynHttpTaskEvent<I,R,D>> {
	/**
	 * 获取异步任务
	 * @param pipeData
	 * @param pipe
	 * @param pipeManager
	 * @return
	 */
	IPipeTask[] createPipeTasks(IPipe<? super I ,? super R> pipe,
			D pipeData,AbstractHttpPipeManager<I,R,D,E> pipeManager);
}
