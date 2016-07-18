package com.framgia.laredolemurs.screen.post;


import android.content.Intent;
import android.net.Uri;
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
import com.framgia.laredolemurs.R;
import com.framgia.laredolemurs.data.model.Post;
import com.framgia.laredolemurs.util.EndlessRecyclerOnScrollListener;

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
public class PostFragment extends Fragment implements OnPostClickListener, GraphRequest.Callback {
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    private List<Post> posts = new ArrayList<>();
    private PostAdapter adapter;
    private EndlessRecyclerOnScrollListener scrollListener;
    private GraphRequest graphRequest;


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
        adapter = new PostAdapter(getActivity(), posts);
        adapter.setOnPostClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        scrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                if (graphRequest != null) {
                    posts.add(null);
                    adapter.notifyItemInserted(posts.size() - 1);
                    graphRequest.setCallback(PostFragment.this);
                    graphRequest.executeAsync();
                }
            }
        };
        mRecyclerView.addOnScrollListener(scrollListener);
        mRecyclerView.setAdapter(adapter);
        retrievePost();
        return view;
    }

    private void retrievePost() {
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/laredolemurs/feed", this);
        Bundle parameters = new Bundle();
        parameters.putString("fields", "message,story,created_time,type,full_picture,source,link");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void showList(List<Post> postList) {
        mProgressBar.setVisibility(View.GONE);
        if (posts.size() > 0) posts.remove(posts.size() - 1);
        scrollListener.setLoaded();
        if (postList == null)
            return;
        posts.addAll(postList);
        adapter.notifyDataSetChanged();
    }

    private List<Post> parseResult(GraphResponse response) {
        graphRequest = response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
        List<Post> list = new ArrayList<>();
        JSONObject object = response.getJSONObject();
        if (object == null) return null;
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

    @Override
    public void onPostClick(int position) {
        if (posts.get(position).getLink() != null) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(posts.get(position).getLink()));
            startActivity(browserIntent);
        }
    }

    @Override
    public void onCompleted(GraphResponse response) {
        if (response != null) {
            showList(parseResult(response));
        }
    }
}
