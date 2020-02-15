package com.jucanos.photomap.ListView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.jucanos.photomap.Activity.SetRepActivity;
import com.jucanos.photomap.Activity.StoryActivity;
import com.jucanos.photomap.Dialog.StoryDialog;
import com.jucanos.photomap.Dialog.StoryDialogListener;
import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.R;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.jucanos.photomap.Structure.RemoveStory;
import com.jucanos.photomap.Viewpager.CustomViewPager;
import com.jucanos.photomap.Viewpager.StoryViewPagerAdapter;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryListViewAdapter extends BaseAdapter {
    private GlobalApplication globalApplication;
    private final int EDIT_STORY_REQEUST = 3;
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<StoryListViewItem> listViewItemList = new ArrayList<StoryListViewItem>();

    // ListViewAdapter의 생성자
    public StoryListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        // "listview_item_group" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_story, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        final CircleImageView circleImageView_thumbnail = convertView.findViewById(R.id.circleImageView_thumbnail);
        final TextView textView_title = convertView.findViewById(R.id.textView_title);
        final TextView textView_upload = convertView.findViewById(R.id.textView_upload);
        final ImageView imageView_more = convertView.findViewById(R.id.imageView_more);
        final CustomViewPager customViewPager_vp = convertView.findViewById(R.id.customViewPager_vp);
        final ExpandableTextView expandableTextView_description = convertView.findViewById(R.id.expandableTextView_description);

        // instance of position
        final StoryListViewItem listViewItem = listViewItemList.get(position);

        // thumbnail image
        Glide.with(context)
                .load(GlobalApplication.getGlobalApplicationContext().userThumbnail.get(listViewItem.getCreator()))
                .placeholder(R.drawable.logo)
                .into(circleImageView_thumbnail);


        // story images
        final StoryViewPagerAdapter StoryViewPagerAdapter = new StoryViewPagerAdapter(context, listViewItem.getFiles());
        customViewPager_vp.setAdapter(StoryViewPagerAdapter);

        textView_title.setText(listViewItem.getTitle());
        textView_upload.setText((new SimpleDateFormat("yyyy-MM-dd hh").format(listViewItem.getCreatedAt())));
        expandableTextView_description.setText(listViewItem.getContext());

        imageView_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoryDialog dialog = new StoryDialog(context);
                dialog.setDialogListener(new StoryDialogListener() {
                    @Override
                    public void onDeleteClicked() {
                        Toast.makeText(context, "change onDeleteClicked is clicked", Toast.LENGTH_SHORT).show();
                        removeStoryRequest(listViewItem.getSid(), position);
                    }

                    @Override
                    public void onEditClicked() {
                        Toast.makeText(context, "change onEditClicked is clicked", Toast.LENGTH_SHORT).show();
//                        final Intent intent = new Intent(context, EditStoryActivity.class);
//                        ((StoryActivity) context).redirectEditStoryActivity(listViewItem.getSid(), listViewItem.getTitle(), listViewItem.getContext(), position);
                    }

                    @Override
                    public void onRepClicked() {
                        Toast.makeText(context, "change onRepClicked is clicked", Toast.LENGTH_SHORT).show();
                        final Intent intent = new Intent(context, SetRepActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onCancelClicked() {
                        Toast.makeText(context, "change onCancelClicked is clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });


        // 아이템 내 각 위젯에 데이터 반영
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public StoryListViewItem getItem(int position) {
        return listViewItemList.get(position);
    }

    public void addItem(StoryListViewItem item, boolean pushBack) {
        if (!pushBack) listViewItemList.add(0, item);
        else listViewItemList.add(item);

    }


    public void removeStoryRequest(String sid, final int pos) {
        final Call<RemoveStory> res = NetworkHelper.getInstance().getService().removeStory("Bearer " + GlobalApplication.getGlobalApplicationContext().token, sid);
        res.enqueue(new Callback<RemoveStory>() {
            @Override
            public void onResponse(Call<RemoveStory> call, Response<RemoveStory> response) {
                if (response.isSuccessful()) {
                    Log.e("StoryActivity", "[removeStoryRequest] is Successful");
                    listViewItemList.remove(pos);
                    notifyDataSetChanged();
                } else {
                    Log.e("StoryActivity", "[removeStoryRequest] " + Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<RemoveStory> call, Throwable t) {
                Log.e("StoryActivity", "[removeStoryRequest fail] " + t.getLocalizedMessage());
            }
        });
    }


}