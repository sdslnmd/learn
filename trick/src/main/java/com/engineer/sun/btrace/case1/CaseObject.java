package com.engineer.sun.btrace.case1;

public class CaseObject{
 
   private static int sleepTotalTime=0; 
 
   public boolean execute(int sleepTime) throws Exception{
       System.out.println("sleep: "+sleepTime);
       sleepTotalTime+=sleepTime;
       Thread.sleep(sleepTime);
       return true;
   }
 
}
