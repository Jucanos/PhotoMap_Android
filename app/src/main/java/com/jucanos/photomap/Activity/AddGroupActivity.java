package com.jucanos.photomap.Activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.R;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.jucanos.photomap.Structure.CreateMap;
import com.jucanos.photomap.Structure.CreateMapRequest;

import mehdi.sakout.dynamicbox.DynamicBox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddGroupActivity extends AppCompatActivity {
    public GlobalApplication globalApplication;
    private EditText et_title;
    private RelativeLayout rl_container, rl_total;

    private DynamicBox mBox;
    private InputMethodManager mKeyBord;

    private String LOADING_PROGRESS = "loading_progress";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        initMember();
        setToolbar();
        setBox();

        showView(rl_container);
        mKeyBord.showSoftInput(et_title, 0);
    }

    private void initMember() {
        globalApplication = GlobalApplication.getGlobalApplicationContext();
        mKeyBord = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        et_title = findViewById(R.id.editText_title);
        rl_total = findViewById(R.id.rl_total);
        rl_container = findViewById(R.id.rl_container);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("그룹 생성");

    }

    private void setBox() {
        mBox = new DynamicBox(this, rl_total);
        View customView = getLayoutInflater().inflate(R.layout.loading_progress, null, false);
        mBox.addCustomView(customView, LOADING_PROGRESS);
        mBox.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKeyBord.showSoftInput(et_title, 0);
                mBox.hideAll();
            }
        });
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
                int size = et_title.getEditableText().toString().length();
                if (size <= 0) {
                    Toast.makeText(this, "그룹 이름을 입력해주세요!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                requestCreateMap(globalApplication.token, et_title.getText().toString());
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void requestCreateMap(String token, final String name) {
        mKeyBord.hideSoftInputFromWindow(et_title.getWindowToken(), 0);
        mBox.showCustomView(LOADING_PROGRESS);
        Log.e("AddGroupActivity", "[token] : " + token);
        Log.e("AddGroupActivity", "[name] : " + name);
        final Call<CreateMap> res = NetworkHelper.getInstance().getService().createMap(token, new CreateMapRequest(name));
        res.enqueue(new Callback<CreateMap>() {
            @Override
            public void onResponse(Call<CreateMap> call, Response<CreateMap> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String mapToken = response.body().getCreateMapData().getMid();
                        Log.e("AddGroupActivity", "response.isSuccessful()");
                        Intent intent = new Intent();
                        intent.putExtra("mapToken", mapToken);
                        intent.putExtra("mapName", name);
                        setResult(RESULT_OK, intent);
                        mBox.hideAll();
                        finish();
                    }
                } else {
                    mBox.showExceptionLayout();
                    Log.e("AddGroupActivity", "response.isNotSuccessful() : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CreateMap> call, Throwable t) {
                mBox.showExceptionLayout();
                Log.e("AddGroupActivity", "onFailure : " + t.getLocalizedMessage());
            }
        });
    }

    private void showView(final View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_slide_in_top);
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_not_move, R.anim.anim_not_move);
    }
}
