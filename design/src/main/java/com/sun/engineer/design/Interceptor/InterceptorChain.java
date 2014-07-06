package com.sun.engineer.design.Interceptor;

import java.util.ArrayList;
import java.util.List;

public class InterceptorChain<E extends CommandHandlerInterceptor> {

    private List<E> interceptorChain = new ArrayList<E>();

    private CommandHandler commandHandler;

    public void proceed() {
        for (CommandHandlerInterceptor chain : interceptorChain) {
            chain.handle(this);
        }

        commandHandler.handle();
    }

    public void addInterceptorChain(E e) {
        interceptorChain.add(e);
    }

    public void setCommandHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

}
