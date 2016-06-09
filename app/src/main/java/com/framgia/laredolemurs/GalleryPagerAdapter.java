package com.framgia.laredolemurs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class GalleryPagerAdapter extends FragmentStatePagerAdapter {

    public GalleryPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return PhotoFragment.newInstance();
            case 1:
                return VideoFragment.newInstance();
            case 2:
        }
        return PhotoFragment.newInstance();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Photos";
            case 1:
                return "Videos";
            case 2:
                return "TV";
            default:
                return "Photos";
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
