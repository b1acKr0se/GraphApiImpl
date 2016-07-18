package com.framgia.laredolemurs.screen.photo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.framgia.laredolemurs.R;
import com.framgia.laredolemurs.data.model.Photo;
import com.framgia.laredolemurs.util.EndlessRecyclerOnScrollListener;
import com.framgia.laredolemurs.widget.GridRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PhotoActivity extends AppCompatActivity implements OnPhotoClickListener, GraphRequest.Callback {
    @Bind(R.id.recycler_view)
    GridRecyclerView mRecyclerView;
    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    private ArrayList<Photo> mPhotoList = new ArrayList<>();
    private static final String TAG = "PhotoActivity";
    private GraphRequest graphRequest;
    private PhotoAdapter mAdapter;
    private EndlessRecyclerOnScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("name"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mProgressBar.setVisibility(View.VISIBLE);
        setUpRecyclerView();
        requestPhotos();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestPhotos() {
        String id = getIntent().getStringExtra("id");
        if (id != null) {
            Bundle parameters = new Bundle();
            parameters.putString("fields", "images,name");
            parameters.putString("limit", "25");
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/" + id + "/photos",
                    parameters,
                    HttpMethod.GET, this).executeAsync();
        }
    }

    private void parseResult(GraphResponse response) {
        JSONObject object = response.getJSONObject();
        graphRequest = response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
        List<Photo> list = new ArrayList<>();
        try {
            JSONArray array = object.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                Photo photo = new Photo();
                photo.setId(jsonObject.getString("id"));
                if (jsonObject.has("name"))
                    photo.setCaption(jsonObject.getString("name"));
                JSONArray images = jsonObject.getJSONArray("images");
                photo.setSourceUrl(images.getJSONObject(0).getString("source"));
                photo.setSmallUrl(images.getJSONObject(images.length() - 1).getString("source"));
                list.add(photo);
            }
        } catch (JSONException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        showPhoto(list);
    }

    private void setUpRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        mAdapter = new PhotoAdapter(this, mPhotoList);
        mAdapter.setOnPhotoClickListener(this);
        scrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                if (graphRequest != null) {
                    mPhotoList.add(null);
                    mAdapter.notifyItemInserted(mPhotoList.size() - 1);
                    graphRequest.setCallback(PhotoActivity.this);
                    graphRequest.executeAsync();
                }
            }
        };
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(scrollListener);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void showPhoto(List<Photo> list) {
        mProgressBar.setVisibility(View.GONE);
        if (mPhotoList.size() > 0) mPhotoList.remove(mPhotoList.size() - 1);
        else mRecyclerView.scheduleLayoutAnimation();
        scrollListener.setLoaded();
        if (list == null)
            return;
        mPhotoList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPhotoClicked(int position) {
        Intent intent = new Intent(this, PreviewActivity.class);
        intent.putExtra("position", position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", mPhotoList);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onCompleted(GraphResponse response) {
        if (response != null)
            parseResult(response);
    }
}
