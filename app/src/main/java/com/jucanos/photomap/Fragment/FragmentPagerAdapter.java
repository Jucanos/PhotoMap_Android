package com.jucanos.photomap.Fragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public FragmentPagerAdapter(FragmentManager fm, int NumOfTabs) {
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
                FragmentGroup tab1 = new FragmentGroup();
                return tab1;
            case 1:
                FragmentRep tab2 = new FragmentRep();
                return tab2;
            case 2:
                FragmentSetting tab3 = new FragmentSetting();
                return tab3;
            default:
                return null;
        }
    }
}