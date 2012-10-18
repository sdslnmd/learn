/**
 * 
 */
package com.taobao.top.xbox.framework;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import com.taobao.top.xbox.framework.event.AsynTaskEvent;

/**
 * 管道管理基本抽象类
 * 
 * @author fangweng
 * 
 */
public abstract class AbstractPipeManager<I extends IPipeInput,
		R extends IPipeResult, D extends IPipeData<I,R>,E extends AsynTaskEvent> 
		implements IPipeManager<I,R,D,E>{
	/**
	 * 记录注册的管道
	 */
	protected CopyOnWriteArrayList<IPipe<? super I, ? super R>> pipeChain = 
		new CopyOnWriteArrayList<IPipe<? super I, ? super R>>();

	
	/**
	 * 获取已经注册的管道数组
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public IPipe<? super I,? super R>[] getPipes() {
		IPipe<? super I,? super R>[] tmpPipes = new IPipe[pipeChain
				.size()];
		return pipeChain.toArray(tmpPipes);
	}

	/**
	 * 注册管道（执行顺序和注册的顺序保持一致）
	 * 
	 * @param pipe
	 */
	@Override
	public void register(IPipe<? super I, ? super R> pipe) {
		pipeChain.add(pipe);
	}

	/**
	 * 注销管道
	 * 
	 * @param pipe
	 */
	@Override
	public void unregister(IPipe<? super I, ? super R> pipe) {
		int index = pipeChain.indexOf(pipe);

		if (index >= 0)
			pipeChain.remove(index);
	}

	/**
	 * 移除所有管道
	 */
	@Override
	public void removeAll() {
		pipeChain.clear();
	}

	/**
	 * 增加大部分管道
	 * 
	 * @param pipes
	 */
	@Override
	public void addAll(Collection<IPipe<? super I, ? super R>> pipes) {
		if (pipes != null && pipes.size() > 0)
			pipeChain.addAll(pipes);
	}
	
	/**
	 * 批量注册管道
	 * @param pipes
	 */
	@Override
	public void addAll(IPipe<? super I, ? super R>[] pipes)
	{
		if (pipes != null && pipes.length > 0)
		{
			for(IPipe<? super I, ? super R> p : pipes)
				pipeChain.add(p);
		}

	}

	/**
	 * 批量替换
	 * 
	 * @param pipes
	 */
	@Override
	public void replaceAll(Collection<IPipe<? super I, ? super R>> pipes) {
		CopyOnWriteArrayList<IPipe<? super I, ? super R>> tmpChain = new CopyOnWriteArrayList<IPipe<? super I, ? super R>>();

		tmpChain.addAll(pipes);
		pipeChain = tmpChain;

	}
}
