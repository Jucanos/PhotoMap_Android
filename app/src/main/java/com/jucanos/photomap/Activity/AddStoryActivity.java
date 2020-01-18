package com.jucanos.photomap.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.jucanos.photomap.Fragment.AddStoryFragmentDescription;
import com.jucanos.photomap.Fragment.AddStoryFragmentImage;
import com.jucanos.photomap.Fragment.AddStoryFragmentPagerAdapter;
import com.jucanos.photomap.Fragment.AddStoryFragmentTitle;
import com.jucanos.photomap.Fragment.FragmentViewPager;
import com.jucanos.photomap.R;

public class AddStoryActivity extends AppCompatActivity {
    private FragmentViewPager viewPager;
    private AddStoryFragmentPagerAdapter adapter;

    FragmentManager fragmentManager;
    AddStoryFragmentImage addStoryFragmentImage;
    AddStoryFragmentTitle addStoryFragmentTitle;
    AddStoryFragmentDescription addStoryFragmentDescription;


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
}
