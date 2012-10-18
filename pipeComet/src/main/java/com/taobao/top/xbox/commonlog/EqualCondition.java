package com.taobao.top.xbox.commonlog;

import java.util.ArrayList;
import java.util.List;

/**
 * =的条件
 * @author zhenzi
 *
 */
public class EqualCondition implements Condition{
	private List<String> listValue = new ArrayList<String>();
	
	public EqualCondition(String valueFilter){
		if(valueFilter != null){
			String[] temp = valueFilter.split("\\|");
			for (String string : temp) {
				if(string.equals("null")){//对null做特殊处理，把配置中的字符串null转化成对象null
					listValue.add(null);
				}else{
					listValue.add(string);
				}
			}
		}
	}
	@Override
	public boolean isSatisfyConditon(String value) {
		/*if(null == value){
			return false;
		}*/
		if(listValue.contains(value)){
			return true;
		}
		return false;
	}

}
