package com.taobao.top.xbox.asynlog.data;

import java.util.LinkedList;
import java.util.List;

public class TopRequestLog extends TopLog
{
	private static final String REQUEST = "request";
	/**
	 * 
	 */
	private static final long serialVersionUID = 8627431125092181816L;
	/**
	 * 服务请求者的ip
	 */
	private String remoteIp;
	private String partnerId;
	private String format;
	private String appKey;
	private String apiName;
	/**
	 * 服务请求字节数
	 */
	private long readBytes;
	private String errorCode;
	private String subErrorCode;
	/**
	 * 当前TOP服务器的ip
	 */
	private String localIp;
	
	private String signMethod;
	private String version;
	
	/**
	 * 时间打点队列
	 */
	private List<Long> timeStampQueue = new LinkedList<Long>();
	private List<Long> methodTimeQueue = new LinkedList<Long>();
	
	/**
	 * 服务调用中绑定的用户昵称
	 */
	private String nick;
	
	private long startTrack = 0;
	
	
	public TopRequestLog() {
		this.className = REQUEST;
		logTime = System.currentTimeMillis();
	}
	
	public String getSignMethod()
	{
		return signMethod;
	}

	public void setSignMethod(String signMethod)
	{
		this.signMethod = signMethod;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public void trackStart()
	{
		startTrack = System.currentTimeMillis();
	}
	
	public void trackEnd(String tag)
	{
		methodTimeQueue.add(System.currentTimeMillis() - startTrack);
		startTrack = 0;
	}
	
	public String getLocalIp() {
		return localIp;
	}
	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getRemoteIp() {
		return remoteIp;
	}
	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getApiName() {
		return apiName;
	}
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
	public long getReadBytes() {
		return readBytes;
	}
	public void setReadBytes(long readBytes) {
		this.readBytes = readBytes;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getSubErrorCode() {
		return subErrorCode;
	}
	public void setSubErrorCode(String subErrorCode) {
		this.subErrorCode = subErrorCode;
	}
	public List<Long> getTimeStampQueue() {
		return timeStampQueue;
	}

	public void track(Long timestamp)
	{
		timeStampQueue.add(timestamp);
	}
	
	public void track()
	{		
		timeStampQueue.add(System.currentTimeMillis());
	}
	
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		
		result.append(remoteIp).append(",")
			.append(partnerId).append(",")
			.append(format).append(",")
			.append(appKey).append(",")
			.append(apiName).append(",")
			.append(readBytes).append(",")
			.append(errorCode).append(",")
			.append(subErrorCode).append(",")
			.append(localIp).append(",")
			.append(nick).append(",")
			.append(version).append(",")
			.append(signMethod);
		
		int size = timeStampQueue.size();
		
		//当前16以前的是固定的打点
		for(int i = 0 ; i < size; i++)
			result.append(",")
				.append(timeStampQueue.get(i));
		
		
		size = methodTimeQueue.size();
		for(int i = 0 ; i < size; i++)
			result.append(",")
				.append(methodTimeQueue.get(i));
		
		return result.toString();
	}
}

