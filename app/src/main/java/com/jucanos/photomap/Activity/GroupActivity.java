package com.jucanos.photomap.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
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
import com.jucanos.photomap.Structure.SetRep;
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

import okhttp3.MediaType;
import okhttp3.RequestBody;
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

    private RelativeLayout rl_drawer;
    private RelativeLayout.LayoutParams layoutParams;
    private final PorterShapeImageView[] porterShapeImageViews = new PorterShapeImageView[10];
    private final ImageView[] mBorders = new ImageView[10];
    private final int[] mWhite = new int[10];
    private final int[] mBlack = new int[10];
    private final ImageView[] imageViews = new ImageView[10];

    private DrawerLayout drawerLayout_drawer;
    private ListView listView_member;
    private MemberListViewAdapter adapter;
    private View mView;
    private RelativeLayout mContainer, pg;

    // floating action button 객체
    private FloatingActionMenu floatingActionMenu_menu;
    private FloatingActionButton floatingActionButton_save, floatingActionButton_rep;

    private String mid, title;
    private int longClickId = -1;

    private Integer SET_REP_REQUEST = 1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        getIntentData();
        setToolbar();
        initView();

        drawerLayout_drawer.openDrawer(Gravity.RIGHT);
        drawerLayout_drawer.closeDrawer(GravityCompat.END);
        adapter = new MemberListViewAdapter(getApplicationContext());
        listView_member.setAdapter(adapter);


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
        zoomView.setClipChildren(true);
        zoomView.addView(mView);
        zoomView.setWillNotDraw(false);
        zoomView.setLayoutParams(layoutParams);
        zoomView.setMaxZoom(8f);
        // 컨테이너 설정후 zoomView 추가
        rl_drawer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mContainer = findViewById(R.id.fragmentViewPager_container);
        mContainer.setClipChildren(true);
        mContainer.addView(zoomView);

        // PorterShapeImageView
        imageView_gyeonggi = findViewById(R.id.imageView_gyeonggi);
        imageView_gyeonggi_front = findViewById(R.id.imageView_gyeonggi_front);
        mBorders[1] = findViewById(R.id.imageView_gyeonggi_border);

        imageView_gyeonggi.setOnTouchListener(mClickListener);
        porterShapeImageViews[1] = imageView_gyeonggi;
        imageViews[1] = imageView_gyeonggi_front;
        mWhite[1] = R.drawable.ic_map_gyeonggi_white;
        mBlack[1] = R.drawable.ic_map_gyeonggi_black;

        imageView_gangwon = findViewById(R.id.imageView_gangwon);
        imageView_gangwon.setOnTouchListener(mClickListener);
        imageView_gangwon_front = findViewById(R.id.imageView_gangwon_front);
        porterShapeImageViews[2] = imageView_gangwon;
        imageViews[2] = imageView_gangwon_front;
        mBorders[2] = findViewById(R.id.imageView_gangwon_border);
        mWhite[2] = R.drawable.ic_map_gangwon_white;
        mBlack[2] = R.drawable.ic_map_gangwon_black;

        imageView_chungbuk = findViewById(R.id.imageView_chungbuk);
        imageView_chungbuk.setOnTouchListener(mClickListener);
        imageView_chungbuk_front = findViewById(R.id.imageView_chungbuk_front);
        porterShapeImageViews[3] = imageView_chungbuk;
        imageViews[3] = imageView_chungbuk_front;
        mBorders[3] = findViewById(R.id.imageView_chungbuk_border);
        mWhite[3] = R.drawable.ic_map_chungbuk_white;
        mBlack[3] = R.drawable.ic_map_chungbuk_black;


        imageView_chungnam = findViewById(R.id.imageView_chungnam);
        imageView_chungnam.setOnTouchListener(mClickListener);
        imageView_chungnam_front = findViewById(R.id.imageView_chungnam_front);
        porterShapeImageViews[4] = imageView_chungnam;
        imageViews[4] = imageView_chungnam_front;
        mBorders[4] = findViewById(R.id.imageView_chungnam_border);
        mWhite[4] = R.drawable.ic_map_chungnam_white;
        mBlack[4] = R.drawable.ic_map_chungnam_black;

        imageView_jeonbuk = findViewById(R.id.imageView_jeonbuk);
        imageView_jeonbuk.setOnTouchListener(mClickListener);
        imageView_jeonbuk_front = findViewById(R.id.imageView_jeonbuk_front);
        porterShapeImageViews[5] = imageView_jeonbuk;
        imageViews[5] = imageView_jeonbuk_front;
        mBorders[5] = findViewById(R.id.imageView_jeonbuk_border);
        mWhite[5] = R.drawable.ic_map_junbuk_white;
        mBlack[5] = R.drawable.ic_map_junbuk_black;

        imageView_jeonnam = findViewById(R.id.imageView_jeonnam);
        imageView_jeonnam.setOnTouchListener(mClickListener);
        imageView_jeonnam_front = findViewById(R.id.imageView_jeonnam_front);
        porterShapeImageViews[6] = imageView_jeonnam;
        imageViews[6] = imageView_jeonnam_front;
        mBorders[6] = findViewById(R.id.imageView_jeonnam_border);
        mWhite[6] = R.drawable.ic_map_junnam_white;
        mBlack[6] = R.drawable.ic_map_junnam_black;

        imageView_gyeongbuk = findViewById(R.id.imageView_gyeongbuk);
        imageView_gyeongbuk.setOnTouchListener(mClickListener);
        imageView_gyeongbuk_front = findViewById(R.id.imageView_gyeongbuk_front);
        porterShapeImageViews[7] = imageView_gyeongbuk;
        imageViews[7] = imageView_gyeongbuk_front;
        mBorders[7] = findViewById(R.id.imageView_gyeongbuk_border);
        mWhite[7] = R.drawable.ic_map_gyeongbuk_white;
        mBlack[7] = R.drawable.ic_map_gyeongbuk_black;

        imageView_gyeongnam = findViewById(R.id.imageView_gyeongnam);
        imageView_gyeongnam.setOnTouchListener(mClickListener);
        imageView_gyeongnam_front = findViewById(R.id.imageView_gyeongnam_front);
        porterShapeImageViews[8] = imageView_gyeongnam;
        imageViews[8] = imageView_gyeongnam_front;
        mBorders[8] = findViewById(R.id.imageView_gyeongnam_border);
        mWhite[8] = R.drawable.ic_map_gyeongnam_white;
        mBlack[8] = R.drawable.ic_map_gyeongnam_black;

        imageView_jeju = findViewById(R.id.imageView_jeju);
        imageView_jeju.setOnTouchListener(mClickListener);
        imageView_jeju_front = findViewById(R.id.imageView_jeju_front);
        porterShapeImageViews[9] = imageView_jeju;
        imageViews[9] = imageView_jeju_front;
        mBorders[9] = findViewById(R.id.imageView_jeju_border);
        mWhite[9] = R.drawable.ic_map_jeju_white;
        mBlack[9] = R.drawable.ic_map_jeju_black;

        floatingActionButton_rep = findViewById(R.id.floatingActionButton_rep);
        floatingActionButton_save = findViewById(R.id.floatingActionButton_save);
        floatingActionMenu_menu = findViewById(R.id.floatingActionMenu_menu);

        floatingActionMenu_menu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (floatingActionMenu_menu.isOpened()) {
                    floatingActionMenu_menu.close(true);
                    return true;
                }
                return false;
            }
        });


        floatingActionButton_rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu_menu.close(true);
                setMapRepRequest(mid, "false");
            }
        });

        floatingActionButton_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu_menu.close(true);
                getMapImage();
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

        GlobalApplication.getGlobalApplicationContext().mRefMaps.child(mid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("GroupActivity", "[mRefMaps] onDataChange");
                getMapInfoRequest();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("GroupActivity", "[mRefMaps] onCancelled");
            }
        });

        // getMapInfoRequest();
    }

    private void getIntentData() {
        globalApplication = GlobalApplication.getGlobalApplicationContext();
        mContext = this;
        mid = getIntent().getStringExtra("mid");
        title = getIntent().getStringExtra("title");
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        drawerLayout_drawer = findViewById(R.id.drawer_layout);
        listView_member = findViewById(R.id.listView_member);
        mView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_map, null, false);
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rl_drawer = findViewById(R.id.rl_drawer);
        pg = findViewById(R.id.pg);
        pg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            longClickId = -1;
            final int regionCode = msg.what;
            final RepDialog dialog = new RepDialog(GroupActivity.this);
            dialog.setDialogListener(new RepDialogListener() {
                @Override
                public void onSetClicked() {
                    redirectSetRepActivity(regionCode);
                    dialog.dismiss();
                }

                @Override
                public void onDeleteClicked() {
                    deleteRepRequest(GlobalApplication.getGlobalApplicationContext().cityKeyInt.get(regionCode), regionCode);
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
                        redirectStoryActivity(Integer.parseInt(v.getContentDescription().toString()));
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


    public void redirectStoryActivity(int cityKey) {
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

    void getMapInfoRequest() {
        Log.e("GroupActivity", "getMapInfoRequest");
        final Call<GetMapInfo> res = NetworkHelper.getInstance().getService().getMapInfo(globalApplication.token, mid);
        res.enqueue(new Callback<GetMapInfo>() {
            @Override
            public void onResponse(Call<GetMapInfo> call, Response<GetMapInfo> response) {
                if (response.isSuccessful()) {
                    Log.e("GroupActivity", "response.isSuccessful()");
                    adapter.clear();
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

    void setMapRepRequest(final String mid, String remove) {
        pg.setVisibility(View.VISIBLE);
        final Call<SetMapRep> res = NetworkHelper.getInstance().getService().setMapRep(globalApplication.token, mid, new SetMapRepRequest(remove));
        res.enqueue(new Callback<SetMapRep>() {
            @Override
            public void onResponse(Call<SetMapRep> call, Response<SetMapRep> response) {
                if (response.isSuccessful()) {
                    Log.e("setMapRepRequest", "response.isSuccessful()");
                    pg.setVisibility(View.INVISIBLE);
                    globalApplication.authorization.getUserData().setPrimary(mid);
                }else {
                    Log.e("setMapRepRequest", "response.isNotSuccessful()");
                }
            }

            @Override
            public void onFailure(Call<SetMapRep> call, Throwable t) {
                Log.e("setMapRepRequest", t.getLocalizedMessage());
            }
        });
    }

    void getMapImage() {
        pg.setVisibility(View.VISIBLE);
        View v = findViewById(R.id.relativeLayout_mapContainer);
        v.setBackgroundColor(Color.WHITE);
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        // MediaStore 에 image 저장
        String filePath = MediaStore.Images.Media.insertImage(getContentResolver(), b, "title", "description");
        Uri myUri = Uri.parse(filePath);
        v.setBackgroundColor(getColor(R.color.colorTransparent));
        pg.setVisibility(View.GONE);
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
            Glide.with(getApplicationContext()).load(gyeonggi).into(porterShapeImageViews[1]);
            mBorders[1].setImageResource(mWhite[1]);
        } else {
            porterShapeImageViews[1].setImageResource(R.drawable.map_gyeonggi);
            mBorders[1].setImageResource(mBlack[1]);
        }

        if (gangwon != null) {
            Glide.with(getApplicationContext()).load(gangwon).into(porterShapeImageViews[2]);
            mBorders[2].setImageResource(mWhite[2]);
        } else {
            porterShapeImageViews[2].setImageResource(R.drawable.map_gangwon);
            mBorders[2].setImageResource(mBlack[2]);
        }

        if (chungbuk != null) {
            Glide.with(getApplicationContext()).load(chungbuk).into(porterShapeImageViews[3]);
            mBorders[3].setImageResource(mWhite[3]);
        } else {
            porterShapeImageViews[3].setImageResource(R.drawable.map_chungbuk);
            mBorders[3].setImageResource(mBlack[3]);
        }

        if (chungnam != null) {
            Glide.with(getApplicationContext()).load(chungnam).into(porterShapeImageViews[4]);
            mBorders[4].setImageResource(mWhite[4]);
        } else {
            porterShapeImageViews[4].setImageResource(R.drawable.map_chungnam);
            mBorders[4].setImageResource(mBlack[4]);
        }

        if (jeonbuk != null) {
            Glide.with(getApplicationContext()).load(jeonbuk).into(porterShapeImageViews[5]);
            mBorders[5].setImageResource(mWhite[5]);
        } else {
            porterShapeImageViews[5].setImageResource(R.drawable.map_jeonbuk);
            mBorders[5].setImageResource(mBlack[5]);
        }

        if (jeonnam != null) {
            Glide.with(getApplicationContext()).load(jeonnam).into(porterShapeImageViews[6]);
            mBorders[6].setImageResource(mWhite[6]);
        } else {
            porterShapeImageViews[6].setImageResource(R.drawable.map_jeonnam);
            mBorders[6].setImageResource(mBlack[6]);
        }

        if (gyeongbuk != null) {
            Glide.with(getApplicationContext()).load(gyeongbuk).into(porterShapeImageViews[7]);
            mBorders[7].setImageResource(mWhite[7]);
        } else {
            porterShapeImageViews[7].setImageResource(R.drawable.map_gyeongbuk);
            mBorders[7].setImageResource(mBlack[7]);
        }

        if (gyeongnam != null) {
            Glide.with(getApplicationContext()).load(gyeongnam).into(porterShapeImageViews[8]);
            mBorders[8].setImageResource(mWhite[8]);
        } else {
            porterShapeImageViews[8].setImageResource(R.drawable.map_gyeongnam);
            mBorders[8].setImageResource(mBlack[8]);
        }

        if (jeju != null) {
            Glide.with(getApplicationContext()).load(jeju).into(porterShapeImageViews[9]);
            mBorders[9].setImageResource(mWhite[9]);
        } else {
            porterShapeImageViews[9].setImageResource(R.drawable.map_jeju);
            mBorders[9].setImageResource(mBlack[9]);
        }


    }

    private void deleteRepRequest(String cityKey, final int regionCode) {
        RequestBody requetCityKey = RequestBody.create(MediaType.parse("text/plain"), cityKey);
        RequestBody requetRemove = RequestBody.create(MediaType.parse("text/plain"), "true");
        HashMap<String, RequestBody> hashMap = new HashMap<>();
        hashMap.put("cityKey", requetCityKey);
        hashMap.put("remove", requetRemove);

        // request
        final Call<SetRep> res = NetworkHelper.getInstance().getService().setRep(globalApplication.token, mid, hashMap, null);
        res.enqueue(new Callback<SetRep>() {
            @Override
            public void onResponse(Call<SetRep> call, Response<SetRep> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e("GroupActivity", response.body().getData().toString());
                        porterShapeImageViews[regionCode].setImageResource(mBlack[regionCode]);
                        mBorders[regionCode].setImageResource(mBlack[regionCode]);
                    }
                } else {
                    Log.e("GroupActivity", "deleteRepRequest error : " + Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<SetRep> call, Throwable t) {
                Log.e("GroupActivity", "deleteRepRequest fail : " + t.getLocalizedMessage());
            }
        });
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
}


