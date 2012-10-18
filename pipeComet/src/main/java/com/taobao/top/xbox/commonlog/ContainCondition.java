package com.taobao.top.xbox.commonlog;

import java.util.ArrayList;
import java.util.List;

/**
 * 包含的关键字过滤条件
 * @author zhenzi
 *
 */
public class ContainCondition implements Condition {
	private List<String> listValue = new ArrayList<String>();
	public ContainCondition(String valueFilter){
		if(valueFilter != null){
			String[] temp = valueFilter.split("\\|");
			for (String string : temp) {
				listValue.add(string);
			}
		}
	}
	@Override
	public boolean isSatisfyConditon(String value) {
		if(value == null){
			return false;
		}
		for (String str : listValue) {
			if(value.indexOf(str) > -1){
				return true;
			}
		}
		return false;
	}

}
