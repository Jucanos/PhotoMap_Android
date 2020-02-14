package com.jucanos.photomap.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jucanos.photomap.MyRecyclerViewAdapter;
import com.jucanos.photomap.R;
import com.jucanos.photomap.SliderViewAdapter.SliderAdapterExample;
import com.jucanos.photomap.photoPicker.ViewUtils;
import com.jucanos.photomap.util.BitmapUtils;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddStoryPreviewActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener, View.OnClickListener {
    static {
        System.loadLibrary("NativeImageProcessor");
    }

    private SliderView sliderView;
    private ArrayList<String> paths = new ArrayList<>();
    private SliderAdapterExample mSlideradapter;
    private MyRecyclerViewAdapter mFilterAdapter;
    private TextView textView_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story_preview);

        getIntentData();
        initView();
        loadImages();
        setLayoutSize();
        loadFilterImages();


    }

    public void getIntentData() {
        paths = getIntent().getStringArrayListExtra("paths");
        Log.e("paths size : ", Integer.toString(paths.size()));

    }

    public void initView() {
        sliderView = findViewById(R.id.imageSlider);
        textView_next = findViewById(R.id.textView_next);
        textView_next.setOnClickListener(this);
    }

    public void loadImages() {
        mSlideradapter = new SliderAdapterExample(this, paths);
        sliderView.setSliderAdapter(mSlideradapter);
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
    }

    private void setLayoutSize() {
        sliderView.getLayoutParams().height = ViewUtils.getScreenWidth();
        sliderView.getLayoutParams().width = ViewUtils.getScreenWidth();
    }

    private void loadFilterImages() {
// data to populate the RecyclerView with

        List<Filter> filters = FilterPack.getFilterPack(AddStoryPreviewActivity.this);

        Bitmap bm = BitmapUtils.getBitmapByPath(paths.get(0));
        ThumbnailItem item = new ThumbnailItem();
        item.image = bm;
        Filter myFilter = new Filter();
        item.filter = myFilter;
        item.filterName = "origin";

        ThumbnailsManager.clearThumbs();
        ThumbnailsManager.addThumb(item);

        for (Filter filter : filters) {
            Log.e("!", filter.getName());
            item = new ThumbnailItem();
            item.image = bm;
            item.filter = filter;
            item.filterName = filter.getName();
            ThumbnailsManager.addThumb(item);
        }
        List<ThumbnailItem> thumbnailItems = ThumbnailsManager.processThumbs(getApplicationContext());


        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview_filter);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(AddStoryPreviewActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        mFilterAdapter = new MyRecyclerViewAdapter(this, thumbnailItems);
        mFilterAdapter.setClickListener(this);
        recyclerView.setAdapter(mFilterAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.e("current Position", Integer.toString(sliderView.getCurrentPagePosition()));
        int viewPos = sliderView.getCurrentPagePosition();
        ThumbnailItem thumbnailItem = mFilterAdapter.getItem(position);
        mSlideradapter.setFilter(viewPos, thumbnailItem);
        mSlideradapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.textView_next) {
            try {
                redirectRegionActivity();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void redirectRegionActivity() throws IOException {
        ArrayList<String> paths = new ArrayList<>();

        for (int i = 0; i < mSlideradapter.getCount(); i++) {
            Bitmap bm = mSlideradapter.getBitmap(i);
            String fileName = "image_" + System.currentTimeMillis();
            String path = BitmapUtils.saveBitmap(fileName, bm, 100, this);
            paths.add(path);
        }

        Intent intent = new Intent(this, AddStoryPeedActivity.class);
        intent.putStringArrayListExtra("paths",paths);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_not_move);
    }
}
