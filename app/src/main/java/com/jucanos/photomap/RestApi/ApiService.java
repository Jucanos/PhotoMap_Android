package com.jucanos.photomap.RestApi;

import com.jucanos.photomap.Structure.Authorization;
import com.jucanos.photomap.Structure.CreateMap;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @GET("dev/users")
    Call<Authorization> loginAccount(@Header("Authorization") String authorization);

    @DELETE("dev/users")
    Call<Authorization> signoutAccount(@Header("Authorization") String authorization);

//    @GET("accounts/{accountId}")
//    Call<Authorization> getAccountInfo(@Header("Authorization") String authKey,//해더에 key Authorization String 형태의 토큰을 요구함, {}
//                                       @Path("accountId") String accountId) ;
//
    @POST("dev/maps")
    Call<CreateMap> createMap(@Header("Authorization") String authKey) ;
//
//    @FormUrlEncoded
//    @POST("user/login")
//    Call<Result> getInfo(@Field("params") String name) ;
}
