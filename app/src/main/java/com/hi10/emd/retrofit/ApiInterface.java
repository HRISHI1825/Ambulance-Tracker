package com.hi10.emd.retrofit;


import com.hi10.emd.activity.SignUpActivity;
import com.hi10.emd.model.BGroupParamModel;
import com.hi10.emd.model.HealthRecords.GetAllHealthParamModel;
import com.hi10.emd.model.HealthRecords.HealthParamModel;
import com.hi10.emd.model.HealthRecords.HealthRootModel;
import com.hi10.emd.model.Hospitals.HospitalRootModel;
import com.hi10.emd.model.SignUp.SignUpParamModel;
import com.hi10.emd.model.SignUp.SignUpRootModel;
import com.hi10.emd.model.getDataByType.RegDataByTypeParamModel;
import com.hi10.emd.model.getDataByType.RegDataByTypeRootModel;
import com.hi10.emd.model.login.LoginParamModel;
import com.hi10.emd.model.login.LoginRootModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import static com.hi10.emd.helper.Constants.*;

public interface ApiInterface {

    @POST(login)
    Call<LoginRootModel> LOGIN_ROOT_MODEL_CALL(@Body LoginParamModel paramLoginParamsModel);


 @POST(signup)
    Call<SignUpRootModel> SIGN_UP_ROOT_MODEL_CALL(@Body SignUpParamModel signUpParamModel);

 @POST(getRegDataByType)
    Call<RegDataByTypeRootModel> REG_DATA_BY_TYPE_ROOT_MODEL_CALL(@Body RegDataByTypeParamModel regDataByTypeParamModel);

 @POST(gethospitals)
    Call<HospitalRootModel> HOSPITAL_ROOT_MODEL_CALL();

 @POST(save_health)
    Call<HealthRootModel> HEALTH_ROOT_MODEL_CALL(@Body HealthParamModel healthParamModel);

 @POST(gethealthData)
    Call<HealthRootModel> GET_ALL_HEALTH__MODEL_CALL(@Body GetAllHealthParamModel getAllHealthParamModel);


    @POST(getBGroup)
    Call<RegDataByTypeRootModel> GET_BGROUP(@Body BGroupParamModel bGroupParamModel);



}
