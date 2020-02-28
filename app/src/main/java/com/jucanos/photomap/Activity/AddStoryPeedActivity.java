package com.jucanos.photomap.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jucanos.photomap.Dialog.LoadingDialog;
import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.R;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.jucanos.photomap.Structure.CreateStory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddStoryPeedActivity extends AppCompatActivity implements View.OnClickListener {

    private GlobalApplication globalApplication;
    private String cityKey;
    private String mid;

    private EditText editText_title, editText_context;

    private ArrayList<String> paths = new ArrayList<>();

    private InputMethodManager mKeyBord;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story_peed);

        getIntentData();
        initMember();
        setToolbar();
        setBox();
        mKeyBord.showSoftInput(editText_title, 0);
    }

    public void getIntentData() {
        globalApplication = GlobalApplication.getGlobalApplicationContext();
        paths = getIntent().getStringArrayListExtra("paths");
        mid = getIntent().getStringExtra("mid");
        cityKey = getIntent().getStringExtra("cityKey");
        // Log.e("AddStoryPeedActivity", "[mid] : " + mid);
        // Log.e("AddStoryPeedActivity", "[cityKey] : " + cityKey);
    }

    public void initMember() {
        editText_title = findViewById(R.id.editText_title);
        editText_context = findViewById(R.id.editText_context);
        mKeyBord = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    public void setToolbar() {
        TextView textView_upload = findViewById(R.id.textView_upload);
        textView_upload.setOnClickListener(this);
        TextView textView_cancel = findViewById(R.id.textView_cancel);
        textView_cancel.setOnClickListener(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setBox() {
        loadingDialog = new LoadingDialog(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.textView_upload:
                String title = editText_title.getText().toString();
                String context = editText_context.getText().toString();
                if (title.length() <= 0) {
                    Toast.makeText(this, "제목을 입력해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (context.length() <= 0) {
                    Toast.makeText(this, "내용을 입력해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                requestUploadImage(title, context, paths);
                break;
            case R.id.textView_cancel:
                finish();
                break;
            default:
                break;
        }
    }

    public void requestUploadImage(String title, String context, ArrayList<String> paths) {
        loadingDialog.show();
        mKeyBord.hideSoftInputFromWindow(editText_context.getWindowToken(), 0);
        mKeyBord.hideSoftInputFromWindow(editText_title.getWindowToken(), 0);
        RequestBody requetCityKey = RequestBody.create(MediaType.parse("text/plain"), cityKey);
        RequestBody requestTitle = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody requestDescription = RequestBody.create(MediaType.parse("text/plain"), context);

        HashMap<String, RequestBody> hashMap = new HashMap<>();
        hashMap.put("cityKey", requetCityKey);
        hashMap.put("title", requestTitle);
        hashMap.put("context", requestDescription);

        final List<MultipartBody.Part> files = new ArrayList<>();
        for (int i = 0; i < paths.size(); i++) {
            File file = new File(paths.get(i));
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            files.add(MultipartBody.Part.createFormData("img", file.getName(), requestFile));
        }

        final Call<CreateStory> res = NetworkHelper.getInstance().getService().createStory(globalApplication.token, mid, hashMap, files);
        res.enqueue(new Callback<CreateStory>() {
            @Override
            public void onResponse(Call<CreateStory> call, Response<CreateStory> response) {
                if (response.isSuccessful()) {
                    Log.e("AddStoryPeedActivity", "response.isSuccessful()");
                    if (response.body() != null) {
                        Log.i("AddStoryActivity", "[mid] : " + response.body().getCreateStoryData().getMid());
                        Log.i("AddStoryActivity", "[title] : " + response.body().getCreateStoryData().getTitle());
                        Log.i("AddStoryActivity", "[content] : " + response.body().getCreateStoryData().getContext());
                        Log.i("AddStoryActivity", "[files] : " + response.body().getCreateStoryData().getFiles().toString());
                        Log.i("AddStoryActivity", "[sid] : " + response.body().getCreateStoryData().getSid());
                        Intent intent = new Intent();
                        intent.putExtra("title", response.body().getCreateStoryData().getTitle());
                        intent.putExtra("context", response.body().getCreateStoryData().getContext());
                        intent.putStringArrayListExtra("files", response.body().getCreateStoryData().getFiles());
                        intent.putExtra("mid", response.body().getCreateStoryData().getMid());
                        intent.putExtra("sid", response.body().getCreateStoryData().getSid());
                        setResult(RESULT_OK, intent);
                        loadingDialog.dismiss();
                        finish();
                    }
                } else {
                    Log.e("AddStoryPeedActivity", "response.isNotSuccessful()");
                    Toast.makeText(AddStoryPeedActivity.this, "요청 실패", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<CreateStory> call, Throwable t) {
                Log.e("AddStoryPeedActivity", "onFailure : " + t.getLocalizedMessage());
                Toast.makeText(AddStoryPeedActivity.this, "요청 실패", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }


}
