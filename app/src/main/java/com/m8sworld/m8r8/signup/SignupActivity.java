package com.m8sworld.m8r8.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.makeramen.roundedimageview.RoundedImageView;
import com.m8sworld.m8r8.R;
import com.m8sworld.m8r8.controller.Controller;
import com.m8sworld.m8r8.login.GetVideo;
import com.m8sworld.m8r8.login.LoginActivity;
import com.m8sworld.m8r8.nointernet.NoInternetActivity;
import com.m8sworld.m8r8.termsandcondition.TermsActivity;
import com.m8sworld.m8r8.util.CheckInternet;
import com.m8sworld.m8r8.util.ProgressBarClass;
import com.m8sworld.m8r8.util.SnackBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity implements Controller.Signup, Controller.getVideo {

    @BindView(R.id.firstname)
    EditText firstname;
    @BindView(R.id.lastname)
    EditText lastname;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.contact)
    EditText contact;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.password_check)
    CheckBox passwordCheck;
    @BindView(R.id.c_password)
    EditText cPassword;
    @BindView(R.id.signUpFace)
    LoginButton facebooklogin;
    @BindView(R.id.facebookSign)
    RoundedImageView facebookSign;
    @BindView(R.id.radio)
    RadioButton radio;
    @BindView(R.id.linaer)
    LinearLayout linaer;
    @BindView(R.id.login_linear)
    LinearLayout loginLinear;
    @BindView(R.id.sign_btn)
    Button signBtn;
    @BindView(R.id.linear2)
    LinearLayout linear2;

    Controller controller;
    CallbackManager callbackManager;
    String token, video;
    ProgressDialog dialog;

    @BindView(R.id.imageVideo)
    ImageView imageVideo;
    @BindView(R.id.videoView)
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        controller = new Controller((Controller.Signup) this, (Controller.getVideo) this);
        callbackManager = CallbackManager.Factory.create();
        dialog = ProgressBarClass.showProgressDialog(this);


        if (CheckInternet.isInternetAvailable(this)) {
            dialog.show();
            controller.setGetVideos();
        } else {
            SnackBar.snackbar(SignupActivity.this, getResources().getString(R.string.noInternet));
        }

        getHashKey();
        termsAndCondition();
        getDeviceToken();

        imageVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.setVisibility(View.VISIBLE);
                imageVideo.setVisibility(View.INVISIBLE);
                videoView.setVideoPath("http://m8r8s.m8s.world/public/upload/users/" + video);
                videoView.start();
            }
        });

        loginLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        facebooklogin.setReadPermissions(Arrays.asList("email", "public_profile"));

        FacebookLogin();

        facebookSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckInternet.isInternetAvailable(getApplicationContext())) {
                    facebooklogin.performClick();
                } else {
                    Toast.makeText(SignupActivity.this, "" + getResources().getString(R.string.noInternet), Toast.LENGTH_LONG).show();
                }
            }
        });

        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(firstname.getText().toString()) || TextUtils.isEmpty(lastname.getText().toString()) || TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(password.getText().toString()) || TextUtils.isEmpty(cPassword.getText().toString()) || TextUtils.isEmpty(contact.getText().toString())) {
                    error(firstname);
                    error(lastname);
                    error(email);
                    error(password);
                    error(contact);
                    error(cPassword);
                } else if (!radio.isChecked()) {
                    Toast.makeText(SignupActivity.this, "Accept the term and condition", Toast.LENGTH_LONG).show();
                } else if (!password.getText().toString().equals(cPassword.getText().toString())) {
                    cPassword.setError("Password not match");
                } else {
                    if (CheckInternet.isInternetAvailable(SignupActivity.this)) {
                        dialog.show();
                        controller.setSignup(firstname.getText().toString(), lastname.getText().toString(), email.getText().toString(), password.getText().toString(), contact.getText().toString(), cPassword.getText().toString(), token);
                    } else {
                        startActivity(new Intent(SignupActivity.this, NoInternetActivity.class));
                    }
                }
            }
        });

        passwordCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    password.setTransformationMethod(null);
                    cPassword.setTransformationMethod(null);
                } else {
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    cPassword.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

    }

    private void error(EditText editText) {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            editText.setError("Invalid Data");
            editText.requestFocus();
        }
    }

    @Override
    public void onSucess(Response<RegisterApi> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus().equals(200)) {
                Snackbar.make(findViewById(android.R.id.content), "" + response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), OtpActivity.class);
                intent.putExtra("UserId", response.body().getData().getUserId().toString());
                intent.putExtra("data", getIntent().getStringExtra("data"));
                startActivity(intent);
                finish();
            } else {
                SnackBar.snackbar(SignupActivity.this, response.body().getMessage());
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

            }
        } else {
            Toast.makeText(this, "" + response.message(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void error(String error) {
        dialog.dismiss();
        SnackBar.snackbar(SignupActivity.this, error);
    }


    public void FacebookLogin() {
        facebooklogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                boolean loggedin = AccessToken.getCurrentAccessToken() == null;

                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken()
                        , new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                if (AccessToken.getCurrentAccessToken() != null) {

                                    try {
                                        firstname.setText(object.getString("first_name"));
                                        lastname.setText(object.getString("last_name"));
                                        email.setText(object.getString("email"));
                                        String id = object.getString("id");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name,last_name,email,id");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }


    // terms and condition
    private void termsAndCondition() {
        SpannableString ss = new SpannableString(getResources().getString(R.string.iagree));
        //click for terms and condtion
        ClickableSpan Terms = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(SignupActivity.this, TermsActivity.class);
                intent.putExtra("terms", "Terms & Condition");
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };


        //privecy
        ClickableSpan privecy = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(SignupActivity.this, TermsActivity.class);
                intent.putExtra("terms", "Privacy policy");
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);

            }
        };

        //art
        ClickableSpan art = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(SignupActivity.this, TermsActivity.class);
                intent.putExtra("terms", "Art & Statement");
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };

        ss.setSpan(Terms, 18, 38, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(privecy, 40, 54, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(art, 59, 72, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        radio.setText(ss, TextView.BufferType.SPANNABLE);
        radio.setHighlightColor(Color.TRANSPARENT);
        radio.setText(ss);
        radio.setMovementMethod(LinkMovementMethod.getInstance());
    }


    public void getHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.m8sworld.m8r8",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

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

                        Log.d("++++Token", token);
                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);

                    }
                });

    }
}
