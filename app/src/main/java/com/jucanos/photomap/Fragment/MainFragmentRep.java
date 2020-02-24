package com.jucanos.photomap.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jucanos.photomap.Activity.GroupActivity;
import com.jucanos.photomap.Dialog.YesNoDialog;
import com.jucanos.photomap.Dialog.YesNoDialogListener;
import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.R;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.jucanos.photomap.Structure.GetMapInfo;
import com.jucanos.photomap.Structure.GetMapInfoDataRepresents;

import java.util.ArrayList;

import dagger.multibindings.IntoMap;
import mehdi.sakout.dynamicbox.DynamicBox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragmentRep extends Fragment {
    private PorterShapeImageView imageView_gyeonggi; // 1
    private ImageView imageView_gyeonggi_front;
    private PorterShapeImageView imageView_gangwon; // 2
    private ImageView imageView_gangwon_front;
    private PorterShapeImageView imageView_chungbuk; // 3
    private ImageView imageView_chungbuk_front;
    private PorterShapeImageView imageView_chungnam; // 4
    private ImageView imageView_chungnam_front;
    private PorterShapeImageView imageView_jeonbuk; // 5
    private ImageView imageView_jeonbuk_front;
    private PorterShapeImageView imageView_jeonnam; // 6
    private ImageView imageView_jeonnam_front;
    private PorterShapeImageView imageView_gyeongbuk; // 7
    private ImageView imageView_gyeongbuk_front;
    private PorterShapeImageView imageView_gyeongnam; // 8
    private ImageView imageView_gyeongnam_front;
    private PorterShapeImageView imageView_jeju; // 9
    private ImageView imageView_jeju_front;

    final PorterShapeImageView[] porterShapeImageViews = new PorterShapeImageView[10];
    final ImageView[] imageViews = new ImageView[10];
    final ImageView[] mBorders = new ImageView[10];
    final int[] mWhite = new int[10];
    final int[] mBlack = new int[10];
    private final int[] mDefault = new int[10];

    private RelativeLayout noRep, existRep, layout_map;
    private DynamicBox box;
    private Context pContext;

    private String LOADING_ONLY_PROGRESS = "loading_only_progress";

    private ValueEventListener mValueEventListener = null;
    private String mValueEventListenerMid = null;

    private String title;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_rep, container, false);
        setToolbar(view);
        initMember(view);
        setBox();
        setRep();
        return view;
    }

    private void setToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_tb);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("대표지도");
        setHasOptionsMenu(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initMember(View view) {
        pContext = getActivity().getApplicationContext();
        noRep = view.findViewById(R.id.layout_noRep);
        existRep = view.findViewById(R.id.relativeLayout_existRep);
        layout_map = view.findViewById(R.id.layout_map);


        layout_map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    final YesNoDialog yesNoDialog = new YesNoDialog(getActivity(), "대표지도의 그룹으로 이동하시겠습니까?");
                    yesNoDialog.setDialogListener(new YesNoDialogListener() {
                        @Override
                        public void onPositiveClicked() {
                            yesNoDialog.dismiss();
                            redirectGroupActivity(mValueEventListenerMid, title);
                        }
                        @Override
                        public void onNegativeClicked() {
                            yesNoDialog.dismiss();
                        }
                    });
                    yesNoDialog.show();
                }
                return true;
            }
        });


        // PorterShapeImageView
        imageView_gyeonggi = view.findViewById(R.id.imageView_gyeonggi);
        imageView_gyeonggi_front = view.findViewById(R.id.imageView_gyeonggi_front);
        mBorders[1] = view.findViewById(R.id.imageView_gyeonggi_border);
        porterShapeImageViews[1] = imageView_gyeonggi;
        imageViews[1] = imageView_gyeonggi_front;
        mWhite[1] = R.drawable.ic_map_gyeonggi_white;
        mBlack[1] = R.drawable.ic_map_gyeonggi_black;
        mDefault[1] = R.drawable.map_gyeonggi;

        imageView_gangwon = view.findViewById(R.id.imageView_gangwon);
        imageView_gangwon_front = view.findViewById(R.id.imageView_gangwon_front);
        porterShapeImageViews[2] = imageView_gangwon;
        imageViews[2] = imageView_gangwon_front;
        mBorders[2] = view.findViewById(R.id.imageView_gangwon_border);
        mWhite[2] = R.drawable.ic_map_gangwon_white;
        mBlack[2] = R.drawable.ic_map_gangwon_black;
        mDefault[2] = R.drawable.map_gangwon;


        imageView_chungbuk = view.findViewById(R.id.imageView_chungbuk);
        imageView_chungbuk_front = view.findViewById(R.id.imageView_chungbuk_front);
        porterShapeImageViews[3] = imageView_chungbuk;
        imageViews[3] = imageView_chungbuk_front;
        mBorders[3] = view.findViewById(R.id.imageView_chungbuk_border);
        mWhite[3] = R.drawable.ic_map_chungbuk_white;
        mBlack[3] = R.drawable.ic_map_chungbuk_black;
        mDefault[3] = R.drawable.map_chungbuk;


        imageView_chungnam = view.findViewById(R.id.imageView_chungnam);
        imageView_chungnam_front = view.findViewById(R.id.imageView_chungnam_front);
        porterShapeImageViews[4] = imageView_chungnam;
        imageViews[4] = imageView_chungnam_front;
        mBorders[4] = view.findViewById(R.id.imageView_chungnam_border);
        mWhite[4] = R.drawable.ic_map_chungnam_white;
        mBlack[4] = R.drawable.ic_map_chungnam_black;
        mDefault[4] = R.drawable.map_chungnam;

        imageView_jeonbuk = view.findViewById(R.id.imageView_jeonbuk);
        imageView_jeonbuk_front = view.findViewById(R.id.imageView_jeonbuk_front);
        porterShapeImageViews[5] = imageView_jeonbuk;
        imageViews[5] = imageView_jeonbuk_front;
        mBorders[5] = view.findViewById(R.id.imageView_jeonbuk_border);
        mWhite[5] = R.drawable.ic_map_junbuk_white;
        mBlack[5] = R.drawable.ic_map_junbuk_black;
        mDefault[5] = R.drawable.map_jeonbuk;

        imageView_jeonnam = view.findViewById(R.id.imageView_jeonnam);
        imageView_jeonnam_front = view.findViewById(R.id.imageView_jeonnam_front);
        porterShapeImageViews[6] = imageView_jeonnam;
        imageViews[6] = imageView_jeonnam_front;
        mBorders[6] = view.findViewById(R.id.imageView_jeonnam_border);
        mBlack[6] = R.drawable.ic_map_junnam_black;
        mWhite[6] = R.drawable.ic_map_junnam_white;
        mDefault[6] = R.drawable.map_jeonnam;

        imageView_gyeongbuk = view.findViewById(R.id.imageView_gyeongbuk);
        imageView_gyeongbuk_front = view.findViewById(R.id.imageView_gyeongbuk_front);
        porterShapeImageViews[7] = imageView_gyeongbuk;
        imageViews[7] = imageView_gyeongbuk_front;
        mBorders[7] = view.findViewById(R.id.imageView_gyeongbuk_border);
        mWhite[7] = R.drawable.ic_map_gyeongbuk_white;
        mBlack[7] = R.drawable.ic_map_gyeongbuk_black;
        mDefault[7] = R.drawable.map_gyeongbuk;

        imageView_gyeongnam = view.findViewById(R.id.imageView_gyeongnam);
        imageView_gyeongnam_front = view.findViewById(R.id.imageView_gyeongnam_front);
        porterShapeImageViews[8] = imageView_gyeongnam;
        imageViews[8] = imageView_gyeongnam_front;
        mBorders[8] = view.findViewById(R.id.imageView_gyeongnam_border);
        mWhite[8] = R.drawable.ic_map_gyeongnam_white;
        mBlack[8] = R.drawable.ic_map_gyeongnam_black;
        mDefault[8] = R.drawable.map_gyeongnam;

        imageView_jeju = view.findViewById(R.id.imageView_jeju);
        imageView_jeju_front = view.findViewById(R.id.imageView_jeju_front);
        porterShapeImageViews[9] = imageView_jeju;
        imageViews[9] = imageView_jeju_front;
        mBorders[9] = view.findViewById(R.id.imageView_jeju_border);
        mWhite[9] = R.drawable.ic_map_jeju_white;
        mBlack[9] = R.drawable.ic_map_jeju_black;
        mDefault[9] = R.drawable.map_jeju;

    }

    private void setBox() {
        box = new DynamicBox(getActivity(), existRep);
        View customView = getLayoutInflater().inflate(R.layout.loading_only_progress, null, false);
        box.addCustomView(customView, LOADING_ONLY_PROGRESS);
    }

    // 대표 지도 유뮤 체크 후 대표 사진 불러오기
    public void setRep() {
        final String repMid = GlobalApplication.getGlobalApplicationContext().authorization.getUserData().getPrimary();
        box.showCustomView(LOADING_ONLY_PROGRESS);
        if (repMid == null) {
            Log.e("setRep", "repMid == null");
            if (mValueEventListener != null) {
                GlobalApplication.getGlobalApplicationContext().mRefMaps.child(mValueEventListenerMid).removeEventListener(mValueEventListener);
                mValueEventListener = null;
                mValueEventListenerMid = null;
            }
            setLayout(false);
        } else {
            if (mValueEventListener == null) {
                Log.e("setRep", "mValueEventListener == null");

                mValueEventListenerMid = repMid;
                mValueEventListener = GlobalApplication.getGlobalApplicationContext().mRefMaps.child(repMid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (GlobalApplication.getGlobalApplicationContext().authorization.getUserData().getPrimary() == null) {
                            Log.e("onDataChange", "GlobalApplication.getGlobalApplicationContext().authorization.getUserData().getPrimary() == null");
                            mValueEventListener = null;
                            mValueEventListenerMid = null;
                            setLayout(false);
                            return;
                        }
                        getMapInfoRequest(mValueEventListenerMid);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            } else {
                Log.e("setRep", "mValueEventListener != null");
                GlobalApplication.getGlobalApplicationContext().mRefMaps.child(mValueEventListenerMid).removeEventListener(mValueEventListener);
                mValueEventListenerMid = repMid;
                mValueEventListener = GlobalApplication.getGlobalApplicationContext().mRefMaps.child(repMid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (GlobalApplication.getGlobalApplicationContext().authorization.getUserData().getPrimary() == null) {
                            Log.e("onDataChange", "GlobalApplication.getGlobalApplicationContext().authorization.getUserData().getPrimary() == null");
                            GlobalApplication.getGlobalApplicationContext().mRefMaps.child(mValueEventListenerMid).removeEventListener(mValueEventListener);
                            mValueEventListener = null;
                            mValueEventListenerMid = null;
                            setLayout(false);
                            return;
                        }
                        getMapInfoRequest(mValueEventListenerMid);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        }
    }

    // 맵 정보 가져오기 request
    void getMapInfoRequest(String mid) {
        final Call<GetMapInfo> res = NetworkHelper.getInstance().getService().getMapInfo(GlobalApplication.getGlobalApplicationContext().token, mid);
        res.enqueue(new Callback<GetMapInfo>() {
            @Override
            public void onResponse(Call<GetMapInfo> call, Response<GetMapInfo> response) {
                if (response.isSuccessful()) {
                    GetMapInfo getMapInfo = response.body();
                    if (response.body() != null) {
                        setRep(getMapInfo.getData().getGetMapInfoDataRepresents());
                        title = getMapInfo.getData().getName();
                        Log.e("getMapInfoRequest", "response.isSuccessful()");
                    }
                } else {
                    Log.e("getMapInfoRequest", "response.isNotSuccessful()");
                }
            }

            @Override
            public void onFailure(Call<GetMapInfo> call, Throwable t) {
                Log.e("getMapInfoRequest", t.getLocalizedMessage());
            }
        });
    }

    // 가져온 대표지도 사진정보로 지도 셋팅
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
        ArrayList<String> paths = new ArrayList<>();
        paths.add("");
        paths.add(gyeonggi);
        paths.add(gangwon);
        paths.add(chungbuk);
        paths.add(chungnam);
        paths.add(jeonbuk);
        paths.add(jeonnam);
        paths.add(gyeongbuk);
        paths.add(gyeongnam);
        paths.add(jeju);

        for (int i = 1; i <= 9; i++) {
            Log.e("error pos", Integer.toString(i));
            String path = paths.get(i);
            if (path != null) {
                Glide.with(getActivity()).load(path).into(porterShapeImageViews[i]);
                mBorders[i].setImageResource(mWhite[i]);
            } else {
                porterShapeImageViews[i].setImageResource(mDefault[i]);
                mBorders[i].setImageResource(mBlack[i]);
            }
        }
        setLayout(true);
    }

    // toolbar 메뉴
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_add:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void setLayout(boolean rep) {
        Log.e("setLayout", Boolean.toString(rep));
        if (!rep) {
            noRep.setVisibility(View.VISIBLE);
            existRep.setVisibility(View.GONE);
            layout_map.setVisibility(View.GONE);
        } else {
            noRep.setVisibility(View.GONE);
            existRep.setVisibility(View.VISIBLE);
            layout_map.setVisibility(View.VISIBLE);

        }
        box.hideAll();
    }



    // redirect
    private void redirectGroupActivity(String mid, String title) {
        final Intent intent = new Intent(getActivity(), GroupActivity.class);
        intent.putExtra("mid", mid);
        intent.putExtra("title", title);
        startActivity(intent);
    }
}