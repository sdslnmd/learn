package com.taobao.top.xbox.control.frequency;

/**
 * Use LruHashMap to store the control entry, limiting the memory consumption.
 * @author haishi
 *
 */
public class LruFrequencyController extends DefaultFrequencyController {
	public LruFrequencyController(int capacity) {
		this.controlEntryMap = new LruHashMap<Object, ControlEntry>(capacity);
	}
}
