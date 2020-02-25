package com.jucanos.photomap.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.jucanos.photomap.MyRecyclerViewAdapter;
import com.jucanos.photomap.R;
import com.jucanos.photomap.SliderViewAdapter.AddStoryImagePreviewSliderAdapter;
import com.jucanos.photomap.photoPicker.ViewUtils;
import com.jucanos.photomap.util.BitmapUtils;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddStoryPreviewActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener, View.OnClickListener {
    static {
        System.loadLibrary("NativeImageProcessor");
    }

    private String mid, cityKey;

    private ViewPager viewPager;
    private ArrayList<String> paths = new ArrayList<>();
    private AddStoryImagePreviewSliderAdapter addStoryImagePreviewSliderAdapter;
    private MyRecyclerViewAdapter mFilterAdapter;
    private TextView textView_indicator;

    // intent request code
    private final int ADD_STORY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story_preview);

        getIntentData();
        initMember();
        setToolbar();
        loadImages();
        setLayoutSize();
        loadFilterImages();


    }

    public void getIntentData() {
        paths = getIntent().getStringArrayListExtra("paths");
        mid = getIntent().getStringExtra("mid");
        cityKey = getIntent().getStringExtra("cityKey");
        Log.e("paths size : ", Integer.toString(paths.size()));

    }

    public void initMember() {
        viewPager = findViewById(R.id.viewPager);
        textView_indicator = findViewById(R.id.tv_indicator);
    }

    public void setToolbar() {
        TextView textView_next = findViewById(R.id.textView_next);
        textView_next.setOnClickListener(this);
        TextView textView_cancel = findViewById(R.id.textView_cancel);
        textView_cancel.setOnClickListener(this);

    }

    public void loadImages() {
        addStoryImagePreviewSliderAdapter = new AddStoryImagePreviewSliderAdapter(this, paths);
        viewPager.setAdapter(addStoryImagePreviewSliderAdapter);
        String indicator = 1 + "/" + paths.size();
        textView_indicator.setText(indicator);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String indicator = (position + 1) + "/" + paths.size();
                textView_indicator.setText(indicator);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setLayoutSize() {
        viewPager.getLayoutParams().height = ViewUtils.getScreenWidth();
        viewPager.getLayoutParams().width = ViewUtils.getScreenWidth();
    }

    private void loadFilterImages() {
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
        int viewPos = viewPager.getCurrentItem();
        Log.e("onClick", Integer.toString(viewPos));
        ThumbnailItem thumbnailItem = mFilterAdapter.getItem(position);
        addStoryImagePreviewSliderAdapter.setFilter(viewPos, thumbnailItem);
        addStoryImagePreviewSliderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.textView_next:
                try {
                    redirectRegionActivity();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.textView_cancel:
                finish();
                break;
            default:
                break;
        }
    }

    public void redirectRegionActivity() throws IOException {
        ArrayList<String> paths = new ArrayList<>();
        for (int i = 0; i < addStoryImagePreviewSliderAdapter.getCount(); i++) {
            Bitmap bm = addStoryImagePreviewSliderAdapter.getBitmap(i);
            String fileName = "image_" + System.currentTimeMillis();
            String path = BitmapUtils.saveBitmap(fileName, bm, 100, this);
            paths.add(path);
        }

        Intent intent = new Intent(this, AddStoryPeedActivity.class);
        intent.putStringArrayListExtra("paths", paths);
        intent.putExtra("mid", mid);
        intent.putExtra("cityKey", cityKey);
        startActivityForResult(intent, ADD_STORY_REQUEST);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_not_move);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_STORY_REQUEST) {
                Intent intent = new Intent();

                String title = data.getStringExtra("title");
                String context = data.getStringExtra("context");
                ArrayList<String> files = data.getStringArrayListExtra("files");
                String sid = data.getStringExtra("sid");
                String mid = data.getStringExtra("mid");

                intent.putExtra("title", title);
                intent.putExtra("context", context);
                intent.putStringArrayListExtra("files", files);
                intent.putExtra("sid", sid);
                intent.putExtra("mid", mid);

                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
