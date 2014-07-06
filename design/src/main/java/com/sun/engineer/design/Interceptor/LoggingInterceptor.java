package com.sun.engineer.design.Interceptor;

public class LoggingInterceptor implements CommandHandlerInterceptor {

    @Override
    public void handle(InterceptorChain interceptorChain) {

        System.out.println("log start");

        interceptorChain.proceed();

        System.out.println("log end");

    }
}
