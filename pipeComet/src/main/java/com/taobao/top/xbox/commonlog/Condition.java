package com.taobao.top.xbox.commonlog;
/**
 * 每种condition都是需要实现这个接口
 * @author zhenzi
 *
 */
public interface Condition {
	/**
	 * 判断字段对应的值是否满足条件
	 * @param value 传入字段对应的值
	 * @return
	 */
	public boolean isSatisfyConditon(String value);
}
