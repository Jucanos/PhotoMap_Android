package com.jucanos.photomap.Viewpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.fenchtose.nocropper.CropperView;
import com.jucanos.photomap.R;
import com.jucanos.photomap.util.SquareImageView;

import java.util.ArrayList;

public class StoryViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<Bitmap> imageList;

    public StoryViewPagerAdapter(Context context, ArrayList<Bitmap> imageList) {
        this.mContext = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.story_viewpager_item, null);

        final SquareImageView imageView = view.findViewById(R.id.imageView_image);

        imageView.setImageBitmap(imageList.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return imageList.size();
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

    public Bitmap getBitmap(int x) {
        return imageList.get((x));
    }

    public void setBitmap(int x, Bitmap mbitmap) {
        imageList.set(x, mbitmap);
    }





}