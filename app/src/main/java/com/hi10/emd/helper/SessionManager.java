package com.hi10.emd.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.hi10.emd.model.login.LoginDataModel;

public class SessionManager {

    private static final String TAG_FILE_NAME = "emd";
    public static final String TAG_IS_LOGIN = "islogin";

    public static final String TAG_ID = "id";
    public static final String TAG_FULLNAME = "fullname";
    public static final String TAG_MNO = "mno";
    public static final String TAG_AGE = "age";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_U_WEIGHT = "u_weight";
    public static final String TAG_R_MNO = "r_mno";
    public static final String TAG_BGROUP = "bgroup";
    public static final String TAG_ALLERGY = "allergy";
    public static final String TAG_A_DETAILS = "a_details";
    public static final String TAG_B_PROB = "b_prob";
    public static final String TAG_GENDER = "gender";
    public static final String TAG_U_TYPE = "u_type";


    static SharedPreferences.Editor editor;
    private static SessionManager sessionManager;
    static SharedPreferences sharedPreferences;
    Context mContext;

    public SessionManager(Context paramContext)
    {
        this.mContext = paramContext;
    }


    public static SessionManager getInstance(Context paramContext) {
        if (sessionManager == null) {
            sessionManager = new SessionManager(paramContext);
        }
        sharedPreferences = paramContext.getSharedPreferences(TAG_FILE_NAME, 0);
         editor = sharedPreferences.edit();
        return sessionManager;
    }

  public LoginDataModel getLoginData() {
        return new LoginDataModel(sharedPreferences.getString(TAG_ID, null),
                sharedPreferences.getString(TAG_FULLNAME, null),
                sharedPreferences.getString(TAG_MNO, null),
                sharedPreferences.getString(TAG_AGE, null),
                sharedPreferences.getString(TAG_USERNAME, null),
                sharedPreferences.getString(TAG_U_WEIGHT, null),
                sharedPreferences.getString(TAG_ALLERGY, null),
                sharedPreferences.getString(TAG_BGROUP, null),
                sharedPreferences.getString(TAG_A_DETAILS, null),
                sharedPreferences.getString(TAG_B_PROB, null),
                sharedPreferences.getString(TAG_GENDER, null),
                sharedPreferences.getString(TAG_U_TYPE, null),
                sharedPreferences.getString(TAG_R_MNO, null));
    }

    public boolean isLogin()
    {
        return sharedPreferences.getBoolean(TAG_IS_LOGIN, false);
    }

    public void setLoginData(LoginDataModel paramLoginDataModel) {
        editor.putString(TAG_ID, paramLoginDataModel.getId());
        editor.putString(TAG_FULLNAME, paramLoginDataModel.getFullName());
        editor.putString(TAG_MNO, paramLoginDataModel.getContactNumber());
        editor.putString(TAG_AGE, paramLoginDataModel.getAge());
        editor.putString(TAG_USERNAME, paramLoginDataModel.getUserName());
        editor.putString(TAG_U_WEIGHT, paramLoginDataModel.getWeight());
        editor.putString(TAG_ALLERGY, paramLoginDataModel.getAllergy());
        editor.putString(TAG_BGROUP, paramLoginDataModel.getBloodGroup());
        editor.putString(TAG_A_DETAILS, paramLoginDataModel.getAllergyDetails());
        editor.putString(TAG_B_PROB, paramLoginDataModel.getBreathing());
        editor.putString(TAG_GENDER, paramLoginDataModel.getGender());
        editor.putString(TAG_U_TYPE, paramLoginDataModel.getType());
        editor.putString(TAG_R_MNO, paramLoginDataModel.getR_mno());
        editor.commit();
    }

    public void setLoginStatus(boolean paramBoolean)  {
        editor.putBoolean(TAG_IS_LOGIN, paramBoolean);
        editor.commit();
    }
}
