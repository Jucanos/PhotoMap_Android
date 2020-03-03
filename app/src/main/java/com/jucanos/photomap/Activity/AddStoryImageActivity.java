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
import com.jucanos.photomap.Fragment.MyBoxingViewFragment;
import com.jucanos.photomap.R;

import java.util.ArrayList;
import java.util.List;


public class AddStoryImageActivity extends AbsBoxingActivity {
    private com.jucanos.photomap.Fragment.MyBoxingViewFragment MyBoxingViewFragment;


    // intent request code
    private final int ADD_STORY_REQUEST = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story_image);

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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_in_left ,R.anim.anim_slide_out_right);
    }
}
