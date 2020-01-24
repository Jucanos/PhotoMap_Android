package com.jucanos.photomap.RestApi;

import android.database.Observable;

import com.jucanos.photomap.Structure.CreateStory;
import com.jucanos.photomap.Structure.GetMapInfo;
import com.jucanos.photomap.Structure.GetStoryList;
import com.jucanos.photomap.Structure.GetUserInfo;
import com.jucanos.photomap.Structure.CreateMap;
import com.jucanos.photomap.Structure.GetMapList;
import com.jucanos.photomap.Structure.RequestCreateMap;
import com.jucanos.photomap.Structure.RequestUserRemove;
import com.jucanos.photomap.Structure.RemoveUser;
import com.jucanos.photomap.Structure.SetRep;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface ApiService {
    @GET("dev/users")
    Call<GetUserInfo> loginAccount(@Header("Authorization") String authorization);

    @DELETE("dev/users")
    Call<GetUserInfo> signoutAccount(@Header("Authorization") String authorization);

    @GET("dev/maps")
    Call<GetMapList> getMapList(@Header("Authorization") String authKey);

    @POST("dev/maps")
    Call<CreateMap> createMap(
            @Header("Authorization") String authorization,
            @Body RequestCreateMap requestCreateMap);

    @PATCH("dev/maps/{mid}")
    Call<RemoveUser> userRemove(@Header("Authorization") String authorization, @Path("mid") String mid, @Body RequestUserRemove requestUserRemove);

    @Multipart
    @POST("dev/stories/{mid}")
    Call<CreateStory> createStory(
            @Header("Authorization") String authorization,
            @Path("mid") String mid,
            @PartMap HashMap<String, RequestBody> info,
            @Part List<MultipartBody.Part> files
    );

    @GET("dev/stories/{mid}/{cityKey}")
    Call<GetStoryList> getStoryList(@Header("Authorization") String authKey,@Path("mid") String mid, @Path("cityKey") String cityKey);

    @GET("dev/maps/{mid}")
    Call<GetMapInfo> getMapInfo(@Header("Authorization") String authKey, @Path("mid") String mid);

    @Multipart
    @POST("dev/maps/{mid}")
    Call<SetRep> setRep(
            @Header("Authorization") String authorization,
            @Path("mid") String mid,
            @PartMap HashMap<String, RequestBody> info,
            @Part MultipartBody.Part file
    );
//    @FormUrlEncoded
//    @POST("user/login")
//    Call<Result> getInfo(@Field("params") String name) ;
}
