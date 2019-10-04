package com.m8sworld.m8r8;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.m8sworld.m8r8.dashboard.Main2Activity;
import com.m8sworld.m8r8.util.SharedToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotifiactionActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txtMsg)
    TextView txtMsg;
    @BindView(R.id.btn_send)
    Button btnSend;
    SharedToken sharedToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifiaction);
        ButterKnife.bind(this);
        sharedToken = new SharedToken(this);

        txtMsg.setText(getIntent().getStringExtra("msg"));
    }

    @OnClick(R.id.btn_send)
    public void onViewClicked() {
        if (TextUtils.isEmpty(sharedToken.getUserId())) {
            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, Main2Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}
