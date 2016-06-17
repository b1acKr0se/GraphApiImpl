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
import com.framgia.laredolemurs.Album;
import com.framgia.laredolemurs.OnAlbumClickListener;
import com.framgia.laredolemurs.PhotoActivity;
import com.framgia.laredolemurs.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class PhotoFragment extends Fragment implements OnAlbumClickListener {
    private List<Album> mAlbumList = new ArrayList<>();
    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    private AlbumAdapter mAdapter;

    public static final String TAG = "PhotoFragment";

    public PhotoFragment() {
        // Required empty public constructor
    }

    public static PhotoFragment newInstance() {
        return new PhotoFragment();
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
        parameters.putString("fields", "count,name,created_time,picture.type(album)");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/LaredoLemurs/albums",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        mAlbumList = parseResult(response);
                        showList();
                    }
                }
        ).executeAsync();
    }

    private List<Album> parseResult(GraphResponse response) {
        List<Album> albumList = new ArrayList<>();
        JSONObject object = response.getJSONObject();
        try {
            JSONArray array = object.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                Album album = new Album();
                album.setId(jsonObject.getString("id"));
                album.setName(jsonObject.getString("name"));
                album.setCreatedTime(jsonObject.getString("created_time"));
                album.setCount(jsonObject.getInt("count"));
                album.setCoverUrl(jsonObject.getJSONObject("picture").getJSONObject("data").getString("url"));
                albumList.add(album);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return albumList;
    }

    private void showList() {
        mProgressBar.setVisibility(View.GONE);
        mAdapter = new AlbumAdapter(getActivity(), mAlbumList);
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
