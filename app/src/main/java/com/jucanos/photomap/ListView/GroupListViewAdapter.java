package com.jucanos.photomap.ListView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jucanos.photomap.R;

import java.io.InputStream;
import java.util.ArrayList;


public class GroupListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<GroupListViewItem> listViewItemList = new ArrayList<GroupListViewItem>();

    // ListViewAdapter의 생성자
    public GroupListViewAdapter() {}

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_group, parent, false);
        }

        ImageView imgView_thumbnail = convertView.findViewById(R.id.imageView_thumbnail);
        TextView txtView_groupName = convertView.findViewById(R.id.textView_groupName);

        GroupListViewItem listViewItem = listViewItemList.get(position);


        String thumbnail_path = "https://s3.soybeans.tech/uploads/" + listViewItem.getMid() + "/main.png";
        Glide.with(context)
                .load(thumbnail_path)
                .placeholder(R.drawable.logo)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imgView_thumbnail);
        txtView_groupName.setText(listViewItem.getTitle());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public GroupListViewItem getItem(int position) {
        return listViewItemList.get(position);
    }

    public void addItem(GroupListViewItem item) {
        listViewItemList.add(item);
    }

    public void delete(int position) {
        listViewItemList.remove(position);
    }

    public void clear() {
        listViewItemList = new ArrayList<GroupListViewItem>();
    }
}