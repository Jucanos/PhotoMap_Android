package com.jucanos.photomap.SliderViewAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jucanos.photomap.R;
import com.jucanos.photomap.util.BitmapUtils;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.ArrayList;

public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

    private Context context;
    private ArrayList<String> paths = new ArrayList<>();
    private ArrayList<Bitmap> aBitmaps = new ArrayList<>();
    private ArrayList<Bitmap> bBitmaps = new ArrayList<>();

    public SliderAdapterExample(Context context, ArrayList<String> paths) {
        this.context = context;
        this.paths = paths;
        for (int i = 0; i < paths.size(); i++) {
            aBitmaps.add(BitmapUtils.getBitmapByPathMutable(paths.get(i)));
            bBitmaps.add(BitmapUtils.getBitmapByPathMutable(paths.get(i)));
        }
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        viewHolder.imageViewBackground.setImageBitmap(bBitmaps.get(position));
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            this.itemView = itemView;
        }
    }

    public void setFilter(int x, ThumbnailItem thumbnailItem) {
        bBitmaps.set(x, BitmapUtils.getBitmapByPathMutable(paths.get(x)));
        thumbnailItem.filter.processFilter(bBitmaps.get(x));
    }
}