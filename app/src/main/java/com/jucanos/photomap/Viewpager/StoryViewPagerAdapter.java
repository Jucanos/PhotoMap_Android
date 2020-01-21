package com.jucanos.photomap.Viewpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.jucanos.photomap.R;
import com.jucanos.photomap.util.SquareImageView;

import java.util.ArrayList;

public class StoryViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<String> realPaths;

    public StoryViewPagerAdapter(Context context, ArrayList<String> imageList) {
        this.mContext = context;
        this.realPaths = imageList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.viewpager_item_story, null);

        final SquareImageView imageView = view.findViewById(R.id.imageView_image);

        Glide.with(mContext).load(realPaths.get(position)).into(imageView);

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return realPaths.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (View) o);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}