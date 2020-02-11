package com.jucanos.photomap.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bilibili.boxing.AbsBoxingActivity;
import com.bilibili.boxing.AbsBoxingViewFragment;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.jucanos.photomap.R;
import com.jucanos.photomap.photoPicker.MyBoxingViewFragment;

import java.util.ArrayList;
import java.util.List;


public class AddStoryImageActivity extends AbsBoxingActivity {
    private com.jucanos.photomap.photoPicker.MyBoxingViewFragment MyBoxingViewFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story_2);

    }

    @NonNull
    @Override
    public AbsBoxingViewFragment onCreateBoxingView(ArrayList<BaseMedia> medias) {
        MyBoxingViewFragment = (MyBoxingViewFragment) getSupportFragmentManager().findFragmentByTag(MyBoxingViewFragment.TAG);
        if (MyBoxingViewFragment == null) {
            BoxingConfig config = new BoxingConfig(BoxingConfig.Mode.MULTI_IMG).withMaxCount(5);
            MyBoxingViewFragment = MyBoxingViewFragment.newInstance();
            MyBoxingViewFragment.setPickerConfig(config);
            getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, MyBoxingViewFragment, MyBoxingViewFragment.TAG).commit();
        }
        return MyBoxingViewFragment;
    }

    @Override
    public void onBoxingFinish(Intent intent, @Nullable List<BaseMedia> medias) {
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
