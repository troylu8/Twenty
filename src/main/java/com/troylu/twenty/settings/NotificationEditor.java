package com.troylu.twenty.settings;

import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

import com.troylu.twenty.frame.*;

public class NotificationEditor extends SettingsComponent {

    private boolean enabled;
    private String caption;
    private String text;

    private ListPanel inner;

    private JCheckBox enableBox;
    private JTextArea captionArea;
    private JTextArea textArea;

    public NotificationEditor(String label, String defaultCaption, String defaultText) {
        setLayout(new BorderLayout());

        inner = new ListPanel(Component.LEFT_ALIGNMENT);
        
        enableBox = new JCheckBox(label, true);
        enableBox.addActionListener((ActionEvent e) -> {
            if (enableBox.isSelected()) {
                inner.add(captionArea);
                inner.add(textArea);
            } else {
                inner.remove(captionArea);
                inner.remove(textArea);
            }
            TwentyFrame.fitFrame();
        });
        inner.add(enableBox);
        
        captionArea = new JTextArea(defaultCaption, 1, 25);
        captionArea.setWrapStyleWord(true);
        captionArea.setLineWrap(true);
        captionArea.setBorder(new LineBorder(Color.RED));

        textArea = new JTextArea(defaultText, 1, 25);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setBorder(new LineBorder(Color.RED));

        inner.add(captionArea);
        inner.add(textArea);
        inner.setBorder(new LineBorder(Color.GREEN));

        this.add(inner, BorderLayout.WEST);

        addToAllSettings();
        setBorder(new LineBorder(Color.BLUE));
    }

    @Override
    protected void readData(DataInputStream dis) throws IOException {
        enableBox.setSelected(dis.readBoolean());
        captionArea.setText(dis.readUTF());
        textArea.setText(dis.readUTF());

        if (!enableBox.isSelected()) {
            inner.remove(captionArea);
            inner.remove(textArea);
        }
    }

    @Override
    protected void useDefault() { /* default specified in constructor */ }

    @Override
    protected void writeData(DataOutputStream dos) throws IOException {
        dos.writeBoolean(enableBox.isSelected());
        dos.writeUTF(captionArea.getText());
        dos.writeUTF(textArea.getText());
    }

    @Override
    protected void applyData() { 
        enabled = enableBox.isSelected();
        caption = captionArea.getText();
        text = textArea.getText();
    }

    public boolean getEnabled() { return enabled; }
    public String getCaption() { return caption; }
    public String getText() { return text; }

    public void sendNotification() {
        if (enabled) {
            TwentyFrame.icon.displayMessage(caption, text, TrayIcon.MessageType.NONE);
            TwentyFrame.icon.setActionCommand(caption);
        }
    }

}