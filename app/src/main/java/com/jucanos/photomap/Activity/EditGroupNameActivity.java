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
import com.jucanos.photomap.Structure.EditGroup;
import com.jucanos.photomap.Structure.EditGroupRequest;

import mehdi.sakout.dynamicbox.DynamicBox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditGroupNameActivity extends AppCompatActivity {
    public GlobalApplication globalApplication;
    private EditText et_title;
    private RelativeLayout rl_container, rl_total;
    String title, mid;
    Integer pos;

    private DynamicBox mBox;
    private InputMethodManager mKeyBord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group_name);

        initMember();
        setToolbar();
        setBox();
        getIntentData();

        et_title.setHint(title);
        showView(rl_container);
        mKeyBord.showSoftInput(et_title, 0);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("그룹 이름 편집");
    }

    private void getIntentData() {
        title = getIntent().getStringExtra("name");
        mid = getIntent().getStringExtra("mid");
        pos = getIntent().getIntExtra("pos", -1);
    }

    private void initMember() {
        globalApplication = GlobalApplication.getGlobalApplicationContext();
        mKeyBord = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        et_title = findViewById(R.id.editText_title);
        rl_total = findViewById(R.id.rl_total);
        rl_container = findViewById(R.id.rl_container);
    }


    private void setBox() {
        mBox = new DynamicBox(this, rl_total);
        View customView = getLayoutInflater().inflate(R.layout.loading_progress, null, false);
        mBox.addCustomView(customView, "loading");
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
                editGroupRequest();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void editGroupRequest() {
        final String title = et_title.getText().toString();
        final Call<EditGroup> res = NetworkHelper.getInstance().getService().editGroup(globalApplication.token, mid, new EditGroupRequest(title));
        res.enqueue(new Callback<EditGroup>() {
            @Override
            public void onResponse(Call<EditGroup> call, Response<EditGroup> response) {
                if (response.isSuccessful()) {
                    Log.e("EditGroupNameActivity", "response.isSuccessful()");
                    Intent intent = new Intent();
                    intent.putExtra("title", title);
                    intent.putExtra("pos", pos);
                    setResult(RESULT_OK, intent);
                    mBox.hideAll();
                    finish();
                } else {
                    Log.e("EditGroupNameActivity", "response.isNotSuccessful()");
                }
            }

            @Override
            public void onFailure(Call<EditGroup> call, Throwable t) {
                Log.e("EditGroupNameActivity", t.getLocalizedMessage());
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
