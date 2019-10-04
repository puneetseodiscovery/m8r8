package com.m8sworld.m8r8.reciver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.m8sworld.m8r8.MessageApis;
import com.m8sworld.m8r8.R;
import com.m8sworld.m8r8.SplashActivity;
import com.m8sworld.m8r8.adjust.AdjustActivity;
import com.m8sworld.m8r8.agreement.AgreeMentActivity;
import com.m8sworld.m8r8.agreement.AgreeMentApi;
import com.m8sworld.m8r8.controller.Controller;
import com.m8sworld.m8r8.login.LoginActivity;
import com.m8sworld.m8r8.myprofile.MyProfileActivity;
import com.m8sworld.m8r8.nointernet.NoInternetActivity;
import com.m8sworld.m8r8.util.CheckInternet;
import com.m8sworld.m8r8.util.Config;
import com.m8sworld.m8r8.util.MultiPart;
import com.m8sworld.m8r8.util.ProgressBarClass;
import com.m8sworld.m8r8.util.SharedToken;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;

import java.io.IOException;
import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import retrofit2.Response;

public class ReciverActivity extends AppCompatActivity implements Controller.ReciverData, Controller.ChooseOne, Controller.ChooseAccept, Controller.Agreement, Controller.UpdateTranstion {

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
    @BindView(R.id.txtView)
    TextView txtView;
    @BindView(R.id.agree)
    ImageView agree;
    @BindView(R.id.adjust)
    ImageView adjust;
    @BindView(R.id.refuse)
    ImageView refuse;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.relativeVideo)
    RelativeLayout relativeVideo;
    @BindView(R.id.imageSign)
    ImageView imageView;


    SharedToken sharedToken;
    Controller controller;
    ProgressDialog dialog;


    @BindView(R.id.textMessage)
    TextView textMessage;
    @BindView(R.id.txtReadAgreement)
    TextView txtReadAgreement;
    @BindView(R.id.signature_pad)
    SignaturePad signaturePad;
    @BindView(R.id.clear_sign)
    Button clearSign;
    @BindView(R.id.relative)
    RelativeLayout relative;
    @BindView(R.id.mandata_ok)
    Button mandataOk;

    MultipartBody.Part part;
    String money;
    String transctionI;
    String invoiceId, code, videoUrl, type = "2", VideoType;


    //paypal
    public static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration configuration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(Config.PAYPAL_CLIENT_ID);


    String amount = "5";
    String data = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciver);
        ButterKnife.bind(this);
        sharedToken = new SharedToken(this);
        controller = new Controller((Controller.ReciverData) this, (Controller.ChooseOne) this, (Controller.ChooseAccept) this, (Controller.Agreement) this, (Controller.UpdateTranstion) this);
        dialog = ProgressBarClass.showProgressDialog(this);

        setSupportActionBar(toolbar);
        toolbartext.setText("Approve, adjust or refuse");
        invoiceId = getIntent().getStringExtra("Inid");

        agree.setEnabled(false);
        refuse.setEnabled(false);
        adjust.setEnabled(false);

        // start payment Gateway
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        startService(intent);


        if (TextUtils.isEmpty(invoiceId)) {
            DeepLink();
        } else {
            if (CheckInternet.isInternetAvailable(this)) {
                dialog.show();
                controller.setReciverData(invoiceId);
            } else {
                startActivity(new Intent(this, NoInternetActivity.class));
            }
        }


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
                    MediaController mediacontroller = new MediaController(ReciverActivity.this);
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

        adjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckInternet.isInternetAvailable(ReciverActivity.this)) {
                    dialog.show();
                    type = "2";
                    controller.setChooseOne(invoiceId, sharedToken.getUserId(), type);
                } else {
                    startActivity(new Intent(ReciverActivity.this, NoInternetActivity.class));
                }
            }
        });

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (money.equals("add")) {
                    PaymentProgress();
                } else {
                    relative.setVisibility(View.VISIBLE);
                    mandataOk.setVisibility(View.VISIBLE);

                }
            }
        });

        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckInternet.isInternetAvailable(ReciverActivity.this)) {
                    dialog.show();
                    type = "3";
                    controller.setChooseOne(invoiceId, sharedToken.getUserId(), type);
                } else {
                    startActivity(new Intent(ReciverActivity.this, NoInternetActivity.class));
                }
            }
        });


        clearSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signaturePad.clear();
            }
        });

        txtReadAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReciverActivity.this, AgreeMentActivity.class);
                intent.putExtra("Inid", invoiceId);
                intent.putExtra("type", "r");
                startActivity(intent);
            }
        });


    }

    @Override
    public void onSucess(Response<ReciverApis> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            Log.d("responce+++++", "" + response.body().getData().toString());
            if (response.body().getStatus() == 200) {
                ReciverApis.Data data = response.body().getData();
                invoiceId = data.getInvoiceId().toString();
                money = data.getFeesDepositType().toString();
                if (data.getMessageType().equalsIgnoreCase("t")) {
                    textMessage.setText(data.getMessage());
                    imageVideo.setVisibility(View.GONE);
                    textMessage.setVisibility(View.VISIBLE);
                    textMessage.setMovementMethod(new ScrollingMovementMethod());
                    VideoType = "t";
                    videoUrl = data.getMessage();
                } else {
                    videoUrl = Config.INVOICE_VIDEO_URL + data.getMessage();
                    VideoType = "v";
                }

                Glide.with(this).load(Config.INVOICE_SIGN_URL + data.getSignImage()).dontTransform().into(imageView);
                txtView.setText(Html.fromHtml(data.getRequestContent().toString()));
                agree.setEnabled(true);
                refuse.setEnabled(true);
                adjust.setEnabled(true);

            } else {
                Toast.makeText(this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "" + response.message(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onSucessChoose(Response<ChooseOneApis> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                Log.d("adadada+++", "rssa");
                if (type.equals("2")) {
                    Intent intent = new Intent(ReciverActivity.this, AdjustActivity.class);
                    intent.putExtra("Inid", invoiceId);
                    intent.putExtra("video", videoUrl);
                    intent.putExtra("type", type);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ReciverActivity.this, MyProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }
            } else {
                Toast.makeText(this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "" + response.message(), Toast.LENGTH_LONG).show();
        }
    }


    //
    @Override
    public void onSucessAccept(Response<ChooseOneApis> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                dialog.show();
                controller.setAgreement(invoiceId);
            } else {
                Toast.makeText(this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "" + response.message(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSucessA(Response<AgreeMentApi> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                Intent intent = new Intent(ReciverActivity.this, AgreeMentActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Inid", invoiceId);
                intent.putExtra("type", "m");
                startActivity(intent);
            } else {
                Toast.makeText(this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "" + response.message(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSucessTranstion(Response<MessageApis> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {

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
        Log.d("+Error++", error);
    }

    private void DeepLink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();

                            String url = deepLink.toString();
                            Log.d("+++++", "++ url deep ++" + url);

                            data = deepLink.toString();
                            code = deepLink.toString().substring(deepLink.toString().lastIndexOf('/') + 1);

                            if (TextUtils.isEmpty(sharedToken.getUserId())) {
                                Intent intent2 = new Intent(ReciverActivity.this, LoginActivity.class);
                                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent2.putExtra("data", data);
                                startActivity(intent2);
                                finish();
                            } else {
                                if (url != null) {
                                    dialog.show();
                                    Log.d("+++code", code);
                                    controller.setReciverData(code);
                                }
                            }
                        } else {
                            data = getIntent().getStringExtra("data");
                            data = data.substring(data.lastIndexOf('/') + 1);
                            dialog.show();
                            controller.setReciverData(data.substring(data.lastIndexOf('/') + 1));
                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("abc", "getDynamicLink:onFailure", e);
                    }
                });
    }

    @OnClick(R.id.mandata_ok)
    public void onViewClicked() {
        Bitmap bitmap = signaturePad.getSignatureBitmap();
        MultiPart multiPart = new MultiPart(this, "sign_image");
        try {
            part = multiPart.sendImageFileToserver(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (CheckInternet.isInternetAvailable(ReciverActivity.this)) {
            dialog.show();
            controller.setChooseAccept(invoiceId, sharedToken.getUserId(), "1", part, transctionI);
        } else {
            startActivity(new Intent(ReciverActivity.this, NoInternetActivity.class));
        }
    }


    //paypal payment
    private void PaymentProgress() {
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "CHF", "M8sR8s Agreement Fee", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if (confirmation != null) {
                    try {
                        String payemtDetails = confirmation.toJSONObject().toString(4);
                        String id = confirmation.getProofOfPayment().getPaymentId().toString();


                        transctionI = id;
                        money = "pay";
                        dialog.show();
                        controller.setUpdateTranstion(sharedToken.getUserId(), invoiceId, transctionI);


                        Log.d("+++++++++Payment", id);
                        relative.setVisibility(View.VISIBLE);
                        mandataOk.setVisibility(View.VISIBLE);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Transaction cancelled", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(this, "Invalid Transaction", Toast.LENGTH_LONG).show();
        }
    }
}
