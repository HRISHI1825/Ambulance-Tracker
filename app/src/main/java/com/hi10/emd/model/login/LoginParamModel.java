package com.hi10.emd.model.login;

public class LoginParamModel {

    private String username;
    private String password;
    private String latlon;

    public LoginParamModel(String username, String password, String latlon) {
        this.username = username;
        this.password = password;
        this.latlon = latlon;
    }
}
