package com.engineer.sun.event;

/**
 * User: luning.sun
 * Date: 12-11-28
 * Time: 下午1:49
 */
public class Main {
    public static void main(String[] args) {
        SoundMaker concert = new SoundMaker();
        MusicFan musicFan = new MusicFan();

        concert.addlistener(musicFan);

        long timeReference = System.currentTimeMillis();

        concert.setTimeReference(timeReference);
        musicFan.setTimeReference(timeReference);


        System.out.printf("-------");
        concert.playConcert(10);

    }
}
