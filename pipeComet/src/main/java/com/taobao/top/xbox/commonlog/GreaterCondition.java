package com.taobao.top.xbox.commonlog;
/**
 * >的过滤条件,如果value能转换成数字比较,则用数字的大小比较,如果不能则进行字符串的比较
 * @author zhenzi
 *
 */
public class GreaterCondition implements Condition{
	private float numberValueFilter;
	private String stringValueFilter = null;
	public GreaterCondition(String valueFilter){
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
		float numberValue = 0;
		try{
			numberValue = Float.parseFloat(value);
			if(numberValue > numberValueFilter){
				return true;
			}else {
				return false;
			}
		}catch(Exception e){
			String temp = stringValueFilter != null ? stringValueFilter:String.valueOf(numberValueFilter);
			if(value.compareTo(temp) > 0){
				return true;
			}else{
				return false;
			}
		}
	}
}
