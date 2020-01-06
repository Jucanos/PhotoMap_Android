package com.jucanos.photomap.Fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.jucanos.photomap.ListView.ListViewAdapter;
import com.jucanos.photomap.R;

public class TabFragment1 extends Fragment {
    private RelativeLayout noGroup, existGroup;
    private ImageButton imgBtn_addGroup;
    private ListView listView_group;

    private int groupCnt = 0;
    private ListViewAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup fragmentContainer, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_1, fragmentContainer, false);
        noGroup = (RelativeLayout) view.findViewById(R.id.layout_noGroup);
        existGroup = (RelativeLayout) view.findViewById(R.id.layout_existGroup);
        imgBtn_addGroup = (ImageButton) view.findViewById(R.id.imgBtn_addGroup);
        listView_group = (ListView) view.findViewById(R.id.listView_group);
        // Adapter 생성
        adapter = new ListViewAdapter();
        // 리스트뷰 참조 및 Adapter달기
        listView_group.setAdapter(adapter);

        imgBtn_addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGroupTest();
            }
        });

        setLayout();
        return view;
    }
    void setLayout() {
        if (groupCnt == 0) {
            noGroup.setVisibility(View.VISIBLE);
            existGroup.setVisibility(View.GONE);
        } else {
            noGroup.setVisibility(View.GONE);
            existGroup.setVisibility(View.VISIBLE);
        }
    }
    void addGroupTest(){
        Drawable drawable = getResources().getDrawable(R.drawable.test_image);
        Bitmap bm = ((BitmapDrawable)drawable).getBitmap();
        adapter.addItem(bm, "Group Name");
        adapter.notifyDataSetChanged();
        groupCnt += 1;
        setLayout();
    }
}

