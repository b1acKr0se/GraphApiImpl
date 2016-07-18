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
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.framgia.laredolemurs.R;
import com.framgia.laredolemurs.data.model.Video;
import com.framgia.laredolemurs.screen.photo.PhotoActivity;
import com.framgia.laredolemurs.screen.photo.PhotoAdapter;
import com.framgia.laredolemurs.util.DateDifferenceConverter;
import com.framgia.laredolemurs.util.EndlessRecyclerOnScrollListener;
import com.framgia.laredolemurs.widget.SimpleDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class VideoFragment extends Fragment implements OnVideoClickListener, GraphRequest.Callback {
    private List<Video> mVideos = new ArrayList<>();
    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    private VideoAdapter mAdapter;
    private GraphRequest graphRequest;
    private EndlessRecyclerOnScrollListener scrollListener;

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
        setupRecyclerView();
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
                HttpMethod.GET, this).executeAsync();
    }

    private void parseResult(GraphResponse response) {
        JSONObject object = response.getJSONObject();
        graphRequest = response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
        List<Video> list = new ArrayList<>();
        try {
            if (object == null) {
                showList(list);
                return;
            }
            JSONArray array = object.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                Video video = new Video();
                video.setId(jsonObject.getString("id"));
                if (jsonObject.has("source"))
                    video.setSource(jsonObject.getString("source"));
                video.setPicture(jsonObject.getString("picture"));
                video.setDuration(DateDifferenceConverter.formatDuration(jsonObject.getDouble("length")));
                if (jsonObject.has("title"))
                    video.setTitle(jsonObject.getString("title"));
                if (jsonObject.has("description"))
                    video.setDescription(jsonObject.getString("description"));
                list.add(video);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showList(list);
    }


    private void setupRecyclerView() {
        mAdapter = new VideoAdapter(getActivity(), mVideos);
        mAdapter.setOnVideoClickListener(this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        scrollListener = new EndlessRecyclerOnScrollListener(manager) {
            @Override
            public void onLoadMore() {
                if (graphRequest != null) {
                    mVideos.add(null);
                    mAdapter.notifyItemInserted(mVideos.size() - 1);
                    graphRequest.setCallback(VideoFragment.this);
                    graphRequest.executeAsync();
                }
            }
        };
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addOnScrollListener(scrollListener);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity(), null));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void showList(List<Video> list) {
        mProgressBar.setVisibility(View.GONE);
        if (mVideos.size() > 0) mVideos.remove(mVideos.size() - 1);
        scrollListener.setLoaded();
        if (list == null)
            return;
        mVideos.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onVideoClicked(int position) {
        if (mVideos.get(position).getSource() != null) {
            Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
            intent.putExtra("url", mVideos.get(position).getSource());
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "Cannot open this video", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCompleted(GraphResponse response) {
        if (response != null)
            parseResult(response);
    }
}
