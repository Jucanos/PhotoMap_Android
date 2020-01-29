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

import com.jucanos.photomap.Activity.LoginActivity;
import com.jucanos.photomap.Activity.NoticeActivity;
import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.R;
import com.jucanos.photomap.Structure.GetUserInfo;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragmentSetting extends Fragment {
    public GlobalApplication globalApplication;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_setting, container, false);
        globalApplication = globalApplication = GlobalApplication.getGlobalApplicationContext();

        Toolbar toolbar = view.findViewById(R.id.toolbar_tb);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("설정");
        setHasOptionsMenu(true);

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
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        redirectLoginActivity();
                    }
                });
            }
        });

        TextView textView_signout = view.findViewById(R.id.textView_signout);
        textView_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSignoutAccount(globalApplication.token);
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
        final Call<GetUserInfo> res = NetworkHelper.getInstance().getService().signoutAccount("Bearer " + token);
        res.enqueue(new Callback<GetUserInfo>() {
            @Override
            public void onResponse(Call<GetUserInfo> call, Response<GetUserInfo> response) {
                if (response.isSuccessful()) {
                    UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onCompleteLogout() {
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