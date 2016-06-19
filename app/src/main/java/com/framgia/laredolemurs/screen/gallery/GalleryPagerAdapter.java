package com.framgia.laredolemurs.screen.gallery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.framgia.laredolemurs.screen.tv.TvFragment;
import com.framgia.laredolemurs.screen.video.VideoFragment;
import com.framgia.laredolemurs.screen.album.AlbumFragment;

public class GalleryPagerAdapter extends FragmentStatePagerAdapter {

    public GalleryPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return AlbumFragment.newInstance();
            case 1:
                return VideoFragment.newInstance();
            case 2:
                return TvFragment.newInstance();
        }
        return AlbumFragment.newInstance();
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
