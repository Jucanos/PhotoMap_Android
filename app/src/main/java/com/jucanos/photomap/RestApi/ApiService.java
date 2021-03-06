package com.jucanos.photomap.RestApi;

import com.jucanos.photomap.Structure.CreateStory;
import com.jucanos.photomap.Structure.EditGroup;
import com.jucanos.photomap.Structure.EditGroupRequest;
import com.jucanos.photomap.Structure.EditStory;
import com.jucanos.photomap.Structure.EditStoryRequest;
import com.jucanos.photomap.Structure.GetMapInfo;
import com.jucanos.photomap.Structure.GetNotice;
import com.jucanos.photomap.Structure.GetStoryList;
import com.jucanos.photomap.Structure.GetUserInfo;
import com.jucanos.photomap.Structure.CreateMap;
import com.jucanos.photomap.Structure.GetMapList;
import com.jucanos.photomap.Structure.CreateMapRequest;
import com.jucanos.photomap.Structure.RemoveStory;
import com.jucanos.photomap.Structure.RemoveUserRequest;
import com.jucanos.photomap.Structure.RemoveUser;
import com.jucanos.photomap.Structure.SetMapRep;
import com.jucanos.photomap.Structure.SetMapRepRequest;
import com.jucanos.photomap.Structure.SetRep;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface ApiService {
    @GET("users")
    Call<GetUserInfo> loginAccount(@Header("Authorization") String authorization);

    @DELETE("users")
    Call<GetUserInfo> signoutAccount(@Header("Authorization") String authorization);

    @GET("maps")
    Call<GetMapList> getMapList(@Header("Authorization") String authKey);

    @POST("maps")
    Call<CreateMap> createMap(
            @Header("Authorization") String authorization,
            @Body CreateMapRequest createMapRequest);

    @PATCH("maps/{mid}")
    Call<RemoveUser> userRemove(@Header("Authorization") String authorization, @Path("mid") String mid, @Body RemoveUserRequest removeUserRequest);

    @Multipart
    @POST("stories/{mid}")
    Call<CreateStory> createStory(
            @Header("Authorization") String authorization,
            @Path("mid") String mid,
            @PartMap HashMap<String, RequestBody> info,
            @Part List<MultipartBody.Part> files
    );

    @GET("stories/{mid}/{cityKey}")
    Call<GetStoryList> getStoryList(@Header("Authorization") String authKey, @Path("mid") String mid, @Path("cityKey") String cityKey);

    @GET("maps/{mid}")
    Call<GetMapInfo> getMapInfo(@Header("Authorization") String authKey, @Path("mid") String mid);

    @Multipart
    @POST("maps/{mid}")
    Call<SetRep> setRep(
            @Header("Authorization") String authorization,
            @Path("mid") String mid,
            @PartMap HashMap<String, RequestBody> info,
            @Part MultipartBody.Part file
    );

    @PUT("maps/{mid}")
    Call<EditGroup> editGroup(@Header("Authorization") String authKey,
                              @Path("mid") String mid,
                              @Body EditGroupRequest editGroupRequest
    );

    @DELETE("stories/{sid}")
    Call<RemoveStory> removeStory(@Header("Authorization") String authorization, @Path("sid") String sid);

    @PATCH("stories/{sid}")
    Call<EditStory> editStory(@Header("Authorization") String authorization, @Path("sid") String sid, @Body EditStoryRequest editStoryRequest);

    @GET("notice")
    Call<GetNotice> getNotice();

    // 대표 지도 설정
    @PATCH("users/{mid}")
    Call<SetMapRep> setMapRep(@Header("Authorization") String authorization, @Path("mid") String mid, @Body SetMapRepRequest setMapRepRequest);
}
