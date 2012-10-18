/**
 * 
 */
package com.taobao.top.xbox.framework;


/**
 * 管道处理的输入，属于管道上下文的一部分
 * @author fangweng
 *
 */
public interface IPipeInput  extends java.io.Serializable
{
	/**
	 * 获取请求参数
	 * @param key
	 * @return
	 */
	public Object getParameter(String key) ;
}
