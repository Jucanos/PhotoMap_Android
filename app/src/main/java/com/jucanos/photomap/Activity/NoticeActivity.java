package com.jucanos.photomap.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jucanos.photomap.ListView.MemberListViewItem;
import com.jucanos.photomap.ListView.NoticeListViewAdapter;
import com.jucanos.photomap.ListView.NoticeListViewItem;
import com.jucanos.photomap.R;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.jucanos.photomap.Structure.GetMapInfo;
import com.jucanos.photomap.Structure.GetNotice;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NoticeActivity extends AppCompatActivity {
    ListView listView_notice;
    NoticeListViewAdapter noticeListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        Toolbar toolbar = findViewById(R.id.toolbar_tb);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("공지사항");

        noticeListViewAdapter = new NoticeListViewAdapter();
        listView_notice = findViewById(R.id.listView_notice);

        // 리스뷰 참조 및 Adapter달기
        listView_notice.setAdapter(noticeListViewAdapter);
        getNoticeRequest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    void getNoticeRequest() {
        final Call<GetNotice> res = NetworkHelper.getInstance().getService().getNotice();
        res.enqueue(new Callback<GetNotice>() {
            @Override
            public void onResponse(Call<GetNotice> call, Response<GetNotice> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().getGetNoticeData().size(); i++) {
                        Log.e("NoticeActivity", "[Context] : " + response.body().getGetNoticeData().get(i).getContext());
                        Log.e("NoticeActivity", "[CreatedAt] : " + response.body().getGetNoticeData().get(i).getCreatedAt());
                        Log.e("NoticeActivity", "[Id] : " + response.body().getGetNoticeData().get(i).getId());
                        Log.e("NoticeActivity", "[Title] : " + response.body().getGetNoticeData().get(i).getTitle());
                        Log.e("NoticeActivity", "[UpdatedAt] : " + response.body().getGetNoticeData().get(i).getUpdatedAt());
                        String context = response.body().getGetNoticeData().get(i).getContext();
                        String createdAt = response.body().getGetNoticeData().get(i).getCreatedAt();
                        String id = response.body().getGetNoticeData().get(i).getId();
                        String title = response.body().getGetNoticeData().get(i).getTitle();
                        String updatedAt = response.body().getGetNoticeData().get(i).getUpdatedAt();
                        NoticeListViewItem noticeListViewItem = new NoticeListViewItem(context, createdAt, id, title, updatedAt);
                        noticeListViewAdapter.addItem(noticeListViewItem);
                    }
                    noticeListViewAdapter.notifyDataSetChanged();
                } else {
                    Log.e("requestCreateMap", Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<GetNotice> call, Throwable t) {
                Log.e("[onFailure]", t.getLocalizedMessage());
            }
        });
    }
}
