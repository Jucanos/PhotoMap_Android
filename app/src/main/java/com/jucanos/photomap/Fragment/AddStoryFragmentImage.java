package com.jucanos.photomap.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.jucanos.photomap.Activity.AddStoryActivity;
import com.jucanos.photomap.R;
import com.jucanos.photomap.Viewpager.AddStoryViewPagerAdapter;
import com.jucanos.photomap.Viewpager.CustomViewPager;
import com.jucanos.photomap.util.BitmapUtils;

import java.util.ArrayList;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

public class AddStoryFragmentImage extends Fragment {
    private CustomViewPager viewPager;
    private AddStoryViewPagerAdapter addStoryViewPagerAdapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup fragmentContainer, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_stroy_image, fragmentContainer, false);

        Toast.makeText(getActivity().getApplicationContext(), "AddStoryFragmentImage onCreateView", Toast.LENGTH_SHORT).show();

        Toolbar toolbar = view.findViewById(R.id.toolbar_tb);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("이미지");
        setHasOptionsMenu(true);

        viewPager = view.findViewById(R.id.viewPager_vp);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setPagingEnabled(false);
        addStoryViewPagerAdapter = new AddStoryViewPagerAdapter(getActivity().getApplicationContext());
        viewPager.setAdapter(addStoryViewPagerAdapter);

        ImageView imageView_left = view.findViewById(R.id.imageView_left);
        ImageView imageView_right = view.findViewById(R.id.imageView_right);
        ImageView imageView_rotate = view.findViewById(R.id.imageView_rotate);
        ImageView imageView_snap = view.findViewById(R.id.imageView_snap);

        imageView_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
                Log.e("AddStoryActivity", "viewPager.getChildCount() : " + Integer.toString(viewPager.getChildCount()));
            }
        });

        imageView_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                Log.e("AddStoryActivity", "viewPager.getChildCount() : " + Integer.toString(viewPager.getChildCount()));
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
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_add_story_image, menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_gallery:
                openGallery();
                return true;
            case R.id.item_next:
                ((AddStoryActivity) getActivity()).setFrag(1);
                return true;
            case android.R.id.home:
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    boolean openGallery() {
        String[] REQUIRED_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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
                if (resultCode == getActivity().RESULT_OK && data != null) {
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

    void addImage(ArrayList<String> photoPaths) {
        for (int i = 0; i < photoPaths.size(); i++) {
            Bitmap bm = BitmapFactory.decodeFile(photoPaths.get(i));
            addStoryViewPagerAdapter.addItem(bm);
        }
        addStoryViewPagerAdapter.notifyDataSetChanged();
    }

    public ArrayList<Bitmap> getBitmaps() {
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        // Log.e("viewPager.getWidth",Integer.toString(viewPager.getWidth()));
        // Log.e("viewPager.getHeight",Integer.toString(viewPager.getHeight()));
        Log.e("AddStoryActivity", "viewPager.getChildCount() : " + Integer.toString(viewPager.getChildCount()));
//        for (int i = 0; i < viewPager.getChildCount(); i++) {
//            View v = viewPager.getChildAt(i);
//            Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
//                    Bitmap.Config.ARGB_8888);
//            Canvas c = new Canvas(b);
//            v.draw(c);
//            bitmaps.add(b);
//        }
        for(int i = 0 ; i < addStoryViewPagerAdapter.getCount(); i++){
            bitmaps.add(addStoryViewPagerAdapter.getCropImage(i));
        }
        return bitmaps;
    }
}
