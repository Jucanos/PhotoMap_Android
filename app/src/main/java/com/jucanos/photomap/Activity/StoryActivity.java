package com.jucanos.photomap.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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
    private int ADD_STORY_REQUEST = 1;

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
        Intent intent = new Intent(this, AddStoryActivity.class);
        startActivityForResult(intent, ADD_STORY_REQUEST);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_not_move);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_STORY_REQUEST) {
            if (resultCode == RESULT_OK) {
                String title = data.getStringExtra("title");
                String description = data.getStringExtra("description");
                ArrayList<String> realPaths = (ArrayList<String>) data.getSerializableExtra("realPaths");
                Log.e("StoryActivity", "title : " + title);
                Log.e("StoryActivity", "description : " + description);
                for (int i = 0; i < realPaths.size(); i++) {
                    Log.e("StoryActivity", "realPaths[" + Integer.toString(i) + "] : " + realPaths.get(i));
                }
                addStoryTest(realPaths, title, description);

            }
        }
    }

    // ====================================================================== for test Code
    // ====================================================================== for test Code
    void addStoryTest(ArrayList<String> image_realPahts, String title, String description) {
        String thumbnail_realPath = "drawable://" + R.drawable.test_image_vertical;
        String time_upload = "0000/00/00 00:00";
        String time_edit = "0000/00/00 00:00";
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
