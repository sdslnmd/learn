package com.engineer.sun.event;

/**
 * User: luning.sun
 * Date: 12-11-28
 * Time: 下午1:43
 */
public class MusicFan implements SoundListener {

    private long timeReference;

    public void setTimeReference(long i) {
        this.timeReference = i;
    }

    @Override
    public void soundReceived(SoundEvent sound) {
        long now = System.currentTimeMillis() - timeReference;
        Long soundTimestamp = sound.getTimestamp();
        long timelag = now - soundTimestamp;
        Integer note = sound.getNote();

        System.out.println("[Music fan] " +
                " Hearing note "+ note +
                " @ "+ now +
                " ( with lag = "+ timelag + " )");

        Wait.milliser(20);
    }
}
