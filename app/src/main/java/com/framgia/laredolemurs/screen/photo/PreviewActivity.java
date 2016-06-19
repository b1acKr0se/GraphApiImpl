package com.framgia.laredolemurs.screen.photo;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.framgia.laredolemurs.R;
import com.framgia.laredolemurs.data.model.Photo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PreviewActivity extends AppCompatActivity implements View.OnClickListener {
    private List<Photo> mPhotos;
    private FullScreenImageAdapter fullScreenImageAdapter;
    @Bind(R.id.view_pager) ViewPager mViewPager;
    @Bind(R.id.position_indicator) TextView mPositionTextView;
    @Bind(R.id.caption) TextView mCaption;
    @Bind(R.id.scroll_view) ScrollView mScrollView;
    @Bind(R.id.close_btn) ImageView mCloseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);
        mPhotos = getIntent().getParcelableArrayListExtra("list");
        int position = getIntent().getIntExtra("position", -1);
        mCloseButton.setOnClickListener(this);
        fullScreenImageAdapter = new FullScreenImageAdapter(this, mPhotos);
        mViewPager.setAdapter(fullScreenImageAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPositionTextView.setText((position + 1) + "/" + mPhotos.size());
                if (mPhotos.get(position).getCaption() == null) {
                    mScrollView.setVisibility(View.GONE);
                } else {
                    mScrollView.setVisibility(View.VISIBLE);
                    mCaption.setText(mPhotos.get(position).getCaption());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(position == -1 ? 0 : position);
        mPositionTextView.setText((position + 1) + "/" + mPhotos.size());
        if (mPhotos.get(position).getCaption() == null) {
            mScrollView.setVisibility(View.GONE);
        } else {
            mScrollView.setVisibility(View.VISIBLE);
            mCaption.setText(mPhotos.get(position).getCaption());
        }
        mViewPager.setPageMargin(convertToPx(20));
    }

    public int convertToPx(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.close_btn) {
            finish();
        }
    }
}
