package com.troylu.twenty.settings;

import java.io.*;
import javax.swing.*;
import java.util.*;

public abstract class SettingsComponent extends JPanel {

    private static String SETTINGS_PATH = "src\\main\\resources\\settings.dat";

    /** to preserve reading/writing order */
    private static ArrayList<SettingsComponent> allSettings = new ArrayList<>();

    protected void addToAllSettings() { allSettings.add(this); }
    
    protected abstract void readData(DataInputStream dis) throws IOException;
    protected abstract void useDefault();
    protected abstract void writeData(DataOutputStream dos) throws IOException;
    protected abstract void applyData();

    public static void readAllData() {
        try {
            new File(SETTINGS_PATH).createNewFile();

            DataInputStream dis = new DataInputStream(new FileInputStream(SETTINGS_PATH));
            
            if (dis.available() == 0) {
                for (SettingsComponent sc : allSettings)
                    sc.useDefault();
            } else {
                for (SettingsComponent sc : allSettings)
                    sc.readData(dis);
            }
            
            dis.close();
        }
        catch(IOException e) { e.printStackTrace(); }
    }

    public static void writeAllData() {
        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(SETTINGS_PATH));
            for (SettingsComponent sc : allSettings)
                sc.writeData(dos);
            dos.close();
        }
        catch(IOException e) { e.printStackTrace(); }
    }

    public static void applyAllData() {
        for (SettingsComponent sc : allSettings)
            sc.applyData();
    }

    public static void clearAllSettings() { 
        allSettings.clear(); 
    }
    
}