package com.taobao.top.xbox.commonlog;

import java.util.Map;

/**
 * 获取需要日志记录的数据。
 * @author zhenzi
 *
 */
public interface ILogDataGet {
	/**
	 * 取到输入需要记录的data
	 * @return
	 */
	public Map<String,String> getInputLogData();
	/**
	 * 取到输出需要记录的data
	 * @return
	 */
	public Map<String,String> getOutputLogData();
}
