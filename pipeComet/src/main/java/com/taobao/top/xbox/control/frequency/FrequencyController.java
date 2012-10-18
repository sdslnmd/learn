package com.taobao.top.xbox.control.frequency;

/**
 * FrequencyController controls the access frequency
 * to precious resources (bandwidth, service).
 * @author haishi
 *
 */
public interface FrequencyController {
	
	/**
	 * Control the access by frequency.
	 * @param key
	 * @return
	 */
	boolean allowsAccess(Object key);
	
	/**
	 * Control a key with specified weight whether
	 * to access.
	 * @param key
	 * @param weight
	 * @return
	 */
	boolean allowsAccess(Object key, int weight);
}
