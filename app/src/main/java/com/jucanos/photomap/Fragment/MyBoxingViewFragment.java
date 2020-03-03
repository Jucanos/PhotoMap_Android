package com.jucanos.photomap.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bilibili.boxing.AbsBoxingViewFragment;
import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.model.BoxingManager;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing.model.entity.AlbumEntity;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing.model.entity.impl.ImageMedia;
import com.bilibili.boxing.utils.BoxingFileHelper;
import com.bilibili.boxing_impl.WindowManagerHelper;
import com.bilibili.boxing_impl.ui.BoxingViewActivity;
import com.bilibili.boxing_impl.view.HackyGridLayoutManager;
import com.bilibili.boxing_impl.view.SpacesItemDecoration;
import com.jucanos.photomap.Activity.AddStoryPreviewActivity;
import com.jucanos.photomap.CropView.InstaCropperView;
import com.jucanos.photomap.Dialog.LoadingDialog;
import com.jucanos.photomap.R;
import com.jucanos.photomap.photoPicker.BoxingAlbumAdapter;
import com.jucanos.photomap.photoPicker.CoordinatorLinearLayout;
import com.jucanos.photomap.photoPicker.CoordinatorRecyclerView;
import com.jucanos.photomap.photoPicker.MyBoxingMediaAdapter;
import com.jucanos.photomap.photoPicker.MyMediaItemLayout;
import com.jucanos.photomap.photoPicker.SeletedMediaInfo;
import com.jucanos.photomap.photoPicker.ViewUtils;
import com.jucanos.photomap.util.BitmapUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MyBoxingViewFragment extends AbsBoxingViewFragment implements View.OnClickListener {
    public static final String TAG = "com.bilibili.boxing_impl.ui.BoxingViewFragment";
    private static final int TOP_REMAIN_HEIGHT = 48;
    private static final int IMAGE_PREVIEW_REQUEST_CODE = 9086;
    private static final int IMAGE_CROP_REQUEST_CODE = 9087;

    private static final int GRID_COUNT = 3;

    private boolean mIsPreview;
    private boolean mIsCamera;

    private CoordinatorRecyclerView mRecycleView;
    private MyBoxingMediaAdapter mMediaAdapter;
    private BoxingAlbumAdapter mAlbumWindowAdapter;
    private TextView mTitleTxt;
    private PopupWindow mAlbumPopWindow;

    // intent data
    private String mid, cityKey;

    private int mMaxCount;

    /**
     * My Member
     **/
    // for half fragment view
    private FrameLayout frameLayout_container;
    private CoordinatorLinearLayout parentLayout;

    // for controling cropView image
    private ArrayList<InstaCropperView> instaCropperViews = new ArrayList<>();

    // default album
    private TextView album_text_view;

    // requestcode
    private final int ADD_STORY_REQUEST = 1;

    private TextView textView_next, textView_cancel;

    // for loading proegress
    LoadingDialog loadingDialog;


    public static MyBoxingViewFragment newInstance() {
        return new MyBoxingViewFragment();
    }

    @Override
    public void onCreateWithSelectedMedias(Bundle savedInstanceState, @Nullable List<BaseMedia> selectedMedias) {
        mAlbumWindowAdapter = new BoxingAlbumAdapter(getContext());
        mMediaAdapter = new MyBoxingMediaAdapter(getContext());
        mMediaAdapter.setSelectedMedias(selectedMedias);
        mMaxCount = getMaxCount();
    }

    @Override
    public void startLoading() {
        loadingDialog.show();
        loadMedias();
        loadAlbum();
    }

    @Override
    public void onRequestPermissionError(String[] permissions, Exception e) {
        if (permissions.length > 0) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(getContext(), "사진을 선택하려면 저장 장치에 액세스해야합니다. \"시스템 설정\"또는 인증 대화 상자에서 \"저장 공간\"권한을 허용하십시오.", Toast.LENGTH_SHORT).show();
                showEmptyData();
            } else if (permissions[0].equals(Manifest.permission.CAMERA)) {
                Toast.makeText(getContext(), "사진을 찍으려면 카메라에 액세스해야합니다. \"시스템 설정\"또는 인증 대화 상자에서 \"카메라 사용\"권한을 허용하십시오.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionSuc(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions[0].equals(STORAGE_PERMISSIONS[0])) {
            startLoading();
        } else if (permissions[0].equals(CAMERA_PERMISSIONS[0])) {
            startCamera(getActivity(), this, null);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmant_boxing_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getIntentData();
        initViews(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void getIntentData() {
        mid = getActivity().getIntent().getStringExtra("mid");
        cityKey = getActivity().getIntent().getStringExtra("cityKey");
    }

    private void initViews(View view) {
        loadingDialog = new LoadingDialog(getActivity());
        parentLayout = view.findViewById(R.id.parent_layout);


        frameLayout_container = view.findViewById(R.id.frameLayout_container);
        instaCropperViews.add((InstaCropperView) view.findViewById(R.id.instaCropperView1));
        instaCropperViews.add((InstaCropperView) view.findViewById(R.id.instaCropperView2));
        instaCropperViews.add((InstaCropperView) view.findViewById(R.id.instaCropperView3));
        instaCropperViews.add((InstaCropperView) view.findViewById(R.id.instaCropperView4));
        instaCropperViews.add((InstaCropperView) view.findViewById(R.id.instaCropperView5));

        album_text_view = view.findViewById(R.id.album_text_view);
        textView_next = view.findViewById(R.id.textView_next);
        textView_cancel = view.findViewById(R.id.textView_cancel);

        textView_next.setOnClickListener(this);
        textView_cancel.setOnClickListener(this);

        mRecycleView = view.findViewById(R.id.media_recycleview);
        mRecycleView.setHasFixedSize(true);

        setTitleTxt(album_text_view);
        setLayoutSize();
        initRecycleView();
    }

    private void setLayoutSize() {
        int topViewHeight = ViewUtils.dip2px(TOP_REMAIN_HEIGHT) + ViewUtils.getScreenWidth();
        int topBarHeight = ViewUtils.dip2px(TOP_REMAIN_HEIGHT);
        parentLayout.setTopViewParam(topViewHeight, topBarHeight);
        frameLayout_container.getLayoutParams().height = ViewUtils.getScreenWidth();
        mRecycleView.getLayoutParams().height = ViewUtils.getScreenHeight() - topBarHeight;
        parentLayout.getLayoutParams().height = topViewHeight + ViewUtils.getScreenHeight() - topBarHeight;
        mRecycleView.setCoordinatorListener(parentLayout);
    }


    private void initRecycleView() {
        GridLayoutManager gridLayoutManager = new HackyGridLayoutManager(getActivity(), GRID_COUNT);
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        mRecycleView.setLayoutManager(gridLayoutManager);
        mRecycleView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelOffset(com.bilibili.boxing_impl.R.dimen.boxing_media_margin), GRID_COUNT));
        mMediaAdapter.setOnCameraClickListener(new MyBoxingViewFragment.OnCameraClickListener());
        mMediaAdapter.setOnCheckedListener(new MyBoxingViewFragment.OnMediaCheckedListener());
        mMediaAdapter.setOnMediaClickListener(new MyBoxingViewFragment.OnMediaClickListener());
        mRecycleView.setAdapter(mMediaAdapter);
        mRecycleView.addOnScrollListener(new MyBoxingViewFragment.ScrollListener());
    }

    @Override
    public void showMedia(@Nullable List<BaseMedia> medias, int allCount) {
        if (medias == null || isEmptyData(medias)
                && isEmptyData(mMediaAdapter.getAllMedias())) {
            showEmptyData();
            return;
        }
        showData();
        mMediaAdapter.addAllData(medias);
        checkSelectedMedia(medias, mMediaAdapter.getSelectedMedias());
        instaCropperViews.get(0).setImageUri(Uri.fromFile(new File(mMediaAdapter.getAllMedias().get(0).getPath())));

    }

    private boolean isEmptyData(List<BaseMedia> medias) {
        return medias.isEmpty() && !BoxingManager.getInstance().getBoxingConfig().isNeedCamera();
    }

    private void showEmptyData() {
        mRecycleView.setVisibility(View.GONE);
    }

    private void showData() {
        loadingDialog.dismiss();
        mRecycleView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAlbum(@Nullable List<AlbumEntity> albums) {
        if ((albums == null || albums.isEmpty())
                && mTitleTxt != null) {
            mTitleTxt.setCompoundDrawables(null, null, null, null);
            mTitleTxt.setOnClickListener(null);
            return;
        }
        mAlbumWindowAdapter.addAllData(albums);
    }

    public MyBoxingMediaAdapter getMediaAdapter() {
        return mMediaAdapter;
    }

    @Override
    public void clearMedia() {
        mMediaAdapter.clearData();
    }

    private void updateMultiPickerLayoutState(List<BaseMedia> medias) {

    }

    @Override
    public void onCameraFinish(BaseMedia media) {
        mIsCamera = false;
        if (media == null) {
            return;
        }
        if (hasCropBehavior()) {
            startCrop(media, IMAGE_CROP_REQUEST_CODE);
        } else if (mMediaAdapter != null && mMediaAdapter.getSelectedMedias() != null) {
            List<BaseMedia> selectedMedias = mMediaAdapter.getSelectedMedias();
            selectedMedias.add(media);
            onFinish(selectedMedias);
        }
    }

    @Override
    public void onCameraError() {
        mIsCamera = false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.textView_next:

                if (mMediaAdapter.getSeletedMediaInfoHashMap().size() <= 0) {
                    Toast.makeText(getActivity(), "1장 이상 선택해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }

                redirectAddStoryActivity();
                break;
            case R.id.textView_cancel:
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PREVIEW_REQUEST_CODE) {
            mIsPreview = false;
            boolean isBackClick = data.getBooleanExtra(BoxingViewActivity.EXTRA_TYPE_BACK, false);
            List<BaseMedia> selectedMedias = data.getParcelableArrayListExtra(Boxing.EXTRA_SELECTED_MEDIA);
            onViewActivityRequest(selectedMedias, mMediaAdapter.getAllMedias(), isBackClick);
            if (isBackClick) {
                mMediaAdapter.setSelectedMedias(selectedMedias);
            }
            updateMultiPickerLayoutState(selectedMedias);
        }

    }

    private void onViewActivityRequest(List<BaseMedia> selectedMedias, List<BaseMedia> allMedias, boolean isBackClick) {
        if (isBackClick) {
            checkSelectedMedia(allMedias, selectedMedias);
        } else {
            onFinish(selectedMedias);
        }
    }


    @Override
    public void onCameraActivityResult(int requestCode, int resultCode) {
        super.onCameraActivityResult(requestCode, resultCode);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ArrayList<BaseMedia> medias = (ArrayList<BaseMedia>) getMediaAdapter().getSelectedMedias();
        onSaveMedias(outState, medias);
    }

    private void setTitleTxt(TextView titleTxt) {
        mTitleTxt = titleTxt;
        mTitleTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mAlbumPopWindow == null) {
                    int height = WindowManagerHelper.getScreenHeight(v.getContext()) -
                            (WindowManagerHelper.getToolbarHeight(v.getContext())
                                    + WindowManagerHelper.getStatusBarHeight(v.getContext()));
                    View windowView = createWindowView();
                    mAlbumPopWindow = new PopupWindow(windowView, ViewGroup.LayoutParams.MATCH_PARENT,
                            height, true);
                    mAlbumPopWindow.setAnimationStyle(com.bilibili.boxing_impl.R.style.Boxing_PopupAnimation);
                    mAlbumPopWindow.setOutsideTouchable(true);
                    mAlbumPopWindow.setBackgroundDrawable(new ColorDrawable
                            (ContextCompat.getColor(v.getContext(), com.bilibili.boxing_impl.R.color.boxing_colorPrimaryAlpha)));
                    mAlbumPopWindow.setContentView(windowView);
                }
                mAlbumPopWindow.showAsDropDown(v, 0, 0);
            }

            @NonNull
            private View createWindowView() {
                View view = LayoutInflater.from(getActivity()).inflate(com.bilibili.boxing_impl.R.layout.layout_boxing_album, null);
                RecyclerView recyclerView = view.findViewById(com.bilibili.boxing_impl.R.id.album_recycleview);
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
                recyclerView.addItemDecoration(new SpacesItemDecoration(2, 1));

                View albumShadowLayout = view.findViewById(com.bilibili.boxing_impl.R.id.album_shadow);
                albumShadowLayout.setOnClickListener(v -> dismissAlbumWindow());
                mAlbumWindowAdapter.setAlbumOnClickListener(new MyBoxingViewFragment.OnAlbumItemOnClickListener());
                recyclerView.setAdapter(mAlbumWindowAdapter);
                return view;
            }
        });
    }

    private void dismissAlbumWindow() {
        if (mAlbumPopWindow != null && mAlbumPopWindow.isShowing()) {
            mAlbumPopWindow.dismiss();
        }
    }

    private class ScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            final int childCount = recyclerView.getChildCount();
            if (childCount > 0) {
                View lastChild = recyclerView.getChildAt(childCount - 1);
                RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();
                int lastVisible = recyclerView.getChildAdapterPosition(lastChild);
                if (lastVisible == outerAdapter.getItemCount() - 1 && hasNextPage() && canLoadNextPage()) {
                    onLoadNextPage();
                }
            }
        }
    }

    private class OnMediaClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            BaseMedia media = (BaseMedia) v.getTag();
            int pos = (int) v.getTag(com.bilibili.boxing_impl.R.id.media_item_check);
            BoxingConfig.Mode mode = BoxingManager.getInstance().getBoxingConfig().getMode();
            if (mode == BoxingConfig.Mode.SINGLE_IMG) {
                singleImageClick(media);
            } else if (mode == BoxingConfig.Mode.MULTI_IMG) {
                multiImageClick(v, media, pos);
            } else if (mode == BoxingConfig.Mode.VIDEO) {
                videoClick(media);
            }
        }

        private void videoClick(BaseMedia media) {
            ArrayList<BaseMedia> iMedias = new ArrayList<>();
            iMedias.add(media);
            onFinish(iMedias);
        }

        private void multiImageClick(View view, BaseMedia iMedia, int pos) {
            parentLayout.switchToWhole();
            mRecycleView.smoothScrollToPosition(pos);
            ClickImage(view, iMedia, pos);
        }

        private void singleImageClick(BaseMedia media) {
            ArrayList<BaseMedia> iMedias = new ArrayList<>();
            iMedias.add(media);
            if (hasCropBehavior()) {
                startCrop(media, IMAGE_CROP_REQUEST_CODE);
            } else {
                onFinish(iMedias);
            }
        }
    }


    private class OnCameraClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (!mIsCamera) {
                mIsCamera = true;
                startCamera(getActivity(), MyBoxingViewFragment.this, BoxingFileHelper.DEFAULT_SUB_DIR);
            }
        }
    }

    // 오른쪽위에 클릭했을때
    private class OnMediaCheckedListener implements MyBoxingMediaAdapter.OnMediaCheckedListener {

        @Override
        public void onChecked(View view, BaseMedia iMedia) {
            ClickImage(view, iMedia, 0);
        }
    }

    private class OnAlbumItemOnClickListener implements BoxingAlbumAdapter.OnAlbumClickListener {
        @Override
        public void onClick(View view, int pos) {
            BoxingAlbumAdapter adapter = mAlbumWindowAdapter;
            if (adapter != null && adapter.getCurrentAlbumPos() != pos) {
                List<AlbumEntity> albums = adapter.getAlums();
                adapter.setCurrentAlbumPos(pos);

                AlbumEntity albumMedia = albums.get(pos);
                loadMedias(0, albumMedia.mBucketId);
                mTitleTxt.setText(albumMedia.mBucketName == null ? ("album") : albumMedia.mBucketName);

                for (AlbumEntity album : albums) {
                    album.mIsSelected = false;
                }
                albumMedia.mIsSelected = true;
                adapter.notifyDataSetChanged();
            }
            dismissAlbumWindow();
        }
    }

    public void ClickImage(View view, BaseMedia iMedia, int pos) {
        if (!mIsPreview) {
//            Log.e("[multiImageClick]", "image click");
            if (!(iMedia instanceof ImageMedia)) {
                return;
            }
            ImageMedia photoMedia = (ImageMedia) iMedia;
            boolean preSelected = photoMedia.isSelected();
            MyMediaItemLayout layout = (MyMediaItemLayout) view;

            List<BaseMedia> selectedMedias = mMediaAdapter.getSelectedMedias();
            HashMap<String, SeletedMediaInfo> seletedMediaInfoHashMap = mMediaAdapter.getSeletedMediaInfoHashMap();
//            Log.e("===============", photoMedia.getId() + "==============");
            if (preSelected) {
                SeletedMediaInfo seletedMediaInfo = seletedMediaInfoHashMap.get(photoMedia.getId());
                if (seletedMediaInfo.getCur()) {
//                    Log.e("seletedMediaInfo", "getCur() is true");
                    int removeCount = seletedMediaInfo.getCount();
                    int albumPos = seletedMediaInfo.getAlbumPos();
                    int removePos = seletedMediaInfo.clear();
                    mMediaAdapter.getPq().offer(removePos);

                    seletedMediaInfoHashMap.remove(photoMedia.getId());
                    for (HashMap.Entry<String, SeletedMediaInfo> seletedMediaInfoEntry : seletedMediaInfoHashMap.entrySet()) {
                        if (seletedMediaInfoEntry.getValue().getCount() > removeCount) {
                            seletedMediaInfoEntry.getValue().setCount(seletedMediaInfoEntry.getValue().getCount() - 1);
                            seletedMediaInfoEntry.getValue().setCur(false);
                        }
                        if (seletedMediaInfoEntry.getValue().getCount() == selectedMedias.size() - 2) {
                            seletedMediaInfoEntry.getValue().setCur(true);
//                            Log.e("album","adapter album pos : " + mAlbumWindowAdapter.getCurrentAlbumPos());
//                            Log.e("album","my album pos : " +seletedMediaInfoEntry.getValue().getAlbumPos());

                            if(mAlbumWindowAdapter.getCurrentAlbumPos() == albumPos){
//                                Log.e("album","pos is equal, viewPos : "+ seletedMediaInfoEntry.getValue().getRecyclerviewPos());
                                mRecycleView.smoothScrollToPosition( seletedMediaInfoEntry.getValue().getRecyclerviewPos());
                            }
                        }
                    }
                    photoMedia.setSelected(false);
                    selectedMedias.remove(photoMedia);
                } else {
//                    Log.e("seletedMediaInfo", "getCur() is false");
                    for (HashMap.Entry<String, SeletedMediaInfo> seletedMediaInfoEntry : seletedMediaInfoHashMap.entrySet()) {
                        if (seletedMediaInfoEntry.getValue().getCur()) {
                            seletedMediaInfoEntry.getValue().setCur(false);
//                            Log.e("seletedMediaInfoEntry", seletedMediaInfoEntry.getKey() + "is set False");
                            break;
                        }
                    }
                    seletedMediaInfo.setCur(true);
//                    Log.e("seletedMediaInfoEntry", photoMedia.getId() + "is set true");

                }

            } else {
                if (selectedMedias.size() >= mMaxCount) {
                    Toast.makeText(getActivity(), "최대 5장만 선택 가능합니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!selectedMedias.contains(photoMedia)) {
                    if (photoMedia.isGifOverSize()) {
                        Toast.makeText(getActivity(), "이미지의 크기가 너무 큽니다", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
//                        Log.e("selectedMedias", "new insert");
                        int nPos = selectedMedias.size();

                        for (HashMap.Entry<String, SeletedMediaInfo> seletedMediaInfoEntry : seletedMediaInfoHashMap.entrySet()) {
                            if (seletedMediaInfoEntry.getValue().getCur()) {
                                seletedMediaInfoEntry.getValue().setCur(false);
//                                Log.e("seletedMediaInfoEntry", seletedMediaInfoEntry.getKey() + "is set False");
                                break;
                            }
                        }
                        selectedMedias.add(photoMedia);
                        int cropViewPos = mMediaAdapter.getPq().poll();
                        seletedMediaInfoHashMap.put(photoMedia.getId(), new SeletedMediaInfo(nPos, true, layout, photoMedia, instaCropperViews.get(cropViewPos),pos,mAlbumWindowAdapter.getCurrentAlbumPos()));
//                        Log.e("info","viewPos : " + seletedMediaInfoHashMap.get(photoMedia.getId()).getRecyclerviewPos() + "/ albumPos : " + seletedMediaInfoHashMap.get(photoMedia.getId()).getAlbumPos());
                    }
                }
            }
        }
    }

    public void redirectAddStoryActivity() {
        loadingDialog.show();
        ArrayList<Pair<Integer, String>> aPaths = new ArrayList<>();
        for (HashMap.Entry<String, SeletedMediaInfo> seletedMediaInfoEntry : mMediaAdapter.getSeletedMediaInfoHashMap().entrySet()) {
            seletedMediaInfoEntry.getValue().getmCropView().crop(
                    View.MeasureSpec.makeMeasureSpec(1024, View.MeasureSpec.AT_MOST),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    bitmap -> {
                        try {
                            String fileName = "image_" + System.currentTimeMillis();
                            String path = BitmapUtils.saveBitmap(fileName, bitmap, 50, Objects.requireNonNull(getActivity()));
                            Integer order = seletedMediaInfoEntry.getValue().getCount();
                            aPaths.add(new Pair<Integer, String>(order, path));
                            if (aPaths.size() == mMediaAdapter.getSeletedMediaInfoHashMap().size()) {
                                redirectAddStoryActivity(aPaths);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            );
        }
    }

    void redirectAddStoryActivity(ArrayList<Pair<Integer, String>> aPaths) {
        ArrayList<String> bPaths = new ArrayList<>();
        Collections.sort(aPaths, (o1, o2) -> o1.first.compareTo(o2.first));

        for (int i = 0; i < aPaths.size(); i++) {
            bPaths.add(aPaths.get(i).second);
        }

        Intent intent = new Intent(getActivity(), AddStoryPreviewActivity.class);
        intent.putStringArrayListExtra("paths", bPaths);
        intent.putExtra("mid", mid);
        intent.putExtra("cityKey", cityKey);
        loadingDialog.dismiss();
        Objects.requireNonNull(getActivity()).startActivityForResult(intent, ADD_STORY_REQUEST);
        getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_not_move);
    }
}
