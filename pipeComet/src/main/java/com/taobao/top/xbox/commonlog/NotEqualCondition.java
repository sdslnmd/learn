package com.taobao.top.xbox.commonlog;

import java.util.ArrayList;
import java.util.List;

/**
 * != 过滤条件
 * 
 * @author zhenzi 2010-9-25
 */
public class NotEqualCondition implements Condition {
	private List<String> listValue = new ArrayList<String>();

	public NotEqualCondition(String valueFilter) {
		if (valueFilter != null) {
			String[] temp = valueFilter.split("\\|");
			for (String string : temp) {
				listValue.add(string);
			}
		}
	}

	@Override
	public boolean isSatisfyConditon(String value) {
		if (value == null) {
			return false;
		}
		if(listValue.contains(value)) {
			return false;
		} else {
			return true;
		}
	}
}
