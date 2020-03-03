package com.jucanos.photomap.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.R;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.jucanos.photomap.Structure.EditStory;
import com.jucanos.photomap.Structure.EditStoryRequest;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditStoryActivity extends AppCompatActivity {

    // intent data
    private String title, context, sid;
    private int pos;

    // view
    private EditText editText_title, editText_context;

    // keybord
    private InputMethodManager mKeyBord;

    // for loading
    private CardView box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_story);

        getIntentData();
        setToolbar();
        initMember();
        setHint();
    }

    private void getIntentData() {
        title = getIntent().getStringExtra("title");
        context = getIntent().getStringExtra("context");
        sid = getIntent().getStringExtra("sid");
        pos = getIntent().getIntExtra("pos", -1);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_tb);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("스토리 편집");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_edit_story, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // 오른쪽 상단 메뉴 버튼
            case R.id.item_ok:
                String title = editText_title.getText().toString();
                String context = editText_context.getText().toString();
                if (title.length() <= 0) {
                    Toast.makeText(this, "제목을 입력 해주세요!", Toast.LENGTH_SHORT).show();
                    showKeyBord(true, editText_title);
                }

                if (context.length() <= 0) {
                    Toast.makeText(this, "내용을 입력해주세요!", Toast.LENGTH_SHORT).show();
                    showKeyBord(true, editText_context);

                }
                showKeyBord(false, editText_context);
                editStoryRequest(title, context);
                return true;
            // 뒤로가기 버튼
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initMember() {
        editText_title = findViewById(R.id.editText_title);
        editText_context = findViewById(R.id.editText_context);
        mKeyBord = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        box = findViewById(R.id.box);
        box.setOnTouchListener((v, event) -> true);
    }

    private void setHint() {
        editText_title.setHint(title);
        editText_context.setHint(context);
        mKeyBord.showSoftInput(editText_title, InputMethodManager.SHOW_IMPLICIT);
    }

    private void showKeyBord(boolean show, EditText editText) {
        if (show) {
            mKeyBord.showSoftInput(editText, 0);
        } else {
            mKeyBord.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    public void editStoryRequest(final String title, final String context) {
        box.setVisibility(View.VISIBLE);
        final Call<EditStory> res = NetworkHelper.getInstance().getService().editStory(GlobalApplication.getGlobalApplicationContext().token, sid, new EditStoryRequest(title, context));
        res.enqueue(new Callback<EditStory>() {
            @Override
            public void onResponse(Call<EditStory> call, Response<EditStory> response) {
                if (response.isSuccessful()) {
                    // Log.e("StoryActivity", "[removeStoryRequest] is Successful");
                    redirectResult(title, context);
                } else {
                    Log.e("StoryActivity", "removeStoryRequest isNotSuccessful" + response.code());
                    Toast.makeText(EditStoryActivity.this, "요청 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EditStory> call, Throwable t) {
                Log.e("StoryActivity", "removeStoryRequest is onFailure" + t.getLocalizedMessage());
                Toast.makeText(EditStoryActivity.this, "요청 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void redirectResult(String title, String context) {
        Intent intent = new Intent();
        intent.putExtra("title", title);
        intent.putExtra("context", context);
        intent.putExtra("pos", pos);
        setResult(RESULT_OK, intent);
        box.setVisibility(View.GONE);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_in_left ,R.anim.anim_slide_out_right);
    }
}
