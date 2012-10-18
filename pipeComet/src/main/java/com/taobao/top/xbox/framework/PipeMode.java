/**
 * 
 */
package com.taobao.top.xbox.framework;

/**
 * 管道的四种模式
 * @author fangweng
 * @email fangweng@taobao.com
 * @date 2011-3-31
 *
 */
public enum PipeMode {

	
	ASYN("asyn"),//异步模式，表示后续管道都会被新线程接管执行，当前线程至此结束并回收
	
	SYN("syn"),//同步模式，管道由当前线程顺序执行
	
	PARALLEL("parallel"),//并行模式，当前管道交由新线程异步执行，当前线程继续顺序执行后面的管道链
	
	//条件模式，当前线程被回收，当前管道被作为条件事件放入事件驱动队列
	//事件可以被invoke（执行被挂起的管道内容），complete结束事件等待，执行剩余管道
	CONDITION("condition");
	
	
	private String value;

	public String getValue() {
		return value;
	}

	private PipeMode(String mode)
	{
		value = mode;
	}
	
	public PipeMode getPipeMode(String mode)
	{
		if (mode == null)
			return  SYN;
		
		if (mode.equalsIgnoreCase("asyn"))
			return ASYN;
		
		if (mode.equalsIgnoreCase("parallel"))
			return PARALLEL;
		
		if (mode.equalsIgnoreCase("condition"))
			return CONDITION;
		
		return SYN;
	}
	
}
