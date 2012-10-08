package com.engineer.sun.guava;

import com.google.common.eventbus.Subscribe;

/**
 * User: sunluning
 * Date: 12-10-8 下午9:26
 */
public class EventListener {
    public int lastMessage = 0;

    @Subscribe
    public void listen(OurTestEvent event) {
        lastMessage = event.getMessage();
    }

    public int getLastMessage() {
        return lastMessage;
    }
}
