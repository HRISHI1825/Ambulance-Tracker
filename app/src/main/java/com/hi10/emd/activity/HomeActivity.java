package com.hi10.emd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.hi10.emd.R;
import com.hi10.emd.helper.GPSTracker;
import com.hi10.emd.helper.SessionManager;
import com.hi10.emd.helper.Utils;
import com.hi10.emd.model.Hospitals.HospitalRootModel;
import com.hi10.emd.model.getDataByType.RegDataByTypeParamModel;
import com.hi10.emd.model.getDataByType.RegDataByTypeRootModel;
import com.hi10.emd.retrofit.ApiClient;
import com.hi10.emd.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatButton mBtnPanic;
    private Button mBtnHospitals, mBtnAddHealth, btn_view_health_records, btn_view_donor_friend, btn_logout;

    GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        gpsTracker = new GPSTracker(this);

    }

    void init() {
        mBtnPanic = findViewById(R.id.btnPanic);
        mBtnPanic.setOnClickListener(this);
        mBtnHospitals = findViewById(R.id.btn_hospitals);
        mBtnHospitals.setOnClickListener(this);

        mBtnAddHealth = findViewById(R.id.btn_health_records);
        mBtnAddHealth.setOnClickListener(this);

        btn_view_health_records = findViewById(R.id.btn_view_health_records);
        btn_view_health_records.setOnClickListener(this);

        btn_view_donor_friend = findViewById(R.id.btn_view_donor_friend);
        btn_view_donor_friend.setOnClickListener(this);

        btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPanic:

                String R_MSG = "My name is " + SessionManager.getInstance(this).getLoginData().getFullName() +
                        "\n My blood group is " + SessionManager.getInstance(this).getLoginData().getBloodGroup() +
                        "\nNeed Help," +
                        "\nLocation :-https://maps.google.com/?q=" + gpsTracker.latitude + "," + gpsTracker.longitude;

                Utils.sendMySMS(SessionManager.getInstance(this).getLoginData().getR_mno(), R_MSG, this);


                String D_MSG = "My name is " + SessionManager.getInstance(this).getLoginData().getFullName() +
                        "\n My blood group is " + SessionManager.getInstance(this).getLoginData().getBloodGroup() +
                        "\nNeed Help," +
                        "\nLocation :-https://www.emd.com/?q=" + gpsTracker.latitude + "," + gpsTracker.longitude;


                sendmsgToHospital(D_MSG);


                break;

            case R.id.btn_hospitals:

                startActivity(new Intent(this, HospitalListActivity.class));
                break;

            case R.id.btn_health_records:

                startActivity(new Intent(this, AddHealthRecords.class));
                break;

            case R.id.btn_view_health_records:

                startActivity(new Intent(this, ViewHeathRecords.class));
                break;

            case R.id.btn_view_donor_friend:

                startActivity(new Intent(this, ViewDonorList.class));
                break;

            case R.id.btn_logout:

                SessionManager.getInstance(this).setLoginStatus(false);
                finish();
                startActivity(new Intent(this, LoginActivity.class));

                break;
        }
    }

    void sendmsgToHospital(final String R_MSG) {

        Utils.showLoading(this);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<RegDataByTypeRootModel> regDataByTypeRootModelCall =
                apiService.REG_DATA_BY_TYPE_ROOT_MODEL_CALL(new RegDataByTypeParamModel("Ambulance Driver"));


        regDataByTypeRootModelCall.enqueue(new Callback<RegDataByTypeRootModel>() {
            @Override
            public void onResponse(Call<RegDataByTypeRootModel> call, Response<RegDataByTypeRootModel> response) {
                Utils.closeLoading(HomeActivity.this);
                if (response.body().getSuccess().equals("1")) {
                    Log.e("HOME", "GET DATA");
                    List<LatLng> driverLocation = new ArrayList<>();
                    for (int i = 0; i < response.body().getDataModel().size(); i++) {

                        driverLocation.add(new LatLng(
                                Double.parseDouble(response.body().getDataModel().get(i).getLatlon().split(" ")[0]),
                                Double.parseDouble(response.body().getDataModel().get(i).getLatlon().split(" ")[1])));
                    }

                    LatLng userLocation = new LatLng(gpsTracker.latitude, gpsTracker.longitude);
                    LatLng nearlatLng = Utils.findNearestPoint(userLocation, driverLocation);


                    Log.e("HOME", "GET nearlatLng" + nearlatLng);


                    for (int i = 0; i < response.body().getDataModel().size(); i++) {
                        if (response.body().getDataModel().get(i).getLatlon().
                                equals(nearlatLng.latitude + " " + nearlatLng.longitude)) {
                            Utils.sendMySMS(response.body().getDataModel().get(i).getContactNumber(), R_MSG, HomeActivity.this);
                            Log.e("HOME", "MSG SEND TO DRIVER" + response.body().getDataModel().get(i).getContactNumber());
                            break;
                        } else {
                            Log.e("HOME", "NO nearlatLng");
                        }
                    }


                } else {
                    Utils.showDialogBox(response.body().getError_msg(), HomeActivity.this);
                }
            }

            @Override
            public void onFailure(Call<RegDataByTypeRootModel> call, Throwable t) {
                Utils.closeLoading(HomeActivity.this);
                Utils.showDialogBox(getString(R.string.onFailure) + t.getLocalizedMessage(), HomeActivity.this);
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
