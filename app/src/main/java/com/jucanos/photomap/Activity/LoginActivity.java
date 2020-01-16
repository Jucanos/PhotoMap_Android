package com.jucanos.photomap.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.R;
import com.jucanos.photomap.Structure.Authorization;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.jucanos.photomap.Structure.GetMapList;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class LoginActivity extends AppCompatActivity {
    private SessionCallback callback;
    public GlobalApplication globalApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.e("[hash_key]", getKeyHash(getApplicationContext()));
        globalApplication = GlobalApplication.getGlobalApplicationContext();

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w("[getKeyHash]", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            Log.e("[getAccessToken]", Session.getCurrentSession().getAccessToken());
            globalApplication.token = Session.getCurrentSession().getAccessToken();
            requestLoginAccount(Session.getCurrentSession().getAccessToken());

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
        }
    }

    protected void redirectSignupActivity() {
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void requestLoginAccount(final String token) {
        final Call<Authorization> res = NetworkHelper.getInstance().getService().loginAccount("Bearer " + token);
        res.enqueue(new Callback<Authorization>() {
            @Override
            public void onResponse(Call<Authorization> call, Response<Authorization> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        globalApplication.authorization = response.body();
                        Log.e("getUid", globalApplication.authorization.getUserData().getUid());
                        Log.e("getThumbnail", globalApplication.authorization.getUserData().getThumbnail());
                        Log.e("getNickname", globalApplication.authorization.getUserData().getNickname());
                        redirectSignupActivity();
                    }
                } else {
                    Log.e("requestLoginAccount", "!!!isSuccessful");
                }
            }

            @Override
            public void onFailure(Call<Authorization> call, Throwable t) {
                Log.e("[onFailure]", t.getLocalizedMessage());
            }
        });
    }


}
