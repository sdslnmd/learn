/**
 * 
 */
package com.taobao.top.xbox.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Http请求工具类
 * 
 * @author fangweng
 * 
 */
public class HttpRequestUtil
{

	private static final Log logger = LogFactory.getLog(HttpRequestUtil.class);

	private static final String PARAMETER_SIGN = "sign";
	/**
	 * Multipart类型判断符
	 */
	private final static String MULTIPART = "multipart/form-data";


	/**
	 * 采用java原生方式发送Http请求
	 * 
	 * @param 目标url
	 * @param 发送内容
	 * @param 需要上传的消息头
	 * @param Http方法
	 * @param 内容类型
	 * @return
	 */
	public static byte[] sendRequestV2(String url, String content,
			Map<String, String> headers, String method, String contenttype)
	{
		byte[] result = null;

		try
		{
			HttpURLConnection httpConn = (HttpURLConnection) new URL(url)
					.openConnection();

			// Should never cache GData requests/responses
			httpConn.setUseCaches(false);

			// Always follow redirects
			httpConn.setInstanceFollowRedirects(true);

			httpConn.setRequestMethod(method);
			httpConn.setRequestProperty("Content-Type", contenttype);
			httpConn.setRequestProperty("Accept-Encoding", "gzip");

			if (headers != null && headers.size() > 0)
			{
				Iterator<String> keys = headers.keySet().iterator();

				while (keys.hasNext())
				{
					String key = keys.next();
					httpConn.setRequestProperty(key, headers.get(key));
				}
			}

			httpConn.setDoOutput(true);

			if (content != null)
				httpConn.getOutputStream().write(content.getBytes("UTF-8"));

			System.setProperty("http.strictPostRedirect", "true");
			httpConn.connect();

			ByteArrayOutputStream bout = new ByteArrayOutputStream();

			try
			{
				InputStream in = httpConn.getInputStream();

				byte[] buf = new byte[500];
				int count = 0;

				while ((count = in.read(buf)) > 0)
				{
					bout.write(buf, 0, count);
				}

				result = bout.toByteArray();
			} catch (Exception ex)
			{
				ex.printStackTrace();
			} finally
			{
				if (bout != null)
					bout.close();
			}

			System.clearProperty("http.strictPostRedirect");

		} catch (Exception e)
		{
			logger.error(e, e);
		}

		return result;
	}

	/**
	 * 使用第三方开源httpclient发送http请求
	 * 
	 * @param 目标地址
	 * @param 参数
	 * @param 是否需要签名
	 *            ，如果为null，表示不需要签名
	 * @param http请求的类型
	 *            ：get，post
	 * @param 是否需要有附件上传
	 *            ，如果有必须是post类型的http请求
	 * @param 编码
	 * @param 签名算法
	 *            ，Hmac和md5
	 * @param 是否有数据需要放在header中上传
	 * @param 请求内容类型
	 *            ，可以为null
	 * @return
	 */
	public static byte[] sendRequestV1(String url, Map<String, Object> params,
			String secretCode, String method, Map<String, String> files,
			String encoding, String signMethod, Map<String, String> headers,
			String contentType)
	{
		HttpClient client = new HttpClient();
		byte[] result = null;

		if (method.equalsIgnoreCase("get"))
		{
			GetMethod getMethod = new GetMethod(url);

			if (contentType == null || contentType.equals(""))
				getMethod.setRequestHeader("Content-Type",
						"application/x-www-form-urlencoded;charset=UTF-8");
			else
				getMethod.setRequestHeader("Content-Type", contentType);

			if (headers != null && headers.size() > 0)
			{
				Iterator<String> keys = headers.keySet().iterator();

				while (keys.hasNext())
				{
					String key = keys.next();
					getMethod.setRequestHeader(key, headers.get(key));
				}
			}

			try
			{
				NameValuePair[] getData;

				if (params != null)
				{
					if (secretCode == null)
						getData = new NameValuePair[params.size()];
					else
						getData = new NameValuePair[params.size() + 1];

					Iterator<?> iters = params.keySet().iterator();
					int i = 0;

					while (iters.hasNext())
					{
						String key = (String) iters.next();
						getData[i] = new NameValuePair(key, params.get(key)
								.toString());
						i++;
					}

					if (secretCode != null)
					{
						boolean isHMac = false;

						if (signMethod != null
								&& signMethod.equalsIgnoreCase("hmac"))
							isHMac = true;

						String sign = EncryptUtil.signature2(params, secretCode,"md5".equalsIgnoreCase(signMethod),
								isHMac, PARAMETER_SIGN);
						getData[i] = new NameValuePair(PARAMETER_SIGN, sign);
					}

					getMethod.setQueryString(getData);
				}

				client.executeMethod(getMethod);

				ByteArrayOutputStream bout = new ByteArrayOutputStream();

				try
				{
					InputStream in = getMethod.getResponseBodyAsStream();

					byte[] buf = new byte[500];
					int count = 0;

					while ((count = in.read(buf)) > 0)
					{
						bout.write(buf, 0, count);
					}
					
					result = bout.toByteArray();
					
				} 
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
				finally
				{
					if (bout != null)
						bout.close();
				}
				
			}
			catch (Exception ex)
			{
				logger.error(ex, ex);
			}
			finally
			{
				if (getMethod != null)
					getMethod.releaseConnection();
			}
		}// end get

		if (method.equalsIgnoreCase("post"))
		{
			PostMethod postMethod = new PostMethod(url);

			if (headers != null && headers.size() > 0)
			{
				Iterator<String> keys = headers.keySet().iterator();

				while (keys.hasNext())
				{
					String key = keys.next();
					postMethod.setRequestHeader(key, headers.get(key));
				}
			}

			try
			{
				if (contentType == null)
				{
					// 带附件的请求处理
					if (files != null && files.size() > 0)
					{
						Part[] parts;

						if (secretCode == null)
							parts = new Part[params.size() + files.size()];
						else
							parts = new Part[params.size() + 1 + files.size()];

						Iterator<?> iters = params.keySet().iterator();
						int i = 0;

						while (iters.hasNext())
						{
							String key = (String) iters.next();
							parts[i] = new StringPart(key, params.get(key)
									.toString(), "UTF-8");
							i++;
						}

						if (secretCode != null)
						{
							boolean isHMac = false;

							if (signMethod != null
									&& signMethod.equalsIgnoreCase("hmac"))
								isHMac = true;

							String sign = EncryptUtil.signature(params,
									secretCode, isHMac, PARAMETER_SIGN);
							parts[i] = new StringPart(PARAMETER_SIGN, sign);
							i++;
						}

						iters = files.keySet().iterator();
						while (iters.hasNext())
						{
							String key = (String) iters.next();
							if (files.get(key).toString().startsWith("http://"))
							{
								InputStream bin = null;
								ByteArrayOutputStream bout = new ByteArrayOutputStream();

								try
								{
									URL fileurl = new URL(files.get(key)
											.toString());
									bin = fileurl.openStream();

									byte[] buf = new byte[500];
									int count = 0;

									while ((count = bin.read(buf)) > 0)
									{
										bout.write(buf, 0, count);
									}

									parts[i] = new FilePart(
											key,
											new ByteArrayPartSource(
													fileurl
															.getFile()
															.substring(
																	fileurl
																			.getFile()
																			.lastIndexOf(
																					"/") + 1),
													bout.toByteArray()));

								}
								catch (Exception ex)
								{
									logger.error(ex, ex);
								}
								finally
								{
									if (bin != null)
										bin.close();

									if (bout != null)
										bout.close();
								}
							} 
							else
								parts[i] = new FilePart(key, new File(files
										.get(key).toString()));
							i++;
						}
						postMethod.setRequestEntity(new MultipartRequestEntity(
								parts, postMethod.getParams()));
					}
					else
					{
						NameValuePair[] postData;

						if (params != null)
						{
							if (secretCode == null)
								postData = new NameValuePair[params.size()];
							else
								postData = new NameValuePair[params.size() + 1];

							Iterator<?> iters = params.keySet().iterator();
							int i = 0;

							while (iters.hasNext())
							{
								String key = (String) iters.next();
								postData[i] = new NameValuePair(key, params
										.get(key).toString());
								i++;
							}

							if (secretCode != null)
							{
								boolean isHMac = false;

								if (signMethod != null
										&& signMethod.equalsIgnoreCase("hmac"))
									isHMac = true;

								String sign = EncryptUtil.signature(params,
										secretCode, isHMac, PARAMETER_SIGN);
								postData[i] = new NameValuePair(PARAMETER_SIGN,
										sign);
							}

							postMethod.setRequestBody(postData);
						}

						if (contentType == null || contentType.equals(""))
							postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
					}
				} 
				else
				{
					String content = (String) params.get(params.keySet()
							.iterator().next());
					RequestEntity entiry = new StringRequestEntity(content,
							contentType, "UTF-8");
					postMethod.setRequestEntity(entiry);
				}

				client.executeMethod(postMethod);

				ByteArrayOutputStream bout = new ByteArrayOutputStream();

				try
				{
					InputStream in = postMethod.getResponseBodyAsStream();

					byte[] buf = new byte[500];
					int count = 0;

					while ((count = in.read(buf)) > 0)
					{
						bout.write(buf, 0, count);
					}

					result = bout.toByteArray();
				} 
				catch (Exception ex)
				{
					logger.error(ex, ex);
				}
				finally
				{
					if (bout != null)
						bout.close();
				}
			}
			catch (Exception e)
			{
				logger.error(e, e);
			} 
			finally
			{
				if (postMethod != null)
					postMethod.releaseConnection();
			}

		}// end post
		
		return result;
	}
	
	/**
	 * 判断是否是Multipart的类型请求
	 * 
	 * @param request
	 * @return
	 */
	public static final boolean isMultipartContent(HttpServletRequest request) {
		if (!"post".equalsIgnoreCase(request.getMethod())) {
			return false;
		}
		String contentType = request.getContentType();
		if (contentType == null) {
			return false;
		}
		if (contentType.toLowerCase().startsWith(MULTIPART)) {
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
