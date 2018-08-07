package com.example.niklas.caravanparking.helpers;

import android.os.AsyncTask;

import com.google.android.gms.ads.internal.gmsg.HttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class UpdateTask extends AsyncTask<String, Void, Boolean> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Boolean doInBackground(String... params) {
        HttpURLConnection conn = null;

        try {

            URL url = new URL("http://192.168.178.26/CaravanParkingServer/Main.php");
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(150000); //milliseconds
            conn.setConnectTimeout(15000); // milliseconds
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");

            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();

                String JSONResponse = sb.toString();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }

        return false;
    }

    protected void onPostExecute(Boolean result) {

    }
}
