package com.troylu.twenty.time;

import java.awt.event.*;
import javax.swing.Timer;

import com.troylu.twenty.frame.*;

public class TimeManager {

    public static int ONE_SEC = 1000;
    public static int ONE_MIN = ONE_SEC * 60;
    public static int ONE_HOUR = ONE_MIN * 60;

    private static boolean resting = true;

    private static long nextEnd;

    private static Timer ticker = new Timer(ONE_SEC,
        (ActionEvent e) -> {
            String clockRemaining = getClockRemaining();

            TwentyFrame.icon.setToolTip( clockRemaining );
            TwentyFrame.time.setText( clockRemaining );
        }
    );

    public static Timer screenTimer = new Timer(20 * ONE_MIN,
        (ActionEvent e) -> {
            SettingsPanel.endScreenNotif.sendNotification();
            SettingsPanel.endSoundEditor.playSound();
            ticker.stop();
        }
    );
    
    public static Timer restTimer = new Timer(20 * ONE_SEC,
        (ActionEvent e) -> {
            SettingsPanel.endRestNotif.sendNotification();
            SettingsPanel.endSoundEditor.playSound();
            ticker.stop();
        }
    );

    

    private TimeManager() {};

    public static void init() {
        screenTimer.setRepeats(false);
        restTimer.setRepeats(false);

        ticker.setRepeats(true);
        ticker.setInitialDelay(0);
    }

    public static int getScreenTime() { return screenTimer.getDelay(); }
    public static void setScreenTime(int ms) { screenTimer.setDelay(ms); }

    public static int getRestTime() { return restTimer.getDelay(); }
    public static void setRestTime(int ms) { restTimer.setDelay(ms); }

    public static boolean isResting() { return resting; }

    public static void switchTimers() {
        if (resting)    startScreenTime();
        else            startRestTime();
    }

    public static void startScreenTime() {
        
        resting = false;
        restTimer.stop();
        screenTimer.start();

        nextEnd = System.currentTimeMillis() + getScreenTime();
        ticker.start();

        SettingsPanel.startSoundEditor.playSound();
        SettingsPanel.startScreenNotif.sendNotification();

        TwentyFrame.skip.setLabel("start rest now");
        TwentyFrame.info.setLabel("currently on screen");
        TwentyFrame.currently.setText("currently on screen");
    }
    public static void startRestTime() {
        
        resting = true;
        screenTimer.stop();
        restTimer.start();

        nextEnd = System.currentTimeMillis() + getRestTime();
        ticker.start();

        SettingsPanel.startSoundEditor.playSound();
        SettingsPanel.startRestNotif.sendNotification();

        TwentyFrame.skip.setLabel("start screentime now");
        TwentyFrame.info.setLabel("currently resting");
        TwentyFrame.currently.setText("currently resting");
    }

    public static String getClockRemaining() {
        return timeToClock(Math.max(nextEnd - System.currentTimeMillis(), 0));
    }

    public static String timeToClock(long ms) {

        long hrs  = ms / ONE_HOUR;
        long mins = ms / ONE_MIN % 60;
        long secs = ms / ONE_SEC % 60;

        return  String.format("%02d",  hrs) + ":" +
                String.format("%02d", mins) + ":" +
                String.format("%02d", secs);
    }

}