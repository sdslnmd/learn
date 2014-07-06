package com.sun.engineer.design.Interceptor;

public class CommandHandlerImpl implements CommandHandler {
    @Override
    public void handle() {
        System.out.println("command handle");
    }
}