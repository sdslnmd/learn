package com.sun.engineer.design.Interceptor;

public class Main {
    public static void main(String[] args) {

        InterceptorChain<CommandHandlerInterceptor> interceptorChain = new InterceptorChain<CommandHandlerInterceptor>();

        interceptorChain.addInterceptorChain(new LoggingInterceptor());

        interceptorChain.setCommandHandler(new CommandHandlerImpl());

        interceptorChain.proceed();

    }
}
