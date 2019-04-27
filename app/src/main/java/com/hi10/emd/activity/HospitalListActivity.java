package com.hi10.emd.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.hi10.emd.R;
import com.hi10.emd.helper.Utils;
import com.hi10.emd.model.Hospitals.HospitalDataModel;
import com.hi10.emd.model.Hospitals.HospitalRootModel;
import com.hi10.emd.retrofit.ApiClient;
import com.hi10.emd.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HospitalListActivity extends AppCompatActivity implements View.OnClickListener {

    List<HospitalDataModel> rootData = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    DataAdapter dataAdapter;
    HospitalListActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_list);
        mContext=this;
        getHospital();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        mFloatingActionButton.setOnClickListener(this);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataAdapter = new DataAdapter(rootData);

        mRecyclerView.setAdapter(dataAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.floatingActionButton:
                dilogbox();
                break;

            default:
                break;
        }
    }

    void dilogbox() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dilog_box);
        dialog.setTitle("Select Type");
        LinearLayout rootView = dialog.findViewById(R.id.root);
        Button btnok= dialog.findViewById(R.id.btnOk);
        Button btncancel= dialog.findViewById(R.id.btnCancel);

        Set<String> stringList = getSortData();
        final RadioGroup group = new RadioGroup(this);
        int count = 1;
        for (Iterator<String> i = stringList.iterator(); i.hasNext(); ) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(i.next());
            radioButton.setId(101 * count);

            count++;
            group.addView(radioButton);
        }

        rootView.removeAllViews();
        rootView.addView(group);

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                if(group.getCheckedRadioButtonId() != -1)
                {
                    RadioButton radioButton= group.findViewById(group.getCheckedRadioButtonId());

                    if(radioButton.getText().toString().trim().equals("All"))
                    {
                        dataAdapter.getFilter().filter("");

                    }else {
                        dataAdapter.getFilter().filter(radioButton.getText().toString());
                    }
                    dialog.cancel();
                }else {
                    Toast.makeText(mContext, "Select Speciality!!!", Toast.LENGTH_SHORT).show();
                }




            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    Set<String> getSortData() {
        Set<String> stringList = new HashSet<>();


        stringList.add("All");
        for (int i = 0; i < rootData.size(); i++) {
            stringList.add(rootData.get(i).getSpeciality());
        }
        return stringList;
    }

    class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder>implements Filterable {

        List<HospitalDataModel> rootDataAdapter = new ArrayList<>();
        List<HospitalDataModel> rootDataFiltered = new ArrayList<>();


        public DataAdapter(List<HospitalDataModel> rootDataFiltered) {
            rootDataAdapter = rootDataFiltered;
            this.rootDataFiltered = rootDataFiltered;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    String inputData=constraint.toString().trim();

                    if(inputData.isEmpty())
                    {
                        rootDataFiltered=rootDataAdapter;


                    }else {
                        List<HospitalDataModel> filtedData=new ArrayList<>();

                        for(HospitalDataModel row:rootDataAdapter)
                        {

                            if(row.getSpeciality().toLowerCase().contains(inputData.toLowerCase()))
                            {
                                filtedData.add(row);
                            }
                        }
                        rootDataFiltered=filtedData;

                    }
                    FilterResults filte=new FilterResults();
                    filte.values=rootDataFiltered;
                    return filte;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    rootDataFiltered = (ArrayList<HospitalDataModel>) results.values;
                    notifyDataSetChanged();
                }
            };
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            TextView txtSpl;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.hospital_name);
                txtSpl = itemView.findViewById(R.id.speciality);
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.data_row, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder h, final int i) {

            h.textView.setText(rootDataFiltered.get(i).getHospital_name());

                    String msg=
                            "\nSpeciality: "+rootDataFiltered.get(i).getSpeciality()+
                                    "\n\nContact Number: "+rootDataFiltered.get(i).getContact()+
                            "\n\nAvailable Beds: "+rootDataFiltered.get(i).getBed_count()+
                    "\n\nTime: "+rootDataFiltered.get(i).getDoctor_available();
            h.txtSpl.setText(msg);

            h.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext,SourceDesinationActivity.class)
                            .putExtra("latlon",rootDataFiltered.get(i).getLatitude()+" "+
                                    rootDataFiltered.get(i).getLongitude()));
                }
            });

        }

        @Override
        public int getItemCount() {
            return rootDataFiltered.size();
        }
    }

    void getHospital() {
        Utils.showLoading(this);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<HospitalRootModel> hospitalRootModelCall = apiService.HOSPITAL_ROOT_MODEL_CALL();


        hospitalRootModelCall.enqueue(new Callback<HospitalRootModel>() {
            @Override
            public void onResponse(Call<HospitalRootModel> call, Response<HospitalRootModel> response) {
                Utils.closeLoading(mContext);
                if (response.body().getSuccess().equals("1")) {

                    rootData=response.body().getDataModel();
                    initView();
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
