package com.jucanos.photomap.photoPicker;

import android.view.View;

import com.bilibili.boxing.model.entity.impl.ImageMedia;
import com.naver.android.helloyako.imagecrop.view.ImageCropView;

public class SeletedMediaInfo {
    private Integer mPos;
    private boolean mCur;
    private MyMediaItemLayout mLayout;
    private ImageCropView mCropView;
    private ImageMedia mImgMedia;

    SeletedMediaInfo() {
        mPos = 0;
        mCur = false;
        mLayout = null;
        mCropView = null;
    }

    SeletedMediaInfo(Integer pos, boolean cur, MyMediaItemLayout layout, ImageMedia imageMedia, ImageCropView imageCropView) {
        mPos = pos;
        mCur = cur;
        mLayout = layout;
        mImgMedia = imageMedia;
        mCropView = imageCropView;

        mLayout.setCount(true, pos + 1);
        mLayout.setCurrent(true);

        mImgMedia.setSelected(true);

        mCropView.resetDisplay();
        mCropView.setImageFilePath(mImgMedia.getPath());
        mCropView.setVisibility(View.VISIBLE);
    }

    public void setPos(Integer pos) {
        mPos = pos;
        mLayout.setCount(true, pos + 1);
    }

    public Integer getPos() {
        return mPos;
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

    public void setCropView(ImageCropView imageCropView) {
        mCropView = imageCropView;
    }

    public ImageCropView getmCropView() {
        return mCropView;
    }

    public ImageMedia getImgMedia() {
        return mImgMedia;
    }

    public void setImgMedia(ImageMedia imgMedia) {
        this.mImgMedia = imgMedia;
    }

    public int clear() {
        mCropView.setVisibility(View.GONE);
        mImgMedia.setSelected(false);


        mLayout.setCount(false, 0);
        mLayout.setCurrent(false);

        return getPos();
    }

}
