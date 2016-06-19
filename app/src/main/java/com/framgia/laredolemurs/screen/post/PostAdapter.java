package com.framgia.laredolemurs.screen.post;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.laredolemurs.R;
import com.framgia.laredolemurs.data.model.Post;

import java.util.List;

class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_STATUS = 1;
    public static final int TYPE_VIDEO = 2;
    public static final int TYPE_PHOTO = 3;
    public static final int TYPE_EVENT = 4;
    public static final int TYPE_LINK = 5;
    public static final int TYPE_OFFER = 6;
    private List<Post> mList;
    private Context mContext;
    private OnPostClickListener onPostClickListener;

    PostAdapter(Context context, List<Post> list) {
        this.mList = list;
        this.mContext = context;
    }

    public void setOnPostClickListener(OnPostClickListener onPostClickListener) {
        this.onPostClickListener = onPostClickListener;
    }

    @Override public int getItemViewType(int position) {
        switch (mList.get(position).getType()) {
            case PHOTO:
                return TYPE_PHOTO;
            case VIDEO:
                return TYPE_VIDEO;
            case STATUS:
                return TYPE_STATUS;
            case LINK:
                return TYPE_LINK;
            case OFFER:
                return TYPE_OFFER;
            case EVENT:
                return TYPE_EVENT;
        }
        return -1;
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_PHOTO:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_post_photo, parent, false);
                return new PhotoViewHolder(view);
            case TYPE_VIDEO:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_post_video, parent, false);
                return new VideoViewHolder(view);
            case TYPE_STATUS:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_post_status, parent, false);
                return new StatusViewHolder(view);
            case TYPE_LINK:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_post_status, parent, false);
                return new StatusViewHolder(view);
            case TYPE_OFFER:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_post_status, parent, false);
                return new StatusViewHolder(view);
            case TYPE_EVENT:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_post_photo, parent, false);
                return new PhotoViewHolder(view);
        }
        view = LayoutInflater.from(mContext).inflate(R.layout.item_post_status, parent, false);
        return new StatusViewHolder(view);
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseViewHolder viewHolder = (BaseViewHolder) holder;
        viewHolder.setPosition(position);
        viewHolder.itemView.setOnClickListener(viewHolder);
        viewHolder.setOnPostClickListener(onPostClickListener);
        Post post = mList.get(position);
        switch (getItemViewType(position)) {
            case TYPE_PHOTO:
                PhotoViewHolder photoViewHolder = (PhotoViewHolder) viewHolder;
                photoViewHolder.setDetails(post);
                break;
            case TYPE_VIDEO:
                VideoViewHolder videoViewHolder = (VideoViewHolder) viewHolder;
                videoViewHolder.setDetails(post);
                break;
            case TYPE_STATUS:
                StatusViewHolder statusViewHolder = (StatusViewHolder) viewHolder;
                statusViewHolder.setDetails(post);
                break;
            case TYPE_LINK:
                StatusViewHolder linkViewHolder = (StatusViewHolder) viewHolder;
                linkViewHolder.setDetails(post);
                break;
            case TYPE_OFFER:
                StatusViewHolder offerViewHolder = (StatusViewHolder) viewHolder;
                offerViewHolder.setDetails(post);
                break;
            case TYPE_EVENT:
                PhotoViewHolder eventViewHolder = (PhotoViewHolder) viewHolder;
                eventViewHolder.setDetails(post);
                break;
        }
    }

    @Override public int getItemCount() {
        return mList.size();
    }

}
