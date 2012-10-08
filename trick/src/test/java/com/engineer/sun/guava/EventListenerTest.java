package com.engineer.sun.guava;

import com.google.common.eventbus.EventBus;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.hamcrest.Matcher.*;

import static org.junit.Assert.assertThat;

/**
 * User: sunluning
 * Date: 12-10-8 下午9:45
 */
public class EventListenerTest {
    @Test
    public void shouldReceiveEvent() throws Exception {

        // given
        EventBus eventBus = new EventBus("test");
        EventListener listener = new EventListener();

        eventBus.register(listener);

        // when
        eventBus.post(new OurTestEvent(200));

        // then
        assertThat(listener.getLastMessage(), Is.is(200));
    }


}
