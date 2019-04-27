package com.hi10.emd.model.login;

import com.google.gson.annotations.SerializedName;

public class LoginRootModel {

    @SerializedName("success")
    private String success;

    @SerializedName("data")
    private LoginDataModel dataModel;

    @SerializedName("error_msg")
    private String error_msg;


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public LoginDataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(LoginDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}
