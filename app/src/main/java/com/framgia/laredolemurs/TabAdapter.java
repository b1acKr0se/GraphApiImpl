package com.framgia.laredolemurs;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class TabAdapter extends FragmentPagerAdapter {

    public static final int NUMBER_OF_SCREEN = 3;

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return HomeFragment.newInstance();
            case 1:
                break;
            case 2:
                return GalleryFragment.newInstance();
        }
        return HomeFragment.newInstance();
    }

    @Override
    public int getCount() {
        return NUMBER_OF_SCREEN;
    }
}