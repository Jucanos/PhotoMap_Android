package com.jucanos.photomap.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.jucanos.photomap.Fragment.AddStoryFragmentDescription;
import com.jucanos.photomap.Fragment.AddStoryFragmentImage;
import com.jucanos.photomap.Fragment.AddStoryFragmentPagerAdapter;
import com.jucanos.photomap.Fragment.AddStoryFragmentTitle;
import com.jucanos.photomap.Fragment.FragmentViewPager;
import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.R;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.jucanos.photomap.Structure.CreateStory;
import com.kroegerama.imgpicker.BottomSheetImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddStoryActivity extends AppCompatActivity {
    public GlobalApplication globalApplication;

    private FragmentViewPager viewPager;
    private AddStoryFragmentPagerAdapter adapter;

    private FragmentManager fragmentManager;
    private AddStoryFragmentImage addStoryFragmentImage;
    private AddStoryFragmentTitle addStoryFragmentTitle;
    private AddStoryFragmentDescription addStoryFragmentDescription;

    private String mid;
    private Integer cityKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);

        globalApplication = GlobalApplication.getGlobalApplicationContext();

        fragmentManager = getSupportFragmentManager();

        addStoryFragmentImage = new AddStoryFragmentImage();
        fragmentManager.beginTransaction().replace(R.id.main_frame, addStoryFragmentImage).commit();


        mid = getIntent().getStringExtra("mid");
        cityKey = getIntent().getIntExtra("cityKey", -1);


        new BottomSheetImagePicker.Builder("ㅠㅜ")
                .multiSelect(3, 6)
                .peekHeight(700)
                .show(getSupportFragmentManager(), null);

    }

    public void setFrag(int pos) {
        switch (pos) {
            case 0:
                if (addStoryFragmentImage == null) {
                    addStoryFragmentImage = new AddStoryFragmentImage();
                    fragmentManager.beginTransaction().add(R.id.main_frame, addStoryFragmentImage).commit();
                }
                if (addStoryFragmentImage != null)
                    fragmentManager.beginTransaction().show(addStoryFragmentImage).commit();
                if (addStoryFragmentTitle != null)
                    fragmentManager.beginTransaction().hide(addStoryFragmentTitle).commit();
                if (addStoryFragmentDescription != null)
                    fragmentManager.beginTransaction().hide(addStoryFragmentDescription).commit();
                break;
            case 1:
                if (addStoryFragmentTitle == null) {
                    addStoryFragmentTitle = new AddStoryFragmentTitle();
                    fragmentManager.beginTransaction().add(R.id.main_frame, addStoryFragmentTitle).commit();
                }
                if (addStoryFragmentImage != null)
                    fragmentManager.beginTransaction().hide(addStoryFragmentImage).commit();
                if (addStoryFragmentTitle != null)
                    fragmentManager.beginTransaction().show(addStoryFragmentTitle).commit();
                if (addStoryFragmentDescription != null)
                    fragmentManager.beginTransaction().hide(addStoryFragmentDescription).commit();
                break;
            case 2:
                if (addStoryFragmentDescription == null) {
                    addStoryFragmentDescription = new AddStoryFragmentDescription();
                    fragmentManager.beginTransaction().add(R.id.main_frame, addStoryFragmentDescription).commit();
                }
                if (addStoryFragmentImage != null)
                    fragmentManager.beginTransaction().hide(addStoryFragmentImage).commit();
                if (addStoryFragmentTitle != null)
                    fragmentManager.beginTransaction().hide(addStoryFragmentTitle).commit();
                if (addStoryFragmentDescription != null)
                    fragmentManager.beginTransaction().show(addStoryFragmentDescription).commit();
                break;
        }
    }

    public void storyUpload() throws IOException {
        ArrayList<Bitmap> bitmaps = addStoryFragmentImage.getBitmaps();
        ArrayList<String> realPaths = new ArrayList<>();
        for (int i = 0; i < bitmaps.size(); i++) {
            realPaths.add(saveBitmap("image_" + Long.toString(System.currentTimeMillis()), bitmaps.get(i)));
        }
        String title = addStoryFragmentTitle.getTitle();
        String description = addStoryFragmentDescription.getDescription();
        Log.e("AddStoryActivity", "title : " + title);
        Log.e("AddStoryActivity", "description : " + description);
        requestUploadImage(title, description, realPaths);
    }

    //create a file to write bitmap data
    public String saveBitmap(String filename, Bitmap bm) throws IOException {
        File f = new File(getApplicationContext().getCacheDir(), filename);
        f.createNewFile();

        //Convert bitmap to byte array
        Bitmap bitmap = bm;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f.getAbsolutePath();
    }


    public void requestUploadImage(String title, String description, ArrayList<String> realPaths) {
        RequestBody requetCityKey = RequestBody.create(MediaType.parse("text/plain"), globalApplication.cityKeyInt.get(cityKey));
        RequestBody requestTitle = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody requestDescription = RequestBody.create(MediaType.parse("text/plain"), description);

        HashMap<String, RequestBody> hashMap = new HashMap<>();
        hashMap.put("cityKey", requetCityKey);
        hashMap.put("title", requestTitle);
        hashMap.put("context", requestDescription);

        final List<MultipartBody.Part> files = new ArrayList<>();
        for (int i = 0; i < realPaths.size(); i++) {
            File file = new File(realPaths.get(i));
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
