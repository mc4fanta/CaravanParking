package com.example.niklas.caravanparking.helpers;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public abstract class UpdateTask extends AsyncTask<String, Void, JSONObject> implements IAsyncResponse, IRequestCode {
    public Map<String, String> params = new HashMap<>();

    public UpdateTask(String requestCode) {
        params.put(REQUEST_CODE, requestCode);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        HttpURLConnection con = null;
        JSONObject JSONResponse = null;

        try {

            // connect to the server
            URL url = new URL("http://192.168.178.26/CaravanParkingServer/Main.php");
            con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(150000); //milliseconds
            con.setConnectTimeout(15000); // milliseconds
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestMethod("POST");

            String urlParameters = processRequestParameters(this.params);
            sendPostParameters(con, urlParameters);

            // conn.connect();

            // if connection was successful
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // build json object with response
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();

                JSONResponse = new JSONObject(sb.toString());
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }  finally {
            if (con != null)
                con.disconnect();
        }

        return JSONResponse;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        try {
            if (result != null)
                progressFinished(result.getJSONObject(DATA));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void sendPostParameters(URLConnection con, String urlParameters) throws IOException {
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
    }

    /**
     * Convert given Map of parameters to URL-encoded string
     *
     * @param parameters request parameters
     * @return URL-encoded parameters string
     */
    private static String processRequestParameters(Map<String, String> parameters) {
        StringBuilder sb = new StringBuilder();
        for (String parameterName : parameters.keySet()) {
            sb.append(parameterName).append('=').append(urlEncode(parameters.get(parameterName))).append('&');
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * Encode given String with URLEncoder in UTF-8
     *
     * @param s string to encode
     * @return URL-encoded string
     */
    private static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // This is impossible, UTF-8 is always supported according to the java standard
            throw new RuntimeException(e);
        }
    }
}
