package com.example.wdgfarm_android.api;

import org.json.JSONException;

public interface ApiListener {
    public void success(String response) throws JSONException;
    public void fail();

}
