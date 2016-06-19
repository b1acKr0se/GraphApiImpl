package com.framgia.laredolemurs.screen.album;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.laredolemurs.data.model.Album;
import com.framgia.laredolemurs.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private List<Album> albumList;
    private Context mContext;
    private OnAlbumClickListener onAlbumClickListener;

    AlbumAdapter(Context context, List<Album> album) {
        mContext = context;
        albumList = album;
    }

    void setOnAlbumClickListener(OnAlbumClickListener onAlbumClickListener) {
        this.onAlbumClickListener = onAlbumClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Album album = albumList.get(position);
        holder.itemView.setOnClickListener(holder);
        holder.position = position;
        Picasso.with(mContext).load(album.getCoverUrl()).into(holder.cover);
        holder.name.setText(album.getName());
        holder.count.setText(album.getCount() + " photos");
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.album_cover) ImageView cover;
        @Bind(R.id.album_name) TextView name;
        @Bind(R.id.album_count) TextView count;
        private int position;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            onAlbumClickListener.onAlbumClicked(position);
        }
    }
}
