package com.hi10.emd.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hi10.emd.R;
import com.hi10.emd.helper.GPSTracker;
import com.hi10.emd.helper.HttpConnection;
import com.hi10.emd.helper.PathJSONParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SourceDesinationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static LatLng AMBULANCE_LOCATION = new LatLng(19.114087, 73.015259);
    private static LatLng HOSPITAL_LOCATION = new LatLng(0, 0);
    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_desination);

        String latlonPass = getIntent().getStringExtra("latlon");
        HOSPITAL_LOCATION = new LatLng(Double.parseDouble(latlonPass.split(" ")[0]),
                Double.parseDouble(latlonPass.split(" ")[1]));
        GPSTracker gpsTracker = new GPSTracker(this);
        AMBULANCE_LOCATION = new LatLng(gpsTracker.latitude, gpsTracker.longitude);

        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        fm.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        MarkerOptions options = new MarkerOptions();
        options.position(AMBULANCE_LOCATION);
        options.position(HOSPITAL_LOCATION);
        googleMap.addMarker(options);
        String url = getMapsApiDirectionsUrl();
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AMBULANCE_LOCATION,
                13));
        addMarkers();
    }

    private String getMapsApiDirectionsUrl() {
        String waypoints = "&waypoints=optimize:true|"
                + AMBULANCE_LOCATION.latitude + "," + AMBULANCE_LOCATION.longitude
                + "|" + HOSPITAL_LOCATION.latitude + ","
                + HOSPITAL_LOCATION.longitude;

        String sensor = "sensor=false";
        String origin = "origin=" + AMBULANCE_LOCATION.latitude + "," + AMBULANCE_LOCATION.longitude;
        String destination = "&destination=" + HOSPITAL_LOCATION.latitude + "," + HOSPITAL_LOCATION.longitude;
        String params = origin + destination + waypoints + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    private void addMarkers() {
        if (googleMap != null) {
            googleMap.addMarker(new MarkerOptions().position(AMBULANCE_LOCATION)
                    .title("First Point").snippet("User Location"));
            googleMap.addMarker(new MarkerOptions().position(HOSPITAL_LOCATION)
                    .title("Third Point").snippet("Hospital Location"));
        }
    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                Log.e("MAP", "URL =" + url[0]);
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.e("MAP", "PATH JSON=" + jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(4);
                polyLineOptions.color(Color.BLUE);
            }

            if (polyLineOptions != null) {
                googleMap.addPolyline(polyLineOptions);
            } else {
                Toast.makeText(SourceDesinationActivity.this, "Path Not Found", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
