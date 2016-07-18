package com.framgia.laredolemurs.screen.video;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.laredolemurs.R;
import com.framgia.laredolemurs.data.model.Video;
import com.framgia.laredolemurs.screen.post.ProgressViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_VIDEO = 1;
    private List<Video> videoList;
    private Context mContext;
    private OnVideoClickListener onVideoClickListener;

    public VideoAdapter(Context context, List<Video> album) {
        mContext = context;
        videoList = album;
    }

    public void setOnVideoClickListener(OnVideoClickListener onVideoClickListener) {
        this.onVideoClickListener = onVideoClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return videoList.get(position) == null ? TYPE_PROGRESS : TYPE_VIDEO;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_VIDEO) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_video, parent, false);
            return new ViewHolder(view);
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_progress, parent, false);
        return new ProgressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            Video video = videoList.get(position);
            holder.itemView.setOnClickListener(holder);
            holder.position = position;
            Picasso.with(mContext).load(video.getPicture()).into(holder.image);
            if (video.getTitle() == null) {
                holder.title.setVisibility(View.GONE);
            } else {
                holder.title.setVisibility(View.VISIBLE);
                holder.title.setText(video.getTitle());
            }
            holder.desc.setText(video.getDescription());
            holder.length.setText(video.getDuration());
        }
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.photo) ImageView image;
        @Bind(R.id.video_title) TextView title;
        @Bind(R.id.video_length) TextView length;
        @Bind(R.id.video_desc) TextView desc;
        private int position;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            onVideoClickListener.onVideoClicked(position);
        }
    }
}
