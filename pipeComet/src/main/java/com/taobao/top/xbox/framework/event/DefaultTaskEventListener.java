/**
 * 
 */
package com.taobao.top.xbox.framework.event;

/**
 * 默认的事件监听接口实现，调用eventkeeper去执行事件状态变更处理
 * @author fangweng
 * @email fangweng@taobao.com
 * @date 2011-5-17
 *
 */
public class DefaultTaskEventListener<E extends AsynTaskEvent> implements IAsynTaskEventListener<E> {

	EventKeeper<E> eventKeeper;
	
	public DefaultTaskEventListener(EventKeeper<E> eventKeeper)
	{
		this.eventKeeper = eventKeeper;
	}
	
	@Override
	public void onStatusChanged(E event) {
		eventKeeper.eventChanged(event);
	}

}
