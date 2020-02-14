package com.jucanos.photomap.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.jucanos.photomap.R;
import com.jucanos.photomap.SliderViewAdapter.SliderAdapterExample;
import com.jucanos.photomap.photoPicker.ViewUtils;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class AddStoryPeedActivity extends AppCompatActivity {

    private SliderView sliderView;
    private ArrayList<String> paths = new ArrayList<>();
    private SliderAdapterExample mSlideradapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story_peed);

        getIntentData();
        initView();
        setLayoutSize();
        loadImages();
    }

    public void getIntentData(){
        paths = getIntent().getStringArrayListExtra("paths");
    }

    public void initView(){
        sliderView = findViewById(R.id.imageSlider);
    }

    private void setLayoutSize() {
        sliderView.getLayoutParams().height = ViewUtils.getScreenWidth();
        sliderView.getLayoutParams().width = ViewUtils.getScreenWidth();
    }

    public void loadImages(){
        mSlideradapter = new SliderAdapterExample(this, paths);
        sliderView.setSliderAdapter(mSlideradapter);
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
    }
}
