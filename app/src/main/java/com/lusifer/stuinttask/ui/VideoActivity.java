package com.lusifer.stuinttask.ui;

import com.lusifer.stuinttask.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoActivity extends AppCompatActivity {

    @BindView(R.id.vvVideo)
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        ButterKnife.bind(this);

        Intent intent=getIntent();
        if(intent.getStringExtra(MainActivity.VIDEO_PATH)==null){
            Toast.makeText(this,"Error in file",Toast.LENGTH_LONG).show();
            finish();
        }
        Uri builtUri = Uri.parse(intent.getStringExtra(MainActivity.VIDEO_PATH)).buildUpon().build();
        videoView.setVideoURI(builtUri);
        videoView.setMediaController(new MediaController(this));
        videoView.start();
    }
}
