/**
 * 
 */
package com.taobao.top.xbox.framework.event;

/**
 * 事件监听接口
 * @author fangweng
 * @email fangweng@taobao.com
 * @date 2011-5-17
 *
 */
public interface IAsynTaskEventListener<E extends AsynTaskEvent> {

	/**
	 * 状态发生变更
	 * @param event
	 */
	public void onStatusChanged(E event);
	
}
