package com.jucanos.photomap.ListView;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.jucanos.photomap.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

public class NoticeListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<NoticeListViewItem> listViewItemList = new ArrayList<NoticeListViewItem>();

    // ListViewAdapter의 생성자
    public NoticeListViewAdapter() {
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item_group" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_notice, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        final Button button_title = convertView.findViewById(R.id.button_title);
        final TextView textView_description = convertView.findViewById(R.id.textView_description);
        final ExpandableLayout expandableLayout_el = convertView.findViewById(R.id.expandableLayout_el);
        // 아이템 내 각 위젯에 데이터 반영

        NoticeListViewItem listViewItem = listViewItemList.get(position);
        button_title.setText(listViewItem.getTitle());
        button_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableLayout_el.toggle();
            }
        });

        button_title.setText(listViewItem.getTitle());
        textView_description.setText(listViewItem.getContext());
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public NoticeListViewItem getItem(int position) {
        return listViewItemList.get(position);
    }

    public void addItem(NoticeListViewItem noticeListViewItem){
        listViewItemList.add(noticeListViewItem);
    }

    public void clear() {
        listViewItemList = new ArrayList<NoticeListViewItem>();
    }

}