/**
 * 
 */
package com.taobao.top.xbox.baseline;

import java.util.List;

/**
 * @author fangweng
 * @email fangweng@taobao.com
 * @date 2011-3-9
 *
 */
public class Newtonfunction {
	public static Number newton(Number x,java.util.List<Point> list){

		if(list.size()==1) return list.get(0).getY();
		
		Number result=difference(list);
		
		list.remove((list.size()-1));
		
		result=result.doubleValue()*xPart(x, list).doubleValue();
		
		
		result=newton(x,list).doubleValue()+result.doubleValue();
		
		return result;
	}
	
	
	public static Number xPart(Number x,List<Point> l){
		double result=1;
		for(Point p:l){
			result*=(x.doubleValue()-p.getX().doubleValue());
		}
		return result;
	}
	
	
	public static Number difference(List<Point> l){
		if(l.size()==2) return difference(l.get(0), l.get(1));
		java.util.List<Point> temp0=new java.util.ArrayList<Point>();
		java.util.List<Point> temp1=new java.util.ArrayList<Point>();
		for(int i=0;i<l.size()-2;i++){
			temp0.add(l.get(i));
			temp1.add(l.get(i));
		}
		
		Point p0=l.get(l.size()-1);
		temp0.add(p0);
		
		Point p1=l.get(l.size()-2);
		temp1.add(p1);

		return (difference(temp0).doubleValue()-difference(temp1).doubleValue())/(p0.getX().doubleValue()-p1.getX().doubleValue());	
	}
	
	private static Number difference(Point p0,Point p1){
		return (p1.getY().doubleValue()-p0.getY().doubleValue())/(p1.getX().doubleValue()-p0.getX().doubleValue());
	}
	
	

	public static void main(String[] args) {

		java.util.List<Point> list=new java.util.ArrayList<Point>();
		list.add(new Point(1,2));
		list.add(new Point(2,4));
		list.add(new Point(3,6));
		list.add(new Point(4,7));
		list.add(new Point(5,10));
		list.add(new Point(6,12));
		list.add(new Point(7,14));
		
		Number num = newton(8,list);
		System.out.println(num);

	}
}

class Point{
	private Number x;
	private Number y;
	
	public Point(){
		
	}
	
	public Point(Number x,Number y){
		this.x=x;
		this.y=y;
	}
	
	public final Number getX() {
		return x;
	}
	public final void setX(Number x) {
		this.x = x;
	}
	public final Number getY() {
		return y;
	}
	public final void setY(Number y) {
		this.y = y;
	}	
	
}
