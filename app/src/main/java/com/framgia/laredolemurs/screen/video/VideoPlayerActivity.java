package com.framgia.laredolemurs.screen.video;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.devbrackets.android.exomedia.EMVideoView;
import com.framgia.laredolemurs.R;

public class VideoPlayerActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {
    private EMVideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        mVideoView = (EMVideoView) findViewById(R.id.video_view);
        mVideoView.setOnPreparedListener(this);

        String videoUrl = getIntent().getStringExtra("url");
        if (TextUtils.isEmpty(videoUrl)) {
            finish();
        }
        mVideoView.setVideoURI(Uri.parse(videoUrl));
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mVideoView.start();
    }
}
