package com.jucanos.photomap.Fragment;

import android.content.Context;
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
import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.R;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.jucanos.photomap.Structure.GetMapInfo;
import com.jucanos.photomap.Structure.GetMapInfoDataRepresents;

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

    private RelativeLayout noRep, existRep;
    private DynamicBox box;
    private Context pContext;

    private String LOADING_ONLY_PROGRESS = "loading_only_progress";

    private ValueEventListener mValueEventListener = null;
    private String mValueEventListenerMid = null;

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

    private void initMember(View view) {
        pContext = getActivity().getApplicationContext();
        noRep = view.findViewById(R.id.layout_noRep);
        existRep = view.findViewById(R.id.relativeLayout_existRep);


        // PorterShapeImageView
        imageView_gyeonggi = view.findViewById(R.id.imageView_gyeonggi);
        imageView_gyeonggi_front = view.findViewById(R.id.imageView_gyeonggi_front);
        porterShapeImageViews[1] = imageView_gyeonggi;
        imageViews[1] = imageView_gyeonggi_front;
        mBorders[1] = view.findViewById(R.id.imageView_gyeonggi_border);
        mWhite[1] = R.drawable.ic_map_gyeonggi_white;
        mBlack[1] = R.drawable.ic_map_gyeonggi_black;

        imageView_gangwon = view.findViewById(R.id.imageView_gangwon);
        imageView_gangwon_front = view.findViewById(R.id.imageView_gangwon_front);
        porterShapeImageViews[2] = imageView_gangwon;
        imageViews[2] = imageView_gangwon_front;
        mBorders[2] = view.findViewById(R.id.imageView_gangwon_border);
        mWhite[2] = R.drawable.ic_map_gangwon_white;
        mBlack[2] = R.drawable.ic_map_gangwon_black;

        imageView_chungbuk = view.findViewById(R.id.imageView_chungbuk);
        imageView_chungbuk_front = view.findViewById(R.id.imageView_chungbuk_front);
        porterShapeImageViews[3] = imageView_chungbuk;
        imageViews[3] = imageView_chungbuk_front;
        mBorders[3] = view.findViewById(R.id.imageView_chungbuk_border);
        mWhite[3] = R.drawable.ic_map_chungbuk_white;
        mBlack[3] = R.drawable.ic_map_chungbuk_black;


        imageView_chungnam = view.findViewById(R.id.imageView_chungnam);
        imageView_chungnam_front = view.findViewById(R.id.imageView_chungnam_front);
        porterShapeImageViews[4] = imageView_chungnam;
        imageViews[4] = imageView_chungnam_front;
        mBorders[4] = view.findViewById(R.id.imageView_chungnam_border);
        mWhite[4] = R.drawable.ic_map_chungnam_white;
        mBlack[4] = R.drawable.ic_map_chungnam_black;

        imageView_jeonbuk = view.findViewById(R.id.imageView_jeonbuk);
        imageView_jeonbuk_front = view.findViewById(R.id.imageView_jeonbuk_front);
        porterShapeImageViews[5] = imageView_jeonbuk;
        imageViews[5] = imageView_jeonbuk_front;
        mBorders[5] = view.findViewById(R.id.imageView_jeonbuk_border);
        mWhite[5] = R.drawable.ic_map_junbuk_white;
        mBlack[5] = R.drawable.ic_map_junbuk_black;

        imageView_jeonnam = view.findViewById(R.id.imageView_jeonnam);
        imageView_jeonnam_front = view.findViewById(R.id.imageView_jeonnam_front);
        porterShapeImageViews[6] = imageView_jeonnam;
        imageViews[6] = imageView_jeonnam_front;
        mBorders[6] = view.findViewById(R.id.imageView_jeonnam_border);
        mWhite[6] = R.drawable.ic_map_junnam_white;
        mBlack[6] = R.drawable.ic_map_junnam_black;

        imageView_gyeongbuk = view.findViewById(R.id.imageView_gyeongbuk);
        imageView_gyeongbuk_front = view.findViewById(R.id.imageView_gyeongbuk_front);
        porterShapeImageViews[7] = imageView_gyeongbuk;
        imageViews[7] = imageView_gyeongbuk_front;
        mBorders[7] = view.findViewById(R.id.imageView_gyeongbuk_border);
        mWhite[7] = R.drawable.ic_map_gyeongbuk_white;
        mBlack[7] = R.drawable.ic_map_gyeongbuk_black;

        imageView_gyeongnam = view.findViewById(R.id.imageView_gyeongnam);
        imageView_gyeongnam_front = view.findViewById(R.id.imageView_gyeongnam_front);
        porterShapeImageViews[8] = imageView_gyeongnam;
        imageViews[8] = imageView_gyeongnam_front;
        mBorders[8] = view.findViewById(R.id.imageView_gyeongnam_border);
        mWhite[8] = R.drawable.ic_map_gyeongnam_white;
        mBlack[8] = R.drawable.ic_map_gyeongnam_black;

        imageView_jeju = view.findViewById(R.id.imageView_jeju);
        imageView_jeju_front = view.findViewById(R.id.imageView_jeju_front);
        porterShapeImageViews[9] = imageView_jeju;
        imageViews[9] = imageView_jeju_front;
        mBorders[9] = view.findViewById(R.id.imageView_jeju_border);
        mWhite[9] = R.drawable.ic_map_jeju_white;
        mBlack[9] = R.drawable.ic_map_jeju_black;
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
            if (mValueEventListener != null) {
                GlobalApplication.getGlobalApplicationContext().mRefMaps.child(mValueEventListenerMid).removeEventListener(mValueEventListener);
                mValueEventListener = null;
                mValueEventListenerMid = null;
            }
            setLayout(false);
        } else {
            if (mValueEventListener == null) {
                mValueEventListenerMid = repMid;
                mValueEventListener = GlobalApplication.getGlobalApplicationContext().mRefMaps.child(repMid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (GlobalApplication.getGlobalApplicationContext().authorization.getUserData().getPrimary() == null) {
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
            } else {
                GlobalApplication.getGlobalApplicationContext().mRefMaps.child(mValueEventListenerMid).removeEventListener(mValueEventListener);
                mValueEventListenerMid = repMid;
                mValueEventListener = GlobalApplication.getGlobalApplicationContext().mRefMaps.child(repMid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (GlobalApplication.getGlobalApplicationContext().authorization.getUserData().getPrimary() == null) {
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

        if (gyeonggi != null) {
            Glide.with(pContext).load(gyeonggi).into(porterShapeImageViews[1]);
            mBorders[1].setImageResource(mWhite[1]);
        } else {
            porterShapeImageViews[1].setImageResource(R.drawable.map_gyeonggi);
            mBorders[1].setImageResource(mBlack[1]);
        }

        if (gangwon != null) {
            Glide.with(pContext).load(gangwon).into(porterShapeImageViews[2]);
            mBorders[2].setImageResource(mWhite[2]);
        } else {
            porterShapeImageViews[2].setImageResource(R.drawable.map_gangwon);
            mBorders[2].setImageResource(mBlack[2]);
        }

        if (chungbuk != null) {
            Glide.with(pContext).load(chungbuk).into(porterShapeImageViews[3]);
            mBorders[3].setImageResource(mWhite[3]);
        } else {
            porterShapeImageViews[3].setImageResource(R.drawable.map_chungbuk);
            mBorders[3].setImageResource(mBlack[3]);
        }

        if (chungnam != null) {
            Glide.with(pContext).load(chungnam).into(porterShapeImageViews[4]);
            mBorders[4].setImageResource(mWhite[4]);
        } else {
            porterShapeImageViews[4].setImageResource(R.drawable.map_chungnam);
            mBorders[4].setImageResource(mBlack[4]);
        }

        if (jeonbuk != null) {
            Glide.with(pContext).load(jeonbuk).into(porterShapeImageViews[5]);
            mBorders[5].setImageResource(mWhite[5]);
        } else {
            porterShapeImageViews[5].setImageResource(R.drawable.map_jeonbuk);
            mBorders[5].setImageResource(mBlack[5]);
        }

        if (jeonnam != null) {
            Glide.with(pContext).load(jeonnam).into(porterShapeImageViews[6]);
            mBorders[6].setImageResource(mWhite[6]);
        } else {
            porterShapeImageViews[6].setImageResource(R.drawable.map_jeonnam);
            mBorders[6].setImageResource(mBlack[6]);
        }

        if (gyeongbuk != null) {
            Glide.with(pContext).load(gyeongbuk).into(porterShapeImageViews[7]);
            mBorders[7].setImageResource(mWhite[7]);
        } else {
            porterShapeImageViews[7].setImageResource(R.drawable.map_gyeongbuk);
            mBorders[7].setImageResource(mBlack[7]);
        }

        if (gyeongnam != null) {
            Glide.with(pContext).load(gyeongnam).into(porterShapeImageViews[8]);
            mBorders[8].setImageResource(mWhite[8]);
        } else {
            porterShapeImageViews[8].setImageResource(R.drawable.map_gyeongnam);
            mBorders[8].setImageResource(mBlack[8]);
        }

        if (jeju != null) {
            Glide.with(pContext).load(jeju).into(porterShapeImageViews[9]);
            mBorders[9].setImageResource(mWhite[9]);
        } else {
            porterShapeImageViews[9].setImageResource(R.drawable.map_jeju);
            mBorders[9].setImageResource(mBlack[9]);
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
        if (!rep) {
            noRep.setVisibility(View.VISIBLE);
            existRep.setVisibility(View.GONE);
        } else {
            noRep.setVisibility(View.GONE);
            existRep.setVisibility(View.VISIBLE);
        }
        box.hideAll();
    }
}