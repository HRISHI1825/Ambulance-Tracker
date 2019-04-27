package com.hi10.emd.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hi10.emd.R;
import com.hi10.emd.helper.SessionManager;
import com.hi10.emd.helper.Utils;
import com.hi10.emd.model.HealthRecords.HealthParamModel;
import com.hi10.emd.model.HealthRecords.HealthRootModel;
import com.hi10.emd.retrofit.ApiClient;
import com.hi10.emd.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddHealthRecords extends AppCompatActivity {

    private TextView name_of_disease;
    private TextView symtoms;
    private TextView treated_by;
    private Context mContext;
    private Button btn_submit;
    private ProgressBar pbLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_health_records);
        mContext = this;
        name_of_disease = (TextView) findViewById(R.id.name_of_disease);
        symtoms = (TextView) findViewById(R.id.symtoms);
        treated_by = (TextView) findViewById(R.id.treated_by);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        pbLoader = (ProgressBar) findViewById(R.id.pbLoader);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name_of_disease.getText().toString().trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Name of Disease.", Toast.LENGTH_SHORT).show();
                }
                else if (symtoms.getText().toString().trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Symtoms.", Toast.LENGTH_SHORT).show();
                }
                else if (treated_by.getText().toString().trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please Enter treated by.", Toast.LENGTH_SHORT).show();
                }

                else{
                    addHealthRecordes(new HealthParamModel(SessionManager.getInstance(AddHealthRecords.this).getLoginData().getId(),
                            name_of_disease.getText().toString(),
                            symtoms.getText().toString(),
                            treated_by.getText().toString()));
                }
            }
        });
    }

    void addHealthRecordes(HealthParamModel model)
    {
        pbLoader.setVisibility(View.VISIBLE);
        btn_submit.setVisibility(View.GONE);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);


        Call<HealthRootModel> HEALTH_ROOT_MODEL_CALL=apiService.HEALTH_ROOT_MODEL_CALL(model);

        HEALTH_ROOT_MODEL_CALL.enqueue(new Callback<HealthRootModel>() {
            @Override
            public void onResponse(Call<HealthRootModel> call, Response<HealthRootModel> response) {
                pbLoader.setVisibility(View.GONE);
                btn_submit.setVisibility(View.VISIBLE);

                    Utils.showDialogBox("Health records Inserted Successfully",AddHealthRecords.this);
            }

            @Override
            public void onFailure(Call<HealthRootModel> call, Throwable t) {
                pbLoader.setVisibility(View.GONE);
                btn_submit.setVisibility(View.VISIBLE);

                Utils.closeLoading(AddHealthRecords.this);
                Utils.showDialogBox(getString(R.string.onFailure) + t.getLocalizedMessage(), AddHealthRecords.this);
            }
        });
    }
}
