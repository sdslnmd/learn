/**
 * 
 */
package com.taobao.top.lifecircle;

/**
 * @author janly
 *
 */
public interface Startable {
	/**
	 * 
	 */
	public void start();
	
	/**
	 * 
	 */
	public void reStart();
	
	
	/**
	 * 
	 * @return
	 */
	public boolean isStart();
}
