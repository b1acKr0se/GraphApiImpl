package com.framgia.laredolemurs.screen.post;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.laredolemurs.R;
import com.framgia.laredolemurs.constant.Const;
import com.framgia.laredolemurs.data.model.Post;
import com.framgia.laredolemurs.util.DateDifferenceConverter;
import com.framgia.laredolemurs.widget.CircleTransformation;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

abstract class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @Bind(R.id.avatar) ImageView avatar;
    @Bind(R.id.story) TextView story;
    @Bind(R.id.time) TextView time;
    @Bind(R.id.message) TextView message;

    private int position;
    private OnPostClickListener onPostClickListener;

    BaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setOnPostClickListener(OnPostClickListener onPostClickListener) {
        this.onPostClickListener = onPostClickListener;
    }

    protected void setDetails(Post post) {
        Picasso.with(itemView.getContext()).load(Const.pageProfilePicture).transform(new CircleTransformation()).into(avatar);
        time.setText(DateDifferenceConverter.getDateDifference(post.getCreatedTime()));
        story.setText(post.getStory() == null ? "Laredo Lemurs" : post.getStory());
        if (TextUtils.isEmpty(post.getMessage())) {
            message.setVisibility(View.GONE);
        } else {
            message.setVisibility(View.VISIBLE);
            message.setText(post.getMessage());
        }
    }

    @Override public void onClick(View view) {
        if (onPostClickListener != null)
            onPostClickListener.onPostClick(position);
    }
}
