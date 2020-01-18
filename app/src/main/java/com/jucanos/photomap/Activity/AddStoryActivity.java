package com.jucanos.photomap.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.jucanos.photomap.Fragment.AddStoryFragmentDescription;
import com.jucanos.photomap.Fragment.AddStoryFragmentImage;
import com.jucanos.photomap.Fragment.AddStoryFragmentPagerAdapter;
import com.jucanos.photomap.Fragment.AddStoryFragmentTitle;
import com.jucanos.photomap.Fragment.FragmentViewPager;
import com.jucanos.photomap.R;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.jucanos.photomap.Structure.CreateStory;

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
    private FragmentViewPager viewPager;
    private AddStoryFragmentPagerAdapter adapter;

    FragmentManager fragmentManager;
    AddStoryFragmentImage addStoryFragmentImage;
    AddStoryFragmentTitle addStoryFragmentTitle;
    AddStoryFragmentDescription addStoryFragmentDescription;

    String mid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);

        fragmentManager = getSupportFragmentManager();

        addStoryFragmentImage = new AddStoryFragmentImage(); //프래그먼트 객채셍성
        fragmentManager.beginTransaction().replace(R.id.main_frame, addStoryFragmentImage).commit();

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
            realPaths.add(saveBitmap("image_" + Integer.toString(i), bitmaps.get(i)));
        }
        String title = addStoryFragmentTitle.getTitle();
        String description = addStoryFragmentDescription.getDescription();
        Log.e("AddStoryActivity", "title : " + title);
        Log.e("AddStoryActivity", "description : " + description);
        Intent intent = new Intent();
        intent.putExtra("title", title);
        intent.putExtra("description", description);
        intent.putExtra("realPaths", realPaths);
        setResult(RESULT_OK, intent);
        finish();
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


    public void requestUploadImage() {
        //Log.e("requestUploadImage","!!!");
        String token = "0M-wiDPwD-me_onONYmwdhImZL8KcUUe52VylQopcFEAAAFvtz67oQ";
        String mid = "4702b72cd291a95aa456836585d7b7ab";

        RequestBody cityKey = RequestBody.create(MediaType.parse("text/plain"), "gangwon");
        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), "제목");
        RequestBody context = RequestBody.create(MediaType.parse("text/plain"), "내용");

        HashMap<String, RequestBody> hashMap = new HashMap<>();
        hashMap.put("cityKey", cityKey);
        hashMap.put("title", title);
        hashMap.put("context", context);

        List<Uri> paths = new ArrayList<>();
        paths.add(Uri.fromFile(new File("/storage/emulated/0/DCIM/Screenshots/Screenshot_20200118-172835_photoMap.jpg")));
        paths.add(Uri.fromFile(new File("/storage/emulated/0/DCIM/Screenshots/Screenshot_20200118-153913_photoMap.jpg")));

        List<MultipartBody.Part> files = new ArrayList<>();

        File file = new File("/storage/emulated/0/DCIM/Screenshots/Screenshot_20200118-172835_photoMap.jpg");
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        Log.e("file1", file.getName());
        files.add(MultipartBody.Part.createFormData("img", file.getName(), requestFile));

        file = new File("/storage/emulated/0/DCIM/Screenshots/Screenshot_20200118-153913_photoMap.jpg");
        requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        Log.e("file2", file.getName());
        files.add(MultipartBody.Part.createFormData("img", file.getName(), requestFile));

        final Call<CreateStory> res = NetworkHelper.getInstance().getService().createStory("Bearer " + token, mid, hashMap, files);
        res.enqueue(new Callback<CreateStory>() {
            @Override
            public void onResponse(Call<CreateStory> call, Response<CreateStory> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e("[isSuccessful]", "isSuccessful");
                    }
                } else {
                    Log.e("requestUploadImage", "requestUploadImage error : " + Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<CreateStory> call, Throwable t) {
                Log.e("FragmentDescription", "requestUploadImage fail");
            }
        });
    }

}
