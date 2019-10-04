package com.m8sworld.m8r8.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.m8sworld.m8r8.R;
import com.m8sworld.m8r8.controller.Controller;
import com.m8sworld.m8r8.dashboard.Main2Activity;
import com.m8sworld.m8r8.forgotpssword.ConfirmEmailActivity;
import com.m8sworld.m8r8.reciver.ReciverActivity;
import com.m8sworld.m8r8.signup.SignupActivity;
import com.m8sworld.m8r8.util.CheckInternet;
import com.m8sworld.m8r8.util.ProgressBarClass;
import com.m8sworld.m8r8.util.SharedToken;
import com.m8sworld.m8r8.util.SnackBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements Controller.Login, Controller.getVideo {


    @BindView(R.id.login_email)
    EditText loginEmail;
    @BindView(R.id.login_password)
    EditText loginPassword;
    @BindView(R.id.passwordshow)
    CheckBox passwordshow;
    @BindView(R.id.checkboxSign)
    CheckBox checkboxSign;
    @BindView(R.id.forgot)
    TextView forgot;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.linear_sign)
    LinearLayout linearSign;
    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.videoView)
    VideoView videoView;
    @BindView(R.id.imageVideo)
    ImageView imageView;

    Controller controller;
    ProgressDialog dialog;
    SharedToken sharedToken;
    String video;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        dialog = ProgressBarClass.showProgressDialog(this);
        controller = new Controller((Controller.Login) this, (Controller.getVideo) this);
        sharedToken = new SharedToken(this);


        getDeviceToken();

        if (CheckInternet.isInternetAvailable(this)) {
            dialog.show();
            controller.setGetVideos();
        }

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConfirmEmailActivity.class);
                intent.putExtra("emial", loginEmail.getText().toString());
                startActivity(intent);
            }
        });

        linearSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                intent.putExtra("data", getIntent().getStringExtra("data"));
                startActivity(intent);
                finish();
            }
        });

        passwordshow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    loginPassword.setTransformationMethod(null);
                } else {
                    loginPassword.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

//        MediaController mediaController = new MediaController(this);
//        mediaController.setAnchorView(videoView);
//        videoView.setMediaController(mediaController);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                videoView.setVideoPath("http://m8r8s.m8s.world/public/upload/users/" + video);
                videoView.start();
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(loginEmail.getText().toString())) {
                    loginEmail.setError("Invalid Email");
                } else if (TextUtils.isEmpty(loginPassword.getText().toString())) {
                    loginPassword.setError("Invalid Password");
                } else {
                    if (CheckInternet.isInternetAvailable(LoginActivity.this)) {
                        dialog.show();
                        controller.setLogin(loginEmail.getText().toString(), loginPassword.getText().toString(), token);
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "" + getResources().getString(R.string.noInternet), Snackbar.LENGTH_LONG).show();
                    }

                }

            }
        });


        SharedPreferences shared = getSharedPreferences("check", Context.MODE_PRIVATE);
        String id = shared.getString("hello", "");
        if (id.equals("1")) {
            checkboxSign.setChecked(true);
            loginPassword.setText(shared.getString("pem", ""));
            loginEmail.setText(shared.getString("lem", ""));

        } else {
            checkboxSign.setChecked(false);
            loginPassword.setText("");
            loginEmail.setText("");

        }


    }

    private void getDeviceToken() {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {

                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);

                        Log.d("++++Token", token);
                    }
                });

    }

    @Override
    public void onSucess(Response<LoginApi> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                sharedToken.setUserId(response.body().getData().getUserId().toString());
                if (getIntent().getStringExtra("data").equals("0")) {
                    Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("abc", "1");
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(LoginActivity.this, ReciverActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("abc", "1");
                    intent.putExtra("data", getIntent().getStringExtra("data"));
                    startActivity(intent);
                    finish();
                }

                if (checkboxSign.isChecked()) {
                    SharedPreferences shared = getSharedPreferences("check", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = shared.edit();
                    edit.putString("hello", "1");
                    edit.putString("pem", loginPassword.getText().toString());
                    edit.putString("lem", loginEmail.getText().toString());
                    edit.apply();
                } else {
                    SharedPreferences shared = getSharedPreferences("check", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = shared.edit();
                    edit.putString("hello", "0");
                    edit.apply();
                }
            } else {
                SnackBar.snackbar(LoginActivity.this, response.body().getMessage());
            }
        } else {
            Toast.makeText(this, "" + response.message(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSuccesVideo(Response<GetVideo> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                video = response.body().getData().get(0).getVideo().toString();

            } else {
                Toast.makeText(this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "" + response.message(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void error(String error) {
        SnackBar.snackbar(LoginActivity.this, error);
        dialog.dismiss();
    }

}
