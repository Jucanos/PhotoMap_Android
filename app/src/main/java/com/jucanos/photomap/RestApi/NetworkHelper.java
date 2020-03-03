package com.jucanos.photomap.RestApi;

import com.jucanos.photomap.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkHelper {
    private static String url = "https://soybeans.tech/api/";



    private static NetworkHelper ourInstance = new NetworkHelper();

    public static NetworkHelper getInstance() {
        return ourInstance;
    }

    private NetworkHelper() {
    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
            .build();

    ApiService service = retrofit.create(ApiService.class);

    public ApiService getService() {
        return service;
    }
}