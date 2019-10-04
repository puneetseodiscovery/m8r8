package com.m8sworld.m8r8.forgotpssword;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.m8sworld.m8r8.R;
import com.m8sworld.m8r8.controller.Controller;
import com.m8sworld.m8r8.login.LoginActivity;
import com.m8sworld.m8r8.util.CheckInternet;
import com.m8sworld.m8r8.util.ProgressBarClass;
import com.m8sworld.m8r8.util.SnackBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class ConfirmEmailActivity extends AppCompatActivity implements Controller.GetForgotOtp, Controller.SendOtpForget, Controller.ResetPassword {

    @BindView(R.id.imglogo)
    ImageView imglogo;
    @BindView(R.id.image)
    RelativeLayout image;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.otp)
    EditText otp;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.c_password)
    EditText cPassword;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.linaer)
    LinearLayout linaer;
    Controller controller;
    ProgressDialog dialog;
    String emailId, Otp, Password, ConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_email);
        ButterKnife.bind(this);
        dialog = ProgressBarClass.showProgressDialog(this);
        controller = new Controller((Controller.GetForgotOtp) this, (Controller.SendOtpForget) this, (Controller.ResetPassword) this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckInternet.isInternetAvailable(ConfirmEmailActivity.this)) {
                    if (button.getText().toString().equals("Send than check your email")) {
                        dialog.show();
                        emailId = email.getText().toString();
                        controller.setGetForgotOtp(emailId);
                    }
                    if (button.getText().toString().equals("Ok")) {
                        dialog.show();
                        Otp = otp.getText().toString();
                        controller.setSendOtpForget(emailId, Otp);
                    }

                    if (button.getText().toString().equals("Set password")) {
                        dialog.show();
                        Password = password.getText().toString();
                        ConfirmPassword = cPassword.getText().toString();
                        controller.setGetResetPassword(emailId, Otp, Password, ConfirmPassword);
                    }

                } else {
                    SnackBar.snackbar(ConfirmEmailActivity.this, getResources().getString(R.string.noInternet));
                }
            }
        });


    }

    //otp get
    @Override
    public void onSuccessOtp(Response<PasswordOTP> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                SnackBar.snackbar(ConfirmEmailActivity.this, response.body().getMessage());

                otp.setVisibility(View.VISIBLE);
                button.setText("Ok");
                email.setVisibility(View.GONE);

            } else {
                SnackBar.snackbar(ConfirmEmailActivity.this, response.body().getMessage());
            }
        } else {
            Toast.makeText(this, "" + response.message(), Toast.LENGTH_LONG).show();
        }
    }

    //verify otp
    @Override
    public void onSuccessSend(Response<PasswordOTP> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                SnackBar.snackbar(ConfirmEmailActivity.this, response.body().getMessage());
                cPassword.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
                otp.setVisibility(View.GONE);

                button.setText("Set password");
            } else {
                SnackBar.snackbar(ConfirmEmailActivity.this, response.body().getMessage());
            }
        } else {
            Toast.makeText(this, "" + response.message(), Toast.LENGTH_LONG).show();
        }
    }

    // reset password
    @Override
    public void onSuccessReset(Response<PasswordReset> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                SnackBar.snackbar(ConfirmEmailActivity.this, response.body().getMessage());
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                SnackBar.snackbar(ConfirmEmailActivity.this, response.body().getMessage());
            }
        } else {
            Toast.makeText(this, "" + response.message(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void error(String error) {
        dialog.dismiss();
        SnackBar.snackbar(ConfirmEmailActivity.this, error);
    }
}
