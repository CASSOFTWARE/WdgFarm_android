package com.example.wdgfarm_android.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.wdgfarm_android.activity.MainActivity;
import com.example.wdgfarm_android.model.Weighing;
import com.example.wdgfarm_android.utils.URLs;
import com.example.wdgfarm_android.viewmodel.WeighingViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class PurchaseApi extends AsyncTask<Void, Void, String> {
    private final static String TAG = "[PurchaseApi]";

    private static ApiListener listener;
    private static String zone;
    private static String session;
    private static String date;
    private static String company;
    private static String product;
    private static int price;
    private static float weight;
    static StringBuilder result;

    private ProgressDialog progressDialog;

    public PurchaseApi(String zone, String session, String date, String company, String product, int price, float weight, Context context, ApiListener listener) {
        this.zone = zone;
        this.session = session;
        this.date = date;
        this.company = company;
        this.product = product;
        this.price = price;
        this.weight = weight;
        this.progressDialog = new ProgressDialog(context);
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
            URL url = new URL(URLs.BASE_REQUEST_URL + zone + URLs.PURCHASE_URL + session);

            jsonData.put("UPLOAD_SER_NO", "");
            jsonData.put("IO_DATE", date);
            jsonData.put("CUST_DES", company);
            jsonData.put("PROD_DES", product);
            jsonData.put("QTY", String.valueOf(weight));
            jsonData.put("SUPPLY_AMT", String.format("%.0f", price*weight));

            jsonList.put("Line", "0");
            jsonList.put("BulkDatas", jsonData);

            jsonArray.put(jsonList);

            jsonObject.put("PurchasesList", jsonArray);

            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", URLs.CONTENT_TYPE);
            conn.setConnectTimeout(3000);
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
    protected void onPreExecute(){
        super.onPreExecute();
        progressDialog.setMessage("ECOUNT ERP 등록중...");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        Log.e(TAG, "Response : " + response);

        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }

        if (response.contains("TRACE_ID") && response.contains("FailCnt\":0")) {
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
