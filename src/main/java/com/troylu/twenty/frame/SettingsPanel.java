package com.troylu.twenty.frame;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.troylu.twenty.settings.*;
import com.troylu.twenty.time.TimeManager;

public class SettingsPanel extends JPanel {

    public static NotificationEditor startScreenNotif;
    public static NotificationEditor endScreenNotif;
    public static NotificationEditor startRestNotif;
    public static NotificationEditor endRestNotif;

    public static SoundEditor startSoundEditor;
    public static SoundEditor endSoundEditor;

    public SettingsPanel() {
        SettingsComponent.clearAllSettings();

        setLayout(new BorderLayout());

        ListPanel inner = new ListPanel(Component.LEFT_ALIGNMENT);

        inner.add(new TimeEditor("screen time", TimeManager.screenTimer, 20 * TimeManager.ONE_MIN));
        inner.add(new TimeEditor("rest time", TimeManager.restTimer, 20 * TimeManager.ONE_SEC));

        startScreenNotif = new NotificationEditor("notification: screen time start", "started screen time", "posture check!");
        endScreenNotif = new NotificationEditor("notification: screen time end", "screen time over", "click to start rest");
        startRestNotif = new NotificationEditor("notification: rest time start", "started rest", "look 20 ft away!");
        endRestNotif = new NotificationEditor("notification: rest time end", "rest over", "click to start screen time");
        inner.add(startScreenNotif);
        inner.add(endScreenNotif);
        inner.add(startRestNotif);
        inner.add(endRestNotif);

        startSoundEditor = new SoundEditor("sound played: timer started", "src\\main\\resources\\default start.mp3");
        endSoundEditor = new SoundEditor("sound played: timer ended", "src\\main\\resources\\default end.mp3");
        inner.add(startSoundEditor);
        inner.add(endSoundEditor);

        this.add(new JScrollPane(inner));
        
        JButton applyButton = new JButton("apply");
        applyButton.addActionListener((ActionEvent e) -> {
            
            SettingsComponent.applyAllData();
            SettingsComponent.writeAllData();

        });

        JPanel applyPanel = new JPanel();
        applyPanel.add(applyButton);
        this.add(applyPanel, BorderLayout.SOUTH);

        SettingsComponent.readAllData();
        SettingsComponent.applyAllData();
    }

}
