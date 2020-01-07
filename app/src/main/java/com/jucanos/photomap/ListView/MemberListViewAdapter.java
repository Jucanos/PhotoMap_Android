package com.jucanos.photomap.ListView;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.jucanos.photomap.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<MemberListViewItem> listViewItemList = new ArrayList<MemberListViewItem>();

    // ListViewAdapter의 생성자
    public MemberListViewAdapter() {

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

        // "group_listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.member_listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        CircleImageView imgView_thumbnail = (CircleImageView) convertView.findViewById(R.id.imgView_thumbnail);
        TextView txtView_groupName = (TextView) convertView.findViewById(R.id.txtView_groupName);
        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        MemberListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        imgView_thumbnail.setClipToOutline(true);
        if(listViewItem.getThumbnail() == null){
            imgView_thumbnail.setImageResource(R.drawable.ic_add_button_inside_black_circle);
        }else{
            imgView_thumbnail.setImageBitmap(listViewItem.getThumbnail());
        }
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

    public void addItem(Bitmap thumnail, String title) {
        MemberListViewItem item = new MemberListViewItem();
        item.setThumbnail(thumnail);
        item.setNmae(title);
        listViewItemList.add(item);
    }

    public void clear() {
        listViewItemList = new ArrayList<MemberListViewItem>();
    }

}