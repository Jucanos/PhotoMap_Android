package com.jucanos.photomap;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.jucanos.photomap.Structure.GetUserInfo;
import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

import java.util.HashMap;

public class GlobalApplication extends Application {
    private static GlobalApplication instance;
    public String token;
    public GetUserInfo authorization;
    public HashMap<String, String> cityKeyString = new HashMap<>();
    public HashMap<Integer, String> cityKeyInt = new HashMap<>();

    public static GlobalApplication getInstance() {
        return instance;
    }

    public static final GlobalApplication getGlobalApplicationContext() {
        if (instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }

    private static class KakaoSDKAdapter extends KakaoAdapter {

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
                public Activity getTopActivity() {
                    return null;
                }

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

        instance = this;
        KakaoSDK.init(new KakaoSDKAdapter());
    }
}