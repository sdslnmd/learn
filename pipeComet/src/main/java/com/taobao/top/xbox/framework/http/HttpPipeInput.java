/**
 * 
 */
package com.taobao.top.xbox.framework.http;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.taobao.top.xbox.framework.IPipeInput;
import com.taobao.top.xbox.framework.http.exception.FileNumInvalidException;
import com.taobao.top.xbox.framework.http.exception.FileSizeInvalidException;
import com.taobao.top.xbox.framework.http.exception.FileTypeInvalidException;

/**
 * 这个是管道输入的结构体，
 * 对Mutlipart采用lazyparse的方式,普通的post暂时不做处理
 * 线程不安全
 * 
 * @author fangweng
 *
 */
public class HttpPipeInput implements IPipeInput
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3680094823805085907L;

	private static final Log logger = LogFactory.getLog(HttpPipeInput.class);
	
	private final static String MULTIPART = "multipart/form-data";
	
	private String httpMethod;
	private String httpContentType;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	/**
	 * 当前请求如果已经被提交或者超时，资源被回收，此标识为true
	 */
	boolean recycle = false;
	
	/**
	 * 保证输出时需要先获得锁，这里先暂时不做复杂，交由外部线程来排队等待
	 */
	private ReentrantLock writeLock;
	/**
	 * 请求所有子任务当前执行情况，当计数为0时，在dopipes最终执行时回收所有资源。
	 */
	private AtomicInteger taskCounter;
	

	public HttpPipeInput(HttpServletRequest req,HttpServletResponse resp)
	{
		request = req;
		response = resp;
		
		this.httpMethod = request.getMethod();
		this.httpContentType = request.getContentType();
		
		writeLock = new ReentrantLock();
		taskCounter = new AtomicInteger(0);
	}
	
	
	
	public boolean isRecycle() {
		return recycle;
	}



	public void setRecycle(boolean recycle) {
		this.recycle = recycle;
	}



	public AtomicInteger getTaskCounter() {
		return taskCounter;
	}



	public HttpServletRequest getRequest(){
		if (isRecycle())
			throw new java.lang.RuntimeException("The request is recyle, now request is invisible.");
		
		return request;
	}
	
	/**
     * @deprecated use {@link #flushToResponse} instead.
     */
	@Deprecated
	public HttpServletResponse getResponse(){
		if (isRecycle())
			throw new java.lang.RuntimeException("The request is recyle, now response is invisible.");
		
		return response;
	}
	
	public void flushToResponse(String content) throws IOException
	{
		if (isRecycle())
			throw new java.lang.RuntimeException("The request is recyle, now response is invisible.");
		
		try
		{
			writeLock.lock();	
			response.getWriter().write(content);	
			response.flushBuffer();
		}
		finally
		{
			writeLock.unlock();
		}
	}
	
	public void flushToResponse(char[] content) throws IOException
	{
		if (isRecycle())
			throw new java.lang.RuntimeException("The request is recyle, now response is invisible.");
		
		
		try
		{
			writeLock.lock();	
			response.getWriter().write(content);	
			response.flushBuffer();
		}
		finally
		{
			writeLock.unlock();
		}
	}
	
	public void flushToResponse(byte[] content) throws IOException
	{
		if (isRecycle())
			throw new java.lang.RuntimeException("The request is recyle, now response is invisible.");
		
		try
		{
			writeLock.lock();	
			response.getOutputStream().write(content);	
			response.flushBuffer();
		}
		finally
		{
			writeLock.unlock();
		}		
	}
	
	@Override
	public Object getParameter(String key)
	{
		boolean isMultipart = isMultipartContent();
		Object value = null;
		
		if (!isMultipart)
		{
			if (isRecycle())
				throw new java.lang.RuntimeException("The request is recyle, now request is invisible.");
			
			value = request.getParameter(key);
		}
		else
		{						
			try
			{
				value = LazyParser.getParameter(request, key);
			}
			catch(Exception ex)
			{
				logger.error("getParameter error!",ex);

				if (ex instanceof FileTypeInvalidException
						|| ex instanceof FileSizeInvalidException
						|| ex instanceof FileNumInvalidException)
				{
					throw new java.lang.RuntimeException(ex);
				}
			}
		}
		
		return value;
	}
	

	/**
	 * 取到上传文件内容的二进制数据
	 * @return
	 */
	public List<FileItem> getFileData() {
		if(!isMultipartContent()){
			return null;
		}
		List<FileItem> fileList = null;
		try {
			fileList = LazyParser.getFileItemsFromRequest(request);
		} catch (Exception e) {
			logger.error("deal with file upload error:", e);
			return null;
		}

		return fileList;
	}
	
	
	/**
	 * 取到所有请求的参数key集合（只取客户端请求的参数集合）
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Set<String> getParameterNames(){
		
		if (isRecycle())
			throw new java.lang.RuntimeException("The request is recyle, now response is invisible.");
		
		
		boolean isMultipart = isMultipartContent();
		
		if (!isMultipart){
			return request.getParameterMap().keySet();
		}else{
			try{
				Map<String,Object> paramters = new HashMap<String,Object>(); 
				Set<String> keys = LazyParser.getParameterNames(request);
				for (String string : keys) {
					Object o = LazyParser.getParameter(request, string);
					
					if(o != null)
						if (!(o instanceof FileItem && ((FileItem)o).isFile()))
					{
						paramters.put(string, o);
					}
				}
				return paramters.keySet();
			}
			catch(Exception ex){
				logger.error("getParameterNames error!",ex);
			}
		}
		return new HashSet<String>();
	}
	
	/**
	 * 取到key对应的值，不同的应用场景下对该值是否需要trim要区别对待.
	 * 大多数情况都需要trim，以下几种典型情况需要区别对待：
	 * 1. 在做签名校验的时候，签名之外的各参数都不能trim
	 * 2. 后传参数到各isp的时候，需要根据参数类型判断是否需要trim
	 * 3. 1.0的情况不需要trim.(因为之前都不trim)
	 * @param key
	 * @param needTrim 是否需要对该值进行trim
	 * @return
	 */
	public String getString(String key, boolean needTrim){
		if(key == null){
			return null;
		}
		Object v = getParameter(key);
		if(v == null){
			return null;
		}
		if(v instanceof FileItem){
			String value = ((FileItem)v).getValue();
			if(value == null){
				value = "";
			}
			return needTrim ? StringUtils.trim(value) : value;//((FileItem)v).getValue();
		}
		String str = null;
		if (v.getClass().equals(String.class)) {
			str = (String) v;
		} else if (v.getClass().isArray()) {
			String[] array = (String[]) v;
			str = (String) array[0];
		}
		return needTrim ? StringUtils.trim(str) : str;
	}
	
	
	public String getHttpMethod()
	{
		if (httpMethod == null)
			httpMethod = request.getMethod();
		
		return httpMethod;
	}
	
	public void setHttpMethod(String httpMethod)
	{
		this.httpMethod = httpMethod;
	}

	public String getStringTrimToNull(String key) {
		return StringUtils.trimToNull(getString(key, true));
	}

	/**
	 * 按逗号分割
	 */
	public String[] getStringArray(String key)
			throws NumberFormatException {
		String list = getString(key, true);
		if (StringUtils.isEmpty(list))
			return null;
		String[] array = StringUtils.split(list, ',');
		return array;
	}

	public Integer getInteger(String key) throws NumberFormatException {
		String value = getString(key, true);
		Integer rs = null;
		if (StringUtils.isNotEmpty(value)) {
			rs = Integer.parseInt(value);
		}
		return rs;
	}

	public Boolean getBoolean(String key) throws NumberFormatException {
		String value = getString(key, true);
		Boolean rs = null;
		if (StringUtils.isNotEmpty(value)) {
			rs = Boolean.parseBoolean(value);
		}
		return rs;
	}

	public Date getDate(String key) throws NumberFormatException, ParseException {
		String value = getString(key, true);
		Date rs = null;
		if (StringUtils.isNotEmpty(value)) {
			if(value.indexOf("-")!=-1){
				rs = ymdOrYmdhms2Date(value);
			}else{
				rs = new Date(Long.parseLong(value));
			}

		}
		return rs;
	}

	/**
	 * 得到所有参数key和value的列表字符串，便于debug，分析问题使用
	 * NOTE: 不太应该作为业务逻辑代码使用 
	 * 大多数时候不会被调用，只有在debug时候使用，现阶段不太需要优先
	 * @return
	 */
	public String getParamStr() {
		StringBuilder tradeInfo = new StringBuilder();
		
		String[] paramNames = getParameterNames().toArray(
				ArrayUtils.EMPTY_STRING_ARRAY);
		Arrays.sort(paramNames);
		tradeInfo.append("params:{");
		for(String paramName : paramNames) {
			tradeInfo.append(paramName).append("=").append(
					getString(paramName, true)).append(";");
		}
		tradeInfo.append("}");
		return tradeInfo.toString();
	}
	
	public boolean isMultipartContent()
	{
		if (!"post".equalsIgnoreCase(this.httpMethod)) {
			return false;
		}
	
		if (httpContentType != null && httpContentType.toLowerCase().startsWith(MULTIPART)) {
			return true;
		}
		
		return false;
	}
	
	public static Date ymdOrYmdhms2Date(String str) throws ParseException {
		if (str == null)
			return null;

		Date date = null;
		if (str.length() == 10) {
			DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			date = f.parse(str);
		} else if (str.length() == 19) {
			DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = f.parse(str);
		} else if (str.length() == 23) {
			DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			date = f.parse(str);
		} else {
			throw new ParseException("error date foramt: " + str, 0);
		}
		return date;
	}
	
}
