package com.jucanos.photomap.ListView;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.jucanos.photomap.R;
import com.jucanos.photomap.util.DateString;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NoticeListViewAdapter extends BaseAdapter {
    private ArrayList<NoticeListViewItem> listViewItemList = new ArrayList<NoticeListViewItem>();

    public NoticeListViewAdapter() {
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_notice, parent, false);
        }

        final Button button_title = convertView.findViewById(R.id.button_title);
        final TextView textView_description = convertView.findViewById(R.id.textView_description);
        final ExpandableLayout expandableLayout_el = convertView.findViewById(R.id.expandableLayout_el);

        NoticeListViewItem listViewItem = listViewItemList.get(position);
        button_title.setText(listViewItem.getTitle());
        button_title.setOnClickListener(v -> expandableLayout_el.toggle());

        button_title.setText(listViewItem.getTitle());
        String dateString = "게시 : " + DateString.getString(listViewItem.getCreatedAt()) + "\n\n" +
                listViewItem.getCreatedAt() + listViewItem.getContext();
        textView_description.setText(dateString);
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

    public void addItem(NoticeListViewItem noticeListViewItem) {
        listViewItemList.add(noticeListViewItem);
    }

    public void clear() {
        listViewItemList = new ArrayList<>();
    }

}