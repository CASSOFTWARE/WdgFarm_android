package com.example.wdgfarm_android.model;

public class Setting {

    private static String scaleAIP;
    private static int scaleAPort;
    private static String scaleBIP;
    private static int scalBPort;

    public Setting (){

    }

    public String getScaleAIP() {
        return scaleAIP;
    }

    public void setScaleAIP(String scaleAIP) {
        this.scaleAIP = scaleAIP;
    }

    public int getScaleAPort() {
        return scaleAPort;
    }

    public void setScaleAPort(int scaleAPort) {
        this.scaleAPort = scaleAPort;
    }

    public String getScaleBIP() {
        return scaleBIP;
    }

    public void setScaleBIP(String scaleBIP) {
        this.scaleBIP = scaleBIP;
    }

    public int getScalBPort() {
        return scalBPort;
    }

    public void setScalBPort(int scalBPort) {
        this.scalBPort = scalBPort;
    }
}
