package com.m8sworld.m8r8.watchvideo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.m8sworld.m8r8.R;
import com.m8sworld.m8r8.controller.Controller;
import com.m8sworld.m8r8.login.GetVideo;
import com.m8sworld.m8r8.nointernet.NoInternetActivity;
import com.m8sworld.m8r8.util.CheckInternet;
import com.m8sworld.m8r8.util.ProgressBarClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class WatchVideoActivity extends AppCompatActivity implements Controller.getVideo {

    @BindView(R.id.tooolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarImage)
    ImageView toolbarImage;
    @BindView(R.id.toolbartext)
    TextView toolbartext;
    @BindView(R.id.videoView)
    VideoView videoView;

    Controller controller;
    ProgressDialog dialog;
    String video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_video);
        ButterKnife.bind(this);
        controller = new Controller(this);
        dialog = ProgressBarClass.showProgressDialog(this);


        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarImage.setVisibility(View.GONE);
        toolbartext.setText("See Video How to Do");

        if (CheckInternet.isInternetAvailable(this)) {
            dialog.show();
            controller.setGetVideos();
        } else {
            startActivity(new Intent(this, NoInternetActivity.class));
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onSuccesVideo(Response<GetVideo> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                video = response.body().getData().get(0).getVideo();
                MediaController mediaController = new MediaController(this);
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
                videoView.setVideoPath("http://m8r8.amrdev.site/public/upload/users/" + video);
                videoView.start();

            } else {
                Toast.makeText(this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "" + response.message(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void error(String error) {

    }
}
