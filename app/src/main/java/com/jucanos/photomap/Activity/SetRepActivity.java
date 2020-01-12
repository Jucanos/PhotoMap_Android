package com.jucanos.photomap.Activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.jucanos.photomap.R;
import com.jucanos.photomap.util.SandboxView;

import java.nio.IntBuffer;

public class SetRepActivity extends AppCompatActivity {
    private Bitmap bitMap_front,bitMap_back;
    private ImageView imageView_front;
    private Button btnCrop;
    private int mapPos, regionPos, storyPos;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_rep);

        // intent 정보 저장
        //Intent intent = getIntent();
        //mapPos = intent.getIntExtra("mapPos", -1);
        //regionPos = intent.getIntExtra("regionPos", -1);
        //storyPos = intent.getIntExtra("storyPos", -1);

        regionPos = 2;

        // view component load
        imageView_front = findViewById(R.id.imageView_image);
        btnCrop = findViewById(R.id.button_crop);

        // 절대경로로 부터 뒷배경 이미지 설정
        // String filePath = globalApplication.groupArrayList.get(mapPos).storys[regionPos].get(storyPos).getRealFilePath();
        //bitMap_back = BitmapFactory.decodeFile(filePath);

        Drawable drawable = getResources().getDrawable(R.drawable.test_image);
        bitMap_back = ((BitmapDrawable) drawable).getBitmap();
        // 확대, 축소, 회전을 위한 view inflate
        view = new SandboxView(this, bitMap_back);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(layoutParams);

        // 컨테이너 설정후 zoomView 추가
        RelativeLayout container = findViewById(R.id.container);
        container.addView(view);

        // 마스크의 검은색 부분 -> 반투명 검은색으로 변경
        setFrontImage();
    }

    // 지역코드에 따른 frontBitmap 설정
    public Drawable getBitmapFront(int regionCode) {
        Drawable drawable = null;
        if (regionCode == 1) {
            drawable = getResources().getDrawable(R.drawable.gyeonggi_empty);
        } else if (regionCode == 2) {
            drawable = getResources().getDrawable(R.drawable.gangwon_empty);
        } else if (regionCode == 3) {
            drawable = getResources().getDrawable(R.drawable.chungbuk_empty);
        } else if (regionCode == 4) {
            drawable = getResources().getDrawable(R.drawable.chungnam_empty);
        } else if (regionCode == 5) {
            drawable = getResources().getDrawable(R.drawable.junbuk_empty);
        } else if (regionCode == 6) {
            drawable = getResources().getDrawable(R.drawable.junnam_empty);
        } else if (regionCode == 7) {
            drawable = getResources().getDrawable(R.drawable.gyeongbuk_empty);
        } else if (regionCode == 8) {
            drawable = getResources().getDrawable(R.drawable.gyeongnam_empty);
        } else if (regionCode == 9) {
            drawable = getResources().getDrawable(R.drawable.jeju_empty);
        }

        return drawable;
    }

    // stream을 통해서 image mapUI activity 에 전달

    // front image setting
    private void setFrontImage() {
        bitMap_front = ((BitmapDrawable) getBitmapFront(regionPos)).getBitmap();
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

        // 버튼 이벤트 리스너
        btnCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 view (뒷배경) 에있는 보여지는 부분을 pixel단위로 변환하여 mapUI activity로 전달
                Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(b);
                view.draw(c);
            }
        });
    }


}

