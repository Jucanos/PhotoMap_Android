package com.jucanos.photomap.Fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class AddStoryFragmentPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public AddStoryFragmentPagerAdapter(FragmentManager fm, int NumOfTabs) {
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
                AddStoryFragmentImage tab1 = new AddStoryFragmentImage();
                return tab1;
            case 1:
                AddStoryFragmentTitle tab2 = new AddStoryFragmentTitle();
                return tab2;
            case 2:
                AddStoryFragmentDescription tab3 = new AddStoryFragmentDescription();
                return tab3;
            default:
                return null;
        }
    }


}
