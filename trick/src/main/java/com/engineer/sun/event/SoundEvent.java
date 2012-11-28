package com.engineer.sun.event;

import java.util.EventObject;

/**
 * User: luning.sun
 * Date: 12-11-28
 * Time: 下午1:27
 */
public class SoundEvent extends EventObject {
    private Integer note;
    private Long timestamp;
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public SoundEvent(Object source,Integer s,long time) {
        super(source);
        note = s;
        timestamp = time;
    }

    public Integer getNote() {
        return note;
    }

    public Long getTimestamp() {
        return timestamp;
    }


}
