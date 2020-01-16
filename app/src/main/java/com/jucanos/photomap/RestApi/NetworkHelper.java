package com.jucanos.photomap.RestApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkHelper {
    private static NetworkHelper ourInstance = new NetworkHelper();

    public static NetworkHelper getInstance() {
        return ourInstance;
    }

    private NetworkHelper() {
    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://soybeans.tech/")
            .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
            .build();

    ApiService service = retrofit.create(ApiService.class);

    public ApiService getService() {
        return service;
    }
}