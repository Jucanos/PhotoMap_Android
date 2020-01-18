package com.jucanos.photomap.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jucanos.photomap.Fragment.AddStoryFragmentDescription;
import com.jucanos.photomap.Fragment.AddStoryFragmentImage;
import com.jucanos.photomap.Fragment.AddStoryFragmentPagerAdapter;
import com.jucanos.photomap.Fragment.AddStoryFragmentTitle;
import com.jucanos.photomap.Fragment.FragmentViewPager;
import com.jucanos.photomap.R;

public class AddStoryActivity extends AppCompatActivity {
    private FragmentViewPager viewPager;
    private AddStoryFragmentPagerAdapter adapter;

    FragmentManager fm;
    FragmentTransaction tran;
    AddStoryFragmentImage frag1;
    AddStoryFragmentTitle frag2;
    AddStoryFragmentDescription frag3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);

        fm = getSupportFragmentManager();

        frag1 = new AddStoryFragmentImage(); //프래그먼트 객채셍성
        fm.beginTransaction().replace(R.id.main_frame, frag1).commit();
    }

    public void setFrag(int pos) {
        switch (pos) {
            case 0:
                if (frag1 == null) {
                    frag1 = new AddStoryFragmentImage();
                    fm.beginTransaction().add(R.id.main_frame, frag1).commit();
                }
                if (frag1 != null) fm.beginTransaction().show(frag1).commit();
                if (frag2 != null) fm.beginTransaction().hide(frag2).commit();
                if (frag3 != null) fm.beginTransaction().hide(frag3).commit();
                break;
            case 1:
                if (frag2 == null) {
                    frag2 = new AddStoryFragmentTitle();
                    fm.beginTransaction().add(R.id.main_frame, frag2).commit();
                }
                if (frag1 != null) fm.beginTransaction().hide(frag1).commit();
                if (frag2 != null) fm.beginTransaction().show(frag2).commit();
                if (frag3 != null) fm.beginTransaction().hide(frag3).commit();
                break;
            case 2:
                if (frag3 == null) {
                    frag3 = new AddStoryFragmentDescription();
                    fm.beginTransaction().add(R.id.main_frame, frag3).commit();
                }
                if (frag1 != null) fm.beginTransaction().hide(frag1).commit();
                if (frag2 != null) fm.beginTransaction().hide(frag2).commit();
                if (frag3 != null) fm.beginTransaction().show(frag3).commit();
                break;
        }
    }
}
