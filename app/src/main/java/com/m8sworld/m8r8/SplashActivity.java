package com.m8sworld.m8r8;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.m8sworld.m8r8.dashboard.Main2Activity;
import com.m8sworld.m8r8.login.LoginActivity;
import com.m8sworld.m8r8.reciver.ReciverActivity;
import com.m8sworld.m8r8.util.SharedToken;

public class SplashActivity extends AppCompatActivity {

    SharedToken sharedToken;
    String code = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedToken = new SharedToken(this);

        DeepLink();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(sharedToken.getUserId())) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.putExtra("data", code);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, Main2Activity.class);
                    intent.putExtra("abc", "1");
                    startActivity(intent);
                    finish();
                }


            }
        }, 3000);

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

                            Toast.makeText(SplashActivity.this, "" + code, Toast.LENGTH_SHORT).show();

                            if (url != null) {
                                code = deepLink.toString();
                            }
                        } else {
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
}
