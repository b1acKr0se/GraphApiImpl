package com.framgia.laredolemurs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.content_frame)
    View mContentView;
    @Bind(R.id.button_gallery)
    View mGalleryButton;
    @Bind(R.id.button_home)View mHomeButton;
    @Bind(R.id.button_post)View mPostButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mGalleryButton.setOnClickListener(this);
        mHomeButton.setOnClickListener(this);
        mPostButton.setOnClickListener(this);
        if (savedInstanceState == null)
            navigateToHome();
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
        }
    }

    void navigateToHome() {
        mHomeButton.setAlpha(1.0f);
        mPostButton.setAlpha(0.5f);
        mGalleryButton.setAlpha(0.5f);
        Fragment fragment = HomeFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment, "Home");
        fragmentTransaction.commit();
    }

    void navigateToGallery() {
        mGalleryButton.setAlpha(1.0f);
        mPostButton.setAlpha(0.5f);
        mHomeButton.setAlpha(0.5f);
        Fragment fragment = GalleryFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment, "Gallery");
        fragmentTransaction.commit();
    }
}
