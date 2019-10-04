package com.m8sworld.m8r8.videoupload;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.m8sworld.m8r8.R;
import com.m8sworld.m8r8.controller.Controller;
import com.m8sworld.m8r8.dashboard.Main2Activity;
import com.m8sworld.m8r8.mandate.MandateActivity;
import com.m8sworld.m8r8.nointernet.NoInternetActivity;
import com.m8sworld.m8r8.util.CheckInternet;
import com.m8sworld.m8r8.util.ProgressBarClass;
import com.m8sworld.m8r8.util.SharedToken;
import com.m8sworld.m8r8.util.SnackBar;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class UploadVideoActivity extends AppCompatActivity implements Controller.AddInvoiceText, Controller.AddInvoiceVideo {

    @BindView(R.id.tooolbar)
    Toolbar tooolbar;
    @BindView(R.id.videoUpload)
    RoundedImageView videoUpload;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.toolbarImage)
    ImageView toolbarImage;
    @BindView(R.id.txt)
    TextView txt;
    @BindView(R.id.toolbartext)
    TextView textView;

    String p_price, p_currency;
    @BindView(R.id.btnNext)
    Button btnNext;

    SharedToken sharedToken;
    Controller controller;
    ProgressDialog dialog;
    String type, text;

    MultipartBody.Part videoPart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        ButterKnife.bind(this);
        dialog = ProgressBarClass.showProgressDialog(this);
        sharedToken = new SharedToken(this);
        controller = new Controller((Controller.AddInvoiceText) this, (Controller.AddInvoiceVideo) this);

        p_price = getIntent().getStringExtra("price");
        p_currency = getIntent().getStringExtra("currency");

        setSupportActionBar(tooolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarImage.setVisibility(View.GONE);
        textView.setText("Create agreement ");


        hasCamera();

        videoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(android.provider.MediaStore.EXTRA_VIDEO_QUALITY, -1);
                startActivityForResult(intent, 200);

            }
        });


    }


    @OnClick(R.id.btnNext)
    public void onViewClicked() {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            Toast.makeText(this, "Enter the text field ", Toast.LENGTH_LONG).show();
        } else if (editText.getText().toString().length() > 500) {
            Toast.makeText(this, "Enter max 500 character ", Toast.LENGTH_LONG).show();
        } else {
//            if (!TextUtils.isEmpty(editText.getText().toString())) {
            type = "t";
            text = editText.getText().toString();
            if (CheckInternet.isInternetAvailable(this)) {
                dialog.show();
                controller.setAddInvoiceText(sharedToken.getUserId(), p_currency, p_price, text, type, Main2Activity.part);
            } else {
                startActivity(new Intent(UploadVideoActivity.this, NoInternetActivity.class));
            }
//            } else if (videoPart != null) {
//                type = "v";
//                if (CheckInternet.isInternetAvailable(this)) {
//                    dialog.show();
//                    controller.setAddInvoiceVideo(sharedToken.getUserId(), p_currency, p_price, type, Main2Activity.part, videoPart);
//                } else {
//                    startActivity(new Intent(UploadVideoActivity.this, NoInternetActivity.class));
//                }
//            }
//            else {
//                Toast.makeText(this, "You can select only one", Toast.LENGTH_LONG).show();
//            }
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                File videoFile = new File(getRealPathFromURIPath(uri));
                RequestBody videoBody = RequestBody.create(MediaType.parse("*/*"), videoFile);
                videoPart = MultipartBody.Part.createFormData("video_msg", videoFile.getName(), videoBody);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancel", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean hasCamera() {
        return (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FRONT));
    }


    @Override
    public void onSucessText(Response<InvoiceApis> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                Intent intent = new Intent(UploadVideoActivity.this, MandateActivity.class);
                intent.putExtra("Inid", response.body().getData().getId().toString());
                startActivity(intent);
            } else {
                Toast.makeText(this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "" + response.message(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSucessVideo(Response<InvoiceApis> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                Intent intent = new Intent(UploadVideoActivity.this, MandateActivity.class);
                intent.putExtra("Inid", response.body().getData().getId().toString());
                startActivity(intent);
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
        Log.d("+++++error", error);
        SnackBar.snackbar(UploadVideoActivity.this, error);
    }


    private String getRealPathFromURIPath(Uri contentURI) {
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


}
