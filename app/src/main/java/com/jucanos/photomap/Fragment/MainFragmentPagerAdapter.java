package com.jucanos.photomap.Fragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MainFragmentPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public MainFragmentGroup tab1 = new MainFragmentGroup();
    public MainFragmentRep tab2 = new MainFragmentRep();
    public MainFragmentSetting tab3 = new MainFragmentSetting();
    public MainFragmentPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return tab1;
            case 1:
                return tab2;
            case 2:
                return tab3;
            default:
                return null;
        }
    }
}