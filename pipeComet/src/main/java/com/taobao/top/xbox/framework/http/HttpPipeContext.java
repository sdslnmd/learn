/**
 * 
 */
package com.taobao.top.xbox.framework.http;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.taobao.top.xbox.framework.IPipeContext;
import com.taobao.top.xbox.framework.event.AsynTaskEvent;


/**
 * Top的管道上下文实现
 * @author fangweng
 *
 */
public class HttpPipeContext implements IPipeContext
{
	//由于异步方式可能会有多线程访问，因此采用线程安全的方式
	private ConcurrentMap<String,Object> attachments = new ConcurrentHashMap<String,Object>();
	/**
	 * 上下文中内附的多个事件
	 */
	private ConcurrentMap<String,AsynTaskEvent> events = new ConcurrentHashMap<String,AsynTaskEvent>();
	
	
	/**
	 * @param attachment
	 */
	public Object getAttachment(String name)
	{
		return this.attachments.get(name);
	}
	
	/**
	 * @param attachment
	 */
	public void setAttachment(String name,Object attachment)
	{
		this.attachments.put(name, attachment);
	}
	
	
	/**
	 * @return the attachments
	 */
	public Map<String, Object> getAttachments() {
		return attachments;
	}

	@Override
	public void removeAttachments() {
		attachments.clear();
	}

	@Override
	public void removeAttachment(String name) {
		attachments.remove(name);
	}

	@Override
	public void addAllAttachments(Map<String, Object> atts)
	{
		if (atts != null && atts.size() > 0)
			attachments.putAll(atts);
	}

	@Override
	public AsynTaskEvent getEvent(String pipeName) {
		return events.get(pipeName);
	}

	@Override
	public void setEvent(String pipeName, AsynTaskEvent event) {
		events.put(pipeName, event);
	}

}
