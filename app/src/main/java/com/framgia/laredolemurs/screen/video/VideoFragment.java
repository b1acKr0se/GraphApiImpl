package com.framgia.laredolemurs.screen.video;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.framgia.laredolemurs.R;
import com.framgia.laredolemurs.util.DateDifferenceConverter;
import com.framgia.laredolemurs.widget.SimpleDividerItemDecoration;
import com.framgia.laredolemurs.data.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;


public class VideoFragment extends Fragment implements OnVideoClickListener {
    private List<Video> mVideos = new ArrayList<>();
    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    private VideoAdapter mAdapter;

    public VideoFragment() {
    }

    public static Fragment newInstance() {
        return new VideoFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        ButterKnife.bind(this, view);
        mProgressBar.setVisibility(View.VISIBLE);
        retrieveVideos();
        return view;
    }

    private void retrieveVideos() {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "source,description,length,picture,title");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/LaredoLemurs/videos",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        parseResult(response);
                    }
                }
        ).executeAsync();
    }

    private void parseResult(GraphResponse response) {
        if (response != null) {
            JSONObject object = response.getJSONObject();
            try {
                JSONArray array = object.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    Video video = new Video();
                    video.setId(jsonObject.getString("id"));
                    video.setSource(jsonObject.getString("source"));
                    video.setPicture(jsonObject.getString("picture"));
                    video.setDuration(DateDifferenceConverter.formatDuration(jsonObject.getDouble("length")));
                    if (jsonObject.has("title"))
                        video.setTitle(jsonObject.getString("title"));
                    if (jsonObject.has("description"))
                        video.setDescription(jsonObject.getString("description"));
                    mVideos.add(video);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        showVideos();
    }


    private void showVideos() {
        mProgressBar.setVisibility(View.GONE);
        mAdapter = new VideoAdapter(getActivity(), mVideos);
        mAdapter.setOnVideoClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity(), null));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onVideoClicked(int position) {
        Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
        intent.putExtra("url", mVideos.get(position).getSource());
        startActivity(intent);
    }
}
