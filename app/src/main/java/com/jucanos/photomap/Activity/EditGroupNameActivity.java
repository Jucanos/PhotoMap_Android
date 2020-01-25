package com.jucanos.photomap.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.R;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.jucanos.photomap.Structure.EditGroup;
import com.jucanos.photomap.Structure.EditGroupRequest;
import com.jucanos.photomap.Structure.SetRep;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditGroupNameActivity extends AppCompatActivity {
    private GlobalApplication globalApplication;
    private EditText editText_name;
    private String mid;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group_name);

        globalApplication = GlobalApplication.getGlobalApplicationContext();

        Toolbar toolbar = findViewById(R.id.toolbar_tb);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("그룹 이름 수정");

        String name = getIntent().getStringExtra("name");
        mid = getIntent().getStringExtra("mid");
        pos = getIntent().getIntExtra("pos", -1);

        RelativeLayout relativeLayout_total = findViewById(R.id.relativeLayout_total);
        hideView(relativeLayout_total);

        editText_name = findViewById(R.id.editText_name);
        editText_name.setHint(name);
        editText_name.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_add_group, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_ok:
                editGroupRequest();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void editGroupRequest() {
        final Call<EditGroup> res = NetworkHelper.getInstance().getService().editGroup("Bearer " + globalApplication.token, mid, new EditGroupRequest(editText_name.getText().toString()));
        res.enqueue(new Callback<EditGroup>() {
            @Override
            public void onResponse(Call<EditGroup> call, Response<EditGroup> response) {
                if (response.isSuccessful()) {
                    Log.e("EditGroupNameActivity", " success");
                    redirectResult();
                } else {
                    Log.e("EditGroupNameActivity", "setRepRequest error : " + Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<EditGroup> call, Throwable t) {
                Log.e("EditGroupNameActivity", "setRepRequest fail : " + t.getLocalizedMessage());
            }
        });
    }

    public void redirectResult() {
        Intent intent = new Intent();
        intent.putExtra("pos", pos);
        intent.putExtra("name", editText_name.getText().toString());
        setResult(RESULT_OK, intent);
        Toast.makeText(this, "그룹 이름이 수정되었습니다", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void hideView(final View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_slide_in_top);
        //use this to make it longer:  animation.setDuration(1000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }
}
