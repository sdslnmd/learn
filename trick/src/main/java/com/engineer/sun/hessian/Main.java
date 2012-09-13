package com.engineer.sun.hessian;

import com.caucho.hessian.client.HessianProxyFactory;

import java.net.MalformedURLException;

/**
 * User: luning.sun
 * Date: 12-9-13
 * Time: 上午11:26
 */
public class Main {
    public static void main(String[] args) throws MalformedURLException {
        String url = "http://hessian.caucho.com/test/test";

        HessianProxyFactory factory = new HessianProxyFactory();
        BasicAPI basic = (BasicAPI) factory.create(BasicAPI.class, url);

        System.out.println("hello(): " + basic.hello());
    }
}
