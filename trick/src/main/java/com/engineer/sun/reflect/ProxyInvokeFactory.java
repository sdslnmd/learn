package com.engineer.sun.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * User: luning.sun
 * Date: 12-9-14
 * Time: 上午10:08
 */
public class ProxyInvokeFactory {
    private static class ProxyInvoke implements InvocationHandler {
        private Map<String, Object> objectMap = new HashMap<String, Object>();
        public ProxyInvoke(Map<String,Object> map) {
            this.objectMap=map;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            Object valueObject =null;
            if (methodName.indexOf("get") != -1) {
                String substring = methodName.substring(3);
                valueObject = objectMap.get(substring);
                if (null==valueObject&&!method.getReturnType().isInstance(0)) {
                    throw new ClassCastException(valueObject.getClass().getName()+"is not a "+method.getReturnType().getName());
                }
                return valueObject;
            } else if (methodName.indexOf("set")!=-1) {
                String fieldName = methodName.substring(methodName.indexOf("set") + 3);
                objectMap.put(fieldName, args[0]);
            } else if (methodName.startsWith("is")) {
                String fieldName = methodName.substring(methodName.indexOf("is") + 2);
                valueObject  = objectMap.get(fieldName);
                if (valueObject != null && !method.getReturnType().isInstance(valueObject))
                    throw new ClassCastException(valueObject.getClass().getName() + " is not a " + method.getReturnType().getName());
                return valueObject;
            } else {
                throw new IllegalArgumentException("Only getters, setters and boolean is-method calls are permitted for this proxy class.");
            }
            return valueObject;
        }
    }

    public static<T> T getProxy(Class<T> obj,Map<String,Object> map) {
        return (T)Proxy.newProxyInstance(ProxyInvokeFactory.class.getClassLoader(),new Class[]{obj},new ProxyInvoke(map));
    }

    public static void main(String[] args) {
        Map<String, Object> kv = new HashMap<String, Object>();
        kv.put("Name", "name");
        kv.put("Age", 12);
        IUserBean proxy = ProxyInvokeFactory.getProxy(IUserBean.class, kv);
        System.out.println(proxy.getName());
    }

}
