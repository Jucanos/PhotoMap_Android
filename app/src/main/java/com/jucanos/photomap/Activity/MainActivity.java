package com.jucanos.photomap.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.jucanos.photomap.Fragment.FragmentViewPager;
import com.jucanos.photomap.Fragment.MainFragmentGroup;
import com.jucanos.photomap.Fragment.MainFragmentPagerAdapter;
import com.jucanos.photomap.Fragment.MainFragmentRep;
import com.jucanos.photomap.R;

public class MainActivity extends AppCompatActivity {
    public TabLayout tabLayout;
    private String mid;
    private Boolean fromLink;
    private MainFragmentPagerAdapter adapter;
    private FragmentViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMember();
        checkLink();
        setTabLayout();
    }

    void initMember() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.fragmentViewPager_fvp);
    }

    void checkLink() {
        mid = getIntent().getStringExtra("mid");
        fromLink = mid != null;
    }

    void setTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_icon_group_fill));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_icon_map));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_icon_setting));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        adapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.disableScroll(true);
        viewPager.setOffscreenPageLimit(3);

        if (fromLink) {
            viewPager.setCurrentItem(0);
            tabLayout.getTabAt(0).setIcon(R.drawable.ic_icon_group_fill);
            tabLayout.getTabAt(1).setIcon(R.drawable.ic_icon_map);
            tabLayout.getTabAt(2).setIcon(R.drawable.ic_icon_setting);
            ((MainFragmentGroup) adapter.getItem(0)).onNewIntent(mid);
        } else {
            viewPager.setCurrentItem(1);
            tabLayout.getTabAt(0).setIcon(R.drawable.ic_icon_group);
            tabLayout.getTabAt(1).setIcon(R.drawable.ic_icon_map_fill);
            tabLayout.getTabAt(2).setIcon(R.drawable.ic_icon_setting);
        }
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), false);
                switch (tab.getPosition()) {
                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_icon_group_fill);
                        tabLayout.getTabAt(1).setIcon(R.drawable.ic_icon_map);
                        tabLayout.getTabAt(2).setIcon(R.drawable.ic_icon_setting);
                        break;
                    case 1:
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_icon_group);
                        tabLayout.getTabAt(1).setIcon(R.drawable.ic_icon_map_fill);
                        tabLayout.getTabAt(2).setIcon(R.drawable.ic_icon_setting);
                        ((MainFragmentRep) adapter.getItem(1)).setRep();
                        break;
                    case 2:
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_icon_group);
                        tabLayout.getTabAt(1).setIcon(R.drawable.ic_icon_map);
                        tabLayout.getTabAt(2).setIcon(R.drawable.ic_icon_setting_fill);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     *
     * @param intent : CEALR_TOP FLAG 를 설정했을경우 onCreate 되지 않기때문에 onNewIntent 를 통해 link
     *               유무를 구분한다.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        String mid = intent.getStringExtra("mid");
        if (mid != null) {
            viewPager.setCurrentItem(0);
            tabLayout.getTabAt(0).setIcon(R.drawable.ic_icon_group_fill);
            tabLayout.getTabAt(1).setIcon(R.drawable.ic_icon_map);
            tabLayout.getTabAt(2).setIcon(R.drawable.ic_icon_setting);
            ((MainFragmentGroup) adapter.getItem(0)).onNewIntent(mid);
        }
        ((MainFragmentGroup) adapter.getItem(0)).onNewIntent(mid);
        super.onNewIntent(intent);
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}