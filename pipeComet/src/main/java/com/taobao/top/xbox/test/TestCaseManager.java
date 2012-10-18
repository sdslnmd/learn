/**
 * 
 */
package com.taobao.top.xbox.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 测试管理类
 * @author fangweng
 *
 */
public class TestCaseManager
{
	private static final Log logger = LogFactory.getLog(TestCaseManager.class);
	
	/**
	 * 存储测试类的池，value是希望出错以后返回的错误信息
	 */
	private ConcurrentMap<ITopTestCase,String> testCasePool;
	
	public TestCaseManager()
	{
		testCasePool = new ConcurrentHashMap<ITopTestCase,String>();
	}
	
	/**
	 * 注册一个需要测试的服务
	 * @param 测试类
	 * @param 结果
	 */
	public void register(ITopTestCase testCase,String result)
	{
		testCasePool.put(testCase, result);
	}
	
	public static String getStrFromTestResults(List<TestResult> results)
	{
		StringBuilder result = new StringBuilder();
		
		if (results != null && results.size() > 0)
		{
			for(TestResult r : results)
			{
				if (r.isSuccess)
					result.append("success").append("\r\n");
				else
					result.append(r.getDesc()).append("\r\n");
			}
		}
		
		return result.toString();
	}
	
	/**
	 * 
	 */
	public List<TestResult> doBatchTest()
	{
		List<TestResult> result = new ArrayList<TestResult>();
		
		if (testCasePool.size() == 0)
			return result;
		
		try
		{
			int threadcount = testCasePool.size();
			
			CountDownLatch startSignal = new CountDownLatch(1);
		    CountDownLatch doneSignal = new CountDownLatch(threadcount);
		    Iterator<ITopTestCase> iter = testCasePool.keySet().iterator();
		    
		    for(int i = 0 ; i < threadcount ; i++)
			{
		    	ITopTestCase testCase = iter.next();
		    	String desc = testCasePool.get(testCase);
		    	Thread job = new Thread(new Task(String.valueOf(i),result,testCase,
						desc,startSignal,doneSignal));
		    	job.setDaemon(true);
		    	job.start();
			}
			
		    startSignal.countDown();
			
			doneSignal.await();
			
		}
		catch(Exception ex)
		{
			logger.error(ex,ex);
		}

		return result;
	}
	
	class Task implements java.lang.Runnable
	{

		String name;
		List<TestResult> results;
		CountDownLatch start;
		CountDownLatch done;
		ITopTestCase testcase;
		String desc;
		
		public Task(String n,List<TestResult> r,ITopTestCase testcase,String desc
				,CountDownLatch start,CountDownLatch done)
		{
			name = n;
			results = r;
			this.start = start;
			this.done = done;
			this.testcase = testcase;
			this.desc = desc;
		}
		
		public void run() 
		{
			TestResult result = new TestResult();
			result.setDesc(desc);
			
			try
			{
				start.await();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
			long time = System.currentTimeMillis();
			
			try
			{
				result.setSuccess(testcase.doTest());
			}
			catch(Exception ex)
			{
				result.setException(ex);
				result.setSuccess(false);
			}
			
			result.setTimeConsume(time - System.currentTimeMillis());
			
			results.add(result);
			System.out.println("finish");
			done.countDown();
		}
		
	}
	
}
