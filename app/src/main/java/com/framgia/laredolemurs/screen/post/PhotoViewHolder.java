package com.framgia.laredolemurs.screen.post;

import android.view.View;
import android.widget.ImageView;

import com.framgia.laredolemurs.R;
import com.framgia.laredolemurs.data.model.Post;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

class PhotoViewHolder extends BaseViewHolder {
    @Bind(R.id.photo) ImageView photo;

    PhotoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override protected void setDetails(Post post) {
        super.setDetails(post);
        Picasso.with(itemView.getContext()).load(post.getPicture()).into(photo);
    }
}
