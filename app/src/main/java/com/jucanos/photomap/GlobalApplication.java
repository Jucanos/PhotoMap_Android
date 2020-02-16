package com.jucanos.photomap;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.loader.IBoxingMediaLoader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jucanos.photomap.Structure.GetUserInfo;
import com.jucanos.photomap.photoPicker.BoxingGlideLoader;
import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import javax.security.auth.callback.Callback;

public class GlobalApplication extends Application {
    private static GlobalApplication instance;
    private static Context appContext;
    public String token;
    public GetUserInfo authorization;
    public HashMap<String, String> cityKeyString = new HashMap<>();
    public HashMap<Integer, String> cityKeyInt = new HashMap<>();

    public HashMap<String, String> userThumbnail = new HashMap<>();
    public HashMap<String, String> userNickName = new HashMap<>();

    // for log

    public HashMap<String,Long> mLog=  new HashMap<>();

    public static GlobalApplication getInstance() {
        return instance;
    }

    public static final GlobalApplication getGlobalApplicationContext() {
        if (instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }

    private static class KakaoSDKAdapter extends KakaoAdapter {
        /**
         * Session Config에 대해서는 default값들이 존재한다.
         * 필요한 상황에서만 override해서 사용하면 됨.
         *
         * @return Session의 설정값.
         */
        @Override
        public ISessionConfig getSessionConfig() {
            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[]{AuthType.KAKAO_LOGIN_ALL};
                }

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                }

                @Override
                public boolean isSecureMode() {
                    return false;
                }

                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                }

                @Override
                public boolean isSaveFormData() {
                    return true;
                }
            };
        }

        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Context getApplicationContext() {
                    return GlobalApplication.getGlobalApplicationContext();
                }
            };
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        cityKeyString.put("경기", "gyeonggi");
        cityKeyString.put("강원", "gangwon");
        cityKeyString.put("충북", "chungbuk");
        cityKeyString.put("충남", "chungnam");
        cityKeyString.put("전북", "jeonbuk");
        cityKeyString.put("전남", "jeonnam");
        cityKeyString.put("경북", "gyeongbuk");
        cityKeyString.put("경남", "gyeongnam");
        cityKeyString.put("제주", "jeju");

        cityKeyInt.put(1, "gyeonggi");
        cityKeyInt.put(2, "gangwon");
        cityKeyInt.put(3, "chungbuk");
        cityKeyInt.put(4, "chungnam");
        cityKeyInt.put(5, "jeonbuk");
        cityKeyInt.put(6, "jeonnam");
        cityKeyInt.put(7, "gyeongbuk");
        cityKeyInt.put(8, "gyeongnam");
        cityKeyInt.put(9, "jeju");


        // for photoPicker image glider
        appContext = getApplicationContext();
        IBoxingMediaLoader loader = new BoxingGlideLoader();
        BoxingMediaLoader.getInstance().init(loader);

        instance = this;
        KakaoSDK.init(new KakaoSDKAdapter());
    }

//    public void saveRepMid(String mid) {
//        SharedPreferences pref = getSharedPreferences("mid", MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putString("mid", mid); //키값, 저장값
//        editor.commit();
//    }
//
//    public String getRePMid() {
//        SharedPreferences prefs = getSharedPreferences("mid", MODE_PRIVATE);
//        String result = prefs.getString("mid", ""); //키값, 디폴트값
//        return result;
//    }
}