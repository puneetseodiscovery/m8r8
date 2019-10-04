package com.m8sworld.m8r8.agreement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.m8sworld.m8r8.R;
import com.m8sworld.m8r8.controller.Controller;
import com.m8sworld.m8r8.invoice.InvoiceActivity;
import com.m8sworld.m8r8.mycontract.MyContractsActivity;
import com.m8sworld.m8r8.nointernet.NoInternetActivity;
import com.m8sworld.m8r8.util.CheckInternet;
import com.m8sworld.m8r8.util.Config;
import com.m8sworld.m8r8.util.ProgressBarClass;
import com.m8sworld.m8r8.util.SharedToken;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class AgreeMentActivity extends AppCompatActivity implements Controller.Agreement {

    @BindView(R.id.tooolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarImage)
    ImageView toolbarImage;
    @BindView(R.id.toolbartext)
    TextView toolbartext;
    @BindView(R.id.imageVideo)
    ImageView imageVideo;
    @BindView(R.id.videoView)
    VideoView videoView;
    @BindView(R.id.textMessage)
    TextView textMessage;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.txtBorrow)
    TextView txtBorrow;
    @BindView(R.id.signBorrow)
    ImageView signBorrow;
    @BindView(R.id.imageBorrow)
    RoundedImageView imageBorrow;
    @BindView(R.id.relativeBorrow)
    RelativeLayout relativeBorrow;
    @BindView(R.id.txtLander)
    TextView txtLander;
    @BindView(R.id.signLander)
    ImageView signLander;
    @BindView(R.id.imageLander)
    RoundedImageView imageLander;
    @BindView(R.id.relativeLender)
    RelativeLayout relativeLender;
    @BindView(R.id.btnView)
    Button btnView;


    Controller controller;
    ProgressDialog dialog;
    String id, videoUrl;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.relativeVideo)
    RelativeLayout relativeVideo;
    SharedToken sharedToken;
    @BindView(R.id.dateBorrow)
    TextView dateBorrow;
    @BindView(R.id.dateLander)
    TextView dateLander;
    @BindView(R.id.imageSing)
    ImageView imageView;

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree_ment);
        ButterKnife.bind(this);
        controller = new Controller(this);
        sharedToken = new SharedToken(this);
        dialog = ProgressBarClass.showProgressDialog(this);


        id = getIntent().getStringExtra("Inid");
        type = getIntent().getStringExtra("type");


        setSupportActionBar(toolbar);
        toolbarImage.setVisibility(View.GONE);
        toolbartext.setText("My orignal offer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        imageVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.setVisibility(View.VISIBLE);
                imageVideo.setVisibility(View.INVISIBLE);
                // Set progressbar message

                // Show progressbar
                avi.setVisibility(View.VISIBLE);
                relativeVideo.setEnabled(false);

                try {
                    // Start the MediaController
                    MediaController mediacontroller = new MediaController(AgreeMentActivity.this);
                    mediacontroller.setAnchorView(videoView);
                    Uri videoUri = Uri.parse(videoUrl);
                    videoView.setMediaController(mediacontroller);
                    videoView.setVideoURI(videoUri);

                } catch (Exception e) {

                    e.printStackTrace();
                }

                videoView.requestFocus();


                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    // Close the progress bar and play the video
                    public void onPrepared(MediaPlayer mp) {
                        videoView.start();
                        avi.setVisibility(View.GONE);
                    }
                });


                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    public void onCompletion(MediaPlayer mp) {
                        videoView.setVisibility(View.INVISIBLE);
                        imageVideo.setVisibility(View.VISIBLE);
                    }
                });
            }
        });


        if (CheckInternet.isInternetAvailable(this)) {
            if (TextUtils.isEmpty(id)) {
                Toast.makeText(this, "Invalid Id", Toast.LENGTH_LONG).show();
            } else {
                dialog.show();
                controller.setAgreement(id);
            }

        } else {
            startActivity(new Intent(this, NoInternetActivity.class));
        }


    }

    @OnClick(R.id.btnView)
    public void onViewClicked() {
//        if (type.equalsIgnoreCase("m")) {
//            Intent intent = new Intent(this, MyProfileActivity.class);
//            startActivity(intent);
        Intent intent = new Intent(this, InvoiceActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
//        } else {
//            onBackPressed();
//        }

    }

    @Override
    public void onSucessA(Response<AgreeMentApi> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                AgreeMentApi.Data data = response.body().getData();

                textView.setText(Html.fromHtml(data.getRequestContent()));
                if (data.getMessageType().equalsIgnoreCase("t")) {
                    imageVideo.setVisibility(View.GONE);
                    textMessage.setVisibility(View.VISIBLE);
                    textMessage.setMovementMethod(new ScrollingMovementMethod());
                    textMessage.setText(data.getMessage());
                } else {
                    videoUrl = Config.INVOICE_VIDEO_URL + data.getMessage();

                }

                Glide.with(this).load(Config.PROFILE_IMAGE_URL + data.getUsersData().getBorrowers().getProfileImage()).dontTransform().dontTransform().into(imageBorrow);
                txtBorrow.setText(getError(data.getUsersData().getBorrowers().getBorrowersName()) + "\n" + getError(data.getUsersData().getBorrowers().getAddress1()) + "\n" + getError(data.getUsersData().getBorrowers().getAddress2()) + "\n"
                        + getError(data.getUsersData().getBorrowers().getPostCode()) + "\n" + getError(data.getUsersData().getBorrowers().getCountry()));
                dateBorrow.setText("Date:" + data.getUsersData().getBorrowers().getCreatedAt());
                Glide.with(this).load(Config.INVOICE_SIGN_URL + data.getUsersData().getBorrowers().getAgreementSign()).dontTransform().dontAnimate().into(signBorrow);
                Glide.with(this).load(Config.INVOICE_SIGN_URL + data.getUsersData().getBorrowers().getAgreementSign()).dontTransform().dontAnimate().into(imageView);


                if (data.getUsersData().getLenders() == null) {
                    relativeLender.setVisibility(View.INVISIBLE);
                } else {
                    Glide.with(this).load(Config.PROFILE_IMAGE_URL + data.getUsersData().getLenders().getProfileImage()).dontTransform().dontTransform().into(imageLander);
                    dateLander.setText("Date:" + data.getUsersData().getLenders().getCreatedAt());
                    txtLander.setText(getError(getError(data.getUsersData().getLenders().getLendersName()) + "\n" + data.getUsersData().getLenders().getAddress1()) + "\n" + getError(data.getUsersData().getLenders().getAddress2()) + "\n"
                            + getError(data.getUsersData().getLenders().getPostCode()) + "\n" + getError(data.getUsersData().getLenders().getCountry()));
                    Glide.with(this).load(Config.INVOICE_SIGN_URL + data.getUsersData().getLenders().getAgreementSign()).dontTransform().dontAnimate().into(signLander);

                }


            } else {
                Toast.makeText(this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "" + response.message(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void error(String error) {
        dialog.dismiss();
        Toast.makeText(this, "" + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public String getError(String message) {
        if (message == null) {
            message = "";
            return message;
        } else {
            return message;
        }

    }


    @Override
    public void onBackPressed() {
        if (type.equalsIgnoreCase("m")) {
            Intent intent = new Intent(this, MyContractsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }

    }
}
