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
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.jucanos.photomap.Activity.EditStoryActivity;
import com.jucanos.photomap.Activity.SetRepActivity;
import com.jucanos.photomap.Activity.StoryActivity;
import com.jucanos.photomap.Dialog.StoryDialog;
import com.jucanos.photomap.Dialog.StoryDialogListener;
import com.jucanos.photomap.Dialog.YesNoDialog;
import com.jucanos.photomap.Dialog.YesNoDialogListener;
import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.R;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.jucanos.photomap.SliderViewAdapter.AddStoryImageSliderAdapter;
import com.jucanos.photomap.Structure.RemoveStory;
import com.jucanos.photomap.photoPicker.ViewUtils;
import com.jucanos.photomap.util.DateString;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryListViewAdapter extends BaseAdapter {
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
        final TextView textView_creator = convertView.findViewById(R.id.textView_creator);
        final TextView textView_title = convertView.findViewById(R.id.textView_title);
        final TextView textView_upload = convertView.findViewById(R.id.textView_upload);
        final ImageView imageView_more = convertView.findViewById(R.id.imageView_more);
        final ViewPager viewPager = convertView.findViewById(R.id.viewPager);
        final TextView textView_indicator = convertView.findViewById(R.id.tv_indicator);

        viewPager.getLayoutParams().height = ViewUtils.getScreenWidth();
        viewPager.getLayoutParams().width = ViewUtils.getScreenWidth();

        final ExpandableTextView expandableTextView_description = convertView.findViewById(R.id.expandableTextView_context);

        // instance of position
        final StoryListViewItem listViewItem = listViewItemList.get(position);

        // thumbnail image
        Glide.with(context)
                .load(GlobalApplication.getGlobalApplicationContext().userThumbnail.get(listViewItem.getCreator()))
                .placeholder(R.drawable.logo)
                .into(circleImageView_thumbnail);


        // story images
        AddStoryImageSliderAdapter addStoryImageSliderAdapter = new AddStoryImageSliderAdapter(context, listViewItem.getFiles());
        viewPager.setAdapter(addStoryImageSliderAdapter);
        String indicator = 1 + "/" + listViewItem.getFiles().size();
        textView_indicator.setText(indicator);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String indicator = (position + 1) + "/" + listViewItem.getFiles().size();
                textView_indicator.setText(indicator);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        textView_creator.setText(GlobalApplication.getGlobalApplicationContext().userNickName.get(listViewItem.getCreator()));
        textView_title.setText(listViewItem.getTitle());
        textView_upload.setText(DateString.getString(listViewItem.getCreatedAt()));
        expandableTextView_description.setText(listViewItem.getContext());

        imageView_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoryDialog dialog = new StoryDialog(context);
                dialog.setDialogListener(new StoryDialogListener() {
                    @Override
                    public void onDeleteClicked() {
                        final YesNoDialog yesNoDialog = new YesNoDialog(context,"정말 삭제 하시겠습니까?");
                        yesNoDialog.setDialogListener(new YesNoDialogListener() {
                            @Override
                            public void onPositiveClicked() {
                                yesNoDialog.dismiss();

                                ((StoryActivity) context).removeStoryRequest(listViewItem.getSid(), position);
                            }
                            @Override
                            public void onNegativeClicked() {
                                yesNoDialog.dismiss();
                            }
                        });
                        yesNoDialog.show();
                    }

                    @Override
                    public void onEditClicked() {
                        ((StoryActivity) context).redirectEditStoryActivity(listViewItem.getSid(), listViewItem.getTitle(), listViewItem.getContext(), position);
                    }

                    @Override
                    public void onCancelClicked() {
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

    public void removeItem(int pos){
        listViewItemList.remove(pos);
    }


}