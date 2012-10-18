package com.taobao.top.xbox.commonlog;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 对外的接口，外部使用者只需要启动一个LogManager实例即可。
 *  ruleName=request
	isOneline=false  //输入和输出是否记录在同一行，默认分开记录在两行
	endTime=2010-08-10 14:50:00 //记录日志的结束时间
	logType=3                 //用于标记是记录输出还是记录输入还是两者都记录,用位操作,第一位表示输入,第二位表示输出
	inputF=sku_properties //记录的fields，如果没有设置则记录所有的，如果设置了则只记录设置的fields
	inputC=method=taobao.item.vip.add //对于输出的条件，条件有> < = != contain 
	inputRule=file:com.taobao.top.tradeApi
	
	outputF=
	outputC=
	//outputRule=file:com.taobao.top.tradeApi
	
	如果没有条件，则全部输入.
	如果有条件，则根据条件过滤，
	如果只有输入条件或者输出条件，则有一个满足则把这条请求的数据输出，
	如果输入条件和输出条件都有，两个条件都满足才输出。
	
	对于输出的格式：
	如果指定了inputF或者outputF:并且输入和输出在同一行，输出格式是：ruleName,a,b,c,a,b,c
	                                                                                   输入和输出不在同一行，格式是：input,a,b,c\r\n output,a,b,c
	如果没有指定inputF或者outputF：输出格式是：param={k=v,k=v}\r\n(或者在同一行)result={k=v,k=v}
 * @author zhenzi
 * 
 */
public class LogManager {
	private static LogManager logManager = new LogManager();

	private LogManager() {

	}

	public static LogManager getInstance() {
		return logManager;
	}

	/**
	 * 保存各个规则
	 */
	private Map<String/* rule name */, LogRule> logRule = new HashMap<String, LogRule>();

	/**
	 * 使用指定的规则对数据过滤
	 */
	public void logData(ILogDataGet logData, String ruleName) {
		logData(logData.getInputLogData(), logData.getOutputLogData(), ruleName);
	}

	/**
	 * @param inputLogData
	 * @param outputLogData
	 * @param ruleName
	 */
	public void logData(Map<String, String> inputLogData,
			Map<String, String> outputLogData, String ruleName) {
		LogRule rule = logRule.get(ruleName);
		if (rule == null || (inputLogData == null && outputLogData == null)) {
			return;
		}
		if(System.currentTimeMillis() > rule.getEndTime() || rule.getLogType() == 0){
			return;
		}
		
		Map<String, Condition> inputCondition = rule.getInputCondition();
		Map<String, Condition> outputCondition = rule.getOutputCondition();
		List<String> inputFields = rule.getLogInputFields();
		List<String> outputFields = rule.getLogOutputFields();
		
		String input = null;
		if((rule.getLogType() & 2) == 2){//需要记录输入
			input = getResult(inputFields,inputCondition, inputLogData);
		}
		String output = null;
		if((rule.getLogType() & 1) == 1){//需要记录输出
			if((inputCondition != null && input != null) || inputCondition == null){
				output = getResult(outputFields,outputCondition,outputLogData);
			}
		}
		//分别组装输入和输出的结果
		StringBuilder result = new StringBuilder();
		
		StringBuilder inputSb = new StringBuilder();
		StringBuilder outputSb = new StringBuilder();
		//TODO 目前的一个限制是如果只想记录输入，那么在配置的时候输出的配置必须不能有，反之亦然
		if(inputCondition != null && outputCondition != null){//两个条件都有，那么必须都满足才能输出
			if(input != null && output != null){
				if(inputFields == null){//记录所有的数据
					inputSb.append("param=").append(input);
				}else{//只记录部分数据
					inputSb.append("input,").append(input);
				}
				if(outputFields == null){
					outputSb.append("result=").append(output);
				}else{
					outputSb.append("output,").append(output);
				}
			}
		}else if(inputCondition != null && outputCondition == null){//只有inputCondition
			if(input != null){
				if(inputFields == null){//记录所有的数据
					inputSb.append("param=").append(input);
				}else{//只记录部分数据
					inputSb.append("input,").append(input);
				}
				if((rule.getLogType() & 1) == 1){//需要记录输出
					if(outputFields == null){
						outputSb.append("result=").append(output);
					}else{
						outputSb.append("output,").append(output);
					}
				}
			}
		}else if(inputCondition == null && outputCondition != null){//只有outputCondition
			if(output != null){
				if((rule.getLogType() & 2) == 2){
					if(inputFields == null){//记录所有的数据
						inputSb.append("param=").append(input);
					}else{//只记录部分数据
						inputSb.append("input,").append(input);
					}
				}
				if(outputFields == null){
					outputSb.append("result=").append(output);
				}else{
					outputSb.append("output,").append(output);
				}
			}
		}else if(inputCondition == null && outputCondition == null){//没有任何过滤条件
			if(input != null){
				if(inputFields == null){//记录所有的数据
					inputSb.append("param=").append(input);
				}else{//只记录部分数据
					inputSb.append("input,").append(input);
				}
			}
			if(output != null){
				if(outputFields == null){
					outputSb.append("result=").append(output);
				}else{
					outputSb.append("output,").append(output);
				}
			}
		}
		//组装结果
		if(rule.isOneline()){//输出到一行
			if(inputSb.length() > 0){
				result.append(inputSb.toString());
			}
			if(outputSb.length() > 0){
				if(inputSb.length() > 0){
					result.append(",").append(outputSb.toString());
				}else{
					result.append(outputSb.toString());
				}
			}
		}else{//输出到两行
			if(inputSb.length() > 0){
				result.append(inputSb.toString());
			}
			if(outputSb.length() > 0){
				if(inputSb.length() > 0){
					result.append("\r\n").append(outputSb.toString());
				}else{
					result.append(outputSb.toString());
				}
			}
		}
		if(result.length() > 0){
			rule.getInputWriter().write(result.toString());
		}
	}
	/**
	 * 根据条件和需要记录的字段进行过滤组装
	 * @param logFields 需要记录的字段
	 * @param conditionMap 过滤的条件
	 * @param data 数据
	 * @return
	 */
	private String getResult(List<String> logFields, Map<String,Condition> conditionMap,Map<String,String> data){
		StringBuilder result = new StringBuilder();
		if(conditionMap != null){//有过滤条件
			Iterator<Map.Entry<String, Condition>> it = conditionMap.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String, Condition> en = it.next();
				if(!en.getValue().isSatisfyConditon(data.get(en.getKey()))){
					return null;
				}
			}
		}
		
		if(logFields == null || (logFields != null && logFields.size() == 0)){//记录全部的数据
			result.append("{");
			Iterator<Map.Entry<String, String>> it = data.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String,String> value = it.next();
				result.append(value.getKey()).append("=").append(value.getValue()).append(",");
			}
			result.append("}");
			return result.toString();
		}else {//记录用户指定的数据
			for (String string : logFields) {
				result.append(data.get(string)).append(",");
			}
			return result.substring(0, result.length() - 1);
		}
	}

	/**
	 * 构建log规则
	 * 
	 * @param ruleStr
	 */
	public void buildLogRule(String ruleStr) throws Exception {
		BuildLogRule buildLogRule = new BuildLogRule(logManager);
		buildLogRule.buildLogRule(ruleStr);
	}

	public Map<String, LogRule> getLogRule() {
		return logRule;
	}

	public void setLogRule(Map<String, LogRule> logRule) {
		this.logRule = logRule;
	}

}
