package com.hi10.emd.model.SignUp;

import com.google.gson.annotations.SerializedName;
import com.hi10.emd.model.login.LoginDataModel;

public class SignUpRootModel {

    @SerializedName("success")
    private String success;

    @SerializedName("error_msg")
    private String error_msg;

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
}
