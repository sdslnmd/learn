package com.taobao.top.xbox.threadpool;

/**
 * 线程资源分配模型
 * 
 * @author fangweng
 */
public class JobThreadWeightModel {

	/**
	 * 预留模式，整个线程池将预留设置数量的线程仅仅用于某一类请求使用
	 */
	public static final String WEIGHT_MODEL_LEAVE = "leave";
	/**
	 * 限制模式，这类请求使用默认线程池，但不能超过设置的最大值
	 */
	public static final String WEIGHT_MODEL_LIMIT = "limit";

	private String key;
	private String type;
	private int value;

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
}
