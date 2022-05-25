package com.example.wdgfarm_android.api;

import android.os.AsyncTask;
import android.util.Log;

import com.example.wdgfarm_android.utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProductApi extends AsyncTask<Void, Void, String> {
    private final String TAG = "[ProductApi]";

    private ApiListener listener;
    private String zone;
    private String session;
    private String productCode;
    private String productName;
    private int productPrice;
    StringBuilder result;

    public ProductApi(String zone, String session, String productCode, String productName, int productPrice, ApiListener listener) {
        this.zone = zone;
        this.session = session;
        this.productCode = productCode;
        this.productName = productName;
        this.productPrice = productPrice;
        this.listener = listener;

    }

    @Override
    protected String doInBackground(Void... voids) {

        result = new StringBuilder();
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonList = new JSONObject();
        JSONObject jsonData = new JSONObject();
        HttpURLConnection conn = null;


        try {
            URL url = new URL(URLs.BASE_TEST_URL + zone + URLs.PRODUCT_URL + session);

            jsonData.put("PROD_CD", productCode);
            jsonData.put("PROD_DES", productName);
            jsonData.put("PROD_TYPE", "1");
            jsonData.put("IN_PRICE", productPrice);

            jsonList.put("Line", "0");
            jsonList.put("BulkDatas", jsonData);

            jsonArray.put(jsonList);

            jsonObject.put("ProductList", jsonArray);
            
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

        if (response.contains("TRACE_ID")) {
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
