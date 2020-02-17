package com.jucanos.photomap.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.jucanos.photomap.Dialog.RepDialog;
import com.jucanos.photomap.Dialog.RepDialogListener;
import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.ListView.MemberListViewAdapter;
import com.jucanos.photomap.ListView.MemberListViewItem;
import com.jucanos.photomap.R;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.jucanos.photomap.Structure.GetMapInfo;
import com.jucanos.photomap.Structure.GetMapInfoDataRepresents;
import com.jucanos.photomap.Structure.SetMapRep;
import com.jucanos.photomap.Structure.SetMapRepRequest;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;

import java.util.HashMap;
import java.util.Map;

import pl.polidea.view.ZoomView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupActivity extends AppCompatActivity {
    public GlobalApplication globalApplication;

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
    final ImageView[] mBorders = new ImageView[10];
    final ImageView[] imageViews = new ImageView[10];

    private DrawerLayout drawerLayout_drawer;
    private ListView listView_member;
    private MemberListViewAdapter adapter;
    private View v;
    private RelativeLayout container;

    // floating action button 객체
    private FloatingActionMenu floatingActionMenu_menu;
    private FloatingActionButton floatingActionButton_save, floatingActionButton_rep;

    private String mid;
    private int longClickId = -1;

    private Integer SET_REP_REQUEST = 1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        globalApplication = GlobalApplication.getGlobalApplicationContext();

        mContext = this;

        Toolbar toolbar = findViewById(R.id.toolbar_tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Group Name");

        drawerLayout_drawer = findViewById(R.id.drawer_layout);
        drawerLayout_drawer.openDrawer(Gravity.RIGHT);
        drawerLayout_drawer.closeDrawer(GravityCompat.END);

        adapter = new MemberListViewAdapter(getApplicationContext());
        listView_member = findViewById(R.id.listView_member);
        listView_member.setAdapter(adapter);

        mid = getIntent().getStringExtra("mid");

        v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_map, null, false);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        RelativeLayout relativeLayout_addMember = findViewById(R.id.relativeLayout_addMember);
        relativeLayout_addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("[addMember]", "onClick");
                FeedTemplate params = FeedTemplate
                        .newBuilder(ContentObject.newBuilder("님이 photoMap에 초대했습니다",
                                "https://ifh.cc/g/ODD7n.png",
                                LinkObject.newBuilder()
                                        .setMobileWebUrl("https://play.google.com")
                                        .setWebUrl("https://play.google.com")
                                        .setAndroidExecutionParams("mid=" + mid)
                                        .setIosExecutionParams("mid=" + mid)
                                        .build()
                        )
                                .build())
                        .addButton(new ButtonObject("초대 받기", LinkObject.newBuilder()
                                .setMobileWebUrl("https://play.google.com")
                                .setWebUrl("https://play.google.com")
                                .setAndroidExecutionParams("mid=" + mid)
                                .setIosExecutionParams("mid=" + mid)
                                .build()))
                        .build();

                //  콜백으로 링크 잘갔는지 확인
                Map<String, String> serverCallbackArgs = new HashMap<String, String>();
                serverCallbackArgs.put("user_id", "${current_user_id}");
                serverCallbackArgs.put("product_id", "${shared_product_id}");

                KakaoLinkService.getInstance().sendDefault(GroupActivity.this, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Logger.e(errorResult.toString());
                    }

                    @Override
                    public void onSuccess(KakaoLinkResponse result) {
                        // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남. 톡에서 정상적으로 보내졌는지 보장은 할 수 없다. 전송 성공 유무는 서버콜백 기능을 이용하여야 한다.
                    }
                });
            }
        });

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
        mBorders[1] = findViewById(R.id.imageView_gyeonggi_white);

        imageView_gangwon = findViewById(R.id.imageView_gangwon);
        imageView_gangwon_front = findViewById(R.id.imageView_gangwon_front);
        imageView_gangwon.setOnTouchListener(mClickListener);
        porterShapeImageViews[2] = imageView_gangwon;
        imageViews[2] = imageView_gangwon_front;
        mBorders[2] = findViewById(R.id.imageView_gangwon_white);

        imageView_chungbuk = findViewById(R.id.imageView_chungbuk);
        imageView_chungbuk_front = findViewById(R.id.imageView_chungbuk_front);
        imageView_chungbuk.setOnTouchListener(mClickListener);
        porterShapeImageViews[3] = imageView_chungbuk;
        imageViews[3] = imageView_chungbuk_front;
        mBorders[3] = findViewById(R.id.imageView_chungbuk_white);

        imageView_chungnam = findViewById(R.id.imageView_chungnam);
        imageView_chungnam_front = findViewById(R.id.imageView_chungnam_front);
        imageView_chungnam.setOnTouchListener(mClickListener);
        porterShapeImageViews[4] = imageView_chungnam;
        imageViews[4] = imageView_chungnam_front;
        mBorders[4] = findViewById(R.id.imageView_chungnam_white);

        imageView_jeonbuk = findViewById(R.id.imageView_jeonbuk);
        imageView_jeonbuk_front = findViewById(R.id.imageView_jeonbuk_front);
        imageView_jeonbuk.setOnTouchListener(mClickListener);
        porterShapeImageViews[5] = imageView_jeonbuk;
        imageViews[5] = imageView_jeonbuk_front;
        mBorders[5] = findViewById(R.id.imageView_jeonbuk_white);

        imageView_jeonnam = findViewById(R.id.imageView_jeonnam);
        imageView_jeonnam_front = findViewById(R.id.imageView_jeonnam_front);
        imageView_jeonnam.setOnTouchListener(mClickListener);
        porterShapeImageViews[6] = imageView_jeonnam;
        imageViews[6] = imageView_jeonnam_front;
        mBorders[6] = findViewById(R.id.imageView_jeonbuk_white);

        imageView_gyeongbuk = findViewById(R.id.imageView_gyeongbuk);
        imageView_gyeongbuk_front = findViewById(R.id.imageView_gyeongbuk_front);
        imageView_gyeongbuk.setOnTouchListener(mClickListener);
        porterShapeImageViews[7] = imageView_gyeongbuk;
        imageViews[7] = imageView_gyeongbuk_front;
        mBorders[7] = findViewById(R.id.imageView_gyeongbuk_white);

        imageView_gyeongnam = findViewById(R.id.imageView_gyeongnam);
        imageView_gyeongnam_front = findViewById(R.id.imageView_gyeongnam_front);
        imageView_gyeongnam.setOnTouchListener(mClickListener);
        porterShapeImageViews[8] = imageView_gyeongnam;
        imageViews[8] = imageView_gyeongnam_front;
        mBorders[8] = findViewById(R.id.imageView_gyeongnam_white);

        imageView_jeju = findViewById(R.id.imageView_jeju);
        imageView_jeju_front = findViewById(R.id.imageView_jeju_front);
        imageView_jeju.setOnTouchListener(mClickListener);
        porterShapeImageViews[9] = imageView_jeju;
        imageViews[9] = imageView_jeju_front;
        mBorders[9] = findViewById(R.id.imageView_jeju_white);

        floatingActionButton_rep = findViewById(R.id.floatingActionButton_rep);
        floatingActionButton_save = findViewById(R.id.floatingActionButton_save);
        floatingActionMenu_menu = findViewById(R.id.floatingActionMenu_menu);

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
                setMapRepRequest(mid);
                floatingActionMenu_menu.close(true);
            }
        });

        floatingActionButton_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GroupActivity.this, "floatingActionButton_save", Toast.LENGTH_SHORT).show();
                getMapImage();
                floatingActionMenu_menu.close(true);
            }
        });

        zoomView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handler.removeMessages(longClickId);
                longClickId = -1;
                return false;
            }
        });

        getMapInfoRequest();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            longClickId = -1;
            Toast.makeText(GroupActivity.this, "LongClick : " + Integer.toString(msg.what), Toast.LENGTH_SHORT).show();
            final int regionCode = msg.what;
            final RepDialog dialog = new RepDialog(GroupActivity.this);
            dialog.setDialogListener(new RepDialogListener() {
                @Override
                public void onSetClicked() {
                    Toast.makeText(GroupActivity.this, "onSetClicked", Toast.LENGTH_SHORT).show();
                    redirectSetRepActivity(regionCode);
                    dialog.dismiss();
                }

                @Override
                public void onDeleteClicked() {
                    Toast.makeText(GroupActivity.this, "onDeleteClicked", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    };

    PorterShapeImageView.OnTouchListener mClickListener = new View.OnTouchListener() {
        int x = 0, y = 0;
        float startX = 0, startY = 0, distanceSum = 0;
        int transparency;

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Bitmap bm = ((BitmapDrawable) imageViews[Integer.parseInt(v.getContentDescription().toString())].getDrawable()).getBitmap();
            Log.e("touch", v.getContentDescription().toString());
            if (event.getPointerCount() >= 2) {
                handler.removeMessages(longClickId);
                longClickId = -1;
                return false;
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) pxToDp(mContext, event.getX());
                    y = (int) pxToDp(mContext, event.getY());
                    distanceSum = 0;
                    startX = event.getX(0);
                    startY = event.getY(0);
                    transparency = bm.getPixel(x, y);
                    Log.e("down transParency : ", Integer.toString(transparency));
                    if (transparency != 0 && longClickId == -1) {
                        longClickId = Integer.parseInt(v.getContentDescription().toString());
                        handler.sendEmptyMessageAtTime(longClickId, event.getDownTime() + (long) 1000);
                    } else if (longClickId != -1) {
                        return true;
                    }
                    return transparency != 0;
                case MotionEvent.ACTION_UP:
                    transparency = bm.getPixel(x, y);
                    Log.e("up transParency : ", Integer.toString(transparency));
                    if (transparency != 0 && longClickId != -1) {
                        Toast.makeText(getApplicationContext(), v.getContentDescription(), Toast.LENGTH_SHORT).show();
                        redirectRegionActivity(Integer.parseInt(v.getContentDescription().toString()));
                        handler.removeMessages(longClickId);
                        longClickId = -1;
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

    public void redirectRegionActivity(int cityKey) {
        Intent intent = new Intent(this, StoryActivity.class);
        intent.putExtra("mid", mid);
        intent.putExtra("cityKey", globalApplication.cityKeyInt.get(cityKey));

        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_not_move);
    }

    void redirectSetRepActivity(int regionCode) {
        Intent intent = new Intent(this, SetRepActivity.class);
        intent.putExtra("mid", mid);
        intent.putExtra("cityKey", globalApplication.cityKeyInt.get(regionCode));
        intent.putExtra("regionCode", regionCode);
        startActivityForResult(intent, SET_REP_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SET_REP_REQUEST) {
            if (resultCode == RESULT_OK) {
                Integer regionCode = data.getIntExtra("regionCode", -1);
                String path = data.getStringExtra("path");
                Log.e("GroupActivity", "regionCode : " + Integer.toString(regionCode));
                Log.e("GroupActivity", "path : " + path);
                Bitmap bm = BitmapFactory.decodeFile(path);
                porterShapeImageViews[regionCode].setImageBitmap(bm);
                mBorders[regionCode].setVisibility(View.VISIBLE);
            }
        }
    }


    /* request function */
    void getMapInfoRequest() {
        final Call<GetMapInfo> res = NetworkHelper.getInstance().getService().getMapInfo("Bearer " + globalApplication.token, mid);
        res.enqueue(new Callback<GetMapInfo>() {
            @Override
            public void onResponse(Call<GetMapInfo> call, Response<GetMapInfo> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        setRep(response.body().getData().getGetMapInfoDataRepresents());
                        for (int i = 0; i < response.body().getData().getGetMapInfoDataOwners().size(); i++) {
                            String thumbnail = response.body().getData().getGetMapInfoDataOwners().get(i).getThumbnail();
                            String name = response.body().getData().getGetMapInfoDataOwners().get(i).getNickname();
                            String uid = response.body().getData().getGetMapInfoDataOwners().get(i).getUid();
                            globalApplication.userThumbnail.put(uid, thumbnail);
                            globalApplication.userNickName.put(uid, name);
                            MemberListViewItem memberListViewItem = new MemberListViewItem(thumbnail, name);
                            adapter.addItem(memberListViewItem);
                        }
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e("requestCreateMap", Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<GetMapInfo> call, Throwable t) {
                Log.e("[onFailure]", t.getLocalizedMessage());
            }
        });
    }

    void setMapRepRequest(final String mid) {
        final Call<SetMapRep> res = NetworkHelper.getInstance().getService().setMapRep("Bearer " + globalApplication.token, mid, new SetMapRepRequest(false));
        res.enqueue(new Callback<SetMapRep>() {
            @Override
            public void onResponse(Call<SetMapRep> call, Response<SetMapRep> response) {
                if (response.isSuccessful()) {
                    Log.e("GroupActivity", "[setMapRepRequest] is success , mid : " + mid);

                    globalApplication.authorization.getUserData().setPrimary(mid);
                } else {
                    Log.e("GroupActivity", "[setMapRepRequest] onResponse is fail : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SetMapRep> call, Throwable t) {
                Log.e("GroupActivity", "[setMapRepRequest] is fail : " + t.getLocalizedMessage());
            }
        });
    }

    void getMapImage() {
        View v = findViewById(R.id.relativeLayout_mapContainer);
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        // MediaStore 에 image 저장
        String filePath = MediaStore.Images.Media.insertImage(getContentResolver(), b, "title", "description");
        Uri myUri = Uri.parse(filePath);
    }

    void setRep(GetMapInfoDataRepresents getMapInfoDataRepresents) {
        String gyeonggi = getMapInfoDataRepresents.getGyeonggi();
        String gangwon = getMapInfoDataRepresents.getGangwon();
        String chungbuk = getMapInfoDataRepresents.getChungbuk();
        String chungnam = getMapInfoDataRepresents.getChungnam();
        String jeonbuk = getMapInfoDataRepresents.getJeonbuk();
        String jeonnam = getMapInfoDataRepresents.getJeonnam();
        String gyeongbuk = getMapInfoDataRepresents.getGyeongbuk();
        String gyeongnam = getMapInfoDataRepresents.getGyeongnam();
        String jeju = getMapInfoDataRepresents.getJeju();

        if (gyeonggi != null) {
            Glide.with(getApplicationContext()).load(gyeonggi).
                    into(porterShapeImageViews[1]);
            mBorders[1].setVisibility(View.VISIBLE);
        }
        if (gangwon != null) {
            Glide.with(getApplicationContext()).load(gangwon).
                    into(porterShapeImageViews[2]);
            mBorders[2].setVisibility(View.VISIBLE);
        }
        if (chungbuk != null) {
            Glide.with(getApplicationContext()).load(chungbuk).
                    into(porterShapeImageViews[3]);
            mBorders[3].setVisibility(View.VISIBLE);
        }
        if (chungnam != null) {
            Glide.with(getApplicationContext()).load(chungnam).
                    into(porterShapeImageViews[4]);
            mBorders[4].setVisibility(View.VISIBLE);
        }
        if (jeonbuk != null) {
            Glide.with(getApplicationContext()).load(jeonbuk).
                    into(porterShapeImageViews[5]);
            mBorders[5].setVisibility(View.VISIBLE);
        }
        if (jeonnam != null) {
            Glide.with(getApplicationContext()).load(jeonnam).
                    into(porterShapeImageViews[6]);
            mBorders[6].setVisibility(View.VISIBLE);
        }
        if (gyeongbuk != null) {
            Glide.with(getApplicationContext()).load(gyeongbuk).
                    into(porterShapeImageViews[7]);
            mBorders[7].setVisibility(View.VISIBLE);
        }
        if (gyeongnam != null) {
            Glide.with(getApplicationContext()).load(gyeongnam).
                    into(porterShapeImageViews[8]);
            mBorders[8].setVisibility(View.VISIBLE);
        }
        if (jeju != null) {
            Glide.with(getApplicationContext()).load(jeju).
                    into(porterShapeImageViews[9]);
            mBorders[9].setVisibility(View.VISIBLE);
        }
    }
}


