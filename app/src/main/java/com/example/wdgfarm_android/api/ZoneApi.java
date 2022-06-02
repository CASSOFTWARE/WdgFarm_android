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

public class ZoneApi extends AsyncTask<Void, Void, String> {
    private final String TAG = "[ZoneApi]";

    private ApiListener listener;
    StringBuilder result;

    public ZoneApi(ApiListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... voids) {

        result = new StringBuilder();
        JSONObject jsonObject = new JSONObject();
        HttpURLConnection conn = null;


        try {
            URL url = new URL(URLs.BASE_REQUEST_URL + URLs.ZONE_URL);
            jsonObject.put("COM_CODE", URLs.COM_CODE);

            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", URLs.CONTENT_TYPE);
            conn.setConnectTimeout(10000);
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

        if (response.contains("ZONE")) {
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
