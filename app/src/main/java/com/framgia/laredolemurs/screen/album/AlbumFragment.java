package com.framgia.laredolemurs.screen.album;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.framgia.laredolemurs.R;
import com.framgia.laredolemurs.data.model.Album;
import com.framgia.laredolemurs.screen.photo.PhotoActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class AlbumFragment extends Fragment implements OnAlbumClickListener {
    private List<Album> mAlbumList = new ArrayList<>();
    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.progress_bar) ProgressBar mProgressBar;

    public static final String TAG = "PhotoFragment";

    public AlbumFragment() {
        // Required empty public constructor
    }

    public static AlbumFragment newInstance() {
        return new AlbumFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        ButterKnife.bind(this, view);
        retrieveAlbums();
        return view;
    }

    private void retrieveAlbums() {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "count,name,created_time,cover_photo.fields(images)");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/LaredoLemurs/albums",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        if (response != null) {
                            mAlbumList = parseResult(response);
                            showList();
                        }
                    }
                }
        ).executeAsync();
    }

    private List<Album> parseResult(GraphResponse response) {
        List<Album> albumList = new ArrayList<>();
        JSONObject object = response.getJSONObject();
        try {
            if(object == null) return albumList;
            JSONArray array = object.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                Album album = new Album();
                album.setId(jsonObject.getString("id"));
                album.setName(jsonObject.getString("name"));
                album.setCreatedTime(jsonObject.getString("created_time"));
                album.setCount(jsonObject.getInt("count"));
                if (jsonObject.has("cover_photo"))
                    album.setCoverUrl(jsonObject.getJSONObject("cover_photo").getJSONArray("images").getJSONObject(0).getString("source"));
                if (jsonObject.getInt("count") > 0)
                    albumList.add(album);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return albumList;
    }

    private void showList() {
        mProgressBar.setVisibility(View.GONE);
        AlbumAdapter mAdapter = new AlbumAdapter(getActivity(), mAlbumList);
        mAdapter.setOnAlbumClickListener(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onAlbumClicked(int position) {
        Album album = mAlbumList.get(position);
        if (album != null) {
            Intent intent = new Intent(getActivity(), PhotoActivity.class);
            intent.putExtra("name", album.getName());
            intent.putExtra("id", album.getId());
            startActivity(intent);
        }
    }
}
