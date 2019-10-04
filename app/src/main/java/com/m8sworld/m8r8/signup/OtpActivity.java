package com.m8sworld.m8r8.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.m8sworld.m8r8.R;
import com.m8sworld.m8r8.controller.Controller;
import com.m8sworld.m8r8.myprofile.EditProfileActivity;
import com.m8sworld.m8r8.util.CheckInternet;
import com.m8sworld.m8r8.util.ProgressBarClass;
import com.m8sworld.m8r8.util.SharedToken;
import com.m8sworld.m8r8.util.SnackBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class OtpActivity extends AppCompatActivity implements Controller.SignUpOtp {

    @BindView(R.id.imglogo)
    ImageView imglogo;
    @BindView(R.id.Otp)
    EditText Otp;
    @BindView(R.id.btn_send)
    Button btnSend;
    String userId;
    Controller controller;
    SharedToken sharedToken;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);
        controller = new Controller(this);
        sharedToken = new SharedToken(this);
        dialog = ProgressBarClass.showProgressDialog(this);
        userId = getIntent().getStringExtra("UserId");

    }

    @OnClick(R.id.btn_send)
    public void onViewClicked() {
        if (TextUtils.isEmpty(Otp.getText().toString())) {
            Otp.setError("Enter your recovery code");
            Otp.requestFocus();
        } else if (Otp.getText().length() > 4) {
            Otp.setError("Enter only 4 numbers  recovery code");
            Otp.requestFocus();
        } else {
            if (CheckInternet.isInternetAvailable(OtpActivity.this)) {
                dialog.show();
                controller.setSignUpOtp(Otp.getText().toString(), userId);
                dialog.show();
            } else {
                Snackbar.make(findViewById(android.R.id.content), "" + getResources().getString(R.string.noInternet), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onSucess(Response<GetOtpApi> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                sharedToken.setUserId(response.body().getData().getUserId().toString());
                Intent intent = new Intent(this, EditProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("abc", "1");
                intent.putExtra("data", getIntent().getStringExtra("data"));
                startActivity(intent);
                finish();
                SnackBar.snackbar(OtpActivity.this, response.body().getMessage());
            }
        } else {
            Toast.makeText(this, "" + response.message(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void error(String error) {
        dialog.dismiss();
        SnackBar.snackbar(OtpActivity.this, error);

    }
}
