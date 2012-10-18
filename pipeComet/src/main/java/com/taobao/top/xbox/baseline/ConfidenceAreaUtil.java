/**
 * 
 */
package com.taobao.top.xbox.baseline;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单的置信空间数据过滤工具类
 * @author fangweng
 * @email fangweng@taobao.com
 * @date 2011-3-8
 *
 */
public class ConfidenceAreaUtil {
	
	/**
	 * 根据斜率获得置信空间
	 * @param 历史采样点，需要有顺序，最后一个表示最新的数据
	 * @param 根据可信度设置得到的正态分布查表结果，默认都先用95%的置信区间，这个值为1.96
	 * @param 根据历史采样点算出合理的偏差幅度，偏差幅度*factor表示需要告警的边界
	 * @return
	 */
	public static ConfidenceArea getSlopeConfidenceArea(final double[]samples
			,double z,double factor)
	{
		if (samples == null || (samples != null && samples.length <= 1))
			throw new java.lang.RuntimeException("samples size must > 1.");
			
		ConfidenceArea area = new ConfidenceArea();
		
		List<Double> trends = new ArrayList<Double>();
		double trendsAvg = 0;
		double trendsStdd = 0;
		
		
		for(int i = 1; i < samples.length ; i++)
		{
			for(int j = 0; j < i ; j++)
			{
				trends.add((samples[i] - samples[j])/(i - j));
				trendsAvg += (samples[i] - samples[j])/(i - j);
			}
		}
		
		trendsAvg = trendsAvg/trends.size();
		
		for(double t : trends)
			trendsStdd += (t - trendsAvg)*(t - trendsAvg);
		
		trendsStdd = Math.sqrt(trendsStdd/trends.size()-1);
		
		double _trendsLow = trendsAvg - trendsStdd*z;
		double _trendsHigh = trendsAvg + trendsStdd*z;
		
		
		double _tmpTotal = 0;
		double _counter = 0;
		
		for(int j = 0; j < trends.size(); j++)
		{
			if (trends.get(j) >= _trendsLow && trends.get(j) <= _trendsHigh)
			{
				_tmpTotal += trends.get(j);
				_counter += 1;
			}
		}
		
		trendsAvg = _tmpTotal/_counter;
		
		area.setLeft(samples[samples.length-1] + trendsAvg * (1 + factor));
		area.setRight(samples[samples.length-1] + trendsAvg * (1 - factor));
		area.setValue(samples[samples.length-1] + trendsAvg);
		
		return area;
	}
	
	
	/**
	 * 获得置信区间
	 * @param 历史采样点，需要有顺序，最后一个表示最新的数据
	 * @param 根据可信度设置得到的正态分布查表结果，默认都先用95%的置信区间，这个值为1.96
	 * @param 根据历史采样点算出合理的偏差幅度，偏差幅度*factor表示需要告警的边界
	 * @return
	 */
	public static ConfidenceArea getConfidenceArea(final double[] samples,double z,double factor)
	{
		if (samples == null || (samples != null && samples.length <= 1))
			throw new java.lang.RuntimeException("samples size must > 1.");
			
		ConfidenceArea area = new ConfidenceArea();
		int n = samples.length;
		
		double avg = 0;//平均值
		double stdd = 0;//均方差
		double[] trends = new double[n-1];
		double trendsAvg = 0;
		double trendsStdd = 0;
		
		for(int i = 0; i < n ; i++)
		{
			avg += samples[i];
			
			if (i > 0)
			{
				trends[i-1] = Math.abs((samples[i] - samples[i-1]))/samples[i-1];
				trendsAvg += trends[i-1];
			}
				
		}
			
		avg = avg/n;
		trendsAvg = trendsAvg/n-1;
		
		for(double s : samples)
			stdd += (s - avg)*(s - avg);
		
		for(double t : trends)
			trendsStdd += (t - trendsAvg)*(t - trendsAvg);
		
		stdd = Math.sqrt(stdd/(n-1));
		trendsStdd = Math.sqrt(trendsStdd/(n-2));
		
		double _low = avg - stdd*z;
		double _high = avg + stdd*z;
		double _trendsLow = trendsAvg - trendsStdd*z;
		double _trendsHigh = trendsAvg + trendsStdd*z;
		
		
		double _tmpTotal = 0;
		double _counter = 0;
		
		for(int j = 0; j < trends.length; j++)
		{
			if (trends[j] >= _trendsLow && trends[j] <= _trendsHigh)
			{
				_tmpTotal += trends[j];
				_counter += 1;
			}
		}
		trendsAvg = _tmpTotal/_counter;
		
		int index = samples.length - 1;
		
		for(int i = samples.length - 1; i >= 0; i--)
		{
			if (_low <= samples[index] && samples[index] <= _high)
				break;
				
			index -= 1;
		}
		
		area.setLeft(samples[index]*Math.pow((1 + trendsAvg*factor),samples.length - index));
		area.setRight(samples[index]*Math.pow((1 - trendsAvg*factor),samples.length - index));
		
		return area;
	}
	
	public static void main(String[] args)
	{
		double[] samples = {10,12,13,20,13,15,-10,14,12,15,16,11,12,14};
		double[] samples1 = {1,4,8,6,10,14,18,16,20};
		double[] samples2 = {50,42,40,38,30,40,21,17};
		
		double[] trueSample1 = {1560417,1741632,1962142,2017684};
		double[] trueSample2 = {1788938,1826859,2080330,2098699};
		
//		double[] trueSample1 = {1930114,1646977,1646548,1591762,1560417,1741632,1962142,2017684,1999186};
//		double[] trueSample2 = {1821978,1648684,1609042,1515566,1788938,1826859,2080330,2098699,2038573};
		
		
//		System.out.println(getConfidenceArea(samples,1.96,1));
//		System.out.println(getSlopeConfidenceArea(samples,1.96,0.2));
//		
//		System.out.println(getConfidenceArea(samples1,1.96,1));
//		System.out.println(getSlopeConfidenceArea(samples1,1.96,0.2));
//		
//		System.out.println(getConfidenceArea(samples2,1.96,1));
//		System.out.println(getSlopeConfidenceArea(samples2,1.96,0.2));
		
		System.out.println(getConfidenceArea(samples,1.96,1));
		System.out.println(getSlopeConfidenceArea(samples,1.96,0.2));
		
		System.out.println("------------------------------------------");
		System.out.println(getSlopeConfidenceArea(trueSample1,1.96,0.5));
		System.out.println(getSlopeConfidenceArea(trueSample2,1.96,0.5));
	}

}
