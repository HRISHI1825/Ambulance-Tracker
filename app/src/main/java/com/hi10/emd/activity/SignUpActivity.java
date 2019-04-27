package com.hi10.emd.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.hi10.emd.R;
import com.hi10.emd.helper.GPSTracker;
import com.hi10.emd.helper.Utils;
import com.hi10.emd.model.SignUp.SignUpParamModel;
import com.hi10.emd.model.SignUp.SignUpRootModel;
import com.hi10.emd.retrofit.ApiClient;
import com.hi10.emd.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageView;
    /**
     * Full Name
     */
    private EditText mEdFullName;
    /**
     * Contact Number
     */
    private EditText mEdContactNumber;
    /**
     * Age
     */
    private EditText mEdAge;
    /**
     * User Name
     */
    private EditText mEdUserName;
    /**
     * Weight In kg
     */
    private EditText mEdWeight;
    private Spinner mSpnBloodGroup;
    private Spinner mSpnAllergy;
    /**
     * Enter Allergy
     */
    private EditText mEdAllergy;
    private Spinner mSpnBreathing;
    private Spinner mSpnGender;
    /**
     * SIGN UP
     */
    private Button mBtnLogin;
    /**
     * Relative Mobile Number
     */
    private EditText mEdRelNumber;
    private Spinner mSpntype;
    GPSTracker gpsTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();

        gpsTracker = new GPSTracker(this);
    }

    private void initView() {
        mImageView = (ImageView) findViewById(R.id.imageView);
        mEdFullName = (EditText) findViewById(R.id.edFullName);
        mEdContactNumber = (EditText) findViewById(R.id.edContactNumber);
        mEdAge = (EditText) findViewById(R.id.edAge);
        mEdUserName = (EditText) findViewById(R.id.edUserName);
        mEdWeight = (EditText) findViewById(R.id.edWeight);
        mSpnBloodGroup = (Spinner) findViewById(R.id.spnBloodGroup);
        mSpnAllergy = (Spinner) findViewById(R.id.spnAllergy);
        mEdAllergy = (EditText) findViewById(R.id.edAllergy);
        mSpnBreathing = (Spinner) findViewById(R.id.spnBreathing);
        mSpnGender = (Spinner) findViewById(R.id.spnGender);
        mBtnLogin = (Button) findViewById(R.id.btnLogin);
        mEdRelNumber = (EditText) findViewById(R.id.edRelNumber);
        mBtnLogin.setOnClickListener(this);
        mSpntype = findViewById(R.id.spntype);

        mSpnAllergy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2) {
                    mEdAllergy.setVisibility(View.VISIBLE);
                } else {
                    mEdAllergy.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.edContactNumber:
                Utils.showDialogBox("Contact number is your password!!!", this);

                break;
            case R.id.btnLogin:
                if (gpsTracker.getIsGPSTrackingEnabled()) {
                    String fullName = mEdFullName.getText().toString();
                    String contact = mEdContactNumber.getText().toString();
                    String age = mEdAge.getText().toString();
                    String username = mEdUserName.getText().toString();
                    String weight = mEdWeight.getText().toString();
                    String relNumber = mEdRelNumber.getText().toString();
                    String allergy = mEdAllergy.getText().toString();

                    if (fullName.isEmpty()) {
                        Utils.showDialogBox("Enter Full Name!!!", this);

                    } else if (contact.isEmpty()) {
                        Utils.showDialogBox("Enter Contact Number!!!", this);

                    } else if (age.isEmpty()) {
                        Utils.showDialogBox("Enter Age!!!", this);


                    } else if (username.isEmpty()) {
                        Utils.showDialogBox("Enter UserName!!!", this);

                    } else if (weight.isEmpty()) {
                        Utils.showDialogBox("Enter Age!!!", this);

                    } else if (relNumber.isEmpty()) {
                        Utils.showDialogBox("Enter Relative Mobile Number!!!", this);

                    }/*else if(allergy.isEmpty())
                {

                }*/ else if (mSpnBloodGroup.getSelectedItemPosition() == 0) {
                        Utils.showDialogBox("Select Blood Group!!!", this);

                    } else if (mSpnAllergy.getSelectedItemPosition() == 0) {
                        Utils.showDialogBox("Select Allergy !!!", this);

                    } else if (mSpnBreathing.getSelectedItemPosition() == 0) {
                        Utils.showDialogBox("Select Breathing !!!", this);

                    } else if (mSpnGender.getSelectedItemPosition() == 0) {
                        Utils.showDialogBox("Select Gender!!!", this);

                    } else if (mSpntype.getSelectedItemPosition() == 0) {
                        Utils.showDialogBox("Select Type!!!", this);

                    } else {

                    Log.e("GPS",""+gpsTracker.latitude+" "+gpsTracker.longitude);
                    signUp(new SignUpParamModel(fullName, contact, age, username, weight, mSpnBloodGroup.getSelectedItem().toString(),
                                mSpnAllergy.getSelectedItem().toString(), allergy, mSpnBreathing.getSelectedItem().toString(),
                                mSpnGender.getSelectedItem().toString(), mSpntype.getSelectedItem().toString(),
                                relNumber,gpsTracker.latitude+" "+gpsTracker.longitude));
                    }
                }else {
                    gpsTracker.showSettingsAlert();
                }
                break;
            default:
                break;
        }
    }

    void signUp(SignUpParamModel signUpParamModel) {
        Utils.showLoading(this);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<SignUpRootModel> signUpRootModelCall = apiInterface.SIGN_UP_ROOT_MODEL_CALL(signUpParamModel);

        signUpRootModelCall.enqueue(new Callback<SignUpRootModel>() {
            @Override
            public void onResponse(Call<SignUpRootModel> call, Response<SignUpRootModel> response) {
                Utils.closeLoading(SignUpActivity.this);

                if(response.body().getSuccess().equals("1"))
                {
                    Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    finish();

                }else {
                    Utils.showDialogBox(response.body().getError_msg(), SignUpActivity.this);

                }
            }

            @Override
            public void onFailure(Call<SignUpRootModel> call, Throwable t) {
                Utils.closeLoading(SignUpActivity.this);
                Utils.showDialogBox(getString(R.string.onFailure) + t.getLocalizedMessage(), SignUpActivity.this);

            }
        });
    }
}
