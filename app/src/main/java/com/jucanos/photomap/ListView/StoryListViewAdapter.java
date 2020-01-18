package com.jucanos.photomap.ListView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.jucanos.photomap.Activity.MainActivity;
import com.jucanos.photomap.Activity.SetRepActivity;
import com.jucanos.photomap.Dialog.StoryDialog;
import com.jucanos.photomap.Dialog.StoryDialogListener;
import com.jucanos.photomap.R;
import com.jucanos.photomap.Viewpager.CustomViewPager;
import com.jucanos.photomap.Viewpager.StoryViewPagerAdapter;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item_group" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_story, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        final CircleImageView circleImageView_thumbnail = (CircleImageView) convertView.findViewById(R.id.circleImageView_thumbnail);
        final TextView textView_title = (TextView) convertView.findViewById(R.id.textView_title);
        final TextView textView_upload = (TextView) convertView.findViewById(R.id.textView_upload);
        final Button button_menu = (Button) convertView.findViewById(R.id.button_menu);
        final CustomViewPager customViewPager_vp = (CustomViewPager) convertView.findViewById(R.id.customViewPager_vp);
        final ExpandableTextView expandableTextView_description = (ExpandableTextView) convertView.findViewById(R.id.expandableTextView_description);
        // GetUserInfoData Set(listViewItemList)에서 position에 위치한 데이터 참조 획득


        StoryListViewItem listViewItem = listViewItemList.get(position);
        Drawable drawable = context.getResources().getDrawable(R.drawable.test_image_square);
        Bitmap bm = ((BitmapDrawable)drawable).getBitmap();
        circleImageView_thumbnail.setImageBitmap(bm);

        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
        for (int i = 0; i < listViewItem.getImage_realPahts().size(); i++) {
            bm = BitmapFactory.decodeFile(listViewItem.getImage_realPahts().get(i));
            bitmaps.add(bm);
        }
        final StoryViewPagerAdapter StoryViewPagerAdapter = new StoryViewPagerAdapter(context, bitmaps);
        customViewPager_vp.setAdapter(StoryViewPagerAdapter);

        textView_title.setText(listViewItem.getTitle());
        textView_upload.setText(listViewItem.getTime_upload());
        expandableTextView_description.setText(listViewItem.getDescription() + "\n\n" + listViewItem.getTime_edit());

        button_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoryDialog dialog = new StoryDialog(context);
                dialog.setDialogListener(new StoryDialogListener() {
                    @Override
                    public void onDeleteClicked() {
                        Toast.makeText(context, "change onDeleteClicked is clicked", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onEditClicked() {
                        Toast.makeText(context, "change onEditClicked is clicked", Toast.LENGTH_SHORT).show();
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

    public void addItem(StoryListViewItem item) {
        listViewItemList.add(item);
    }

    public void clear() {
        listViewItemList = new ArrayList<StoryListViewItem>();
    }

    public Bitmap getBitmap(String path,Context context) {
        Uri uriFromPath = Uri.fromFile(new File(path));
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uriFromPath));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void redirectSignupActivity() {

    }

}