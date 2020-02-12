package com.jucanos.photomap.photoPicker;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.model.BoxingManager;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing.model.entity.impl.ImageMedia;
import com.bilibili.boxing.model.entity.impl.VideoMedia;
import com.bilibili.boxing_impl.BoxingResHelper;
import com.bilibili.boxing_impl.WindowManagerHelper;
import com.jucanos.photomap.R;

public class MyMediaItemLayout extends FrameLayout {
    private static final int BIG_IMG_SIZE = 5 * 1024 * 1024;

    private ImageView mCheckImg;
    private View mVideoLayout;
    private View mFontLayout;
    private ImageView mCoverImg;
    private MyMediaItemLayout.ScreenType mScreenType;

    // my member
    private TextView mCountTxt;

    private enum ScreenType {
        SMALL(100), NORMAL(180), LARGE(320);
        int value;

        ScreenType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    public MyMediaItemLayout(Context context) {
        this(context, null, 0);
    }

    public MyMediaItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyMediaItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_boxing_media_item, this, true);
        mCoverImg = (ImageView) view.findViewById(R.id.media_item);
        mCheckImg = (ImageView) view.findViewById(R.id.media_item_check);
        mCountTxt = (TextView)view.findViewById(R.id.media_item_count);

        mVideoLayout = view.findViewById(R.id.video_layout);
        mFontLayout = view.findViewById(R.id.media_font_layout);


        mScreenType = getScreenType(context);
        // my clode
        setImageRect(context);
    }
    private void setImageRect(Context context) {
        int screenHeight = WindowManagerHelper.getScreenHeight(context);
        int screenWidth = WindowManagerHelper.getScreenWidth(context);
        int width = 100;
        if (screenHeight != 0 && screenWidth != 0) {
            width = (screenWidth - getResources().getDimensionPixelOffset(com.bilibili.boxing_impl.R.dimen.boxing_media_margin) * 4) / 3;
        }
        mCoverImg.getLayoutParams().width = width;
        mCoverImg.getLayoutParams().height = width;
        mFontLayout.getLayoutParams().width = width;
        mFontLayout.getLayoutParams().height = width;
    }

    private MyMediaItemLayout.ScreenType getScreenType(Context context) {
        int type = context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        MyMediaItemLayout.ScreenType result;
        switch (type) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                result = MyMediaItemLayout.ScreenType.SMALL;
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                result = MyMediaItemLayout.ScreenType.NORMAL;
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                result = MyMediaItemLayout.ScreenType.LARGE;
                break;
            default:
                result = MyMediaItemLayout.ScreenType.NORMAL;
                break;
        }
        return result;
    }

    public void setImageRes(@DrawableRes int imageRes) {
        if (mCoverImg != null) {
            mCoverImg.setImageResource(imageRes);
        }
    }

    public void setMedia(BaseMedia media) {
        if (media instanceof ImageMedia) {
            mVideoLayout.setVisibility(GONE);
            setCover(((ImageMedia) media).getThumbnailPath());
        } else if (media instanceof VideoMedia) {
            mVideoLayout.setVisibility(VISIBLE);
            VideoMedia videoMedia = (VideoMedia) media;
            TextView durationTxt = ((TextView) mVideoLayout.findViewById(com.bilibili.boxing_impl.R.id.video_duration_txt));
            durationTxt.setText(videoMedia.getDuration());
            durationTxt.setCompoundDrawablesWithIntrinsicBounds(BoxingManager.getInstance().getBoxingConfig().getVideoDurationRes(), 0, 0, 0);
            ((TextView) mVideoLayout.findViewById(com.bilibili.boxing_impl.R.id.video_size_txt)).setText(videoMedia.getSizeByUnit());
            setCover(videoMedia.getPath());
        }
    }

    private void setCover(@NonNull String path) {
        if (mCoverImg == null || TextUtils.isEmpty(path)) {
            return;
        }
        mCoverImg.setTag(com.bilibili.boxing_impl.R.string.boxing_app_name, path);
        BoxingMediaLoader.getInstance().displayThumbnail(mCoverImg, path, mScreenType.getValue(), mScreenType.getValue());
    }

    @SuppressWarnings("deprecation")
    public void setChecked(boolean isChecked, int count) {
        if (isChecked) {
            mFontLayout.setVisibility(View.VISIBLE);
            setCount(isChecked,count);
        } else {
            mFontLayout.setVisibility(View.GONE);
            setCount(isChecked,count);
            //mCheckImg.setImageDrawable(getResources().getDrawable(BoxingResHelper.getMediaUncheckedRes()));
        }
    }

    public void setCount(boolean isChecked, int count){
        if(isChecked){
            mCountTxt.setText(Integer.toString(count));
            mCheckImg.setVisibility(GONE);
            mCountTxt.setVisibility(VISIBLE);
        }else{
            mCheckImg.setVisibility(VISIBLE);
            mCountTxt.setVisibility(GONE);
        }
    }

    public void setCurrent(boolean current){
        if(current)
            mFontLayout.setVisibility(View.VISIBLE);
        else
            mFontLayout.setVisibility(View.GONE);

    }
}
