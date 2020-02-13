package com.jucanos.photomap.photoPicker;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
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
import com.jucanos.photomap.R;
import com.naver.android.helloyako.imagecrop.view.ImageCropView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import dagger.multibindings.IntoMap;

public class MyBoxingViewFragment extends AbsBoxingViewFragment implements View.OnClickListener {
    public static final String TAG = "com.bilibili.boxing_impl.ui.BoxingViewFragment";
    private static final int TOP_REMAIN_HEIGHT = 48;
    private static final int IMAGE_PREVIEW_REQUEST_CODE = 9086;
    private static final int IMAGE_CROP_REQUEST_CODE = 9087;

    private static final int GRID_COUNT = 3;

    private boolean mIsPreview;
    private boolean mIsCamera;

    //private Button mPreBtn;
    //private Button mOkBtn;
    private CoordinatorRecyclerView mRecycleView;

    private MyBoxingMediaAdapter mMediaAdapter;
    private BoxingAlbumAdapter mAlbumWindowAdapter;

    private ProgressDialog mDialog;
    private TextView mEmptyTxt;
    private TextView mTitleTxt;
    private PopupWindow mAlbumPopWindow;
    private ProgressBar mLoadingView;

    // My Member

    // for half fragment view
    private FrameLayout frameLayout_container;
    private CoordinatorLinearLayout parentLayout;

    private ArrayList<ImageCropView> imageCropViews = new ArrayList<>();

    // default album
    private TextView album_text_view;

    private int mMaxCount;

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
        loadMedias();
        loadAlbum();
    }

    @Override
    public void onRequestPermissionError(String[] permissions, Exception e) {
        if (permissions.length > 0) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(getContext(), com.bilibili.boxing_impl.R.string.boxing_storage_permission_deny, Toast.LENGTH_SHORT).show();
                showEmptyData();
            } else if (permissions[0].equals(Manifest.permission.CAMERA)) {
                Toast.makeText(getContext(), com.bilibili.boxing_impl.R.string.boxing_camera_permission_deny, Toast.LENGTH_SHORT).show();
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
//        return inflater.inflate(com.bilibili.boxing_impl.R.layout.fragmant_boxing_view, container, false);
        return inflater.inflate(R.layout.fragmant_boxing_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initViews(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void initViews(View view) {
        parentLayout = view.findViewById(R.id.parent_layout);
        frameLayout_container = view.findViewById(R.id.frameLayout_container);
        imageCropViews.add((ImageCropView) view.findViewById(R.id.imageCropView_image1));
        imageCropViews.add((ImageCropView) view.findViewById(R.id.imageCropView_image2));
        imageCropViews.add((ImageCropView) view.findViewById(R.id.imageCropView_image3));
        imageCropViews.add((ImageCropView) view.findViewById(R.id.imageCropView_image4));
        imageCropViews.add((ImageCropView) view.findViewById(R.id.imageCropView_image5));
        album_text_view = view.findViewById(R.id.album_text_view);

//        imageCropView1.setGridInnerMode(ImageCropView.GRID_OFF);
//        imageCropView1.setGridOuterMode(ImageCropView.GRID_OFF);

        mEmptyTxt = (TextView) view.findViewById(R.id.empty_txt);
        mRecycleView = (CoordinatorRecyclerView) view.findViewById(R.id.media_recycleview);
        mRecycleView.setHasFixedSize(true);
        mLoadingView = (ProgressBar) view.findViewById(R.id.loading);


        setTitleTxt(album_text_view);
        setLayoutSize();
        initRecycleView();

        //boolean isMultiImageMode = BoxingManager.getInstance().getBoxingConfig().isMultiImageMode();
        //View multiImageLayout = view.findViewById(R.id.multi_picker_layout);
        //multiImageLayout.setVisibility(isMultiImageMode ? View.VISIBLE : View.GONE);
        //if (isMultiImageMode) {
//            mPreBtn = (Button) view.findViewById(R.id.choose_preview_btn);
//            mOkBtn = (Button) view.findViewById(R.id.choose_ok_btn);
//
//            mPreBtn.setText("미리보기");
//            mOkBtn.setText("확인");
//
//            mPreBtn.setOnClickListener(this);
//            mOkBtn.setOnClickListener(this);
        //  updateMultiPickerLayoutState(mMediaAdapter.getSelectedMedias());
        //}
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
        imageCropViews.get(0).setImageFilePath(mMediaAdapter.getAllMedias().get(0).getPath());
    }

    private boolean isEmptyData(List<BaseMedia> medias) {
        return medias.isEmpty() && !BoxingManager.getInstance().getBoxingConfig().isNeedCamera();
    }

    private void showEmptyData() {
        mLoadingView.setVisibility(View.GONE);
        mEmptyTxt.setVisibility(View.VISIBLE);
        mRecycleView.setVisibility(View.GONE);
    }

    private void showData() {
        mLoadingView.setVisibility(View.GONE);
        mEmptyTxt.setVisibility(View.GONE);
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
        updateOkBtnState(medias);
        updatePreviewBtnState(medias);
    }

    private void updatePreviewBtnState(List<BaseMedia> medias) {
//        if (mPreBtn == null || medias == null) {
//            return;
//        }
//        boolean enabled = medias.size() > 0 && medias.size() <= mMaxCount;
//        mPreBtn.setEnabled(enabled);
    }

    private void updateOkBtnState(List<BaseMedia> medias) {
//        if (mOkBtn == null || medias == null) {
//            return;
//        }
//        boolean enabled = medias.size() > 0 && medias.size() <= mMaxCount;
//        mOkBtn.setEnabled(enabled);
//        mOkBtn.setText(enabled ? getString(com.bilibili.boxing_impl.R.string.boxing_image_select_ok_fmt, String.valueOf(medias.size())
//                , String.valueOf(mMaxCount)) : ("확인"));
    }

    @Override
    public void onCameraFinish(BaseMedia media) {
        dismissProgressDialog();
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
        dismissProgressDialog();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
//        if (id == R.id.choose_ok_btn) {
//            onFinish(mMediaAdapter.getSelectedMedias());
//        } else if (id == R.id.choose_preview_btn) {
//            if (!mIsPreview) {
//                mIsPreview = true;
//                ArrayList<BaseMedia> medias = (ArrayList<BaseMedia>) mMediaAdapter.getSelectedMedias();
//                Boxing.get().withIntent(getActivity(), BoxingViewActivity.class, medias)
//                        .start(this, MyBoxingViewFragment.IMAGE_PREVIEW_REQUEST_CODE, BoxingConfig.ViewMode.PRE_EDIT);
//
//            }
//            onFinish(mMediaAdapter.getSelectedMedias());
//        }

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
        showProgressDialog();
        super.onCameraActivityResult(requestCode, resultCode);
    }

    private void showProgressDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(getActivity());
            mDialog.setIndeterminate(true);
            mDialog.setMessage(getString(com.bilibili.boxing_impl.R.string.boxing_handling));
        }
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.hide();
            mDialog.dismiss();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ArrayList<BaseMedia> medias = (ArrayList<BaseMedia>) getMediaAdapter().getSelectedMedias();
        onSaveMedias(outState, medias);
    }

    public void setTitleTxt(TextView titleTxt) {
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
                RecyclerView recyclerView = (RecyclerView) view.findViewById(com.bilibili.boxing_impl.R.id.album_recycleview);
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
                recyclerView.addItemDecoration(new SpacesItemDecoration(2, 1));

                View albumShadowLayout = view.findViewById(com.bilibili.boxing_impl.R.id.album_shadow);
                albumShadowLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissAlbumWindow();
                    }
                });
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
            ClickImage(view, iMedia);
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
            ClickImage(view, iMedia);
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

    public void ClickImage(View view, BaseMedia iMedia) {
        if (!mIsPreview) {
            Log.e("[multiImageClick]", "image click");
            if (!(iMedia instanceof ImageMedia)) {
                return;
            }
            ImageMedia photoMedia = (ImageMedia) iMedia;
            boolean preSelected = photoMedia.isSelected();
            MyMediaItemLayout layout = (MyMediaItemLayout) view;

            List<BaseMedia> selectedMedias = mMediaAdapter.getSelectedMedias();
            HashMap<String, SeletedMediaInfo> seletedMediaInfoHashMap = mMediaAdapter.getSeletedMediaInfoHashMap();
            Log.e("===============", photoMedia.getId() + "==============");
            if (preSelected) {
                SeletedMediaInfo seletedMediaInfo = seletedMediaInfoHashMap.get(photoMedia.getId());
                if (seletedMediaInfo.getCur()) {
                    Log.e("seletedMediaInfo", "getCur() is true");
                    int removePos = seletedMediaInfo.clear();
                    mMediaAdapter.getPq().offer(removePos);
                    seletedMediaInfoHashMap.remove(photoMedia.getId());
                    for (HashMap.Entry<String, SeletedMediaInfo> seletedMediaInfoEntry : seletedMediaInfoHashMap.entrySet()) {
                        if (seletedMediaInfoEntry.getValue().getPos() > removePos) {
                            seletedMediaInfoEntry.getValue().setPos(seletedMediaInfoEntry.getValue().getPos() - 1);
                        }
                        if (seletedMediaInfoEntry.getValue().getPos() == selectedMedias.size() - 2) {
                            seletedMediaInfoEntry.getValue().setCur(true);
                        }
                    }
                    photoMedia.setSelected(false);
                    selectedMedias.remove(photoMedia);
                } else {
                    Log.e("seletedMediaInfo", "getCur() is false");
                    for (HashMap.Entry<String, SeletedMediaInfo> seletedMediaInfoEntry : seletedMediaInfoHashMap.entrySet()) {
                        if (seletedMediaInfoEntry.getValue().getCur()) {
                            seletedMediaInfoEntry.getValue().setCur(false);
                            Log.e("seletedMediaInfoEntry", seletedMediaInfoEntry.getKey() + "is set False");
                            break;
                        }
                    }
                    seletedMediaInfo.setCur(true);
                    Log.e("seletedMediaInfoEntry", photoMedia.getId() + "is set true");

                }

            } else {
                // 일단 필요없는부분
                if (selectedMedias.size() >= mMaxCount) {
                    Toast.makeText(getActivity(), "최대 5장만 선택 가능합니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!selectedMedias.contains(photoMedia)) {
                    if (photoMedia.isGifOverSize()) {
                        Toast.makeText(getActivity(), "이미지의 크기가 너무 큽니다", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Log.e("selectedMedias", "new insert");
                        // 새롭게 들어가는 곳
                        int lPos = selectedMedias.size() - 1;
                        int nPos = selectedMedias.size();

                        if (lPos >= 0) {
                            String lId = selectedMedias.get(lPos).getId();
                            SeletedMediaInfo lSeletedMediaInfo = seletedMediaInfoHashMap.get(lId);
                            lSeletedMediaInfo.setCur(false);
                        }

                        selectedMedias.add(photoMedia);
                        int cropViewPos = mMediaAdapter.getPq().poll();
                        seletedMediaInfoHashMap.put(photoMedia.getId(), new SeletedMediaInfo(nPos, true, layout, photoMedia, imageCropViews.get(cropViewPos)));
                        return;
                    }
                }
            }
        }
    }

}
