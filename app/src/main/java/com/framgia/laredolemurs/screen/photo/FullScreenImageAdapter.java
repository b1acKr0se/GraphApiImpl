package com.framgia.laredolemurs.screen.photo;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.framgia.laredolemurs.R;
import com.framgia.laredolemurs.data.model.Photo;
import com.framgia.laredolemurs.widget.TouchImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FullScreenImageAdapter extends PagerAdapter {
    private List<Photo> mPhotos;
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;

    public FullScreenImageAdapter(Activity a, List<Photo> list) {
        mActivity = a;
        mPhotos = list;
        mLayoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View layout = mLayoutInflater.inflate(R.layout.item_full_screen_image, container, false);
        TouchImageView imageView = (TouchImageView) layout.findViewById(R.id.image);
        if (mPhotos.get(position) != null)
            Picasso.with(mActivity).load(mPhotos.get(position).getSourceUrl()).into(imageView);
        layout.setTag(position);
        imageView.resetZoom();
        container.addView(layout);
        return layout;
    }


    @Override
    public int getCount() {
        return mPhotos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }
}
