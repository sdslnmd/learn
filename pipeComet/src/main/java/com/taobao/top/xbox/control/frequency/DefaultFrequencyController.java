package com.taobao.top.xbox.control.frequency;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A fast thread-safe frequency controller, but doesn't have precise frequency
 * control.
 * 
 * @author Helk
 * 
 */
public class DefaultFrequencyController implements FrequencyController {

	/**
	 * Time window of controlling.
	 * <p>
	 * If the count of access is larger then allowedCount the newly access would
	 * be reject (depends on programmer's reaction).
	 */
	protected long timeWindowInMillis = 15000; // default to each 15 seconds.

	/**
	 * Allows how many access within time window.
	 */
	protected int allowedCount = 45000; // 3000 per second

	protected boolean strictControl = true;

	protected Map<Object, ControlEntry> controlEntryMap = new ConcurrentHashMap<Object, ControlEntry>();

	protected final Lock lock = new ReentrantLock();

	public DefaultFrequencyController() {
	}

	@Override
	public boolean allowsAccess(Object key) {
		return allowsAccess(key, 1);
	}

	@Override
	public boolean allowsAccess(Object key, int weight) {
		if (strictControl) {
			try {
				lock.lock();
				return looseAccessControl(key, weight);
			} finally {
				lock.unlock();
			}
		} else {
			return looseAccessControl(key, weight);
		}
	}

	boolean looseAccessControl(Object key, int weight) {
		ControlEntry controlEntry = controlEntryMap.get(key);

		if (controlEntry == null) {
			controlEntry = new ControlEntry();
			controlEntry.setClearTimestamp(System.currentTimeMillis()
					+ timeWindowInMillis);
			controlEntryMap.put(key, controlEntry);
		}

		synchronized (controlEntry) {
			int count = controlEntry.getCount() + weight;

			if (count > allowedCount) {
				// The clear time breaches, clear the count.
				if (System.currentTimeMillis() > controlEntry
						.getClearTimestamp()) {
					controlEntry.setClearTimestamp(System.currentTimeMillis()
							+ timeWindowInMillis);
					if (weight > allowedCount) {
						controlEntry.setCount(0); // clear the count.
						return false;
					} else {
						controlEntry.setCount(weight); // i is smaller than
						// allowedCount.
						return true;
					}
				} else { // still in previous time window, reject the access.
					return false;
				}
			}

			// No put action if count exceeds allowedCount.
			controlEntry.setCount(count);
			return true;
		}
	}

	public long getTimeWindowInMillis() {
		return timeWindowInMillis;
	}

	public void setTimeWindowInMillis(long timeWindow) {
		this.timeWindowInMillis = timeWindow;
	}

	public int getAllowedCount() {
		return allowedCount;
	}

	public void setAllowedCount(int allowedCount) {
		this.allowedCount = allowedCount;
	}

	public Map<Object, ControlEntry> getControlEntryMap() {
		return controlEntryMap;
	}

	public void setControlEntryMap(Map<Object, ControlEntry> controlEntryMap) {
		this.controlEntryMap = controlEntryMap;
	}

	public boolean isStrictControl() {
		return strictControl;
	}

	public void setStrictControl(boolean strictControl) {
		this.strictControl = strictControl;
	}

}
