package com.taobao.top.xbox.commonlog;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 用于构建log规则
 * @author zhenzi
 * 2010-9-25 下午09:25:25
 */
public final class BuildLogRule {
	private static final Log log = LogFactory.getLog(BuildLogRule.class);
	private LogManager logManager;
	protected BuildLogRule(LogManager logManager){
		this.logManager = logManager;
	}
	/**
	 * 构建log规则
	 * 
	 * @param ruleStr
	 */
	public void buildLogRule(String ruleStr) throws Exception {
		if (StringUtils.isEmpty(ruleStr)) {
			return;
		}
		ByteArrayInputStream byteArrayInput = new ByteArrayInputStream(
				ruleStr.getBytes());
		Properties p = new Properties();
		try {
			p.load(byteArrayInput);
		} catch (IOException e) {
			log.error(e, e);
			throw new Exception(e.getMessage());
		}
		// 规则名称
		String ruleName = p.getProperty(Constants.RULE_NAME);
		if (null == ruleName) {
			throw new Exception("ruleName is null");
		}
		LogRule rule = new LogRule();
		// 构建inputFields和outputFields
		rule.setLogInputFields(getFields(p.getProperty(Constants.INPUT_FIELDS)));
		rule.setLogOutputFields(getFields(p.getProperty(Constants.OUTPUT_FIELDS)));
		// 构建inputCondition和outputCondition
		rule.setInputCondition(getCondition(p.getProperty(Constants.INPUT_CONDITION)));
		rule.setOutputCondition(getCondition(p.getProperty(Constants.OUTPUT_CONDITION)));
		// 构建inputRule和outputRule
		buildRule(p.getProperty(Constants.INPUT_GLOABLE_RULE), rule, true);
		buildRule(p.getProperty(Constants.OUTPUT_GLOABLE_RULE), rule,
				false);
		// 是否把输入和输出分开放到不同的文件里
//		String isSeparate = p.getProperty(Constants.IS_SEPARATE);
//		if (isSeparate != null) {
//			try {
//				rule.setSeparate(new Boolean(isSeparate));
//			} catch (Exception e) {
//				log.error(e, e);
//				throw e;
//			}
//		}
		//输入和输出是否放在同一行
		String isOneline = p.getProperty(Constants.IS_ONELINE);
		if(isOneline != null){
			try{
				rule.setOneline(new Boolean(isOneline));
			}catch(Exception e){
				log.error(e,e);
				throw e;
			}
		}
		//日志记录结束时间
		String endTime = p.getProperty(Constants.END_TIME);
		if(endTime != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			rule.setEndTime(sdf.parse(endTime).getTime());
		}
		//logType
		String logType = p.getProperty(Constants.LOG_TYPE);
		if(logType != null){
			try{
				rule.setLogType(Integer.valueOf(logType));
			}catch(Exception e){
				log.error(e, e);
				throw e;
			}
		}
		rule.init();//初始化线程
		logManager.getLogRule().put(ruleName, rule);
	}

	/**
	 * 获取到要记录的字段，inputF,outputF
	 * @param fStr
	 * @return
	 */
	private List<String> getFields(String fStr) {
		if (fStr == null) {
			return null;
		} else {
			List<String> fList = new ArrayList<String>();
			String[] fields = fStr.split(",");
			for (String string : fields) {
				fList.add(string.trim());
			}
			return fList;
		}
	}

	/**
	 * //TODO 目前只支持=,<,>,!=,@(表示包含符号)号操作，后续需要将强对其他操作符的支持
	 * 
	 * @param cStr
	 * @return
	 */
	private Map<String, Condition> getCondition(String cStr) {
		if (cStr == null) {
			return null;
		} else {
			Map<String, Condition> cMap = new HashMap<String, Condition>();

			String[] conditions = cStr.split(",");			
			for (String condition : conditions) {// 对每个condition配置处理
				if(condition.indexOf("=") > -1 && condition.indexOf("!=") == -1){//=条件
					int index = condition.indexOf("=");
					cMap.put(condition.substring(0, index), new EqualCondition(condition.substring(index + 1, condition.length())));
				}else if(condition.indexOf(">") > -1){//>条件
					int index = condition.indexOf(">");
					cMap.put(condition.substring(0, index), new GreaterCondition(condition.substring(index + 1, condition.length())));
				}else if(condition.indexOf("<") > -1){//<条件
					int index = condition.indexOf("<");
					cMap.put(condition.substring(0, index), new LessCondition(condition.substring(index + 1, condition.length())));
				}else if(condition.indexOf("!=") > -1){//!=条件
					int index = condition.indexOf("!=");
					cMap.put(condition.substring(0, index), new NotEqualCondition(condition.substring(index + 2, condition.length())));
				}else if(condition.indexOf("@") > -1){//包含条件
					int index = condition.indexOf("@");
					cMap.put(condition.substring(0, index), new ContainCondition(condition.substring(index + 1, condition.length())));
				}else{
					log.error("conditon " + condition + "is not correct");
				}
			}
			return cMap;
		}
	}

	/**
	 * 
	 * @param ruleStr
	 * @param rule
	 * @param isInputRule
	 *            inputRule = true,outputRule = false
	 */
	private void buildRule(String ruleStr, LogRule rule,
			boolean isInputRule) {
		if (ruleStr == null || rule == null) {
			return;
		}
		String logFile = null;
		String[] r = ruleStr.split(",");
		for (String string : r) {
			if (string.startsWith(Constants.LOG_FILE) && logFile == null) {
				logFile = string.split(":", 2)[1];
			}
		}
		if (isInputRule) {// 构建inputRule
			if (logFile != null) {
				rule.setInputLog(LogFactory.getLog(logFile));
			}
		} else {// 构建outputRule
			if (logFile != null) {
				rule.setOutputLog(LogFactory.getLog(logFile));
			}
		}
	}
}
