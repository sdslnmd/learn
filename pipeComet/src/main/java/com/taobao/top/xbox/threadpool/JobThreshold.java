/**
 * 
 */
package com.taobao.top.xbox.threadpool;

import java.util.HashMap;
import java.util.Map;

/**
 * 权重设置记录对象
 * @author fangweng
 * @email fangweng@taobao.com
 * @date 2011-2-28
 *
 */
public class JobThreshold {
	
	private Map<String, Integer> thresholdPool = new HashMap<String, Integer>();
	private int defaultThreshold;
	
	
	public Map<String, Integer> getThresholdPool() {
		return thresholdPool;
	}
	public void setThresholdPool(Map<String, Integer> thresholdPool) {
		this.thresholdPool = thresholdPool;
	}
	public int getDefaultThreshold() {
		return defaultThreshold;
	}
	public void setDefaultThreshold(int defaultThreshold) {
		this.defaultThreshold = defaultThreshold;
	}
	
}
