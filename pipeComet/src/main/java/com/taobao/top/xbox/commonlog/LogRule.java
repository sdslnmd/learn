package com.taobao.top.xbox.commonlog;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.taobao.top.xbox.asynlog.AsynWriter;
/**
 * log的规则，包括需要记录的field，记录这些field的条件
 * 包括一些全局的条件
 * @author zhenzi
 *
 */
public class LogRule {
	/*
	 * 标识是log input还是log output
	 * 位标识，总共两位：11，第一位标示input，第二位标识output
	 */
	private int logType;
	/*
	 * 是否分开单独的日志文件记录输入和输出
	 * 默认放在一个文件里
	 */
//	private boolean isSeparate = false;
	/*
	 * 输入和输出是否在同一行，默认分开两行
	 */
	private boolean isOneline = false;
	/*
	 * 日志记录的结束时间
	 */
	private long endTime;
	/**
	 * 对于输入的日志记录在哪里
	 */
	private Log inputLog = LogFactory.getLog(LogRule.class);
	private AsynWriter<String> inputWriter;
	/**
	 * 对于输出的日志记录在哪里
	 */
	private Log outputLog = LogFactory.getLog(LogRule.class);
	private AsynWriter<String> outputWriter;
	/**
	 * 需要记录输入的那些数据
	 */
	private List<String> logInputFields;
	/**
	 * 需要记录输出的那些数据
	 */
	private List<String> logOutputFields;
	
	/**
	 * 对应于logInputFields的数据是否输出的condition
	 */
	private Map<String,Condition> inputCondition;
	/**
	 * 对应于logOutputFields的数据是否输出的condition
	 */
	private Map<String,Condition> outputCondition;
	
	public AsynWriter<String> getInputWriter() {
		return inputWriter;
	}
	public AsynWriter<String> getOutputWriter() {
		return outputWriter;
	}
	public void setInputLog(Log inputLog) {
		if(inputLog != null){
			this.inputLog = inputLog;
		}
	}
	public void setOutputLog(Log outputLog) {
		if(outputLog != null){
			this.outputLog = outputLog;
		}
	}
	public Log getInputLog() {
		return inputLog;
	}
	public Log getOutputLog() {
		return outputLog;
	}
	public List<String> getLogInputFields() {
		return logInputFields;
	}
	public void setLogInputFields(List<String> logInputFields) {
		this.logInputFields = logInputFields;
	}
	public List<String> getLogOutputFields() {
		return logOutputFields;
	}
	public void setLogOutputFields(List<String> logOutputFields) {
		this.logOutputFields = logOutputFields;
	}
	public Map<String, Condition> getInputCondition() {
		return inputCondition;
	}
	public void setInputCondition(Map<String, Condition> inputCondition) {
		this.inputCondition = inputCondition;
	}
	public Map<String, Condition> getOutputCondition() {
		return outputCondition;
	}
	public void setOutputCondition(Map<String, Condition> outputCondition) {
		this.outputCondition = outputCondition;
	}
//	public boolean isSeparate() {
//		return isSeparate;
//	}
//	public void setSeparate(boolean isSeparate) {
//		this.isSeparate = isSeparate;
//	}
	
	public boolean isOneline() {
		return isOneline;
	}
	public void setOneline(boolean isOneline) {
		this.isOneline = isOneline;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
	public int getLogType() {
		return logType;
	}
	public void setLogType(int logType) {
		this.logType = logType;
	}
	public void init(){
		inputWriter = new AsynWriter<String>(inputLog,null);
		inputWriter.init();
		outputWriter = new AsynWriter<String>(outputLog,null);
		outputWriter.init();
	}
}
