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
    public HashMap<String, String> citiKeyString = new HashMap<>();
    public HashMap<Integer, String> citiKeyInt = new HashMap<>();

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
        citiKeyString.put("경기", "gyeonggi");
        citiKeyString.put("강원", "gangwon");
        citiKeyString.put("충북", "chungbuk");
        citiKeyString.put("충남", "chungnam");
        citiKeyString.put("전북", "jeonbuk");
        citiKeyString.put("전남", "jeonnam");
        citiKeyString.put("경북", "gyeongbuk");
        citiKeyString.put("경남", "gyeongnam");
        citiKeyString.put("제주", "jeju");

        citiKeyInt.put(1, "gyeonggi");
        citiKeyInt.put(2, "gangwon");
        citiKeyInt.put(3, "chungbuk");
        citiKeyInt.put(4, "chungnam");
        citiKeyInt.put(5, "jeonbuk");
        citiKeyInt.put(6, "jeonnam");
        citiKeyInt.put(7, "gyeongbuk");
        citiKeyInt.put(8, "gyeongnam");
        citiKeyInt.put(9, "jeju");

        instance = this;
        KakaoSDK.init(new KakaoSDKAdapter());
    }
}