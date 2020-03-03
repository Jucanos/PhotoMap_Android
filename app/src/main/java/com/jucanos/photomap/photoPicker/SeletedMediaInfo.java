package com.jucanos.photomap.photoPicker;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.bilibili.boxing.model.entity.impl.ImageMedia;
import com.jucanos.photomap.CropView.InstaCropperView;

import java.io.File;

public class SeletedMediaInfo {
    private Integer mCount;
    private boolean mCur;
    private MyMediaItemLayout mLayout;
    private InstaCropperView mCropView;
    private ImageMedia mImgMedia;
    private Integer mCropViewPos;
    private Context mContext;
    private int mRecyclerviewPos;
    private int mAlbumPos;

    SeletedMediaInfo() {
        mCount = 0;
        mCur = false;
        mLayout = null;
        mCropView = null;
        mCropViewPos = 0;
    }

    public SeletedMediaInfo(Integer pos, boolean cur, MyMediaItemLayout layout, ImageMedia imageMedia, InstaCropperView imageCropView,int recyclerviewPos, int albumPos ) {
        mCount = pos;
        mCropViewPos = pos;
        mCur = cur;
        mLayout = layout;
        mImgMedia = imageMedia;
        mCropView = imageCropView;

        mLayout.setCount(true, pos + 1);
        mLayout.setCurrent(true);

        mImgMedia.setSelected(true);

        // mCropView.resetDisplay();
        mCropView.setImageUri(Uri.fromFile(new File(mImgMedia.getPath())));
        mCropView.setVisibility(View.VISIBLE);

        this.mRecyclerviewPos = recyclerviewPos;
        this.mAlbumPos = albumPos;
    }

    public void setCount(Integer count) {
        mCount = count;
        mLayout.setCount(true, count + 1);
    }

    public Integer getCount() {
        return mCount;
    }

    public void setCur(boolean cur) {
        mCur = cur;
        mLayout.setCurrent(cur);

        if (cur) mCropView.setVisibility(View.VISIBLE);
        else mCropView.setVisibility(View.GONE);

    }

    public boolean getCur() {
        return mCur;
    }

    public void setLayout(MyMediaItemLayout layout) {
        mLayout = layout;
    }

    public MyMediaItemLayout getLayout() {
        return mLayout;
    }

    public void setCropView(InstaCropperView imageCropView) {
        mCropView = imageCropView;
    }

    public InstaCropperView getmCropView() {
        return mCropView;
    }

    public ImageMedia getImgMedia() {
        return mImgMedia;
    }

    public void setImgMedia(ImageMedia imgMedia) {
        this.mImgMedia = imgMedia;
    }

    public Integer getCropViewPos() {
        return mCropViewPos;
    }

    public void setCropViewPos(Integer CropViewPos) {
        this.mCropViewPos = CropViewPos;
    }

    public int clear() {
        mCropView.setVisibility(View.GONE);
        mImgMedia.setSelected(false);

        mLayout.setCount(false, 0);
        mLayout.setCurrent(false);

        return mCropViewPos;
    }


    public void setAlbumPos(int pos) {
        this.mAlbumPos = pos;
    }

    public int getAlbumPos(){
        return this.mAlbumPos;
    }

    public void setRecyclerviewPos(int pos){
        this.mRecyclerviewPos = pos;
    }

    public int getRecyclerviewPos(){
        return this.mRecyclerviewPos;
    }



}
