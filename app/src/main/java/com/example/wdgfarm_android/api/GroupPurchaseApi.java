package com.example.wdgfarm_android.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GroupPurchaseApi extends AsyncTask<Void, Void, String> {
    private final static String TAG = "[GroupPurchaseApi]";

    private static ApiListener listener;
    private static String zone;
    private static String session;

    static StringBuilder result;

    private WeighingViewModel weighingViewModel;
    private ArrayList<Weighing> weighingArrayList;
    private ProgressDialog progressDialog;
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    public GroupPurchaseApi(String zone, String session, ArrayList<Weighing> weighingArrayList, WeighingViewModel weighingViewModel, Context context, ApiListener listener) {
        this.zone = zone;
        this.session = session;
        this.weighingArrayList = weighingArrayList;
        this.weighingViewModel = weighingViewModel;
        this.progressDialog = new ProgressDialog(context);
        this.listener = listener;
    }


    @Override
    protected String doInBackground(Void... voids) {

        result = new StringBuilder();

        HttpURLConnection conn = null;


        try {
            URL url = new URL(URLs.BASE_REQUEST_URL + zone + URLs.PURCHASE_URL + session);
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < weighingArrayList.size(); i++) {
                long now = System.currentTimeMillis();

                JSONObject jsonList = new JSONObject();
                JSONObject jsonData = new JSONObject();

                jsonData.put("UPLOAD_SER_NO", "");
                jsonData.put("IO_DATE", format.format(weighingArrayList.get(i).getDate()));
                jsonData.put("CUST_DES", weighingArrayList.get(i).getCompanyName());
                jsonData.put("PROD_DES", weighingArrayList.get(i).getProductName());
                jsonData.put("QTY", 1);
                jsonData.put("SUPPLY_AMT", weighingArrayList.get(i).getProductPrice());

                jsonList.put("Line", "0");
                jsonList.put("BulkDatas", jsonData);

                jsonArray.put(jsonList);

                jsonObject.put("PurchasesList", jsonArray);
            }
//            jsonData.put("UPLOAD_SER_NO", "");
//            jsonData.put("IO_DATE", date);
//            jsonData.put("CUST_DES", company);
//            jsonData.put("PROD_DES", product);
//            jsonData.put("QTY", 1);
//            jsonData.put("SUPPLY_AMT", price);
//
//            jsonList.put("Line", "0");
//            jsonList.put("BulkDatas", jsonData);
//
//            jsonArray.put(jsonList);
//
//            jsonObject.put("PurchasesList", jsonArray);

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
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("ECOUNT ERP 등록중...");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        Log.e(TAG, "Response : " + response);

        if (progressDialog != null && progressDialog.isShowing()) {
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
