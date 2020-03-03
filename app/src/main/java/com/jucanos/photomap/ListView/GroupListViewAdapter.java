package com.jucanos.photomap.ListView;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.R;
import com.jucanos.photomap.Structure.FirebaseUserData;
import com.jucanos.photomap.util.DateString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class GroupListViewAdapter extends BaseAdapter {
    private ArrayList<GroupListViewItem> listViewItemList = new ArrayList<>();
    private boolean activated = false;

    public GroupListViewAdapter() {
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_group, parent, false);
        }

        // load global context
        final GlobalApplication globalContext = GlobalApplication.getGlobalApplicationContext();

        // load view
        final ImageView imgView_thumbnail = convertView.findViewById(R.id.imageView_thumbnail);
        final TextView textView_groupName = convertView.findViewById(R.id.textView_groupName);
        final TextView textView_lastUpdated = convertView.findViewById(R.id.textView_lastUpdated);
        final TextView textView_log = convertView.findViewById(R.id.textView_log);
        final RelativeLayout rl_container = convertView.findViewById(R.id.rl_container);

        // get Item
        final GroupListViewItem listViewItem = listViewItemList.get(position);

        // title thumbnail
        final String thumbnail_path = "https://s3.soybeans.tech/uploads/" + context.getResources().getString(R.string.server_url) + "/" + listViewItem.getMid() + "/main.png";

        Glide.with(context)
                .load(thumbnail_path)
                .signature(new ObjectKey(listViewItem.getUserNumber()))
                //.skipMemoryCache(true)
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.default_user)
                .override(200, 200)
                .centerCrop()
                .into(imgView_thumbnail);


        long gap = listViewItem.getCurLog() - listViewItem.getPastLog();
        if (gap > 0) {
            if (gap > 99) textView_log.setText("99");
            else textView_log.setText(Long.toString(gap));
            textView_log.setVisibility(View.VISIBLE);
        } else {
            textView_log.setVisibility(View.INVISIBLE);
        }


        // set Title
        textView_groupName.setText(listViewItem.getTitle());
        textView_lastUpdated.setText(DateString.getString(listViewItem.getUpdatedAt()));
        listViewItem.setOnLogCb((log, own) -> {
            if (!listViewItem.isLoaded()) {
                listViewItem.setLoad(true);
            } else if (!own) {
                listViewItem.setUpdatedAt(new Date(System.currentTimeMillis()));
            }

            if (listViewItem.getActivated() && activated) {
                listViewItem.setPastLog(log);
                listViewItem.setCurLog(log);
                globalContext.mRefUser.child(listViewItem.getMid()).setValue(listViewItem.getCurLog());
            } else {
                if (own) listViewItem.setPastLog(log);
                else {
                    listViewItem.setCurLog(log);
                    resort();
                }
            }
            notifyDataSetChanged();
        });

        if (listViewItem.getUesrValueEventListener() == null) {
            listViewItem.setUesrValueEventListener(globalContext.mRefUser.child(listViewItem.getMid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // noinspection ConstantConditions
                    Log.e("dataSapSHt", dataSnapshot.toString());
                    if (dataSnapshot.getValue() == null) {
                        globalContext.mRefUser.child(listViewItem.getMid()).removeEventListener(listViewItem.getUesrValueEventListener());
                        return;
                    }
                    listViewItem.setPastLog(dataSnapshot.getValue(long.class));
                    if (!listViewItem.getLoadUserRef()) {
                        listViewItem.setLoadUserRef(true);
                        if (listViewItem.getMapValueEventListener() == null) {
                            listViewItem.setMapValueEventListener(globalContext.mRefMaps.child(listViewItem.getMid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() == null) {
                                        globalContext.mRefMaps.child(listViewItem.getMid()).removeEventListener(listViewItem.getMapValueEventListener());
                                        return;
                                    }
                                    Log.e("GroupListViewAdapter", "[mRefMaps] onDataChange");
                                    // noinspection ConstantConditions
                                    FirebaseUserData firebaseUserData = dataSnapshot.getValue(FirebaseUserData.class);
                                    Log.e("GroupListViewAdapter", "[mRefMaps] logNumber " + firebaseUserData.getLogNumber());
                                    Log.e("GroupListViewAdapter", "[mRefMaps] userNumber " + firebaseUserData.getUserNumber());
                                    listViewItem.setUserNumber(firebaseUserData.getUserNumber());
                                    listViewItem.setLog(firebaseUserData.getLogNumber(), false);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e("GroupListViewAdapter", "[mRefMaps] onCancelled");
                                }
                            }));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("GroupListViewAdapter", "[mRefUser]" + " is onCancelled");
                }
            }));
        }
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

    public void addItem(GroupListViewItem item, boolean pushBack) {
        if (pushBack) listViewItemList.add(item);
        else listViewItemList.add(0, item);
    }

    public void delete(int position) {

        listViewItemList.remove(position);
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public void resort() {
        Collections.sort(listViewItemList, (o1, o2) ->
                o1.getUpdatedAt().compareTo(o2.getUpdatedAt()) * -1);
    }

    public void clear() {
        listViewItemList.clear();
        notifyDataSetChanged();
    }

}