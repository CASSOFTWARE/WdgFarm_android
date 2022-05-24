package com.example.wdgfarm_android.api;

import android.os.AsyncTask;
import android.util.Log;

import com.example.wdgfarm_android.utils.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginApi extends AsyncTask<Void, Void, String> {
    private final String TAG = "[LoginApi]";

    private ApiListener listener;
    private String zone;
    StringBuilder result;

    public LoginApi(String zone, ApiListener listener) {
        this.listener = listener;
        this.zone = zone;
    }

    @Override
    protected String doInBackground(Void... voids) {

        result = new StringBuilder();
        JSONObject jsonObject = new JSONObject();
        HttpURLConnection conn = null;


        try {
            URL url = new URL(URLs.BASE_TEST_URL + zone + URLs.LOGIN_URL);
            jsonObject.put("COM_CODE", URLs.COM_CODE);
            jsonObject.put("USER_ID", URLs.USER_ID);
            jsonObject.put("API_CERT_KEY", URLs.API_CERT_KEY);
            jsonObject.put("LAN_TYPE", URLs.LAN_TYPE);
            jsonObject.put("ZONE", zone);

            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", URLs.CONTENT_TYPE);
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(jsonObject.toString());
            bw.flush();
            bw.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            JSONObject jsonResult = new JSONObject(br.readLine());


            result.append(jsonResult.getString("Data"));

            br.close();

        } catch (Exception e) {
            Log.i(TAG, "Error : " + e);
            result.append(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return result.toString();
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        Log.e(TAG, "Response : " + response);

        if (response.contains("SESSION_ID")) {
            try {
                listener.success(result.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            listener.fail();
        }
    }
}
