package com.sun.engineer.design.factory;

/**
 * User: sunluning
 * Date: 12-10-24 下午10:34
 */
public class RuleFactory {

    public static RuleFactory make(String rule) {
        return new RuleFactory(rule);
    }

    private RuleFactory(String rule) {
        readConfig(rule);
    }

    private void readConfig(String rule) {
        System.out.println("load config "+rule);
    }

    public void sayHello() {
        System.out.println("hello");
    }




}
