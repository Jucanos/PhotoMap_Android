package com.jucanos.photomap.Activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jucanos.photomap.R;
import com.jucanos.photomap.Viewpager.CustomViewPager;
import com.jucanos.photomap.Viewpager.AddStoryViewPagerAdapter;
import com.jucanos.photomap.util.BitmapUtils;

import java.util.ArrayList;

public class AddStoryActivity extends AppCompatActivity {
    private ArrayList<Bitmap> imageList;
    private CustomViewPager viewPager;
    private AddStoryViewPagerAdapter addStoryViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);

        // set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("사진 선택 및 크기 설정");

        viewPager = findViewById(R.id.viewPager_vp);
        addGroupTest();
        viewPager.setPagingEnabled(false);
        addStoryViewPagerAdapter = new AddStoryViewPagerAdapter(this, imageList);
        viewPager.setAdapter(addStoryViewPagerAdapter);

        ImageView imageView_left = findViewById(R.id.imageView_left);
        ImageView imageView_right = findViewById(R.id.imageView_right);
        ImageView imageView_rotate = findViewById(R.id.imageView_rotate);
        ImageView imageView_snap = findViewById(R.id.imageView_snap);

        imageView_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
            }
        });

        imageView_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            }
        });

        imageView_rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap mBitmap = addStoryViewPagerAdapter.getBitmap(viewPager.getCurrentItem());
                mBitmap = BitmapUtils.rotateBitmap(mBitmap, 90);
                addStoryViewPagerAdapter.setBitmap(viewPager.getCurrentItem(), mBitmap);
                addStoryViewPagerAdapter.notifyDataSetChanged();
            }
        });

        imageView_snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_add_story, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // 오른쪽 상단 메뉴 버튼
            case R.id.item_next:
                return true;
            // 뒤로가기 버튼
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.anim_not_move, R.anim.anim_slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void addGroupTest() {
        imageList = new ArrayList<>();
        Drawable drawable = getResources().getDrawable(R.drawable.test_image);
        Bitmap bm = ((BitmapDrawable) drawable).getBitmap();
        for (int i = 0; i < 5; i++) {
            imageList.add(bm);
        }
    }
}
