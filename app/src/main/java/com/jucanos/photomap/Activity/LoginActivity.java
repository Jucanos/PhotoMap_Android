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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.MyFirebaseMessagingService;
import com.jucanos.photomap.R;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.jucanos.photomap.Structure.GetUserInfo;
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

        // 링크를 타고 들어온 경우
        if (getIntent().getData() != null) {
            mid = Objects.requireNonNull(getIntent().getData()).getQueryParameter("mid");
            fromLink = true;
            ActivityCompat.finishAffinity(this);
        }

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    /**
     * kakao hash key
     */
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

    /**
     * kakao session result
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) return;
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
            globalApplication.token = "Bearer " + Session.getCurrentSession().getAccessToken();
            requestLoginAccount();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
        }
    }


    /**
     * 링크를 타고 들어온 경우에는 CEARL_TOP으로 이전 ACTIVITY를 모두 삭제해주고, intent에 mid를 넣어준뒤 MainActivity로 넘어간다.
     */
    protected void redirectSignupActivity() {
        MyFirebaseMessagingService.subscribe(globalApplication.authorization.getUserData().getUid());
        final Intent intent = new Intent(this, MainActivity.class);
        if (fromLink) {
            intent.putExtra("mid", mid);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        startActivity(intent);
        finish();
    }

    /**
     * 로그인 리퀘스트( 카카오 세션으로부터 token 을 받은뒤 호출됨 )
     */
    public void requestLoginAccount() {
        final Call<GetUserInfo> res = NetworkHelper.getInstance().getService().loginAccount(globalApplication.token);
        res.enqueue(new Callback<GetUserInfo>() {
            @Override
            public void onResponse(Call<GetUserInfo> call, Response<GetUserInfo> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        globalApplication.authorization = response.body();
                        if(globalApplication.authorization.getUserData().getPrimary() == null)
                            globalApplication.authorization.getUserData().setThumbnail("http://s3.soybeans.tech/default_user.png");

                        Log.e("getUid", globalApplication.authorization.getUserData().getUid());
                        Log.e("getThumbnail", globalApplication.authorization.getUserData().getThumbnail());
                        Log.e("getNickname", globalApplication.authorization.getUserData().getNickname());
                        loadFireBase();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity", "requestLoginAccount isNotSuccessful() : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GetUserInfo> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                Log.e("LoginActivity", "requestLoginAccount onFailure : " + t.getLocalizedMessage());
            }
        });
    }


    /**
     * GlobalApplicatoin 에서는 MRefUser, mRefMaps 에 대해서만 로딩함
     * 때문에, 유저정보를 가져온 이후에 mRefUser 데 대해 참조설정.
     */
    public void loadFireBase() {
        globalApplication.mRefUser = globalApplication.mRefUsers.child(globalApplication.authorization.getUserData().getUid());
        redirectSignupActivity();
    }
}
