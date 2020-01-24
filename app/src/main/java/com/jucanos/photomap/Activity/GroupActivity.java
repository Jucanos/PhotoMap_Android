package com.jucanos.photomap.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.jucanos.photomap.Dialog.RepDialog;
import com.jucanos.photomap.Dialog.RepDialogListener;
import com.jucanos.photomap.Dialog.StoryDialog;
import com.jucanos.photomap.Dialog.StoryDialogListener;
import com.jucanos.photomap.ListView.MemberListViewAdapter;
import com.jucanos.photomap.R;

import pl.polidea.view.ZoomView;

public class GroupActivity extends AppCompatActivity {
    public static Context mContext;
    PorterShapeImageView imageView_gyeonggi; // 1
    ImageView imageView_gyeonggi_front;
    PorterShapeImageView imageView_gangwon; // 2
    ImageView imageView_gangwon_front;
    PorterShapeImageView imageView_chungbuk; // 3
    ImageView imageView_chungbuk_front;
    PorterShapeImageView imageView_chungnam; // 4
    ImageView imageView_chungnam_front;
    PorterShapeImageView imageView_jeonbuk; // 5
    ImageView imageView_jeonbuk_front;
    PorterShapeImageView imageView_jeonnam; // 6
    ImageView imageView_jeonnam_front;
    PorterShapeImageView imageView_gyeongbuk; // 7
    ImageView imageView_gyeongbuk_front;
    PorterShapeImageView imageView_gyeongnam; // 8
    ImageView imageView_gyeongnam_front;
    PorterShapeImageView imageView_jeju; // 9
    ImageView imageView_jeju_front;

    final PorterShapeImageView[] porterShapeImageViews = new PorterShapeImageView[10];
    final ImageView[] imageViews = new ImageView[10];

    private DrawerLayout drawerLayout_drawer;
    private ListView listView_member;
    private MemberListViewAdapter adapter;
    private View v;
    private RelativeLayout container;

    // floating action button 객체
    private FloatingActionMenu floatingActionMenu_menu;
    private FloatingActionButton floatingActionButton_save, floatingActionButton_share, floatingActionButton_rep;

    private String mid;

    private Boolean longClick = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        mContext = this;

        Toolbar toolbar = findViewById(R.id.toolbar_tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Group Name");

        drawerLayout_drawer = findViewById(R.id.drawer_layout);
        drawerLayout_drawer.openDrawer(Gravity.RIGHT);
        drawerLayout_drawer.closeDrawer(GravityCompat.END);

        adapter = new MemberListViewAdapter();
        listView_member = findViewById(R.id.listView_member);
        listView_member.setAdapter(adapter);
        adapter.addItem(null, "그룹멤버 초대");
        adapter.notifyDataSetChanged();

        mid = getIntent().getStringExtra("mid");

        v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_map, null, false);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // ZoomView 설정
        ZoomView zoomView = new ZoomView(this);
        zoomView.setClipChildren(false);
        zoomView.addView(v);
        zoomView.setLayoutParams(layoutParams);
        zoomView.setMaxZoom(8f);

        // 컨테이너 설정후 zoomView 추가
        container = findViewById(R.id.fragmentViewPager_container);
        container.setClipChildren(false);
        container.addView(zoomView);

        // PorterShapeImageView
        imageView_gyeonggi = findViewById(R.id.imageView_gyeonggi);
        imageView_gyeonggi_front = findViewById(R.id.imageView_gyeonggi_front);
        imageView_gyeonggi.setOnTouchListener(mClickListener);
        porterShapeImageViews[1] = imageView_gyeonggi;
        imageViews[1] = imageView_gyeonggi_front;

        imageView_gangwon = findViewById(R.id.imageView_gangwon);
        imageView_gangwon_front = findViewById(R.id.imageView_gangwon_front);
        imageView_gangwon.setOnTouchListener(mClickListener);
        porterShapeImageViews[2] = imageView_gangwon;
        imageViews[2] = imageView_gangwon_front;

        imageView_chungbuk = findViewById(R.id.imageView_chungbuk);
        imageView_chungbuk_front = findViewById(R.id.imageView_chungbuk_front);
        imageView_chungbuk.setOnTouchListener(mClickListener);
        porterShapeImageViews[3] = imageView_chungbuk;
        imageViews[3] = imageView_chungbuk_front;

        imageView_chungnam = findViewById(R.id.imageView_chungnam);
        imageView_chungnam_front = findViewById(R.id.imageView_chungnam_front);
        imageView_chungnam.setOnTouchListener(mClickListener);
        porterShapeImageViews[4] = imageView_chungnam;
        imageViews[4] = imageView_chungnam_front;

        imageView_jeonbuk = findViewById(R.id.imageView_jeonbuk);
        imageView_jeonbuk_front = findViewById(R.id.imageView_jeonbuk_front);
        imageView_jeonbuk.setOnTouchListener(mClickListener);
        porterShapeImageViews[5] = imageView_jeonbuk;
        imageViews[5] = imageView_jeonbuk_front;

        imageView_jeonnam = findViewById(R.id.imageView_jeonnam);
        imageView_jeonnam_front = findViewById(R.id.imageView_jeonnam_front);
        imageView_jeonnam.setOnTouchListener(mClickListener);
        porterShapeImageViews[6] = imageView_jeonnam;
        imageViews[6] = imageView_jeonnam_front;

        imageView_gyeongbuk = findViewById(R.id.imageView_gyeongbuk);
        imageView_gyeongbuk_front = findViewById(R.id.imageView_gyeongbuk_front);
        imageView_gyeongbuk.setOnTouchListener(mClickListener);
        porterShapeImageViews[7] = imageView_gyeongbuk;
        imageViews[7] = imageView_gyeongbuk_front;

        imageView_gyeongnam = findViewById(R.id.imageView_gyeongnam);
        imageView_gyeongnam_front = findViewById(R.id.imageView_gyeongnam_front);
        imageView_gyeongnam.setOnTouchListener(mClickListener);
        porterShapeImageViews[8] = imageView_gyeongnam;
        imageViews[8] = imageView_gyeongnam_front;

        imageView_jeju = findViewById(R.id.imageView_jeju);
        imageView_jeju_front = findViewById(R.id.imageView_jeju_front);
        imageView_jeju.setOnTouchListener(mClickListener);
        porterShapeImageViews[9] = imageView_jeju;
        imageViews[9] = imageView_jeju_front;

        addGroupTest();

        floatingActionButton_rep = v.findViewById(R.id.floatingActionButton_rep);
        floatingActionButton_save = v.findViewById(R.id.floatingActionButton_save);
        floatingActionButton_share = v.findViewById(R.id.floatingActionButton_share);
        floatingActionMenu_menu = v.findViewById(R.id.floatingActionMenu_menu);

        floatingActionMenu_menu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {

            }
        });


        floatingActionMenu_menu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (floatingActionMenu_menu.isOpened()) {
                    floatingActionMenu_menu.close(true);
                    return false;
                }
                return false;
            }
        });


        floatingActionButton_rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GroupActivity.this, "floatingActionButton_rep", Toast.LENGTH_SHORT).show();
                floatingActionMenu_menu.close(true);
            }
        });

        floatingActionButton_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GroupActivity.this, "floatingActionButton_save", Toast.LENGTH_SHORT).show();
                floatingActionMenu_menu.close(true);
            }
        });

        floatingActionButton_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GroupActivity.this, "floatingActionButton_share", Toast.LENGTH_SHORT).show();
                floatingActionMenu_menu.close(true);
            }
        });

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            longClick = true;
            Toast.makeText(GroupActivity.this, "LongClick : " + Integer.toString(msg.what), Toast.LENGTH_SHORT).show();
            RepDialog dialog = new RepDialog(GroupActivity.this);
            dialog.setDialogListener(new RepDialogListener() {
                @Override
                public void onSetClicked() {
                    Toast.makeText(GroupActivity.this, "onSetClicked", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onDeleteClicked() {
                    Toast.makeText(GroupActivity.this, "onDeleteClicked", Toast.LENGTH_SHORT).show();

                }
            });
            dialog.show();
        }
    };

    PorterShapeImageView.OnTouchListener mClickListener = new View.OnTouchListener() {
        int x = 0, y = 0;
        int transparency;

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Bitmap bm = ((BitmapDrawable) imageViews[Integer.parseInt(v.getContentDescription().toString())].getDrawable()).getBitmap();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) pxToDp(mContext, event.getX());
                    y = (int) pxToDp(mContext, event.getY());
                    transparency = bm.getPixel(x, y);
                    Log.e(v.getContentDescription().toString(),"down transparency : " + transparency);
                    if(transparency != 0){
                        longClick = false;
                        handler.sendEmptyMessageAtTime(Integer.parseInt(v.getContentDescription().toString()), event.getDownTime() + (long)1000);
                    }
                    return transparency != 0;
                case MotionEvent.ACTION_UP:
                    transparency = bm.getPixel(x, y);
                    Log.e(v.getContentDescription().toString(),"up transparency : " + transparency);
                    if (transparency != 0 && !longClick) {
                        Toast.makeText(getApplicationContext(), v.getContentDescription(), Toast.LENGTH_SHORT).show();
                        redirectRegionActivity(Integer.parseInt(v.getContentDescription().toString()));
                        handler.removeMessages(Integer.parseInt(v.getContentDescription().toString()));
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_group, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // 오른쪽 상단 메뉴 버튼
            case R.id.item_menu:
                drawerLayout_drawer.openDrawer(Gravity.END);
                return true;
            // 뒤로가기 버튼
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.anim_not_move, R.anim.anim_slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public float pxToDp(Context context, float px) {
        // 해상도 마다 다른 density 를 반환. xxxhdpi는 density = 4
        float density = context.getResources().getDisplayMetrics().density;

        if (density == 1.0)      // mpdi  (160dpi) -- xxxhdpi (density = 4)기준으로 density 값을 재설정 한다
            density *= 4.0;
        else if (density == 1.5) // hdpi  (240dpi)
            density *= (8.0 / 3);
        else if (density == 2.0) // xhdpi (320dpi)
            density *= 2.0;
        return px / density;     // dp 값 반환
    }

    public void redirectRegionActivity(int citykey) {
        Intent intent = new Intent(this, StoryActivity.class);
        intent.putExtra("mid", mid);
        intent.putExtra("citykey", citykey);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_not_move);
    }

    // ====================================================================== for test Code
    // ====================================================================== for test Code
    void addGroupTest() {
        Drawable drawable = getResources().getDrawable(R.drawable.test_image_vertical);
        Bitmap bm = ((BitmapDrawable) drawable).getBitmap();
        adapter.addItem(bm, "일청원");
        adapter.addItem(bm, "이청원");
        adapter.addItem(bm, "삼청원");
        adapter.addItem(bm, "사청원");
        adapter.notifyDataSetChanged();
        imageView_gangwon.setImageBitmap(bm);
    }
}
