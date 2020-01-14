package com.jucanos.photomap.Activity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.jucanos.photomap.ListView.NoticeListViewAdapter;
import com.jucanos.photomap.R;


public class NoticeActivity extends AppCompatActivity {
    ListView listView_notice;
    NoticeListViewAdapter noticeListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        noticeListViewAdapter = new NoticeListViewAdapter();
        listView_notice = findViewById(R.id.listView_notice);

        // 리스뷰 참조 및 Adapter달기
        listView_notice.setAdapter(noticeListViewAdapter);
        addNoticeTest();


    }

    // ====================================================================== for test Code
    // ====================================================================== for test Code
    void addNoticeTest() {
        noticeListViewAdapter.addItem("공지사항1", "공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항");
        noticeListViewAdapter.addItem("공지사항2", "공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항");
        noticeListViewAdapter.addItem("공지사항3", "공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항");
        noticeListViewAdapter.addItem("공지사항4", "공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항공지사항");
        noticeListViewAdapter.notifyDataSetChanged();
    }

}
