package com.jucanos.photomap.util;

import android.util.Log;

import com.google.gson.JsonObject;
import com.jucanos.photomap.Structure.Authorization;

import java.util.ArrayList;

import javax.xml.transform.Result;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("dev/users")
    Call<Authorization> loginAccount(@Header("Authorization") String authorization);

    @DELETE("dev/users")
    Call<Authorization> signoutAccount(@Header("Authorization") String authorization);

//    @GET("accounts/{accountId}")
//    Call<Authorization> getAccountInfo(@Header("Authorization") String authKey,//해더에 key Authorization String 형태의 토큰을 요구함, {}
//                                       @Path("accountId") String accountId) ;
//
//    @GET("accounts/test")
//    Call<Authorization> getTestApi(@Header("Authorization") String authKey,//해더에 key Authorization String 형태의 토큰을 요구함
//                                   @Query("params") String accountId) ;
//
//    @FormUrlEncoded
//    @POST("user/login")
//    Call<Result> getInfo(@Field("params") String name) ;
}
