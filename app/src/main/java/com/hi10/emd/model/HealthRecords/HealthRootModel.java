package com.hi10.emd.model.HealthRecords;

import com.google.gson.annotations.SerializedName;
import com.hi10.emd.model.login.LoginDataModel;

import java.util.List;

public class HealthRootModel {

    @SerializedName("success")
    private String success;

    @SerializedName("error_msg")
    private String error_msg;

    @SerializedName("data")
    private List<HealthRecordDataModel> healthRecordDataModels;

    public List<HealthRecordDataModel> getHealthRecordDataModels() {
        return healthRecordDataModels;
    }

    public void setHealthRecordDataModels(List<HealthRecordDataModel> healthRecordDataModels) {
        this.healthRecordDataModels = healthRecordDataModels;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public HealthRootModel(String success, String error_msg) {
        this.success = success;
        this.error_msg = error_msg;
    }
}
