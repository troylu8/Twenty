package com.troylu.twenty.frame;


import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Tab {

    static Tab activeTab;

    JLabel btn = new JLabel();
    JPanel panel;

    public Tab(JPanel bar, String name, JPanel panel) {
        
        btn = new JLabel(name);
        btn.setBackground(Color.DARK_GRAY);
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setActive();
            }
        });

        this.panel = panel;

        bar.add(this.btn);
        
    }

    public void setActive() {
        if (activeTab == this) return;

        if (activeTab != null) {
            activeTab.btn.setForeground(Color.BLACK);
            activeTab.btn.setOpaque(false);
        }

        TwentyFrame.frame.view.removeAll();
        TwentyFrame.frame.view.add(panel);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);

        TwentyFrame.fitFrame();

        activeTab = this;
    }

}
