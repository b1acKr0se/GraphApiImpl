package com.framgia.laredolemurs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PhotoActivity extends AppCompatActivity implements OnPhotoClickListener {
    @Bind(R.id.recycler_view) GridRecyclerView mRecyclerView;
    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    private List<Photo> mPhotoList = new ArrayList<>();
    private static final String TAG = "PhotoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("name"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mProgressBar.setVisibility(View.VISIBLE);
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
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/" + id + "/photos",
                    parameters,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            Log.i(TAG, "onCompleted: " + response.getRawResponse());
                            parseResult(response);
                        }
                    }
            ).executeAsync();
        }
    }

    private void parseResult(GraphResponse response) {
        JSONObject object = response.getJSONObject();
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
                mPhotoList.add(photo);
            }
        } catch (JSONException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        mProgressBar.setVisibility(View.GONE);
        PhotoAdapter mAdapter = new PhotoAdapter(this, mPhotoList);
        mAdapter.setOnPhotoClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.scheduleLayoutAnimation();
    }

    @Override
    public void onPhotoClicked(int position) {
        Intent intent = new Intent(this, PreviewActivity.class);
        intent.putExtra("position", position);
        intent.putParcelableArrayListExtra("list", (ArrayList) mPhotoList);
        startActivity(intent);
    }
}
