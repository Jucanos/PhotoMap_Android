package com.jucanos.photomap.SliderViewAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.jucanos.photomap.R;
import com.jucanos.photomap.util.BitmapUtils;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.ArrayList;

public class AddStoryImagePreviewSliderAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<String> paths = new ArrayList<>();
    private ArrayList<Bitmap> aBitmaps = new ArrayList<>();
    private ArrayList<Bitmap> bBitmaps = new ArrayList<>();

    public AddStoryImagePreviewSliderAdapter(Context context,ArrayList<String> paths)
    {
        this.mContext = context;
        this.paths = paths;
        for (int i = 0; i < paths.size(); i++) {
            aBitmaps.add(BitmapUtils.getBitmapByPathMutable(paths.get(i)));
            bBitmaps.add(BitmapUtils.getBitmapByPathMutable(paths.get(i)));
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.image_slider_layout_item, null);

        final ImageView imageView = view.findViewById(R.id.iv_auto_image_slider);

        imageView.setImageBitmap(bBitmaps.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (View)o);
    }

    public void setFilter(int x, ThumbnailItem thumbnailItem) {
        bBitmaps.set(x, BitmapUtils.getBitmapByPathMutable(paths.get(x)));
        thumbnailItem.filter.processFilter(bBitmaps.get(x));
    }

    public Bitmap getBitmap(int x){
        return bBitmaps.get(x);
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}