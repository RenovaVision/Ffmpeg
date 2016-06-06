package com.renovavision.ffmpeg.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

import com.renovavision.ffmpeg.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VideoPlayerActivity extends AppCompatActivity {

    @Bind(R.id.player_view)
    VideoView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ButterKnife.bind(this);

        Uri videoUri = getIntent().getData();

        if (videoUri == null) {
            finish();
        }

        playerView.setVideoURI(videoUri);
        playerView.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        playerView.resume();
    }

    @Override
    public void onPause() {
        playerView.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        playerView.stopPlayback();
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
