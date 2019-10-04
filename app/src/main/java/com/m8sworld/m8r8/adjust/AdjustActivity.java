package com.m8sworld.m8r8.adjust;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.m8sworld.m8r8.R;
import com.m8sworld.m8r8.controller.Controller;
import com.m8sworld.m8r8.myprofile.MyProfileActivity;
import com.m8sworld.m8r8.nointernet.NoInternetActivity;
import com.m8sworld.m8r8.util.CheckInternet;
import com.m8sworld.m8r8.util.ProgressBarClass;
import com.m8sworld.m8r8.util.SharedToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class AdjustActivity extends AppCompatActivity implements Controller.Adjust {

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
    @BindView(R.id.spinnerComission)
    Spinner spinnerComission;
    @BindView(R.id.edtext)
    EditText edtext;
    @BindView(R.id.spinnerMonth)
    Spinner spinnerMonth;
    @BindView(R.id.btnOk)
    Button button;


    String type, comission, month, offers, invoiceId, videoType;
    Controller controller;
    ProgressDialog dialog;
    SharedToken sharedToken;
    String videoUrl;
    @BindView(R.id.textMessage)
    TextView textMessage;
    @BindView(R.id.reltive)
    RelativeLayout reltive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust);
        ButterKnife.bind(this);
        controller = new Controller(this);
        sharedToken = new SharedToken(this);
        dialog = ProgressBarClass.showProgressDialog(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        toolbartext.setText("Adjust");
        toolbarImage.setVisibility(View.GONE);


        setDiscount();
        setMonth();

        invoiceId = getIntent().getStringExtra("Inid");
        videoUrl = getIntent().getStringExtra("video");
        videoType = getIntent().getStringExtra("type");

        if (videoType.equalsIgnoreCase("t")) {
            textMessage.setText(videoUrl);
            imageVideo.setVisibility(View.GONE);
            textMessage.setVisibility(View.VISIBLE);
            textMessage.setMovementMethod(new ScrollingMovementMethod());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckInternet.isInternetAvailable(AdjustActivity.this)) {

                    if (!offers.equalsIgnoreCase("Select(%)") && !edtext.getText().toString().equals("")) {
                        Toast.makeText(AdjustActivity.this, "Select only on field", Toast.LENGTH_LONG).show();
                    }
                    else if (!TextUtils.isEmpty(edtext.getText().toString())) {
                        type = "f";
                        comission = edtext.getText().toString();
                        dialog.show();
                        controller.setAdjust(invoiceId, sharedToken.getUserId(), type, comission, month);
                    }

                    else if (!offers.equalsIgnoreCase("Select(%)")) {
                        type = "p";
                        comission = offers;
                        dialog.show();
                        controller.setAdjust(invoiceId, sharedToken.getUserId(), type, comission, month);
                    }
                    else {
                        Toast.makeText(AdjustActivity.this, "Enter your commission amount", Toast.LENGTH_LONG).show();
                    }


                } else {
                    startActivity(new Intent(AdjustActivity.this, NoInternetActivity.class));
                }
            }
        });

        imageVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.setVisibility(View.VISIBLE);
                imageVideo.setVisibility(View.INVISIBLE);
                videoView.setVideoPath(videoUrl);
                videoView.start();
            }
        });

    }


    private void setDiscount() {
        String number[] = {"Select(%)", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, number);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerComission.setAdapter(arrayAdapter);
        spinnerComission.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                offers = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setMonth() {
        String number[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, number);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(arrayAdapter);
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onSucessAdjust(Response<AdjustApi> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {

            if (response.body().getStatus() == 200) {
                Toast.makeText(this, "Your counter offer has been sent please wait for a reply", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AdjustActivity.this, MyProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
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
}
