package com.framgia.laredolemurs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
    private List<Photo> albumList;
    private Context mContext;
    private OnPhotoClickListener onPhotoClickListener;

    public PhotoAdapter(Context context, List<Photo> album) {
        mContext = context;
        albumList = album;
    }

    public void setOnPhotoClickListener(OnPhotoClickListener onPhotoClickListener) {
        this.onPhotoClickListener = onPhotoClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Photo photo = albumList.get(position);
        holder.itemView.setOnClickListener(holder);
        holder.position = position;
        Picasso.with(mContext).load(photo.getSmallUrl()).placeholder(R.drawable.placeholder).into(holder.image);
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
