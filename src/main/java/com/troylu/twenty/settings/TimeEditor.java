package com.troylu.twenty.settings;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

import com.troylu.twenty.time.TimeManager;

public class TimeEditor extends SettingsComponent {

    Timer timer;
    int defaultTime;

    TimeField hr;
    TimeField min;
    TimeField sec;

    public TimeEditor(String label, Timer timer, int defaultTime) {
        this.timer = timer;
        this.defaultTime = defaultTime;

        this.setLayout(new BorderLayout());
        this.add(new JLabel(label), BorderLayout.NORTH);

        JPanel clock = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

        hr = new TimeField();
        min = new TimeField();
        sec = new TimeField();

        clock.add(hr);
        clock.add(new Colon());
        clock.add(min);
        clock.add(new Colon());
        clock.add(sec);
        clock.setBorder(new LineBorder(Color.GREEN));

        this.add(clock);

        addToAllSettings();
        setBorder(new LineBorder(Color.BLUE));
    }

    public void setTime(int ms) {
        String[] values = TimeManager.timeToClock(ms).split(":");
        hr.setText(values[0]);
        min.setText(values[1]);
        sec.setText(values[2]);
    }

    public int getTime() {
        return  TimeManager.ONE_HOUR * Integer.parseInt(hr.getText())  +
                TimeManager.ONE_MIN  * Integer.parseInt(min.getText()) +
                TimeManager.ONE_SEC  * Integer.parseInt(sec.getText());
    }

    protected void readData(DataInputStream dis) throws IOException {
        setTime(dis.readInt());
    }

    protected void useDefault() {
        setTime(defaultTime);
    }
    
    protected void writeData(DataOutputStream dos) throws IOException {
        dos.writeInt(getTime());
    }

    protected void applyData() {
        timer.setDelay(getTime());
    }

    class TimeField extends JTextField {
        public TimeField() {
            setFont(getFont().deriveFont(30f));
            setColumns(2);
        }
    }

    class Colon extends JPanel {
        public Colon() {
            setPreferredSize(new Dimension(11, 40));
        }
        @Override
        public void paintComponent(Graphics g) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.fillOval(0, 5, 10, 10);
            g.fillOval(0, 25, 10, 10);
        }
    }
}