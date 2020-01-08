package com.jucanos.photomap.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jucanos.photomap.R;

public class StoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Group Name");

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

    public void redirectAddStoryActivity(){
        Intent intent = new Intent(this,AddStoryActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_not_move);
    }
}
