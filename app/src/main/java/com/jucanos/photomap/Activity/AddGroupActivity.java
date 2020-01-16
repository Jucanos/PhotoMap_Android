package com.jucanos.photomap.Activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.R;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.jucanos.photomap.Structure.CreateMap;
import com.jucanos.photomap.Structure.RequestCreateMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddGroupActivity extends AppCompatActivity {
    public GlobalApplication globalApplication;
    private EditText editText_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        globalApplication = GlobalApplication.getGlobalApplicationContext();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("그룹 생성");

        editText_name = findViewById(R.id.editText_name);
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
            // 오른쪽 상단 메뉴 버튼
            case R.id.item_ok:
                requestCreateMap(globalApplication.token, editText_name.getText().toString());
                return true;
            // 뒤로가기 버튼
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void requestCreateMap(String token, final String name) {
        Log.e("token", token);
        Log.e("name", name);
        final Call<CreateMap> res = NetworkHelper.getInstance().getService().createMap("Bearer " + token, new RequestCreateMap(name));
        res.enqueue(new Callback<CreateMap>() {
            @Override
            public void onResponse(Call<CreateMap> call, Response<CreateMap> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String mapToken = response.body().getCreateMapData().getMid();
                        Log.e("requestCreateMap", mapToken);
                        Intent intent = new Intent();
                        intent.putExtra("mapToken", mapToken);
                        intent.putExtra("mapName", name);
                        setResult(1, intent);
                        finish();
                        overridePendingTransition(R.anim.anim_slide_out_top, R.anim.anim_not_move);
                    }
                } else {
                    Log.e("requestCreateMap", Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<CreateMap> call, Throwable t) {
                Log.e("[onFailure]", t.getLocalizedMessage());
            }
        });
    }


}
