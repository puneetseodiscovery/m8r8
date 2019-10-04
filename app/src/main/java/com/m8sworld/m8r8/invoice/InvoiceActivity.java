package com.m8sworld.m8r8.invoice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.m8sworld.m8r8.R;
import com.m8sworld.m8r8.controller.Controller;
import com.m8sworld.m8r8.nointernet.NoInternetActivity;
import com.m8sworld.m8r8.util.CheckInternet;
import com.m8sworld.m8r8.util.ProgressBarClass;
import com.m8sworld.m8r8.util.SharedToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class InvoiceActivity extends AppCompatActivity implements Controller.InvoiceImage {

    @BindView(R.id.tooolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarImage)
    ImageView toolbarImage;
    @BindView(R.id.toolbartext)
    TextView toolbartext;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.avi)
    ProgressBar progressBar;

    Controller controller;
    SharedToken sharedToken;
    ProgressDialog dialog;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        ButterKnife.bind(this);
        controller = new Controller(this);
        dialog = ProgressBarClass.showProgressDialog(this);

        id = getIntent().getStringExtra("id");

        setSupportActionBar(toolbar);
        toolbarImage.setVisibility(View.GONE);
        toolbartext.setText("View invoice");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        if (CheckInternet.isInternetAvailable(this)) {
            dialog.show();
            controller.setInvoiceImage(id);
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
    public void onSucess(Response<InvoiceApi> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                Glide.with(this).load("http://m8r8.amrdev.site/upload/invoices/image/" + response.body().getData().get(0).getBillImage()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(imageView);
            } else {
                Toast.makeText(this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "" + response.message(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void error(String error) {
        dialog.dismiss();
        Toast.makeText(this, "" + error, Toast.LENGTH_LONG).show();
    }
}
