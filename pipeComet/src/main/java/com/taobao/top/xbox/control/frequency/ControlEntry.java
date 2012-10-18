package com.taobao.top.xbox.control.frequency;

public class ControlEntry {
	int count;
	/**
	 * Timestamp of clearing the count.
	 */
	long clearTimestamp;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getClearTimestamp() {
		return clearTimestamp;
	}

	public void setClearTimestamp(long clearTimestamp) {
		this.clearTimestamp = clearTimestamp;
	}

}
