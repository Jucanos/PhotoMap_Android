package com.jucanos.photomap.SliderViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.R;

import java.util.ArrayList;

public class AddStoryImageSliderAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<String> imageList;

    public AddStoryImageSliderAdapter(Context context, ArrayList<String> imageList)
    {
        this.mContext = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.image_slider_layout_item, null);

        final ImageView imageView = view.findViewById(R.id.iv_auto_image_slider);
        Glide.with(mContext)
                .load(imageList.get(position))
                .into(imageView);

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (View)o);
    }
}