package com.jucanos.photomap.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.R;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.jucanos.photomap.SliderViewAdapter.AddStoryImageSliderAdapter;
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

    private RelativeLayout rv_total, rl_loading;
    //private  // ,textView_indicator;
    private EditText editText_title, editText_context;

    private ArrayList<String> paths = new ArrayList<>();

    private AddStoryImageSliderAdapter addStoryImageSliderAdapter;
    //  private ViewPager viewPager;


    private CardView box;
    private String LOADING_PROGRESS = "loading_progress";
    private InputMethodManager mKeyBord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story_peed);

        getIntentData();
        initMember();
        setToolbar();
//        setLayoutSize();
//        loadImages();
        setBox();

        mKeyBord.showSoftInput(editText_title, 0);
    }

    public void getIntentData() {
        globalApplication = GlobalApplication.getGlobalApplicationContext();
        paths = getIntent().getStringArrayListExtra("paths");
        mid = getIntent().getStringExtra("mid");
        cityKey = getIntent().getStringExtra("cityKey");
        Log.e("AddStoryPeedActivity", "[mid] : " + mid);
        Log.e("AddStoryPeedActivity", "[cityKey] : " + cityKey);
    }

    public void initMember() {
//        viewPager = findViewById(R.id.viewPager);
//        textView_indicator= findViewById(R.id.tv_indicator);
        // textView_upload = findViewById(R.id.textView_upload);
        editText_title = findViewById(R.id.editText_title);
        editText_context = findViewById(R.id.editText_context);
        rv_total = findViewById(R.id.rl_total);
        mKeyBord = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        //textView_upload.setOnClickListener(this);
    }

    public void setToolbar() {
        TextView textView_upload = findViewById(R.id.textView_upload);
        textView_upload.setOnClickListener(this);
        TextView textView_cancel = findViewById(R.id.textView_upload);
        textView_cancel.setOnClickListener(this);
    }

//    private void setLayoutSize() {
//        viewPager.getLayoutParams().height = ViewUtils.getScreenWidth();
//        viewPager.getLayoutParams().width = ViewUtils.getScreenWidth();
//    }

//    public void loadImages() {
//        addStoryImageSliderAdapter = new AddStoryImageSliderAdapter(this, paths);
//        viewPager.setAdapter(addStoryImageSliderAdapter);
//        String indicator =  1 + "/" + paths.size();
//        textView_indicator.setText(indicator);
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                String indicator = (position + 1) + "/" + paths.size();
//                textView_indicator.setText(indicator);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//    }

    @SuppressLint("ClickableViewAccessibility")
    private void setBox() {
        box = findViewById(R.id.box);
        box.setVisibility(View.GONE);
        box.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
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
        mKeyBord.hideSoftInputFromWindow(editText_context.getWindowToken(), 0);
        mKeyBord.hideSoftInputFromWindow(editText_title.getWindowToken(), 0);
        box.setVisibility(View.VISIBLE);
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
//                        Log.e("AddStoryActivity", "[mid] : " + response.body().getCreateStoryData().getMid());
//                        Log.e("AddStoryActivity", "[title] : " + response.body().getCreateStoryData().getTitle());
//                        Log.e("AddStoryActivity", "[content] : " + response.body().getCreateStoryData().getContext());
//                        Log.e("AddStoryActivity", "[files] : " + response.body().getCreateStoryData().getFiles().toString());
//                        Log.e("AddStoryActivity", "[sid] : " + response.body().getCreateStoryData().getSid());
                        Intent intent = new Intent();
                        intent.putExtra("title", response.body().getCreateStoryData().getTitle());
                        intent.putExtra("context", response.body().getCreateStoryData().getContext());
                        intent.putStringArrayListExtra("files", response.body().getCreateStoryData().getFiles());
                        intent.putExtra("mid", response.body().getCreateStoryData().getMid());
                        intent.putExtra("sid", response.body().getCreateStoryData().getSid());
                        setResult(RESULT_OK, intent);
                        box.setVisibility(View.GONE);
                        finish();
                    }
                } else {
                    Log.e("AddStoryPeedActivity", "response.isNotSuccessful()");

                }
            }

            @Override
            public void onFailure(Call<CreateStory> call, Throwable t) {
                Log.e("AddStoryPeedActivity", "onFailure : " + t.getLocalizedMessage());
            }
        });
    }


}
