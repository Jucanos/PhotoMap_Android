package com.jucanos.photomap.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.jucanos.photomap.Activity.LoginActivity;
import com.jucanos.photomap.Activity.NoticeActivity;
import com.jucanos.photomap.Dialog.YesNoDialog;
import com.jucanos.photomap.Dialog.YesNoDialogListener;
import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.MyFirebaseMessagingService;
import com.jucanos.photomap.R;
import com.jucanos.photomap.Structure.GetUserInfo;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragmentSetting extends Fragment {
    private GlobalApplication globalApplication;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_setting, container, false);

        getIntentData();
        setToolbar(view);

        // set nickName
        TextView textView_nickName = view.findViewById(R.id.textView_nickName);
        textView_nickName.setText(globalApplication.authorization.getUserData().getNickname());

        // set thumbNail
        CircleImageView circleImageView_thumbnail = view.findViewById(R.id.circleImageView_thumbnail);
        Glide.with(Objects.requireNonNull(getActivity())).load(globalApplication.authorization.getUserData().getThumbnail()).into(circleImageView_thumbnail);

        TextView textView_notice = view.findViewById(R.id.textView_notice);
        textView_notice.setOnClickListener(v -> redirectNoticeActivity());

        TextView textView_logout = view.findViewById(R.id.textView_logout);
        textView_logout.setOnClickListener(v -> {
            YesNoDialog yesNoDialog = new YesNoDialog(getActivity(), "정말 로그아웃 하시겠습니까?");
            yesNoDialog.setDialogListener(new YesNoDialogListener() {
                @Override
                public void onPositiveClicked() {
                    yesNoDialog.dismiss();
                    UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onCompleteLogout() {
                            redirectLoginActivity();
                        }
                    });
                }

                @Override
                public void onNegativeClicked() {
                    yesNoDialog.dismiss();
                }
            });
            yesNoDialog.show();
            ;

        });

        TextView textView_signout = view.findViewById(R.id.textView_signout);
        textView_signout.setOnClickListener(v -> {
            YesNoDialog yesNoDialog = new YesNoDialog(getActivity(), "정말 회원 탈퇴 하시겠습니까?");
            yesNoDialog.setDialogListener(new YesNoDialogListener() {
                @Override
                public void onPositiveClicked() {
                    requestSignoutAccount(globalApplication.token);
                    yesNoDialog.dismiss();
                }

                @Override
                public void onNegativeClicked() {
                    yesNoDialog.dismiss();
                }
            });
            yesNoDialog.show();
        });

        return view;
    }

    private void getIntentData() {
        globalApplication = GlobalApplication.getGlobalApplicationContext();
    }

    private void setToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_tb);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("설정");
    }

    private void redirectNoticeActivity() {
        final Intent intent = new Intent(getActivity(), NoticeActivity.class);
        Objects.requireNonNull(getActivity()).startActivity(intent);
    }

    private void redirectLoginActivity() {
        final Intent intent = new Intent(getActivity(), LoginActivity.class);
        Objects.requireNonNull(getActivity()).startActivity(intent);
        getActivity().finish();
    }

    private void requestSignoutAccount(String token) {
        final Call<GetUserInfo> res = NetworkHelper.getInstance().getService().signoutAccount(token);
        res.enqueue(new Callback<GetUserInfo>() {
            @Override
            public void onResponse(Call<GetUserInfo> call, Response<GetUserInfo> response) {
                if (response.isSuccessful()) {
                    UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onCompleteLogout() {
                            Toast.makeText(getActivity(), "회원 탈퇴", Toast.LENGTH_SHORT).show();
                            MyFirebaseMessagingService.unSubscribe(globalApplication.authorization.getUserData().getUid());
                            redirectLoginActivity();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "요청 실패", Toast.LENGTH_SHORT).show();
                    Log.e("MainFragmentSetting", "requestSignoutAccount isNotSuccessful : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GetUserInfo> call, Throwable t) {
                Toast.makeText(getActivity(), "요청 실패", Toast.LENGTH_SHORT).show();
                Log.e("MainFragmentSetting", "requestSignoutAccount onFailure : " + t.getLocalizedMessage());
            }
        });
    }
}