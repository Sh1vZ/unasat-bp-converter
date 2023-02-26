package com.unasat.bpconconverter.audioplayer;

import javax.sound.sampled.*;

public class MorseCodePlayer {
    private static final int SAMPLE_RATE = 8000;
    private static final int DOT_DURATION = 100; // in milliseconds
    private static final int DASH_DURATION = 2 * DOT_DURATION;
    private static final int LETTER_GAP_DURATION = DOT_DURATION;
    private static final int WORD_GAP_DURATION = 3 * DOT_DURATION;
    private static final byte[] DOT_SOUND = generateBeep(DOT_DURATION);
    private static final byte[] DASH_SOUND = generateBeep(DASH_DURATION);
    private static final byte[] LETTER_GAP_SOUND = generateSilence(LETTER_GAP_DURATION);
    private static final byte[] WORD_GAP_SOUND = generateSilence(WORD_GAP_DURATION);

    private static SourceDataLine line = null;
    public static boolean isPlaying=false;
    public static void play(String message) throws LineUnavailableException, InterruptedException {
        AudioFormat format = new AudioFormat(SAMPLE_RATE, 8, 1, true, true);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();
        isPlaying=true;
        String formattedMsg = message.replace("/", " ");
        for (char c : formattedMsg.toCharArray()) {
            switch (c) {
                case ' ':
                    playSound(WORD_GAP_SOUND, line);
                    break;
                case '.':
                    playSound(DOT_SOUND, line);
                    playSound(LETTER_GAP_SOUND, line);
                    break;
                case '-':
                    playSound(DASH_SOUND, line);
                    playSound(LETTER_GAP_SOUND, line);
                    break;
            }
        }
        line.drain();
        line.stop();
        line.close();
        isPlaying=false;
    }

    private static void playSound(byte[] sound, SourceDataLine line) throws InterruptedException {
        line.write(sound, 0, sound.length);
        Thread.sleep(10);
    }

    private static byte[] generateBeep(int duration) {
        int numSamples = duration * SAMPLE_RATE / 1000;
        byte[] sound = new byte[numSamples];
        for (int i = 0; i < numSamples; i++) {
            double angle = 2.0 * Math.PI * i / (SAMPLE_RATE / 440.0);
            sound[i] = (byte) (Math.sin(angle) * 127.0);
        }
        return sound;
    }

    private static byte[] generateSilence(int duration) {
        int numSamples = duration * SAMPLE_RATE / 1000;
        return new byte[numSamples];
    }
    public static void stop(){
        line.stop();
        isPlaying=false;
    }
}
