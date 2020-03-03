package com.jucanos.photomap.Fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
import com.kakao.network.ErrorResult;
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

        TextView textView_policy = view.findViewById(R.id.textView_policy);
        textView_policy.setOnClickListener(v -> {
            YesNoDialog yesNoDialog = new YesNoDialog(getActivity(),"개인정보 처리방침 링크로 이동하시겠습니까?");
            yesNoDialog.setDialogListener(new YesNoDialogListener() {
                @Override
                public void onPositiveClicked() {
                    yesNoDialog.dismiss();
                    Uri uri = Uri.parse("http://s3.soybeans.tech/PhotoMap_Privacy_Policy.html");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

                @Override
                public void onNegativeClicked() {
                    yesNoDialog.dismiss();
                }
            });
            yesNoDialog.show();
        });

        TextView textView_review= view.findViewById(R.id.textView_review);
        textView_review.setOnClickListener(v -> {
            Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" +  getActivity().getPackageName())));
            }
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
        getActivity().overridePendingTransition(R.anim.anim_slide_in_right ,R.anim.anim_slide_out_left);
    }

    private void redirectLoginActivity() {
        final Intent intent = new Intent(getActivity(), LoginActivity.class);
        Objects.requireNonNull(getActivity()).startActivity(intent);
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.anim_slide_out_right ,R.anim.anim_slide_in_left);
    }

    private void requestSignoutAccount(String token) {
        final Call<GetUserInfo> res = NetworkHelper.getInstance().getService().signoutAccount(token);
        res.enqueue(new Callback<GetUserInfo>() {
            @Override
            public void onResponse(Call<GetUserInfo> call, Response<GetUserInfo> response) {
                if (response.isSuccessful()) {
                    // Log.e("MainFragmentSetting", "requestSignoutAccount isSuccessful : " + response.code());
                    UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onCompleteLogout() {
                        }
                        @Override
                        public void onSuccess(Long result) {
                            Toast.makeText(getActivity(), "회원탈퇴 성공", Toast.LENGTH_SHORT).show();
                            MyFirebaseMessagingService.unSubscribe(globalApplication.authorization.getUserData().getUid());
                            final Intent intent = new Intent(getActivity(), LoginActivity.class);
                            Objects.requireNonNull(getActivity()).startActivity(intent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(R.anim.anim_slide_out_right ,R.anim.anim_slide_in_left);
                            super.onSuccess(result);
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