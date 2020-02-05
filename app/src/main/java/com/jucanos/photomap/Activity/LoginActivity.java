package com.jucanos.photomap.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.MyFirebaseMessagingService;
import com.jucanos.photomap.R;
import com.jucanos.photomap.Structure.GetUserInfo;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class LoginActivity extends AppCompatActivity {
    private SessionCallback callback;
    public GlobalApplication globalApplication;
    private String mid;
    private Boolean fromLink = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.e("[hash_key]", getKeyHash(getApplicationContext()));
        globalApplication = GlobalApplication.getGlobalApplicationContext();

        if(getIntent().getData() != null) {
            mid = Objects.requireNonNull(getIntent().getData()).getQueryParameter("mid");
            fromLink = true;
            ActivityCompat.finishAffinity(this);
        }
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
        if(fromLink){
            Log.e("LoginActivity","[redirectSignupActivity][mid]" + mid);
            intent.putExtra("mid",mid);
        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM Login","getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        Log.e("FCM Token : ", token);

                        MyFirebaseMessagingService.subscribe(globalApplication.authorization.getUserData().getUid());
                        // Toast.makeText(LoginActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });


        startActivity(intent);
        finish();
    }

    public void requestLoginAccount(final String token) {
        final Call<GetUserInfo> res = NetworkHelper.getInstance().getService().loginAccount("Bearer " + token);
        res.enqueue(new Callback<GetUserInfo>() {
            @Override
            public void onResponse(Call<GetUserInfo> call, Response<GetUserInfo> response) {
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
            public void onFailure(Call<GetUserInfo> call, Throwable t) {
                Log.e("[onFailure]", t.getLocalizedMessage());
            }
        });
    }


}
