package com.hi10.emd.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hi10.emd.R;
import com.hi10.emd.helper.SessionManager;
import com.hi10.emd.helper.Utils;
import com.hi10.emd.model.HealthRecords.GetAllHealthParamModel;
import com.hi10.emd.model.HealthRecords.HealthParamModel;
import com.hi10.emd.model.HealthRecords.HealthRecordDataModel;
import com.hi10.emd.model.HealthRecords.HealthRootModel;
import com.hi10.emd.retrofit.ApiClient;
import com.hi10.emd.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewHeathRecords extends AppCompatActivity {

    private HealthRecordsAdapter adapter;
    private List<HealthRecordDataModel> appointmentList;
    ViewHeathRecords mContext;
    private ViewHeathRecords activity;
    String TAG = ViewHeathRecords.class.getSimpleName();
    private RecyclerView bookedAppointmentList;
    private LinearLayoutManager mLayoutManager;

    private TextView parvaah;
    private TextView green_earth_campaign;

    List<HealthRecordDataModel> booked_appointments = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_heath_records);
        bookedAppointmentList = (RecyclerView) findViewById(R.id.booked_appointment_recycler_view);
        mLayoutManager = new LinearLayoutManager(activity);
        bookedAppointmentList.setLayoutManager(mLayoutManager);
        bookedAppointmentList.setItemAnimator(new DefaultItemAnimator());
        mContext = this;
        activity = this;

        getHealthRecords();
    }

    private void getHealthRecords()
    {
        Utils.showLoading(this);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<HealthRootModel> GET_ALL_HEALTH__MODEL=apiService.GET_ALL_HEALTH__MODEL_CALL(
                new GetAllHealthParamModel(SessionManager.getInstance(ViewHeathRecords.this).getLoginData().getId()));

        GET_ALL_HEALTH__MODEL.enqueue(new Callback<HealthRootModel>() {
            @Override
            public void onResponse(Call<HealthRootModel> call, Response<HealthRootModel> response) {
                Utils.closeLoading(ViewHeathRecords.this);

                if(response.body().getSuccess().equals("1")) {

                    booked_appointments = response.body().getHealthRecordDataModels();

                    Log.e("MAP","booked_appointments=="+booked_appointments.size());

                    adapter = new HealthRecordsAdapter(activity , booked_appointments);
                    bookedAppointmentList.setAdapter(adapter);

                }else {
                    Utils.showDialogBox(response.body().getError_msg(), ViewHeathRecords.this);
                }
            }

            @Override
            public void onFailure(Call<HealthRootModel> call, Throwable t) {
                Utils.closeLoading(ViewHeathRecords.this);
                Utils.showDialogBox(getString(R.string.onFailure) + t.getLocalizedMessage(), ViewHeathRecords.this);
            }
        });
    }


    public class HealthRecordsAdapter extends RecyclerView.Adapter<HealthRecordsAdapter.StudentListViewHolder> {


        private Context mContext;
        private List<HealthRecordDataModel> studentList;


        public HealthRecordsAdapter(Context mContext, List<HealthRecordDataModel> appointmentList) {
            this.mContext = mContext;
            this.studentList = appointmentList;
        }

        @Override
        public HealthRecordsAdapter.StudentListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_health_records, parent, false);


            return new HealthRecordsAdapter.StudentListViewHolder(itemView);
        }

        public void onBindViewHolder(HealthRecordsAdapter.StudentListViewHolder holder, int position) {
            HealthRecordDataModel obj = studentList.get(position);
            Log.e("MAP","Disease Name "+obj.getD_name());
            holder.name_of_disease.setText("Disease Name : " + obj.getD_name());
            holder.symtoms.setText("Symtoms : " + obj.getSymtoms());
            holder.treated_by.setText("Doctor Name : " + obj.getDoc_name());
            holder.records_entered.setText("Records Date: " + obj.getR_date());

        }


        @Override
        public int getItemCount() {
            return studentList.size();
        }

        class StudentListViewHolder extends RecyclerView.ViewHolder {
            TextView name_of_disease, symtoms, treated_by, records_entered;

            public StudentListViewHolder(View itemView) {
                super(itemView);
                name_of_disease=itemView.findViewById(R.id.disease_name);
                symtoms=itemView.findViewById(R.id.symtoms);
                treated_by=itemView.findViewById(R.id.doctor_name);
                records_entered=itemView.findViewById(R.id.records_entered);
            }
        }
    }
}
