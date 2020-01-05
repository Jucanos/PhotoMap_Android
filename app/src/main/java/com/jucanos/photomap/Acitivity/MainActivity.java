package com.jucanos.photomap.Acitivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jucanos.photomap.Fragment.CustomViewPager;
import com.jucanos.photomap.Fragment.PagerAdapter;
import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.R;

public class MainActivity extends AppCompatActivity {
    public TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        /* TextView */
        TextView mCustomTabTextView1 = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_item, null);
        TextView mCustomTabTextView2 = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_item, null);
        TextView mCustomTabTextView3 = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_item, null);


        mCustomTabTextView1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_icon_map , 0, 0);
        tabLayout.addTab(tabLayout.newTab().setCustomView(mCustomTabTextView1));

        mCustomTabTextView2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_icon_group , 0, 0);
        tabLayout.addTab(tabLayout.newTab().setCustomView(mCustomTabTextView2));

        mCustomTabTextView3.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_icon_menu , 0, 0);
        tabLayout.addTab(tabLayout.newTab().setCustomView(mCustomTabTextView3));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.disableScroll(true);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}