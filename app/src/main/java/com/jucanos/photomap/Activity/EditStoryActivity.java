package com.jucanos.photomap.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.jucanos.photomap.Fragment.AddStoryFragmentDescription;
import com.jucanos.photomap.Fragment.AddStoryFragmentTitle;
import com.jucanos.photomap.Fragment.EditStoryFragmentContext;
import com.jucanos.photomap.Fragment.EditStoryFragmentTitle;
import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.R;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.jucanos.photomap.Structure.EditStory;
import com.jucanos.photomap.Structure.EditStoryRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditStoryActivity extends AppCompatActivity {
    public GlobalApplication globalApplication;

    private FragmentManager fragmentManager;
    private EditStoryFragmentTitle editStoryFragmentTitle;
    private EditStoryFragmentContext editStoryFragmentDescription;

    private String sid;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_story);

        globalApplication = GlobalApplication.getGlobalApplicationContext();

        fragmentManager = getSupportFragmentManager();

        editStoryFragmentTitle = new EditStoryFragmentTitle();
        fragmentManager.beginTransaction().replace(R.id.main_frame, editStoryFragmentTitle).commit();

        sid = getIntent().getStringExtra("sid");
        pos = getIntent().getIntExtra("pos",-1);
    }

    public void setFrag(int pos) {
        switch (pos) {
            case 0:
                if (editStoryFragmentTitle == null) {
                    editStoryFragmentTitle = new EditStoryFragmentTitle();
                    fragmentManager.beginTransaction().add(R.id.main_frame, editStoryFragmentTitle).commit();
                }
                if (editStoryFragmentTitle != null)
                    fragmentManager.beginTransaction().show(editStoryFragmentTitle).commit();
                if (editStoryFragmentDescription != null)
                    fragmentManager.beginTransaction().hide(editStoryFragmentDescription).commit();
                break;
            case 1:
                if (editStoryFragmentDescription == null) {
                    editStoryFragmentDescription = new EditStoryFragmentContext();
                    fragmentManager.beginTransaction().add(R.id.main_frame, editStoryFragmentDescription).commit();
                }
                if (editStoryFragmentTitle != null)
                    fragmentManager.beginTransaction().hide(editStoryFragmentTitle).commit();
                if (editStoryFragmentDescription != null)
                    fragmentManager.beginTransaction().show(editStoryFragmentDescription).commit();
                break;
        }
    }

    public void editStory(){
        String title = editStoryFragmentTitle.getTitle();
        String context= editStoryFragmentDescription.getDescription();
        editStoryRequest(sid,title,context);
    }

    public void editStoryRequest(String sid, final String title, final String context){
        final Call<EditStory> res = NetworkHelper.getInstance().getService().editStory("Bearer " + GlobalApplication.getGlobalApplicationContext().token,sid,new EditStoryRequest(title,context));
        res.enqueue(new Callback<EditStory>() {
            @Override
            public void onResponse(Call<EditStory> call, Response<EditStory> response) {
                if (response.isSuccessful()) {
                    Log.e("StoryActivity", "[removeStoryRequest] is Successful");
                    redirectResult(title,context);
                } else {
                    Log.e("StoryActivity", "[removeStoryRequest] " + Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<EditStory> call, Throwable t) {
                Log.e("StoryActivity", "[removeStoryRequest fail] " + t.getLocalizedMessage());
            }
        });
    }

    public void redirectResult(String title, String context){
        Intent intent = new Intent();
        intent.putExtra("title", title);
        intent.putExtra("context", context);
        intent.putExtra("pos",pos);
        setResult(RESULT_OK, intent);
        finish();
    }
}
