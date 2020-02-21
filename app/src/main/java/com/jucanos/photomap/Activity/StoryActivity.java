package com.jucanos.photomap.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.ListView.StoryListViewAdapter;
import com.jucanos.photomap.ListView.StoryListViewItem;
import com.jucanos.photomap.R;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.jucanos.photomap.Structure.GetStoryList;
import com.jucanos.photomap.Structure.GetStoryListData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import mehdi.sakout.dynamicbox.DynamicBox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryActivity extends AppCompatActivity {
    public GlobalApplication globalApplication;

    private RelativeLayout rl_existStory, rl_noStory;
    private ListView listView_story;
    private StoryListViewAdapter listView_storyApater;
    private int ADD_STORY_REQUEST = 1;
    private int EDIT_STORY_REQUEST = 2;

    private String mid;
    private String cityKey;

    // for loading
    private DynamicBox box;
    private String LOADING_ONLY_PROGRESS = "loading_only_progress";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        getIntentData();
        setToolbar();
        initMember();
        setBox();
        loadStoryList();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(cityKey);
    }


    private void getIntentData() {
        globalApplication = GlobalApplication.getGlobalApplicationContext();
        mid = getIntent().getStringExtra("mid");
        cityKey = getIntent().getStringExtra("cityKey");
    }

    private void initMember(){
        listView_storyApater = new StoryListViewAdapter();
        listView_story = findViewById(R.id.listView_story);
        listView_story.setAdapter(listView_storyApater);
        rl_existStory = findViewById(R.id.rl_existStory);
        rl_noStory = findViewById(R.id.rl_noStory);
    }

    private void setBox(){
        box = new DynamicBox(this, rl_existStory);
        View customView = getLayoutInflater().inflate(R.layout.loading_only_progress, null, false);
        box.addCustomView(customView, LOADING_ONLY_PROGRESS);
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
            case R.id.item_add:
                redirectAddStoryActivity(mid);
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void redirectAddStoryActivity(String mid) {
        Intent intent = new Intent(this, AddStoryImageActivity.class);
        intent.putExtra("mid", mid);
        intent.putExtra("cityKey", cityKey);

        startActivityForResult(intent, ADD_STORY_REQUEST);
    }

    public void redirectEditStoryActivity(String sid, String title, String context, int pos) {
//        Intent intent = new Intent(this, EditStoryActivity.class);
//        intent.putExtra("sid", sid);
//        intent.putExtra("title", title);
//        intent.putExtra("context", context);
//        intent.putExtra("pos", pos);
//        startActivityForResult(intent, EDIT_STORY_REQUEST);
//        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_not_move);
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
                Date createdAt = new Date(System.currentTimeMillis());
                Date updatedAt = new Date(System.currentTimeMillis());
                storyListViewItem.setThumbnail(globalApplication.authorization.getUserData().getThumbnail());
                storyListViewItem.setCreator(globalApplication.authorization.getUserData().getUid());
                storyListViewItem.setCreatedAt(createdAt);
                storyListViewItem.setUpdatedAt(updatedAt);
                storyListViewItem.setTitle(title);
                storyListViewItem.setContext(context);
                storyListViewItem.setFiles(files);
                storyListViewItem.setSid(sid);
                storyListViewItem.setMid(mid);
                addStoryTest(storyListViewItem, false);
                listView_storyApater.notifyDataSetChanged();
                setLayout();
            } else if (requestCode == EDIT_STORY_REQUEST) {
                int pos = data.getIntExtra("pos", -1);
                String title = data.getStringExtra("title");
                String context = data.getStringExtra("context");
                listView_storyApater.getItem(pos).setContext(context);
                listView_storyApater.getItem(pos).setTitle(title);
                listView_storyApater.notifyDataSetChanged();
            }
        }
    }

    void loadStoryList() {
        box.showCustomView(LOADING_ONLY_PROGRESS);
        final Call<GetStoryList> res = NetworkHelper.getInstance().getService().getStoryList(globalApplication.token, mid, cityKey);
        res.enqueue(new Callback<GetStoryList>() {
            @Override
            public void onResponse(Call<GetStoryList> call, Response<GetStoryList> response) {
                if (response.isSuccessful()) {
                    Log.e("StoryActivity", "response.isSuccessful()");
                    if (response.body() != null) {
                        Collections.sort(response.body().getGetStoryListItems(), new Comparator<GetStoryListData>() {
                            @Override
                            public int compare(GetStoryListData o1, GetStoryListData o2) {
                                return o1.getCreatedAt().compareTo(o2.getCreatedAt());
                            }
                        });
                        for (int i = 0; i < response.body().getGetStoryListItems().size(); i++) {
//                            Log.e("StoryActivity", "[createdAt] : " + response.body().getGetStoryListItems().get(i).getCreatedAt());
//                            Log.e("StoryActivity", "[updatedAt] : " + response.body().getGetStoryListItems().get(i).getUpdatedAt());
//                            Log.e("StoryActivity", "[title] : " + response.body().getGetStoryListItems().get(i).getTitle());
//                            Log.e("StoryActivity", "[context] : " + response.body().getGetStoryListItems().get(i).getContext());
//                            if (response.body().getGetStoryListItems().get(i).getFiles() == null) {
//                                Log.e("StoryActivity", "[file] : is null");
//                            } else {
//                                Log.e("StoryActivity", "[files] : " + response.body().getGetStoryListItems().get(i).getFiles().toString());
//                            }
//                            Log.e("StoryActivity", "[sid] : " + response.body().getGetStoryListItems().get(i).getSid());
//                            Log.e("StoryActivity", "[mid] : " + response.body().getGetStoryListItems().get(i).getMid());
//                            Log.e("StoryActivity", "[creator] : " + response.body().getGetStoryListItems().get(i).getCreator());
                            Date createdAt = response.body().getGetStoryListItems().get(i).getCreatedAt();
                            Date updatedAt = response.body().getGetStoryListItems().get(i).getUpdatedAt();
                            String title = response.body().getGetStoryListItems().get(i).getTitle();
                            String context = response.body().getGetStoryListItems().get(i).getContext();
                            ArrayList<String> files = response.body().getGetStoryListItems().get(i).getFiles();
                            String sid = response.body().getGetStoryListItems().get(i).getSid();
                            String mid = response.body().getGetStoryListItems().get(i).getMid();
                            String creator = response.body().getGetStoryListItems().get(i).getCreator();
                            StoryListViewItem storyListViewItem = new StoryListViewItem();
                            storyListViewItem.setCreatedAt(createdAt);
                            storyListViewItem.setUpdatedAt(updatedAt);
                            storyListViewItem.setTitle(title);
                            storyListViewItem.setContext(context);
                            storyListViewItem.setFiles(files);
                            storyListViewItem.setSid(sid);
                            storyListViewItem.setMid(mid);
                            storyListViewItem.setCreator(creator);
                            addStoryTest(storyListViewItem, false);
                        }
                        listView_storyApater.notifyDataSetChanged();
                        setLayout();
                    }
                } else {
                    Log.e("StoryActivity", "response.isNotSuccessful() : " + response.code());
                    box.showExceptionLayout();
                }
            }

            @Override
            public void onFailure(Call<GetStoryList> call, Throwable t) {
                Log.e("StoryActivity", "onFailure : " + t.getLocalizedMessage());
            }
        });
    }

    void addStoryTest(StoryListViewItem storyListViewItem, boolean pushBack) {
        listView_storyApater.addItem(storyListViewItem, pushBack);
    }

    private void setLayout(){
        if(listView_storyApater.getCount() > 0){
            rl_existStory.setVisibility(View.VISIBLE);
            rl_noStory.setVisibility(View.GONE);
        }else{
            rl_existStory.setVisibility(View.GONE);
            rl_noStory.setVisibility(View.VISIBLE);
        }
        box.hideAll();
    }
}
