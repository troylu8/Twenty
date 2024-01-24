package com.troylu.twenty.frame;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.*;

import com.troylu.twenty.time.TimeManager;

import javafx.embed.swing.JFXPanel;

public class TwentyFrame extends JFrame {
    
    static Image cat = new ImageIcon("src\\main\\resources\\catto icon.jpg").getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);;

    static TwentyFrame frame;

    public static TrayIcon icon;

    static TimeManager timeManager;

    public static MenuItem skip;
    public static MenuItem info;

    JPanel barLeft;
    JPanel barRight;
    Tab clockTab;
    Tab settingsTab;

    JPanel view;

    public static JLabel time = new JLabel("--:--:--", SwingConstants.CENTER);
    public static JLabel currently = new JLabel("currently ", SwingConstants.CENTER);

    public TwentyFrame() {

        setIconImage(cat);

        JPanel clockPanel = new JPanel(new GridLayout(2, 1, 0, 0));

        JPanel clock = new JPanel(new BorderLayout());
        
        time.setFont(time.getFont().deriveFont(45f));
        clock.add(time, BorderLayout.SOUTH);
        clockPanel.add(clock);

        JPanel currAndControls = new JPanel(new BorderLayout());

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel pauseBtn = new JLabel(new ImageIcon(cat.getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        JLabel fastForwardBtn = new JLabel(new ImageIcon(cat.getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        controls.add(pauseBtn);
        controls.add(fastForwardBtn);

        currAndControls.add(controls, BorderLayout.NORTH);
        currAndControls.add(currently, BorderLayout.SOUTH);
        clockPanel.add(currAndControls);

        JPanel aboutPanel = new JPanel(new BorderLayout());
        aboutPanel.add(new Paragraphs(
            "the 20-20-20 rule ## to reduce eye strain, look at something 20 feet away for 20 seconds every 20 minutes"
        ));
        aboutPanel.add(new JLabel("-", SwingConstants.CENTER), BorderLayout.SOUTH);

        JPanel helpPanel = new JPanel(new BorderLayout());
        helpPanel.add(new JScrollPane(new Paragraphs(
            "title1 ##right click the tray icon and choose \\\"start rest now\\\", or click on the fast-forward button under the clock tab",
            "opening the clock to check remaining time every time is annoying!##try hovering over the tray icon",
            "i cant decide between waffles and pancakes!##waffles are objectively better."
        )));

        JPanel bar = new JPanel(new BorderLayout());
        barLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        clockTab = new Tab(barLeft, "clock", clockPanel);
        settingsTab = new Tab(barLeft, "settings", new SettingsPanel());
        new Tab(barRight, "help", helpPanel);
        new Tab(barRight, "about", aboutPanel);

        bar.add(barLeft, BorderLayout.WEST);
        bar.add(barRight, BorderLayout.EAST);
        this.add(bar, BorderLayout.NORTH);

        this.add(view = new JPanel(new BorderLayout()));

        setSize(300, 200);

        this.setTitle("twenty");

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) throws AWTException, InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {


        if (!SystemTray.isSupported()) {

            JPanel msg = new JPanel(new GridLayout(2, 1, 0, 10));
            msg.add(new JLabel("cannot run twenty"));
            msg.add(new JLabel("tray icons are not supported on your device"));

            JOptionPane.showMessageDialog(null, 
                msg, "twenty", JOptionPane.DEFAULT_OPTION, 
                new ImageIcon(cat.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            
            System.exit(0);
        }

        // initialize javafx for notification sounds
        new JFXPanel();

        icon = new TrayIcon(cat);
        icon.setImageAutoSize(true);

        PopupMenu popup = new PopupMenu();

        info = new MenuItem("currently ");
        
        skip = new MenuItem("-");
        skip.addActionListener((ActionEvent e) -> {TimeManager.switchTimers();});
        skip.setShortcut(new MenuShortcut(KeyEvent.VK_A, false));
        
        popup.add(info);
        popup.add("-");
        popup.add(skip);
        popup.add("-");
        popup.add("clock");
        popup.add("settings");
        popup.add("close app");
        popup.addActionListener(new PopupListener());

        icon.setPopupMenu(popup);
        icon.addActionListener((ActionEvent e) -> {
            final String SCREEN_END = SettingsPanel.endScreenNotif.getCaption();
            final String REST_END = SettingsPanel.endRestNotif.getCaption();
            final String actionCommand = e.getActionCommand();

            if (actionCommand.equals(SCREEN_END) || actionCommand.equals(REST_END))
                TimeManager.switchTimers();
        });

        SystemTray tray = SystemTray.getSystemTray();
        tray.add(icon);

        // Runtime.getRuntime().addShutdownHook(new Thread(() -> {SettingsComponent.writeAllData();}));

        TwentyFrame.frame = new TwentyFrame();

        TimeManager.init();
        TimeManager.startScreenTime();
        
    }

    public static void fitFrame() {
        TwentyFrame.frame.view.setPreferredSize(TwentyFrame.frame.view.getSize());
        TwentyFrame.frame.pack();
        TwentyFrame.frame.repaint();
    }

    class Paragraphs extends JTextPane {

        StyledDocument doc;
        Style titleStyle;

        public Paragraphs(String... text) {

            doc = getStyledDocument();
            titleStyle = doc.addStyle("title", null);
            StyleConstants.setBold(titleStyle, true); 

            setEditable(false);
            setFocusable(false);
            setBackground(Color.WHITE);

            setBorder(new LineBorder(Color.RED));

            for (String t : text) {
                String[] data = t.split("##");
                addTitle(data[0]);
                addBody(data[1]);
            }
            
        }

        public void addTitle(String text) {
            try {
                int len = doc.getLength();
                doc.insertString(len, (len != 0)? "\n\n" + text : text, titleStyle);
            } 
            catch (BadLocationException e) { e.printStackTrace(); }
        }

        public void addBody(String text) {
            try {
                int len = doc.getLength();
                doc.insertString(len, (len != 0)? "\n" + text : text, null);
            } 
            catch (BadLocationException e) { e.printStackTrace(); }
        }

    }

    static class PopupListener implements ActionListener {

        void openFrame() {
            if (TwentyFrame.frame != null)
                TwentyFrame.frame.dispose();

            TwentyFrame.frame = new TwentyFrame();
            TwentyFrame.frame.setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {

                case "clock":
                    openFrame();
                    TwentyFrame.frame.clockTab.setActive();
                    break;

                case "settings":
                    openFrame();
                    TwentyFrame.frame.settingsTab.setActive();
                    break;

                case "close app":
                    System.exit(0);
                    break;
            }
        }

    }

}

