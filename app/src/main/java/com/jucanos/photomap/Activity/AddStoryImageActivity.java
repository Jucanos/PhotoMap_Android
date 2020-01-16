package com.jucanos.photomap.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jucanos.photomap.R;
import com.jucanos.photomap.Viewpager.AddStoryViewPagerAdapter;
import com.jucanos.photomap.Viewpager.CustomViewPager;
import com.jucanos.photomap.util.BitmapUtils;

import java.util.ArrayList;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;


public class AddStoryImageActivity extends AppCompatActivity {
    private ArrayList<Bitmap> imageList;
    private CustomViewPager viewPager;
    private AddStoryViewPagerAdapter addStoryViewPagerAdapter;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story_image);

        // set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("사진 선택 및 크기 설정");

        viewPager = findViewById(R.id.viewPager_vp);
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

        openGallery();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_add_story_image, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // 오른쪽 상단 메뉴 버튼
            case R.id.item_next:
                return true;
            case R.id.item_gallery:
                openGallery();
                return true;
            // 뒤로가기 버튼
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.anim_not_move, R.anim.anim_slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    boolean openGallery() {
        String[] REQUIRED_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 최초 권한 요청인지, 혹은 사용자에 의한 재요청인지 확인
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // 사용자가 임의로 권한을 취소한 경우
                // 권한 재요청
                Log.e("[PackageManager]", "권한 재요청");
                requestPermissions(REQUIRED_PERMISSIONS, 300);
                return true;
            } else {
                // 최초로 권한을 요청하는 경우(첫실행)
                Log.e("[PackageManager]", "권한 최초요청");
                requestPermissions(REQUIRED_PERMISSIONS, 300);
                return true;
            }

        } else { // 접근권한이 있을때
            Log.e("[PackageManager]", "접근 허가");
        }

        FilePickerBuilder.getInstance().setMaxCount(5)
                .setActivityTheme(R.style.LibAppTheme)
                .pickPhoto(this, FilePickerConst.REQUEST_CODE_PHOTO);
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if (data == null) Log.e("AddStoryImageActivity", "[data] : null");
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> photoPaths = new ArrayList<>();
                    photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                    for (int i = 0; i < photoPaths.size(); i++) {
                        Log.e("AddStoryImageActivity", "[photoPaths] : " + photoPaths.get(i));
                    }
                    addImage(photoPaths);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 300:
                // 권한 허용
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("[PackageManager]", "접근 허가");
                } else { //권한 허용 불가
                    Log.e("[PackageManager]", "접근 불가");
                }
                return;
        }
    }

    void addImage(ArrayList<String> photoPaths){
        for(int i = 0 ; i < photoPaths.size(); i++){
            Bitmap bm = BitmapFactory.decodeFile(photoPaths.get(i));
            imageList.add(bm);
        }
        addStoryViewPagerAdapter.notifyDataSetChanged();
    }
}
