package com.example.niklas.caravanparking.map.sync;

import com.example.niklas.caravanparking.helpers.UpdateTask;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapConnection extends UpdateTask implements IMapConnection{
    GoogleMap mMap;

    public MapConnection(GoogleMap mMap, LatLngBounds mapBounds) {
        super(GET_LOCATIONS);

        this.mMap = mMap;

        // set camera bounds as param
        params.put(NORTH_HEAST_LAT, String.valueOf(mapBounds.northeast.latitude));
        params.put(NORTH_HEAST_LONG, String.valueOf(mapBounds.northeast.longitude));
        params.put(SOUTH_WEST_LAT, String.valueOf(mapBounds.southwest.latitude));
        params.put(SOUTH_WEST_LONG, String.valueOf(mapBounds.southwest.longitude));

        // TODO don't download already downloaded locations again
    }

    @Override
    public void progressFinished(JSONObject JSONResponse) throws JSONException {
        JSONArray jLocations = JSONResponse.getJSONArray(LOCATIONS);

        // loop through locations
        for (int i = 0; i < jLocations.length(); i++) {
            JSONObject jLocation = jLocations.getJSONObject(i);

            Double x = jLocation.getDouble(LATITUDE);
            Double y = jLocation.getDouble(LONGITUDE);

            String description = jLocation.getString(DESCRIPTION);

            // set marker on map
            mMap.addMarker(new MarkerOptions().position(new LatLng(x, y)));

            // TODO set on click listener for markers
        }
    }
}
