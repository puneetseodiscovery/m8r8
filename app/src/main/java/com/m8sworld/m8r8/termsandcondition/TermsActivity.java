package com.m8sworld.m8r8.termsandcondition;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.m8sworld.m8r8.R;
import com.m8sworld.m8r8.controller.Controller;
import com.m8sworld.m8r8.util.CheckInternet;
import com.m8sworld.m8r8.util.ProgressBarClass;
import com.m8sworld.m8r8.util.SnackBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class TermsActivity extends AppCompatActivity implements Controller.TermsAndCondition {

    @BindView(R.id.txtTerms)
    TextView txtTerms;
    @BindView(R.id.tooolbar)
    Toolbar toolbar;
    Controller controller;
    ProgressDialog dialog;

    String terms;
    @BindView(R.id.toolbarImage)
    ImageView toolbarImage;
    @BindView(R.id.toolbartext)
    TextView toolbartext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        ButterKnife.bind(this);
        controller = new Controller(this);
        dialog = ProgressBarClass.showProgressDialog(this);

        terms = getIntent().getStringExtra("terms");

        if (CheckInternet.isInternetAvailable(this)) {
            dialog.show();
            controller.setTermsAndCondition();
        } else {
            SnackBar.snackbar(this, getResources().getString(R.string.noInternet));
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        toolbarImage.setVisibility(View.GONE);

    }

    @Override
    public void onSuccess(Response<TermsConditionApi> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                if (terms.equals("Terms & Condition")) {
                    txtTerms.setText(Html.fromHtml(response.body().getData().getTermAndCondition()));
                    toolbartext.setText("Terms & Condition");
                }
                if (terms.equals("Art & Statement")) {
                    txtTerms.setText(Html.fromHtml(response.body().getData().getArtStatement()));
                    toolbartext.setText("Art & Statement");

                }
                if (terms.equals("Privacy policy")) {
                    txtTerms.setText(Html.fromHtml(response.body().getData().getPrivacyPolicy()));
                    toolbartext.setText("Privacy policy");
                }
            }
        } else {
            Toast.makeText(this, "" + response.message(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void error(String error) {
        dialog.dismiss();
        SnackBar.snackbar(TermsActivity.this, error);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
