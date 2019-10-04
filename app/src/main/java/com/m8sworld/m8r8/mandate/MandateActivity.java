package com.m8sworld.m8r8.mandate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.m8sworld.m8r8.MessageApis;
import com.m8sworld.m8r8.R;
import com.m8sworld.m8r8.agreement.AgreeMentActivity;
import com.m8sworld.m8r8.controller.Controller;
import com.m8sworld.m8r8.myprofile.apis.GetProfileDetailsApi;
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

import org.json.JSONException;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import retrofit2.Response;

public class MandateActivity extends AppCompatActivity implements Controller.GetInvoice, Controller.AddMandate, Controller.GetProfileDetails, Controller.UpdateTranstion {


    @BindView(R.id.tooolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarImage)
    ImageView toolbarImage;
    @BindView(R.id.toolbartext)
    TextView toolbartext;
    @BindView(R.id.textMessage)
    TextView textMessage;
    @BindView(R.id.imageSign)
    ImageView imageView;
    //        @BindView(R.id.imageVideo)
//    ImageView imageVideo;
//    @BindView(R.id.videoView)
//    VideoView videoView;
    @BindView(R.id.spinnerComission)
    Spinner spinnerComission;
    @BindView(R.id.edtext)
    EditText edtext;
    @BindView(R.id.spinnerMonth)
    Spinner spinnerMonth;
    @BindView(R.id.imagePay)
    ImageView imagePay;
    @BindView(R.id.imageAdd)
    ImageView imageAdd;
    @BindView(R.id.signature_pad)
    SignaturePad signaturePad;
    @BindView(R.id.clear_sign)
    Button clearSign;
    @BindView(R.id.relative)
    RelativeLayout relative;
    @BindView(R.id.mandata_ok)
    Button mandataOk;
    @BindView(R.id.image_set)
    ImageView imageSet;
    @BindView(R.id.mandata_date)
    TextView mandataDate;
    @BindView(R.id.linear1)
    LinearLayout linear1;
    @BindView(R.id.btnNext)
    Button btnNext;
    @BindView(R.id.imageSend)
    ImageView imageSend;

    String chose, name;

    //paypal
    public static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration configuration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(Config.PAYPAL_CLIENT_ID);


    String amount = "5";

    String videoUrl, date, month, comission, type, transctionI = "", moneyType = "add";
    Bitmap bitmap;
    String invoiceId;
    Controller controller;
    ProgressDialog dialog;
    SharedToken sharedToken;
    String min, max, offers;
    MultipartBody.Part sign;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

//    @BindView(R.id.avi)
//    AVLoadingIndicatorView avi;
//    @BindView(R.id.relativeVideo)
//    RelativeLayout relativeVideo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandate);
        ButterKnife.bind(this);
        sharedToken = new SharedToken(this);
        dialog = ProgressBarClass.showProgressDialog(this);
        controller = new Controller((Controller.GetInvoice) this, (Controller.AddMandate) this, (Controller.GetProfileDetails) this, (Controller.UpdateTranstion) this);

        invoiceId = getIntent().getStringExtra("Inid");

        sharedToken.setCatid(invoiceId);
        // start payment Gateway
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        startService(intent);


        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarImage.setVisibility(View.GONE);
        toolbartext.setText("My offer");

        if (CheckInternet.isInternetAvailable(this)) {
            dialog.show();
            controller.setGetInvoice(invoiceId);
            controller.setGetProfileDetails(sharedToken.getUserId());

        } else {
            startActivity(new Intent(this, NoInternetActivity.class));
        }

        setDiscount();
        setMonth();
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        mandataDate.setText("Date \n" + date);


        mandataOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.fullScroll(View.FOCUS_DOWN);
                bitmap = signaturePad.getSignatureBitmap();
                imageSet.setImageBitmap(bitmap);
                imageView.setImageBitmap(bitmap);
                imageSet.requestFocus();
                MultiPart multiPart = new MultiPart(MandateActivity.this, "signature");
                try {
                    sign = multiPart.sendImageFileToserver(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        clearSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signaturePad.clear();
            }
        });

//        imageVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                videoView.setVisibility(View.VISIBLE);
//                imageVideo.setVisibility(View.INVISIBLE);
//                // Set progressbar message
//
//                // Show progressbar
//                avi.setVisibility(View.VISIBLE);
//                relativeVideo.setEnabled(false);
//
//                try {
//                    // Start the MediaController
//                    MediaController mediacontroller = new MediaController(MandateActivity.this);
//                    mediacontroller.setAnchorView(videoView);
//                    Uri videoUri = Uri.parse(videoUrl);
//                    videoView.setMediaController(mediacontroller);
//                    videoView.setVideoURI(videoUri);
//
//                } catch (Exception e) {
//
//                    e.printStackTrace();
//                }
//
//                videoView.requestFocus();
//
//
//                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                    // Close the progress bar and play the video
//                    public void onPrepared(MediaPlayer mp) {
//                        videoView.start();
//                        avi.setVisibility(View.GONE);
//                    }
//                });
//
//
//                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//
//                    public void onCompletion(MediaPlayer mp) {
//                        videoView.setVisibility(View.INVISIBLE);
//                        imageVideo.setVisibility(View.VISIBLE);
//                    }
//                });
//            }
//        });
        imagePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentProgress();
            }
        });

        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moneyType = "add";
                transctionI = "";
                Toast.makeText(MandateActivity.this, "Amount added", Toast.LENGTH_LONG).show();
            }
        });

        imageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckInternet.isInternetAvailable(MandateActivity.this)) {
                    if (!offers.equalsIgnoreCase("Select(%)") && !edtext.getText().toString().equals("")) {
                        Toast.makeText(MandateActivity.this, "Select only on field", Toast.LENGTH_LONG).show();
                    } else if (!TextUtils.isEmpty(edtext.getText().toString())) {
                        type = "f";
                        comission = edtext.getText().toString();
                        dialog.show();
                        controller.setAddMandate(invoiceId, type, comission, "5", month, transctionI, moneyType, sign);
                    } else if (!offers.equals("Select(%)")) {
                        type = "p";
                        comission = offers;
                        dialog.show();
                        controller.setAddMandate(invoiceId, type, comission, "5", month, transctionI, moneyType, sign);
                    } else {
                        Toast.makeText(MandateActivity.this, "Enter your commission amount", Toast.LENGTH_LONG).show();
                    }

                } else {
                    startActivity(new Intent(MandateActivity.this, NoInternetActivity.class));
                }
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(chose)) {
                    if (CheckInternet.isInternetAvailable(MandateActivity.this)) {
                        if (!offers.equalsIgnoreCase("Select(%)") && !edtext.getText().toString().equals("")) {
                            Toast.makeText(MandateActivity.this, "Please select either a % or cash amount", Toast.LENGTH_LONG).show();
                        } else if (!TextUtils.isEmpty(edtext.getText().toString())) {
                            type = "f";
                            comission = edtext.getText().toString();
                            dialog.show();
                            controller.setAddMandate(invoiceId, type, comission, "5", month, transctionI, moneyType, sign);
                        } else if (!offers.equals("Select(%)")) {
                            type = "p";
                            comission = offers;
                            dialog.show();
                            controller.setAddMandate(invoiceId, type, comission, "5", month, transctionI, moneyType, sign);
                        } else {
                            Toast.makeText(MandateActivity.this, "Enter your commission amount", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        startActivity(new Intent(MandateActivity.this, NoInternetActivity.class));
                    }
                } else {
                    Toast.makeText(MandateActivity.this, "" + getResources().getString(R.string.good), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MandateActivity.this, AgreeMentActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("Inid", invoiceId);
                    intent.putExtra("type", "m");
                    startActivity(intent);
                }
            }
        });


    }

    //send url to user
    private void sendData() {
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.m8r8.com/" + invoiceId))
                .setDomainUriPrefix("m8r8.page.link")
                // Open links with this app on Android  amitpandey12.page.link
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("m8r8.page.link").build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();

        Log.d("hello123", "1" + dynamicLink.getUri());


        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse("https://" + dynamicLink.getUri().toString()))
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            String link = "Message from borrower" +
                                    "\n\nDear M8,\n \nYour M8 (" + name +
                                    ") has sent you an offer (click the link to open it).\n\n" + shortLink.toString() + "\n\nYou can accept, adjust or reject it.\n\nWarm regards \n\nThe M8sR8s Team";
                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                            sharingIntent.putExtra(Intent.EXTRA_TEXT, link);
                            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
                        } else {

                        }
                    }
                });
    }

    //paypal payment
    private void PaymentProgress() {
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "CHF", "M8sR8s Agreement Fee", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);


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

    //How many months would you like to pay off this loan

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
    public void onSucess(Response<GetInvoiceApi> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                GetInvoiceApi.Data data = response.body().getData();
                min = data.getOnePercent().toString();
                max = data.getTwentyPercent().toString();

                if (data.getMessageType().equalsIgnoreCase("t")) {
                    textMessage.setText(data.getMessage().toString());
                    textMessage.setVisibility(View.VISIBLE);
                    textMessage.setMovementMethod(new ScrollingMovementMethod());
                } else {
                    videoUrl = Config.INVOICE_VIDEO_URL + data.getMessage();
                }

            } else {
                Toast.makeText(this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "" + response.message(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onsuccesMandate(Response<AddMandateApi> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                sendData();
                chose = "1";
                Toast.makeText(this, "Please choose a friend  to send this invoice to", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "" + response.isSuccessful(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onSucessProfile(Response<GetProfileDetailsApi> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                name = response.body().getData().getName();

            } else {
                Toast.makeText(this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onSucessTranstion(Response<MessageApis> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                Toast.makeText(this, "Transaction paid", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "" + response.message(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void error(String error) {
        dialog.dismiss();
        Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if (confirmation != null) {
                    try {
                        String payemtDetails = confirmation.toJSONObject().toString(4);
                        String id = confirmation.getProofOfPayment().getPaymentId().toString();


                        transctionI = id;
                        moneyType = "pay";

                        controller.setUpdateTranstion(sharedToken.getUserId(), invoiceId, transctionI);

                        Log.d("payment", payemtDetails + "\n" + id);

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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
