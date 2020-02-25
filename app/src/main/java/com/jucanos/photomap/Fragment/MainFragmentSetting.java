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

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragmentSetting extends Fragment {
    public GlobalApplication globalApplication;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_setting, container, false);
        globalApplication  = GlobalApplication.getGlobalApplicationContext();

        Toolbar toolbar = view.findViewById(R.id.toolbar_tb);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("설정");
        setHasOptionsMenu(true);

        // userInfo
        // nickName
        TextView textView_nickName = view.findViewById(R.id.textView_nickName);
        textView_nickName.setText(globalApplication.authorization.getUserData().getNickname());

        // thumbnail
        CircleImageView circleImageView_thumbnail = view.findViewById(R.id.circleImageView_thumbnail);
        Glide.with(getActivity()).load(globalApplication.authorization.getUserData().getThumbnail()).into(circleImageView_thumbnail);

        TextView textView_notice = view.findViewById(R.id.textView_notice);
        textView_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectNoticeActivity();
            }
        });

        TextView textView_logout = view.findViewById(R.id.textView_logout);
        textView_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YesNoDialog yesNoDialog = new YesNoDialog(getActivity(),"정말 로그아웃 하시겠습니까?");
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
                yesNoDialog.show();;

            }
        });

        TextView textView_signout = view.findViewById(R.id.textView_signout);
        textView_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YesNoDialog yesNoDialog = new YesNoDialog(getActivity(),"정말 회원 탈퇴 하시겠습니까?");
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
            }
        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_add:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void redirectNoticeActivity() {
        final Intent intent = new Intent(getActivity(), NoticeActivity.class);
        getActivity().startActivity(intent);
    }

    void redirectLoginActivity() {
        final Intent intent = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    public void requestSignoutAccount(String token) {
        final Call<GetUserInfo> res = NetworkHelper.getInstance().getService().signoutAccount(token);
        res.enqueue(new Callback<GetUserInfo>() {
            @Override
            public void onResponse(Call<GetUserInfo> call, Response<GetUserInfo> response) {
                if (response.isSuccessful()) {
                    UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onCompleteLogout() {
                            MyFirebaseMessagingService.unSubscribe(globalApplication.authorization.getUserData().getUid());
                            redirectLoginActivity();
                        }
                    });
                } else {
                    Log.e("[MainFragmentSetting]", Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<GetUserInfo> call, Throwable t) {
                Log.e("[onFailure]", t.getLocalizedMessage());
            }
        });
    }
}