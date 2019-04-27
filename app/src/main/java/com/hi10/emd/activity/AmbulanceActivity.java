package com.hi10.emd.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hi10.emd.R;
import com.hi10.emd.helper.GPSTracker;
import com.hi10.emd.helper.HttpConnection;
import com.hi10.emd.helper.PathJSONParser;
import com.hi10.emd.helper.SessionManager;
import com.hi10.emd.helper.Utils;
import com.hi10.emd.model.Hospitals.HospitalRootModel;
import com.hi10.emd.retrofit.ApiClient;
import com.hi10.emd.retrofit.ApiInterface;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AmbulanceActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    GoogleMap googleMap;

    private AmbulanceActivity mContext;
    GPSTracker gpsTracker;
    String hospitalName;
    LatLng nearHospitalLatLgn;

    //
    private static LatLng AMBULANCE_LOCATION = new LatLng(40.722543,-73.998585);
    private static LatLng USER_LOCATION = new LatLng(40.7057, -73.9964);
    private static LatLng HOSPITAL_LOCATION = new LatLng(40.7064, -74.0094);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance);
        mContext = this;
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

        gpsTracker = new GPSTracker(this);
        Log.e("MAP", "action=" + action);
        Log.e("MAP", "data=" + data.toString());
        Log.e("MAP", "data=" + data.toString().split("=")[0]);

        if(SessionManager.getInstance(this).isLogin()) {
            if (data.toString().contains("=") && data.toString().contains(",")) {
               //live location
               // AMBULANCE_LOCATION = new LatLng(gpsTracker.latitude, gpsTracker.longitude);
               // AMBULANCE_LOCATION = new LatLng(19.136497, 73.003017);
                USER_LOCATION = new LatLng(Double.parseDouble(data.toString().split("=")[1].split(",")[0])
                        , Double.parseDouble(data.toString().split("=")[1].split(",")[1]));

                Log.e("MAP", "USER_LOCATION=" + USER_LOCATION);

                getHospital(USER_LOCATION);
            } else {
                Toast.makeText(mContext, "Incorrect URL...", Toast.LENGTH_SHORT).show();
                finish();
            }
        }else {
            Toast.makeText(mContext, "Kindly Login...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,LoginActivity.class));
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

       /* SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/
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
        options.position(USER_LOCATION);
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
                + "|" + "|" + USER_LOCATION.latitude + ","
                + USER_LOCATION.longitude + "|" + HOSPITAL_LOCATION.latitude + ","
                + HOSPITAL_LOCATION.longitude;

        String sensor = "sensor=false";
        String origin="origin="+AMBULANCE_LOCATION.latitude + "," + AMBULANCE_LOCATION.longitude;
        String destination="&destination="+HOSPITAL_LOCATION.latitude + "," + HOSPITAL_LOCATION.longitude;
        String params = origin+destination+waypoints + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params+"&key="+getString(R.string.google_maps_key);
        return url;
    }

    private void addMarkers() {
        if (googleMap != null) {
            googleMap.addMarker(new MarkerOptions().position(USER_LOCATION)
                    .title("Second Point").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).snippet("User Location"));
            googleMap.addMarker(new MarkerOptions().position(AMBULANCE_LOCATION)
                    .title("First Point").icon(BitmapDescriptorFactory.fromResource(R.drawable.ambulance)).snippet("Ambulance Location"));
            googleMap.addMarker(new MarkerOptions().position(HOSPITAL_LOCATION)
                    .title("Third Point").snippet(hospitalName));
        }
    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                Log.e("MAP","URL ="+url[0]);
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
                Log.e("MAP","PATH JSON="+jsonData[0]);
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

            if(polyLineOptions!=null) {
                googleMap.addPolyline(polyLineOptions);
            }else {
                Toast.makeText(mContext, "Path Not Found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void getHospital(final LatLng userLocation) {
        Utils.showLoading(this);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<HospitalRootModel> hospitalRootModelCall = apiService.HOSPITAL_ROOT_MODEL_CALL();


        hospitalRootModelCall.enqueue(new Callback<HospitalRootModel>() {
            @Override
            public void onResponse(Call<HospitalRootModel> call, Response<HospitalRootModel> response) {
                Utils.closeLoading(mContext);
                if (response.body().getSuccess().equals("1")) {

                    List<LatLng> hospitalLocation = new ArrayList<>();
                    for (int i = 0; i < response.body().getDataModel().size(); i++) {
                        hospitalLocation.add(new LatLng(Double.parseDouble(response.body().getDataModel().get(i).getLatitude()),
                                Double.parseDouble(response.body().getDataModel().get(i).getLongitude())));

                    }


                    HOSPITAL_LOCATION = Utils.findNearestPoint(userLocation, hospitalLocation);
                    Log.e("Ambulance","HOSPITAL_LOCATION ="+HOSPITAL_LOCATION);

                    for (int i = 0; i < response.body().getDataModel().size(); i++) {
                        double Latitude = Double.parseDouble(response.body().getDataModel().get(i).getLatitude());
                        double Longitude = Double.parseDouble(response.body().getDataModel().get(i).getLongitude());

                        if (HOSPITAL_LOCATION.latitude == Latitude && HOSPITAL_LOCATION.longitude == Longitude) {
                            hospitalName = response.body().getDataModel().get(i).getHospital_name();
                            Log.e("Ambulance","Hospital Name="+hospitalName);
                            break;
                        }
                    }

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(mContext);

                } else {
                    Utils.showDialogBox(response.body().getError_msg(), mContext);
                }
            }

            @Override
            public void onFailure(Call<HospitalRootModel> call, Throwable t) {
                Utils.closeLoading(mContext);
                Utils.showDialogBox(getString(R.string.onFailure) + t.getLocalizedMessage(), mContext);

            }
        });
    }
}
