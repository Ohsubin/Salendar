package com.example.user.project;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class Video_View extends YouTubeBaseActivity {

    YouTubePlayerView youTubePlayerView;
    Button button;
    YouTubePlayer.OnInitializedListener listener;
    String url;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);


        button = (Button) findViewById(R.id.youtubeButton);
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubeView);

        intent = getIntent();
        url = intent.getStringExtra("youtube");

        listener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(url);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youTubePlayerView.initialize("AIzaSyCVyvZe8QCaKEASrMUbSPWovstaqcpjb-g",listener);
            }
        });

    }
}
