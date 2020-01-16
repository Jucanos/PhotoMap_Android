package com.jucanos.photomap.RestApi;

import com.jucanos.photomap.Structure.Authorization;
import com.jucanos.photomap.Structure.CreateMap;
import com.jucanos.photomap.Structure.GetMapList;
import com.jucanos.photomap.Structure.RequestCreateMap;
import com.jucanos.photomap.Structure.RequestUserRemove;
import com.jucanos.photomap.Structure.UserRemove;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("dev/users")
    Call<Authorization> loginAccount(@Header("Authorization") String authorization);

    @DELETE("dev/users")
    Call<Authorization> signoutAccount(@Header("Authorization") String authorization);

    @GET("dev/maps")
    Call<GetMapList> getMapList(@Header("Authorization") String authKey);

    @POST("dev/maps")
    Call<CreateMap> createMap(
            @Header("Authorization") String authorization,
            @Body RequestCreateMap requestCreateMap);

    @PATCH("dev/maps/{mid}")
    Call<UserRemove> userRemove(@Header("Authorization") String authorization,@Path("mid") String mid, @Body RequestUserRemove requestUserRemove);

//    @FormUrlEncoded
//    @POST("user/login")
//    Call<Result> getInfo(@Field("params") String name) ;
}
