package com.framgia.laredolemurs.screen.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.framgia.laredolemurs.R;
import com.framgia.laredolemurs.screen.gallery.GalleryFragment;
import com.framgia.laredolemurs.screen.home.HomeFragment;
import com.framgia.laredolemurs.screen.post.PostFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.button_gallery) View mGalleryButton;
    @Bind(R.id.button_home) View mHomeButton;
    @Bind(R.id.button_post) View mPostButton;
    private Fragment mHomeFragment, mGalleryFragment, mPostFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mGalleryButton.setOnClickListener(this);
        mHomeButton.setOnClickListener(this);
        mPostButton.setOnClickListener(this);
        if (savedInstanceState == null) {
            mHomeFragment = HomeFragment.newInstance();
            mGalleryFragment = GalleryFragment.newInstance();
            mPostFragment = PostFragment.newInstace();
            navigateToHome();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_home:
                navigateToHome();
                break;
            case R.id.button_gallery:
                navigateToGallery();
                break;
            case R.id.button_post:
                navigateToPost();
                break;
        }
    }

    void navigateToHome() {
        mHomeButton.setAlpha(1.0f);
        mPostButton.setAlpha(0.5f);
        mGalleryButton.setAlpha(0.5f);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (mHomeFragment.isAdded()) fragmentTransaction.show(mHomeFragment);
        else fragmentTransaction.add(R.id.content_frame, mHomeFragment, "Home");
        if (mGalleryFragment.isAdded()) fragmentTransaction.hide(mGalleryFragment);
        if (mPostFragment.isAdded()) fragmentTransaction.hide(mPostFragment);
        fragmentTransaction.commit();
    }

    void navigateToGallery() {
        mGalleryButton.setAlpha(1.0f);
        mPostButton.setAlpha(0.5f);
        mHomeButton.setAlpha(0.5f);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (mGalleryFragment.isAdded()) fragmentTransaction.show(mGalleryFragment);
        else fragmentTransaction.add(R.id.content_frame, mGalleryFragment, "Gallery");
        if (mHomeFragment.isAdded()) fragmentTransaction.hide(mHomeFragment);
        if (mPostFragment.isAdded()) fragmentTransaction.hide(mPostFragment);
        fragmentTransaction.commit();
    }

    void navigateToPost() {
        mGalleryButton.setAlpha(0.5f);
        mPostButton.setAlpha(1.0f);
        mHomeButton.setAlpha(0.5f);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (mPostFragment.isAdded()) fragmentTransaction.show(mPostFragment);
        else fragmentTransaction.add(R.id.content_frame, mPostFragment, "Post");
        if (mHomeFragment.isAdded()) fragmentTransaction.hide(mHomeFragment);
        if (mGalleryFragment.isAdded()) fragmentTransaction.hide(mGalleryFragment);
        fragmentTransaction.commit();
    }
}
