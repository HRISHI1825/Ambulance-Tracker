package com.hi10.emd.model.getDataByType;

import com.google.gson.annotations.SerializedName;
import com.hi10.emd.model.login.LoginDataModel;

import java.util.List;

public class RegDataByTypeRootModel {

    @SerializedName("success")
    private String success;

    @SerializedName("data")
    private List<LoginDataModel> dataModel;

    @SerializedName("error_msg")
    private String error_msg;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<LoginDataModel> getDataModel() {
        return dataModel;
    }

    public void setDataModel(List<LoginDataModel> dataModel) {
        this.dataModel = dataModel;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public RegDataByTypeRootModel(String success, List<LoginDataModel> dataModel, String error_msg) {
        this.success = success;
        this.dataModel = dataModel;
        this.error_msg = error_msg;
    }
}
