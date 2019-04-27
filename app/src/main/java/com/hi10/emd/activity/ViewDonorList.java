package com.hi10.emd.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hi10.emd.R;
import com.hi10.emd.helper.SessionManager;
import com.hi10.emd.helper.Utils;
import com.hi10.emd.model.BGroupParamModel;
import com.hi10.emd.model.getDataByType.RegDataByTypeRootModel;
import com.hi10.emd.model.login.LoginDataModel;
import com.hi10.emd.retrofit.ApiClient;
import com.hi10.emd.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewDonorList extends AppCompatActivity {

    private DonorListAdapter adapter;
    private List<LoginDataModel> appointmentList;
    ViewDonorList mContext;
    private ViewDonorList activity;
    String TAG = ViewHeathRecords.class.getSimpleName();
    private RecyclerView bookedAppointmentList;
    private LinearLayoutManager mLayoutManager;
    private String userPhoneNumber;

    List<LoginDataModel> booked_appointments = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_donor_list);
        bookedAppointmentList = (RecyclerView) findViewById(R.id.booked_appointment_recycler_view);
        mLayoutManager = new LinearLayoutManager(activity);
        bookedAppointmentList.setLayoutManager(mLayoutManager);
        bookedAppointmentList.setItemAnimator(new DefaultItemAnimator());
        mContext = this;
        activity = this;
        getDonorList();
    }


    void getDonorList()
    {
        Utils.showLoading(this);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<RegDataByTypeRootModel> BGROUP=apiService.GET_BGROUP(
                new BGroupParamModel(SessionManager.getInstance(ViewDonorList.this).getLoginData().getBloodGroup()));

        BGROUP.enqueue(new Callback<RegDataByTypeRootModel>() {
            @Override
            public void onResponse(Call<RegDataByTypeRootModel> call, Response<RegDataByTypeRootModel> response) {

                Utils.closeLoading(ViewDonorList.this);

                if(response.body().getSuccess().equals("1")) {
                    List<LoginDataModel> tempDataModel= response.body().getDataModel();


                    for(int i=0;i<tempDataModel.size();i++)
                    {
                        if(!tempDataModel.get(i).getFullName().equals(SessionManager.getInstance(ViewDonorList.this).getLoginData().getFullName()))
                        {
                            booked_appointments.add(tempDataModel.get(i));
                        }

                    }

                   // booked_appointments = response.body().getDataModel();


                    adapter = new DonorListAdapter(activity , booked_appointments);
                    bookedAppointmentList.setAdapter(adapter);
                }else {
                    Utils.showDialogBox(response.body().getError_msg(), ViewDonorList.this);

                }
            }

            @Override
            public void onFailure(Call<RegDataByTypeRootModel> call, Throwable t) {
                Utils.closeLoading(ViewDonorList.this);
                Utils.showDialogBox(getString(R.string.onFailure) + t.getLocalizedMessage(), ViewDonorList.this);

            }
        });
    }

    public class DonorListAdapter extends RecyclerView.Adapter<DonorListAdapter.StudentListViewHolder> {


        private Context mContext;
        private List<LoginDataModel> studentList;


        public DonorListAdapter(Context context) {

        }

        public DonorListAdapter(Context mContext, List<LoginDataModel> appointmentList) {
            this.mContext = mContext;
            this.studentList = appointmentList;
        }

        @Override
        public DonorListAdapter.StudentListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_health_records, parent, false);


            return new DonorListAdapter.StudentListViewHolder(itemView);
        }

        public void onBindViewHolder(DonorListAdapter.StudentListViewHolder holder, int position) {
            final LoginDataModel obj = studentList.get(position);
            holder.name_of_disease.setText("Name : " + obj.getFullName());
            holder.symtoms.setText("Blood Group : " + obj.getBloodGroup());
            holder.treated_by.setVisibility(View.GONE);
            holder.records_entered.setVisibility(View.GONE);

            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCustomDialog(obj.getContactNumber());
                }
            });

        }


        @Override
        public int getItemCount() {
            return studentList.size();
        }

        protected class StudentListViewHolder extends RecyclerView.ViewHolder {
            TextView name_of_disease,symtoms,treated_by,records_entered;
            LinearLayout root;

            public StudentListViewHolder(View itemView) {
                super(itemView);
                root =  itemView.findViewById(R.id.root);
                name_of_disease = (TextView) itemView.findViewById(R.id.disease_name);
                symtoms = (TextView) itemView.findViewById(R.id.symtoms);
                treated_by = (TextView) itemView.findViewById(R.id.doctor_name);
                records_entered = (TextView) itemView.findViewById(R.id.records_entered);

            }
        }
    }

    private void showCustomDialog(final String mobile) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.my_custom_dialog_call, viewGroup, false);

        Button buttonOk = dialogView.findViewById(R.id.buttonOk);
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = mContext.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                String userFulname = preferences.getString("full_name","");
                alertDialog.hide();
                Utils.sendMySMS(mobile,"Hi My name is " + SessionManager.getInstance(ViewDonorList.this).getLoginData().getFullName() + ",\nDue to a Medical condition I am in urgent need of blood matching with your type.\n " +
                        "Could you Please help me with the same.",ViewDonorList.this);
            }
        });
    }
}
