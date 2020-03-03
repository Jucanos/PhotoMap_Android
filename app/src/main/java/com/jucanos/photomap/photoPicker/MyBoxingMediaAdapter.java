package com.jucanos.photomap.photoPicker;

/*
 *  Copyright (C) 2017 Bilibili
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bilibili.boxing.model.BoxingManager;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing.model.entity.impl.ImageMedia;
import com.bilibili.boxing_impl.BoxingResHelper;
import com.jucanos.photomap.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;


/**
 * A RecyclerView.Adapter for image or video picker showing.
 *
 * @author ChenSL
 */
public class MyBoxingMediaAdapter extends RecyclerView.Adapter {
    private static final int CAMERA_TYPE = 0;
    private static final int NORMAL_TYPE = 1;

    private int mOffset;
    private boolean mMultiImageMode;

    private List<BaseMedia> mMedias;
    private List<BaseMedia> mSelectedMedias;
    // view Holder 를 수정해야 합니다.
    private LayoutInflater mInflater;
    private BoxingConfig mMediaConfig;
    private View.OnClickListener mOnCameraClickListener;
    private View.OnClickListener mOnMediaClickListener;
    private MyBoxingMediaAdapter.OnCheckListener mOnCheckListener;
    private MyBoxingMediaAdapter.OnMediaCheckedListener mOnCheckedListener;
    private int mDefaultRes;

    // My Member
    // <meidaId,
    private HashMap<String, SeletedMediaInfo> seletedMediaInfoHashMap;
    private PriorityQueue<Integer> pq = new PriorityQueue<Integer>();


    public MyBoxingMediaAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mMedias = new ArrayList<>();
        this.mSelectedMedias = new ArrayList<>(9);
        this.mMediaConfig = BoxingManager.getInstance().getBoxingConfig();
        this.mOffset = mMediaConfig.isNeedCamera() ? 1 : 0;
        this.mMultiImageMode = mMediaConfig.getMode() == BoxingConfig.Mode.MULTI_IMG;
        this.mOnCheckListener = new MyBoxingMediaAdapter.OnCheckListener();
        this.mDefaultRes = mMediaConfig.getMediaPlaceHolderRes();
        this.seletedMediaInfoHashMap = new HashMap<>();
        for (int i = 0; i < 5; i++) pq.offer(i);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mMediaConfig.isNeedCamera()) {
            return CAMERA_TYPE;
        }
        return NORMAL_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (CAMERA_TYPE == viewType) {
            return new MyBoxingMediaAdapter.CameraViewHolder(mInflater.inflate(R.layout.layout_boxing_recycleview_header, parent, false));
        }
        return new MyBoxingMediaAdapter.ImageViewHolder(mInflater.inflate(R.layout.layout_boxing_recycleview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyBoxingMediaAdapter.CameraViewHolder) {
            MyBoxingMediaAdapter.CameraViewHolder viewHolder = (MyBoxingMediaAdapter.CameraViewHolder) holder;
            viewHolder.mCameraLayout.setOnClickListener(mOnCameraClickListener);
            viewHolder.mCameraImg.setImageResource(BoxingResHelper.getCameraRes());
        } else {
            int pos = position - mOffset;
            final BaseMedia media = mMedias.get(pos);
            final MyBoxingMediaAdapter.ImageViewHolder vh = (MyBoxingMediaAdapter.ImageViewHolder) holder;

            vh.mItemLayout.setImageRes(mDefaultRes);
            vh.mItemLayout.setTag(media);

            vh.mItemLayout.setOnClickListener(mOnMediaClickListener);
            vh.mItemLayout.setTag(R.id.media_item_check, pos);
            vh.mItemLayout.setMedia(media);
            vh.mItemChecked.setVisibility(mMultiImageMode ? View.VISIBLE : View.GONE);

            if (mMultiImageMode && media instanceof ImageMedia) {
                final SeletedMediaInfo seletedMediaInfo = seletedMediaInfoHashMap.get(media.getId());
                if (seletedMediaInfo == null) {
                    vh.mItemLayout.setCount(false, 0);
                    vh.mItemLayout.setCurrent(false);
                } else {
                    if (seletedMediaInfo.getCur()) {
                        vh.mItemLayout.setCurrent(true);
                    } else {
                        vh.mItemLayout.setCurrent(false);
                    }
                    vh.mItemLayout.setCount(true, seletedMediaInfo.getCount() + 1);
                    seletedMediaInfo.setLayout(vh.mItemLayout);
                }
                vh.mItemChecked.setTag(R.id.media_layout, vh.mItemLayout);
                vh.mItemChecked.setTag(media);
                vh.mItemChecked.setOnClickListener(mOnCheckListener);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mMedias.size() + mOffset;
    }

    public HashMap<String, SeletedMediaInfo> getSeletedMediaInfoHashMap() {
        return seletedMediaInfoHashMap;
    }

    public void setSeletedMediaInfoHashMap(HashMap<String, SeletedMediaInfo> seletedMediaInfoHashMap) {
        this.seletedMediaInfoHashMap = seletedMediaInfoHashMap;
    }

    public PriorityQueue<Integer> getPq() {
        return pq;
    }

    public void setPq(PriorityQueue<Integer> pq) {
        this.pq = pq;
    }

    public void setOnCameraClickListener(View.OnClickListener onCameraClickListener) {
        mOnCameraClickListener = onCameraClickListener;
    }

    public void setOnCheckedListener(MyBoxingMediaAdapter.OnMediaCheckedListener onCheckedListener) {
        mOnCheckedListener = onCheckedListener;
    }

    public void setOnMediaClickListener(View.OnClickListener onMediaClickListener) {
        mOnMediaClickListener = onMediaClickListener;
    }

    public List<BaseMedia> getSelectedMedias() {
        return mSelectedMedias;
    }

    public void setSelectedMedias(List<BaseMedia> selectedMedias) {
        if (selectedMedias == null) {
            return;
        }
        mSelectedMedias.clear();
        mSelectedMedias.addAll(selectedMedias);
        notifyDataSetChanged();
    }

    public void addAllData(@NonNull List<BaseMedia> data) {
        int oldSize = mMedias.size();
        this.mMedias.addAll(data);
        int size = data.size();
        notifyItemRangeInserted(oldSize, size);
    }

    public void clearData() {
        int size = mMedias.size();
        this.mMedias.clear();
        notifyItemRangeRemoved(0, size);
    }

    public List<BaseMedia> getAllMedias() {
        return mMedias;
    }

    private static class ImageViewHolder extends RecyclerView.ViewHolder {
        MyMediaItemLayout mItemLayout;
        View mItemChecked;

        ImageViewHolder(View itemView) {
            super(itemView);
            mItemLayout = (MyMediaItemLayout) itemView.findViewById(R.id.media_layout);
            mItemChecked = itemView.findViewById(R.id.media_item_check);
        }
    }

    private static class CameraViewHolder extends RecyclerView.ViewHolder {
        View mCameraLayout;
        ImageView mCameraImg;

        CameraViewHolder(final View itemView) {
            super(itemView);
            mCameraLayout = itemView.findViewById(R.id.camera_layout);
            mCameraImg = (ImageView) itemView.findViewById(R.id.camera_img);
        }
    }

    private class OnCheckListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            MyMediaItemLayout itemLayout = (MyMediaItemLayout) v.getTag(R.id.media_layout);
            BaseMedia media = (BaseMedia) v.getTag();
            if (mMediaConfig.getMode() == BoxingConfig.Mode.MULTI_IMG) {
                if (mOnCheckedListener != null) {
                    mOnCheckedListener.onChecked(itemLayout, media);
                }
            }
        }
    }

    public interface OnMediaCheckedListener {
        /**
         * In multi image mode, selecting a {@link BaseMedia} or undo.
         */
        void onChecked(View v, BaseMedia iMedia);
    }

}
