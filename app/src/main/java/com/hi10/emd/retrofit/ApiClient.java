package com.hi10.emd.retrofit;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.hi10.emd.helper.Constants.BASE_URL;


public class ApiClient {
    private static Retrofit retrofit;

    public static Retrofit getClient()
    {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}