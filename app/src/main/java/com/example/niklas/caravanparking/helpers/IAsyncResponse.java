package com.example.niklas.caravanparking.helpers;

import org.json.JSONException;
import org.json.JSONObject;

public interface IAsyncResponse {
    /**
     * called getting JSONString from Server
     * @param JSONResponse
     */
    void progressFinished(JSONObject JSONResponse) throws JSONException;
}
