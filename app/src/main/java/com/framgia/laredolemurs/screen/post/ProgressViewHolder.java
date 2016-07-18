package com.framgia.laredolemurs.screen.post;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.framgia.laredolemurs.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FRAMGIA\nguyen.thanh.hai on 18/07/2016.
 */

public class ProgressViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.progress_bar) ProgressBar progressBar;

    public ProgressViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
