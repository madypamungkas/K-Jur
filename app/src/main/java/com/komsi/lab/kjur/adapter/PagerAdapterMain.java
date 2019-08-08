package com.komsi.lab.kjur.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.komsi.lab.kjur.HomeFragment;
import com.komsi.lab.kjur.OrderFragment;

public class PagerAdapterMain extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapterMain(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                HomeFragment tabHome = new HomeFragment();
                return tabHome;
            case 1:
                OrderFragment tabOrder = new OrderFragment();
                return tabOrder;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}