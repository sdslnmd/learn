package com.sun.engineer.design.Interceptor;

public interface CommandHandlerInterceptor {
    void handle(InterceptorChain interceptorChain);
}
