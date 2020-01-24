package com.jucanos.photomap.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.R;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.jucanos.photomap.Structure.SetRep;
import com.jucanos.photomap.util.BitmapUtils;
import com.jucanos.photomap.util.SandboxView;

import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetRepActivity extends AppCompatActivity {
    public GlobalApplication globalApplication;

    private Bitmap bitMap_front, bitMap_back;
    private ImageView imageView_front;
    private Button btnCrop;
    private String mid, cityKey;
    private Integer regionCode;
    private View view;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_rep);

        globalApplication = GlobalApplication.getGlobalApplicationContext();

        Toolbar toolbar = findViewById(R.id.toolbar_tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("사진 편집");

        mid = getIntent().getStringExtra("mid");
        Log.e("SetRepActivity","mid : " + mid);
        cityKey = getIntent().getStringExtra("cityKey");
        regionCode = getIntent().getIntExtra("regionCode", -1);
        Log.e("regionCode", Integer.toString(regionCode));


        // view component load
        imageView_front = findViewById(R.id.imageView_image);

        // 절대경로로 부터 뒷배경 이미지 설정
        // String filePath = globalApplication.groupArrayList.get(mapPos).storys[regionPos].get(storyPos).getRealFilePath();
        //bitMap_back = BitmapFactory.decodeFile(filePath);


        // 마스크의 검은색 부분 -> 반투명 검은색으로 변경
        setFrontImage();
        openGallery();

    }

    // 지역코드에 따른 frontBitmap 설정
    public Drawable getBitmapFront(int regionCode) {
        Drawable drawable = null;
        if (regionCode == 1) {
            drawable = getResources().getDrawable(R.drawable.map_gyeonggi_empty);
        } else if (regionCode == 2) {
            drawable = getResources().getDrawable(R.drawable.map_gangwon_empty);
        } else if (regionCode == 3) {
            drawable = getResources().getDrawable(R.drawable.map_chungbuk_empty);
        } else if (regionCode == 4) {
            drawable = getResources().getDrawable(R.drawable.map_chungnam_empty);
        } else if (regionCode == 5) {
            drawable = getResources().getDrawable(R.drawable.map_junbuk_empty);
        } else if (regionCode == 6) {
            drawable = getResources().getDrawable(R.drawable.map_junnam_empty);
        } else if (regionCode == 7) {
            drawable = getResources().getDrawable(R.drawable.map_gyeongbuk_empty);
        } else if (regionCode == 8) {
            drawable = getResources().getDrawable(R.drawable.map_gyeongnam_empty);
        } else if (regionCode == 9) {
            drawable = getResources().getDrawable(R.drawable.map_jeju_empty);
        }

        return drawable;
    }

    // stream을 통해서 image mapUI activity 에 전달

    // front image setting
    public void setFrontImage() {
        bitMap_front = ((BitmapDrawable) getBitmapFront(regionCode)).getBitmap();
        int sW = bitMap_front.getWidth();
        int sH = bitMap_front.getHeight();
        int[] pixels = new int[sW * sH];
        Log.e("[sW]", Integer.toString((sW)));
        Log.e("[sH]", Integer.toString((sH)));
        Log.e("[pixels]", Integer.toString(pixels.length));
        bitMap_front.getPixels(pixels, 0, sW, 0, 0, sW, sH);
        int colorTranslucent = Color.parseColor("#99000000");
        // for 픽셀수 do 투명색이 아니면 반투명색으로 변경(검은색 -> 반투명)
        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] != 0)
                pixels[i] = colorTranslucent;

        }
        Bitmap bm = Bitmap.createBitmap(bitMap_front.getWidth(), bitMap_front.getHeight(), Bitmap.Config.ARGB_8888);
        bm.copyPixelsFromBuffer(IntBuffer.wrap(pixels));
        imageView_front.setImageBitmap(bm);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_set_rep, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // 오른쪽 상단 메뉴 버튼
            case R.id.item_gallery:
                openGallery();
                return true;
            // 뒤로가기 버튼
            case R.id.item_cut:
                if (view == null) {
                    openGallery();
                    return true;
                }
                Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(b);
                view.draw(c);
                String path = null;
                try {
                    path = BitmapUtils.saveBitmap("image_" + Long.toString(System.currentTimeMillis()), b, 50, getApplicationContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                intent.putExtra("regionCode", regionCode);
                intent.putExtra("path", path);
                setRepRequest(path);
                setResult(RESULT_OK, intent);
                finish();
                return true;
            case android.R.id.home:
                finish();
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

        FilePickerBuilder.getInstance().setMaxCount(1)
                .setActivityTheme(R.style.LibAppTheme)
                .enableCameraSupport(false)
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
                    String filePath = photoPaths.get(0);

                    bitMap_back = BitmapFactory.decodeFile(filePath);
                    // 확대, 축소, 회전을 위한 view inflate
                    view = new SandboxView(this, bitMap_back);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    view.setLayoutParams(layoutParams);

                    // 컨테이너 설정후 zoomView 추가
                    RelativeLayout container = findViewById(R.id.fragmentViewPager_container);
                    container.addView(view);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 300:
                // 권한 허용
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Log.e("[SetRepActivity]", "onRequestPermissionsResult 접근 허가");
                else  //권한 허용 불가
                    Log.e("[SetRepActivity]", "onRequestPermissionsResult 접근 불가");
                return;
        }
    }

    private void setRepRequest(String path) {
        Log.e("setRepRequest", "cityKey : " + cityKey);

        // body part
        RequestBody requetCityKey = RequestBody.create(MediaType.parse("text/plain"), cityKey);
        HashMap<String, RequestBody> hashMap = new HashMap<>();
        hashMap.put("cityKey", requetCityKey);

        // multi body part
        MultipartBody.Part file;
        File f = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        file = MultipartBody.Part.createFormData("img", f.getName(), requestFile);

        // request
        final Call<SetRep> res = NetworkHelper.getInstance().getService().setRep("Bearer " + globalApplication.token, mid, hashMap, file);
        res.enqueue(new Callback<SetRep>() {
            @Override
            public void onResponse(Call<SetRep> call, Response<SetRep> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e("SetRepActivity", response.body().getData().toString());
                    }
                } else {
                    Log.e("SetRepActivity", "setRepRequest error : " + Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<SetRep> call, Throwable t) {
                Log.e("SetRepActivity", "setRepRequest fail : " + t.getLocalizedMessage());
            }
        });
    }
}

