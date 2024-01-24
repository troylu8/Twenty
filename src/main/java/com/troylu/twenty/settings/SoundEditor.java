package com.troylu.twenty.settings;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import javafx.scene.media.*;

import com.troylu.twenty.frame.ListPanel;

public class SoundEditor extends SettingsComponent {

    private ListPanel inner;
    private JCheckBox enableBox;
    private JLabel soundDisplay;

    private MediaPlayer player;

    String soundpath;
    private boolean enabled;

    public SoundEditor(String label, String soundpath) {
        this.soundpath = soundpath;

        enableBox = new JCheckBox(label, true);
        
        soundDisplay = new JLabel(getPathName(soundpath));        

        inner = new ListPanel(Component.LEFT_ALIGNMENT);

        JPanel title = new JPanel(new BorderLayout());
        title.add(enableBox, BorderLayout.WEST);
        inner.add(title);

        inner.add(soundDisplay);
        
        this.add(inner);

        setSound(soundpath);

        addToAllSettings();
        setBorder(new LineBorder(Color.BLUE));
    }

    private static String getPathName(String path) {
        return new File(path).getName();
    }

    public void playSound() {
        if (enabled) {
            player.seek(player.getStartTime());
            player.play();
        }
    }

    public void setSound(String soundpath) {
        player = new MediaPlayer(new Media(new File(soundpath).toURI().toString()));
    }

    @Override
    protected void readData(DataInputStream dis) throws IOException {
        enableBox.setSelected(dis.readBoolean());
        soundpath = dis.readUTF();
        soundDisplay.setText(getPathName(soundpath));

        if (!enableBox.isSelected()) 
            inner.remove(soundDisplay);
    }

    @Override
    protected void useDefault() { /* default specified in constructor */ }

    @Override
    protected void writeData(DataOutputStream dos) throws IOException {
        dos.writeBoolean(enabled);
        dos.writeUTF(soundpath);
    }

    @Override
    protected void applyData() {
        this.enabled = enableBox.isSelected();
        setSound(soundpath);
    }
}
