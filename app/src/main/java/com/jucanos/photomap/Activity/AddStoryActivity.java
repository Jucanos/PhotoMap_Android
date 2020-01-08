package com.jucanos.photomap.Activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.jucanos.photomap.R;
import com.jucanos.photomap.Viewpager.CustomViewPager;
import com.jucanos.photomap.Viewpager.ViewPagerAdapter;

import java.util.ArrayList;

public class AddStoryActivity extends AppCompatActivity {
    private static final int DP = 24;
    private ArrayList<Bitmap> imageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);

        // set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("사진 선택 및 크기 설정");

        CustomViewPager viewPager = (CustomViewPager)findViewById(R.id.viewPager_vp);
        addGroupTest();
        viewPager.setPagingEnabled(false);
        viewPager.setAdapter(new ViewPagerAdapter(this, imageList));
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

//    public void onWindowFocusChanged(boolean hasFocus) {
//        View layoutMainView = (View)this.findViewById(R.id.relativeLayout_container);
//        relativeLayout_container_width = layoutMainView.getHeight();
//        relativeLayout_container_height = layoutMainView.getWidth();
//    }

    // ====================================================================== for test Code
    // ====================================================================== for test Code
    void addGroupTest(){
        imageList = new ArrayList();
        Drawable drawable = getResources().getDrawable(R.drawable.test_image);
        Bitmap bm = ((BitmapDrawable)drawable).getBitmap();
        for (int i = 0; i < 5; i++) {
            imageList.add(bm);
        }
    }
}
