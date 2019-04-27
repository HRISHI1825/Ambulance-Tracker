package com.hi10.emd.model.Hospitals;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HospitalRootModel {

    @SerializedName("success")
    private String success;

    @SerializedName("data")
    private List<HospitalDataModel> dataModel;

    @SerializedName("error_msg")
    private String error_msg;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<HospitalDataModel> getDataModel() {
        return dataModel;
    }

    public void setDataModel(List<HospitalDataModel> dataModel) {
        this.dataModel = dataModel;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public HospitalRootModel(String success, List<HospitalDataModel> dataModel, String error_msg) {
        this.success = success;
        this.dataModel = dataModel;
        this.error_msg = error_msg;
    }
}
