package com.framgia.laredolemurs;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import butterknife.Bind;
import butterknife.ButterKnife;


public class GalleryFragment extends Fragment {

    @Bind(R.id.view_pager)ViewPager mViewPager;
    @Bind(R.id.tab_layout)TabLayout mTabLayout;

    public GalleryFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new GalleryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);
        GalleryPagerAdapter adapter = new GalleryPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setTabTextColors(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark), ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        mTabLayout.setupWithViewPager(mViewPager);
        return view;
    }
}
