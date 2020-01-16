package com.jucanos.photomap.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jucanos.photomap.ListView.StoryListViewAdapter;
import com.jucanos.photomap.ListView.StoryListViewItem;
import com.jucanos.photomap.R;

import java.util.ArrayList;

public class StoryActivity extends AppCompatActivity {
    private ListView listView_story;
    private StoryListViewAdapter listView_storyApater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        Toolbar toolbar = findViewById(R.id.toolbar_tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Group Name");

        listView_storyApater = new StoryListViewAdapter();
        listView_story = findViewById(R.id.listView_story);
        listView_story.setAdapter(listView_storyApater);
        addStoryTest();
        addStoryTest();
        addStoryTest();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_story, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // 오른쪽 상단 메뉴 버튼
            case R.id.item_add:
                redirectAddStoryActivity();
                return true;
            // 뒤로가기 버튼
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.anim_not_move, R.anim.anim_slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void redirectAddStoryActivity() {
        Intent intent = new Intent(this, AddStoryImageActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_not_move);
    }

    // ====================================================================== for test Code
    // ====================================================================== for test Code
    void addStoryTest() {
        String thumbnail_realPath = "drawable://" + R.drawable.test_image_vertical;
        ArrayList<String> image_realPahts = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            String image_realpath = "drawable://" + R.drawable.test_image_vertical;
            image_realPahts.add(image_realpath);
        }
        String time_upload = "0000/00/00 00:00";
        String time_edit = "0000/00/00 00:00";
        String description = "난 귀엽다.난 귀엽다.난 귀엽다.난 귀엽다.난 귀엽다.난 귀엽다.난 귀엽다.난 귀엽다.난 귀엽다.난 귀엽다.난 귀엽다.난 귀엽다.난 귀엽다.";
        String title = "백만스물둘청원";

        StoryListViewItem storyListViewItem = new StoryListViewItem();

        storyListViewItem.setThumnail_realPath(thumbnail_realPath);
        storyListViewItem.setImage_realPahts(image_realPahts);
        storyListViewItem.setTime_upload(time_upload);
        storyListViewItem.setTime_edit(time_edit);
        storyListViewItem.setDescription(description);
        storyListViewItem.setTitle(title);

        listView_storyApater.addItem(storyListViewItem);
        listView_storyApater.notifyDataSetChanged();
    }
}
