package com.engineer.sun.gc;

public class MinorGC {

	private static  int _1MB = 1024 * 1024;
	public static void main(String[] args) {
//		new MinorGC().testMinorGC();
//		new MinorGC().testPretenureSizeThreshold();
		new MinorGC().testTenuringDistribution();
//		new MinorGC().testTenuringThreshold2();
//		new MinorGC().testHandlePromotion();
	}
	/**
	 * -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8 -XX:+PrintGCDetails
	 */
	 void testMinorGC(){
		byte[] b1,b2,b3,b4;
		b1 = new byte[2 * _1MB];
		b2 = new byte[2 * _1MB];
		b3 = new byte[2 * _1MB];
		b4 = new byte[4 * _1MB];
	}
	
	 /**
	  * -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8 -XX:+PrintGCDetails -XX:PretenureSizeThreshold=3145728
	  */
	 void testPretenureSizeThreshold(){
		 byte[] b;
		 b = new byte[4*_1MB];
	 }
	 /**
	  * -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=1 -XX:+PrintTenuringDistribution -XX:+PrintGCDetails
	  */
	 void testTenuringDistribution(){
		 byte[] a1,a2,a3;
		 a1 = new byte[4*_1MB];
		 a2 = new byte[4*_1MB];
		 a3 = new byte[4*_1MB];
//		 a3 = null;
//		 a3 = new byte[4*_1MB];
	 }
	 
	 /**
	  * 动态对象年龄判定
	  * -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=15  -XX:+PrintTenuringDistribution -XX:+PrintGCDetails
	  */
	 void testTenuringThreshold2() {
		 byte[] a1,a2,a3,a4;
		 a1 = new byte[_1MB/4];
		 //a1+a2大于survivor空间的一半
		 a2 = new byte[_1MB/4];
		 a3 = new byte[4*_1MB];
		 a4 = null;
		 a4 = new byte[4*_1MB];
	 }
	 
	 /**
	  * 空间分配担保
	  * -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8 -XX:-HandlePromotionFailure
	  */
	 void testHandlePromotion() {
		 byte[] a1,a2,a3,a4,a5,a6,a7;
		 a1 = new byte[2 * _1MB];
		 a2 = new byte[2 * _1MB];
		 a3 = new byte[2 * _1MB];
		 a1 = null;
		 a4 = new byte[2 * _1MB];
		 a5 = new byte[2 * _1MB];
		 a6 = new byte[2 * _1MB];
		 a4 = null;
		 a5 = null;
		 a6 = null;
		 a7 = new byte[2 * _1MB];
	 }
}
