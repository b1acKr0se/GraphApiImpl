package com.framgia.laredolemurs.screen.post;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.framgia.laredolemurs.R;
import com.framgia.laredolemurs.data.model.Post;
import com.framgia.laredolemurs.widget.SimpleDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment implements OnPostClickListener {
    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    private List<Post> posts = new ArrayList<>();

    public PostFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstace() {
        return new PostFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        ButterKnife.bind(this, view);
        retrievePost();
        return view;
    }

    private void retrievePost() {
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/laredolemurs/feed",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        posts = parseResult(response);
                        showList();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "message,story,created_time,type,full_picture,source,link");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void showList() {
        mProgressBar.setVisibility(View.GONE);
        PostAdapter adapter = new PostAdapter(getActivity(), posts);
        adapter.setOnPostClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
    }

    private List<Post> parseResult(GraphResponse response) {
        List<Post> list = new ArrayList<>();
        JSONObject object = response.getJSONObject();
        try {
            JSONArray array = object.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                Post post = new Post();
                if (jsonObject.has("message")) post.setMessage(jsonObject.getString("message"));
                if (jsonObject.has("story")) post.setStory(jsonObject.getString("story"));
                post.setCreatedTime(jsonObject.getString("created_time"));
                post.setType(getPostType(jsonObject.getString("type")));
                if (jsonObject.has("full_picture"))
                    post.setPicture(jsonObject.getString("full_picture"));
                if (jsonObject.has("source")) post.setSource(jsonObject.getString("source"));
                if (jsonObject.has("link")) post.setLink(jsonObject.getString("link"));
                post.setId(jsonObject.getString("id"));
                list.add(post);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Post.Type getPostType(String input) {
        switch (input) {
            case "video":
                return Post.Type.VIDEO;
            case "photo":
                return Post.Type.PHOTO;
            case "status":
                return Post.Type.STATUS;
            case "link":
                return Post.Type.LINK;
            case "offer":
                return Post.Type.OFFER;
            case "event":
                return Post.Type.EVENT;
        }
        return Post.Type.STATUS;
    }

    @Override public void onPostClick(int position) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(posts.get(position).getLink()));
        startActivity(browserIntent);
    }
}
