package com.framgia.laredolemurs.screen.photo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.framgia.laredolemurs.R;
import com.framgia.laredolemurs.data.model.Photo;
import com.framgia.laredolemurs.screen.post.ProgressViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Photo> albumList;
    private Context mContext;
    private OnPhotoClickListener onPhotoClickListener;
    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_PHOTO = 1;

    public PhotoAdapter(Context context, List<Photo> album) {
        mContext = context;
        albumList = album;
    }

    @Override
    public int getItemViewType(int position) {
        return albumList.get(position) == null ? TYPE_PROGRESS : TYPE_PHOTO;
    }

    public void setOnPhotoClickListener(OnPhotoClickListener onPhotoClickListener) {
        this.onPhotoClickListener = onPhotoClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_PHOTO) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_photo, parent, false);
            return new ViewHolder(view);
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_progress, parent, false);
        return new ProgressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            Photo photo = albumList.get(position);
            viewHolder.itemView.setOnClickListener(viewHolder);
            viewHolder.position = position;
            Picasso.with(mContext).load(photo.getSmallUrl()).placeholder(R.drawable.placeholder).into(viewHolder.image);
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.photo)
        ImageView image;
        private int position;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            onPhotoClickListener.onPhotoClicked(position);
        }
    }
}
