package com.jucanos.photomap.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jucanos.photomap.Fragment.FragmentViewPager;
import com.jucanos.photomap.Fragment.FragmentPagerAdapter;
import com.jucanos.photomap.R;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.jucanos.photomap.Structure.GetMapList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        /* TextView */
        TextView mCustomTabTextView1 = (TextView) LayoutInflater.from(this).inflate(R.layout.layout_tab_item, null);
        TextView mCustomTabTextView2 = (TextView) LayoutInflater.from(this).inflate(R.layout.layout_tab_item, null);
        TextView mCustomTabTextView3 = (TextView) LayoutInflater.from(this).inflate(R.layout.layout_tab_item, null);

        mCustomTabTextView1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_icon_group, 0, 0);
        tabLayout.addTab(tabLayout.newTab().setCustomView(mCustomTabTextView1));

        mCustomTabTextView2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_icon_map, 0, 0);
        tabLayout.addTab(tabLayout.newTab().setCustomView(mCustomTabTextView2));

        mCustomTabTextView3.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_icon_menu, 0, 0);
        tabLayout.addTab(tabLayout.newTab().setCustomView(mCustomTabTextView3));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final FragmentViewPager viewPager = findViewById(R.id.pager);
        final FragmentPagerAdapter adapter = new FragmentPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.disableScroll(true);
        viewPager.setCurrentItem(1);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }


}