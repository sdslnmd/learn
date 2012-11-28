package com.engineer.sun.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * User: luning.sun
 * Date: 12-11-28
 * Time: 下午1:31
 */
public class SoundMaker {
    private Integer sound;
    private List listeners;
    private Random rgen;
    private long timeReference;//time is counted from this reference

    public SoundMaker() {
        listeners = new ArrayList();
        rgen = new Random();
        sound = new Integer(0);
        timeReference = 0;
    }

    public void setTimeReference(long i) {
        timeReference = i;
    }

    public synchronized void addlistener(SoundListener s1) {
        listeners.add(s1);
    }

    public synchronized void removeListener(SoundListener s1) {
        listeners.remove(s1);
    }

    public synchronized void playNote() {
        sound = rgen.nextInt(1000);
        long now = System.currentTimeMillis() - timeReference;
        System.out.println("[soundMarker] playing note " + sound + "@" + now);
        fireSoundEvent(now);
    }

    private synchronized void fireSoundEvent(long time) {
        SoundEvent soundEvent = new SoundEvent(this, sound, time);
        Iterator iterator = listeners.iterator();
        while (iterator.hasNext()) {
            SoundListener next = (SoundListener)iterator.next();
            next.soundReceived(soundEvent);
        }
    }

    public synchronized void playConcert(int nbNotes) {
        for (int i = 0; i < nbNotes; i++) {
            playNote();
        }
    }
}
