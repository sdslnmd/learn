package com.taobao.top.xbox.commonlog;

/**
 * <的condition
 * @author Administrator
 *
 */
public class LessCondition implements Condition {
	private float numberValueFilter;
	private String stringValueFilter = null;
	
	public LessCondition(String valueFilter) {
		if(valueFilter != null){
			try{
				numberValueFilter = Float.parseFloat(valueFilter);
			}catch(Exception e){
				stringValueFilter = valueFilter;
			}
		}
	}

	@Override
	public boolean isSatisfyConditon(String value) {
		if(value == null){
			return false;
		}
		try{
			float numberValue = 0;
			numberValue = Float.parseFloat(value);
			if(numberValue < numberValueFilter){
				return true;
			}else {
				return false;
			}
		}catch(Exception e){
			String temp = stringValueFilter != null ? stringValueFilter:String.valueOf(numberValueFilter);
			if(value.compareTo(temp) < 0){
				return true;
			}else{
				return false;
			}
		}
	}
}
