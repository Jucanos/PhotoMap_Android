package com.jucanos.photomap.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.R;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.jucanos.photomap.SliderViewAdapter.SliderAdapterExample;
import com.jucanos.photomap.Structure.CreateStory;
import com.jucanos.photomap.photoPicker.ViewUtils;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

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

    private SliderView sliderView;
    private TextView textView_upload;
    private EditText editText_title, editText_context;

    private ArrayList<String> paths = new ArrayList<>();
    private SliderAdapterExample mSlideradapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story_peed);

        getIntentData();
        initView();
        setLayoutSize();
        loadImages();
    }

    public void getIntentData(){
        globalApplication = GlobalApplication.getGlobalApplicationContext();
        paths = getIntent().getStringArrayListExtra("paths");
        mid = getIntent().getStringExtra("mid");
        cityKey = getIntent().getStringExtra("cityKey");
        Log.e("AddStoryPeedActivity","[mid] : " + mid);
        Log.e("AddStoryPeedActivity","[cityKey] : " + cityKey);
    }

    public void initView(){
        sliderView = findViewById(R.id.imageSlider);
        textView_upload = findViewById(R.id.textView_upload);
        editText_title= findViewById(R.id.editText_title);
        editText_context = findViewById(R.id.editText_context);

        textView_upload.setOnClickListener(this);
    }

    private void setLayoutSize() {
        sliderView.getLayoutParams().height = ViewUtils.getScreenWidth();
        sliderView.getLayoutParams().width = ViewUtils.getScreenWidth();
    }

    public void loadImages(){
        mSlideradapter = new SliderAdapterExample(this, paths);
        sliderView.setSliderAdapter(mSlideradapter);
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.textView_upload){
            String title = editText_title.getText().toString();
            String context = editText_context.getText().toString();
            requestUploadImage(title,context,paths);
            Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
        }
    }

    public void requestUploadImage(String title, String context, ArrayList<String> paths) {
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

        final Call<CreateStory> res = NetworkHelper.getInstance().getService().createStory("Bearer " + globalApplication.token, mid, hashMap, files);
        res.enqueue(new Callback<CreateStory>() {
            @Override
            public void onResponse(Call<CreateStory> call, Response<CreateStory> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e("AddStoryActivity", "[mid] : " + response.body().getCreateStoryData().getMid());
                        Log.e("AddStoryActivity", "[title] : " + response.body().getCreateStoryData().getTitle());
                        Log.e("AddStoryActivity", "[content] : " + response.body().getCreateStoryData().getContext());
                        Log.e("AddStoryActivity", "[files] : " + response.body().getCreateStoryData().getFiles().toString());
                        Log.e("AddStoryActivity", "[sid] : " + response.body().getCreateStoryData().getSid());
                        Intent intent = new Intent();
                        intent.putExtra("title", response.body().getCreateStoryData().getTitle());
                        intent.putExtra("context", response.body().getCreateStoryData().getContext());
                        intent.putStringArrayListExtra("files", response.body().getCreateStoryData().getFiles());
                        intent.putExtra("mid", response.body().getCreateStoryData().getMid());
                        intent.putExtra("sid", response.body().getCreateStoryData().getSid());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } else {
                    Log.e("requestUploadImage", "requestUploadImage error : " + Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<CreateStory> call, Throwable t) {
                Log.e("FragmentDescription", "requestUploadImage fail : " + t.getLocalizedMessage());
            }
        });
    }


}
