package com.jucanos.photomap.ListView;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.jucanos.photomap.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<MemberListViewItem> listViewItemList = new ArrayList<MemberListViewItem>();
    private Context mContext;

    // ListViewAdapter의 생성자
    public MemberListViewAdapter(Context context) {
        mContext = context;
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

        // "listview_item_group" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_member, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        final ImageView imageView_thumbnail = convertView.findViewById(R.id.imageView_thumbnail);
        final TextView txtView_groupName = convertView.findViewById(R.id.textView_groupName);
        // GetUserInfoData Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        MemberListViewItem listViewItem = listViewItemList.get(position);
        imageView_thumbnail.setClipToOutline(true);

        Glide.with(mContext)
                .load(listViewItem.getThumbnail())
                .placeholder(R.drawable.logo)
                 //.error(R.drawable.ic_icon_add_button_inside_black)
                .dontAnimate()
                .into(imageView_thumbnail);

        txtView_groupName.setText(listViewItem.getName());
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public MemberListViewItem getItem(int position) {
        return listViewItemList.get(position);
    }

    public void addItem(MemberListViewItem item) {
        listViewItemList.add(item);
    }

    public void clear() {
        listViewItemList = new ArrayList<MemberListViewItem>();
    }

}