package com.jucanos.photomap.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.ListView.StoryListViewAdapter;
import com.jucanos.photomap.ListView.StoryListViewItem;
import com.jucanos.photomap.R;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.jucanos.photomap.Structure.GetStoryList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryActivity extends AppCompatActivity {
    public GlobalApplication globalApplication;

    private ListView listView_story;
    private StoryListViewAdapter listView_storyApater;
    private int ADD_STORY_REQUEST = 1;
    private int EDIT_STORY_REQUEST = 2;

    private String mid;
    private Integer citykey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        globalApplication = GlobalApplication.getGlobalApplicationContext();

        Toolbar toolbar = findViewById(R.id.toolbar_tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Group Name");

        mid = getIntent().getStringExtra("mid");
        citykey = getIntent().getIntExtra("citykey", -1);

        listView_storyApater = new StoryListViewAdapter();
        listView_story = findViewById(R.id.listView_story);
        listView_story.setAdapter(listView_storyApater);

        loadStoryList();
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
                redirectAddStoryActivity(mid);
                return true;
            // 뒤로가기 버튼
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.anim_not_move, R.anim.anim_slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void redirectAddStoryActivity(String mid) {
        Intent intent = new Intent(this, AddStoryActivity.class);
        intent.putExtra("mid", mid);
        intent.putExtra("cityKey", citykey);

        startActivityForResult(intent, ADD_STORY_REQUEST);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_not_move);
    }

    public void redirectEditStoryActivity(String sid, String title, String context, int pos) {
        Intent intent = new Intent(this, EditStoryActivity.class);
        intent.putExtra("sid", sid);
        intent.putExtra("title", title);
        intent.putExtra("context", context);
        intent.putExtra("pos", pos);
        startActivityForResult(intent, EDIT_STORY_REQUEST);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_not_move);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_STORY_REQUEST) {
                StoryListViewItem storyListViewItem = new StoryListViewItem();
                String title = data.getStringExtra("title");
                String context = data.getStringExtra("context");
                ArrayList<String> files = data.getStringArrayListExtra("files");
                String sid = data.getStringExtra("sid");
                String mid = data.getStringExtra("mid");
                String createdAt = Long.toString(System.currentTimeMillis());
                String updatedAt = Long.toString(System.currentTimeMillis());
                storyListViewItem.setCreatedAt(createdAt);
                storyListViewItem.setUpdatedAt(updatedAt);
                storyListViewItem.setTitle(title);
                storyListViewItem.setContext(context);
                storyListViewItem.setFiles(files);
                storyListViewItem.setSid(sid);
                storyListViewItem.setMid(mid);
                addStoryTest(storyListViewItem);
            }else if(requestCode == EDIT_STORY_REQUEST){
                int pos = data.getIntExtra("pos",-1);
                String title = data.getStringExtra("title");
                String context= data.getStringExtra("context");
                listView_storyApater.getItem(pos).setContext(context);
                listView_storyApater.getItem(pos).setTitle(title);
                listView_storyApater.notifyDataSetChanged();

            }
        }
    }

    void loadStoryList() {
        final Call<GetStoryList> res = NetworkHelper.getInstance().getService().getStoryList("Bearer " + globalApplication.token, mid, globalApplication.cityKeyInt.get(citykey));
        res.enqueue(new Callback<GetStoryList>() {
            @Override
            public void onResponse(Call<GetStoryList> call, Response<GetStoryList> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        for (int i = 0; i < response.body().getGetStoryListItems().size(); i++) {
                            Log.e("StoryActivity", "[createdAt] : " + response.body().getGetStoryListItems().get(i).getCreatedAt());
                            Log.e("StoryActivity", "[updatedAt] : " + response.body().getGetStoryListItems().get(i).getUpdatedAt());
                            Log.e("StoryActivity", "[title] : " + response.body().getGetStoryListItems().get(i).getTitle());
                            Log.e("StoryActivity", "[context] : " + response.body().getGetStoryListItems().get(i).getContext());

                            if (response.body().getGetStoryListItems().get(i).getFiles() == null) {
                                Log.e("StoryActivity", "[file] : is null");
                            } else {
                                Log.e("StoryActivity", "[files] : " + response.body().getGetStoryListItems().get(i).getFiles().toString());
                            }
                            Log.e("StoryActivity", "[sid] : " + response.body().getGetStoryListItems().get(i).getSid());
                            Log.e("StoryActivity", "[mid] : " + response.body().getGetStoryListItems().get(i).getMid());
                            String createdAt = response.body().getGetStoryListItems().get(i).getCreatedAt();
                            String updatedAt = response.body().getGetStoryListItems().get(i).getUpdatedAt();
                            String title = response.body().getGetStoryListItems().get(i).getTitle();
                            String context = response.body().getGetStoryListItems().get(i).getContext();
                            ArrayList<String> files = response.body().getGetStoryListItems().get(i).getFiles();
                            String sid = response.body().getGetStoryListItems().get(i).getSid();
                            String mid = response.body().getGetStoryListItems().get(i).getMid();
                            StoryListViewItem storyListViewItem = new StoryListViewItem();
                            storyListViewItem.setCreatedAt(createdAt);
                            storyListViewItem.setUpdatedAt(updatedAt);
                            storyListViewItem.setTitle(title);
                            storyListViewItem.setContext(context);
                            storyListViewItem.setFiles(files);
                            storyListViewItem.setSid(sid);
                            storyListViewItem.setMid(mid);
                            addStoryTest(storyListViewItem);
                        }
                    }
                } else {
                    Log.e("requestCreateMap", Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<GetStoryList> call, Throwable t) {
                Log.e("[onFailure]", t.getLocalizedMessage());
            }
        });
    }


    // ====================================================================== for test Code
    // ====================================================================== for test Code
    void addStoryTest(StoryListViewItem storyListViewItem) {
        String thumbnail_realPath = "drawable://" + R.drawable.test_image_vertical;
        listView_storyApater.addItem(storyListViewItem);
        listView_storyApater.notifyDataSetChanged();
    }
}
