package com.example.wdgfarm_android.utils;

public enum PreferencesKey {
    A_SCALE_IP("A_SCALE_IP"),
    A_SCALE_PORT("A_SCALE_PORT"),
    B_SCALE_IP("B_SCALE_IP"),
    B_SCALE_PORT("B_SCALE_PORT");

    private String key;

    PreferencesKey(String key) {
        this.key = key;
    }

}