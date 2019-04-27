package com.hi10.emd.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hi10.emd.R;
import com.hi10.emd.helper.GPSTracker;
import com.hi10.emd.helper.SessionManager;
import com.hi10.emd.helper.Utils;
import com.hi10.emd.model.login.LoginParamModel;
import com.hi10.emd.model.login.LoginRootModel;
import com.hi10.emd.retrofit.ApiClient;
import com.hi10.emd.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * UserName*
     */
    private EditText mUsername;
    /**
     * Password*
     */
    private EditText mPassword;
    /**
     * LOGIN Button
     */
    private Button mBtnLogin;

    private String[] permins = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_COARSE_LOCATION};
    /**
     * Sign Up --->
     */
    private TextView mSignup;

    GPSTracker gpsTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermi();
        initView();

        gpsTracker = new GPSTracker(this);



    }

    void getPermi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&

                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                            != PackageManager.PERMISSION_GRANTED &&

                    ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                            != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(permins, 100);
            } else {
                goNext();
            }
        }
    }

    private void initView() {
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mBtnLogin = (Button) findViewById(R.id.btnLogin);
        mBtnLogin.setOnClickListener(this);
        mSignup = (TextView) findViewById(R.id.signup);
        mSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btnLogin:
                if (gpsTracker.getIsGPSTrackingEnabled()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED &&

                                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                                        != PackageManager.PERMISSION_GRANTED &&

                                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                                        != PackageManager.PERMISSION_GRANTED &&

                                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                        != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(permins, 100);
                        } else {

                            String username = mUsername.getText().toString().trim();
                            String mobile = mPassword.getText().toString().trim();

                            if (username.isEmpty()) {
                                Utils.showDialogBox("Enter Username!!!", this);
                            } else if (mobile.isEmpty()) {
                                Utils.showDialogBox("Enter Password!!!", this);
                            } else if (mobile.length() != 10) {
                                Utils.showDialogBox("Enter Password is incorrect enter your mobile number!!!", this);
                            } else {
                                userLogin(new LoginParamModel(username, mobile,gpsTracker.latitude+" "+gpsTracker.longitude));
                            }
                        }
                    }
                }else
                {
                    gpsTracker.showSettingsAlert();
                }
                break;

            case R.id.signup:
                startActivity(new Intent(this,SignUpActivity.class));
                break;
        }
    }

    void userLogin(LoginParamModel model) {
        Utils.showLoading(this);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<LoginRootModel> loginRootModelCall = apiInterface.LOGIN_ROOT_MODEL_CALL(model);

        loginRootModelCall.enqueue(new Callback<LoginRootModel>() {
            @Override
            public void onResponse(Call<LoginRootModel> call, Response<LoginRootModel> response) {
                Utils.closeLoading(LoginActivity.this);

                if(response.body().getSuccess().equals("1")) {
                    SessionManager.getInstance(LoginActivity.this).setLoginStatus(true);
                    SessionManager.getInstance(LoginActivity.this).setLoginData(response.body().getDataModel());
                    goNext();
                }else {
                    Utils.showDialogBox(response.body().getError_msg(), LoginActivity.this);
                }
            }

            @Override
            public void onFailure(Call<LoginRootModel> call, Throwable t) {
                Utils.closeLoading(LoginActivity.this);
                Utils.showDialogBox(getString(R.string.onFailure) + t.getLocalizedMessage(), LoginActivity.this);
            }
        });
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                grantResults[2] == PackageManager.PERMISSION_GRANTED&&
                grantResults[3] == PackageManager.PERMISSION_GRANTED
        ) {
            getPermi();
        } else {
            getPermi();
        }
    }

    void goNext() {
        if (SessionManager.getInstance(this).isLogin()) {
            startActivity(new Intent(this, HomeActivity.class));
        }else {
            Utils.showDialogBox("Your Password is your Contact Number  ",this);
        }
    }
}
