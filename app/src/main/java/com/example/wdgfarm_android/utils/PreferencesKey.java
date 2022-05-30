package com.example.wdgfarm_android.utils;

public enum PreferencesKey {
    A_SCALE_NAME("A_SCALE_NAME"),
    A_SCALE_IP("A_SCALE_IP"),
    A_SCALE_PORT("A_SCALE_PORT"),
    B_SCALE_NAME("B_SCALE_NAME"),
    B_SCALE_IP("B_SCALE_IP"),
    B_SCALE_PORT("B_SCALE_PORT"),
    AUTH_KEY("AUTH_KEY"),
    CONNECTED_SCALE("CONNECTED_SCALE");

    private String key;

    PreferencesKey(String key) {
        this.key = key;
    }

}