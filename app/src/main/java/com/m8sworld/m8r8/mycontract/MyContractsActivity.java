package com.m8sworld.m8r8.mycontract;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.m8sworld.m8r8.R;
import com.m8sworld.m8r8.controller.Controller;
import com.m8sworld.m8r8.dashboard.Main2Activity;
import com.m8sworld.m8r8.login.LoginActivity;
import com.m8sworld.m8r8.myprofile.MyProfileActivity;
import com.m8sworld.m8r8.myprofile.adapter.GetForgive;
import com.m8sworld.m8r8.myprofile.adapter.GetId;
import com.m8sworld.m8r8.myprofile.adapter.MandateAdapter;
import com.m8sworld.m8r8.myprofile.adapter.MandateListApis;
import com.m8sworld.m8r8.myprofile.apis.AgreementCompletedApi;
import com.m8sworld.m8r8.myprofile.apis.ForgiveApi;
import com.m8sworld.m8r8.nointernet.NoInternetActivity;
import com.m8sworld.m8r8.termsandcondition.TermsActivity;
import com.m8sworld.m8r8.util.CheckInternet;
import com.m8sworld.m8r8.util.ProgressBarClass;
import com.m8sworld.m8r8.util.SharedToken;
import com.m8sworld.m8r8.util.SpacesItemDecoration;
import com.m8sworld.m8r8.watchvideo.WatchVideoActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class MyContractsActivity extends AppCompatActivity implements Controller.GetMandateList, Controller.Forgive, Controller.CompleteAgreement {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tooolbar)
    Toolbar toolbar;

    ActionBarDrawerToggle mToggle;
    SharedToken sharedToken;
    Controller controller;
    ProgressDialog dialog;
    MandateAdapter adapter;
    String invoiceId;
    @BindView(R.id.imgeLogo)
    ImageView imgeLogo;
    @BindView(R.id.toolbartext)
    TextView toolbartext;
    @BindView(R.id.imageDrawer)
    ImageView imageDrawer;
    @BindView(R.id.Drawer_navigation)
    NavigationView navigation;
    @BindView(R.id.drawer)
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contracts);
        ButterKnife.bind(this);
        sharedToken = new SharedToken(this);
        dialog = ProgressBarClass.showProgressDialog(this);
        controller = new Controller((Controller.GetMandateList) this, (Controller.Forgive) this, (Controller.CompleteAgreement) this);

        setSupportActionBar(toolbar);
        toolbartext.setText("My agreements");


        NavigationMenuView navigationMenu = (NavigationMenuView) navigation.getChildAt(0);
        navigationMenu.addItemDecoration(new DividerItemDecoration(MyContractsActivity.this, DividerItemDecoration.VERTICAL));


        imageDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.closeDrawer(Gravity.RIGHT);
                } else
                    drawer.openDrawer(Gravity.RIGHT);
            }
        });


        if (CheckInternet.isInternetAvailable(this)) {
            dialog.show();
            controller.setGetMandateList(sharedToken.getUserId());
        } else {
            startActivity(new Intent(getApplicationContext(), NoInternetActivity.class));
        }


        onCLick();

    }


    private void onCLick() {
        // set new title to the MenuIte
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.home) {
                    Intent intent = new Intent(MyContractsActivity.this, Main2Activity.class);
                    startActivity(intent);
                    drawer.closeDrawer(Gravity.RIGHT);
                }
                if (id == R.id.video) {
                    Intent intent = new Intent(MyContractsActivity.this, WatchVideoActivity.class);
                    startActivity(intent);
                    drawer.closeDrawer(Gravity.RIGHT);

                }

                if (id == R.id.myprofile) {
                    drawer.closeDrawer(Gravity.RIGHT);
                    Intent intent = new Intent(MyContractsActivity.this, MyProfileActivity.class);
                    startActivity(intent);

                }


                if (id == R.id.contract) {
                    drawer.closeDrawer(Gravity.RIGHT);
                    Intent intent = new Intent(MyContractsActivity.this, MyContractsActivity.class);
                    startActivity(intent);
                }

                if (id == R.id.terms) {
                    drawer.closeDrawer(Gravity.RIGHT);
                    Intent intent = new Intent(MyContractsActivity.this, TermsActivity.class);
                    intent.putExtra("terms", "Terms & Condition");
                    startActivity(intent);


                }
                if (id == R.id.privacy) {
                    drawer.closeDrawer(Gravity.RIGHT);
                    Intent intent = new Intent(MyContractsActivity.this, TermsActivity.class);
                    intent.putExtra("terms", "Privacy policy");
                    startActivity(intent);

                }
                if (id == R.id.art) {
                    drawer.closeDrawer(Gravity.RIGHT);
                    Intent intent = new Intent(MyContractsActivity.this, TermsActivity.class);
                    intent.putExtra("terms", "Art & Statement");
                    startActivity(intent);

                }
                if (id == R.id.www) {
                    drawer.closeDrawer(Gravity.RIGHT);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.m8s.world"));
                    startActivity(browserIntent);
                }
                if (id == R.id.logout) {
                    sharedToken.clearShaerd();
                    Intent logout = new Intent(MyContractsActivity.this, LoginActivity.class);
                    logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    logout.putExtra("data", "0");
                    startActivity(logout);
                    finish();
                }

                return false;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSucessMandate(Response<MandateListApis> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                ArrayList<MandateListApis.Datum> arrayList = new ArrayList<>();
                for (int i = 0; i < response.body().getData().size(); i++) {
                    arrayList.add(response.body().getData().get(i));
                    setReyclerView(arrayList);
                }
            } else {
                Toast.makeText(this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "" + response.message(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSucessForgive(Response<ForgiveApi> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                Toast.makeText(this, "Congratulations your very good M8 has forgiven your agreement, you no longer have to pay them any more money. You have a Gr8 M8!", Toast.LENGTH_LONG).show();
                finish();
                startActivity(getIntent());
            } else {
                Toast.makeText(this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "" + response.message(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSucessComplete(Response<AgreementCompletedApi> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                Toast.makeText(this, "You have now stated that this agreement has been completed, the borrower is no longer committed to pay you any more payments.", Toast.LENGTH_LONG).show();
                finish();
                startActivity(getIntent());
            } else {
                Toast.makeText(this, "Only a lender can state that an agreement has been completed", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "" + response.message(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void error(String error) {
        dialog.dismiss();
        Toast.makeText(this, "" + error, Toast.LENGTH_LONG).show();
    }

    // set the data into reyclrView
    private void setReyclerView(ArrayList<MandateListApis.Datum> arrayList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new MandateAdapter(this, arrayList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.addItemDecoration(new SpacesItemDecoration(10));

        //forgive
        adapter.setGetForgive(new GetForgive() {
            @Override
            public void onSucess(int pos) {
                invoiceId = String.valueOf(pos);
                if (CheckInternet.isInternetAvailable(MyContractsActivity.this)) {
                    dialog.show();
                    controller.setForgive(sharedToken.getUserId(), invoiceId);
                } else {
                    startActivity(new Intent(MyContractsActivity.this, NoInternetActivity.class));
                }
            }
        });

        adapter.setGetHand(new GetId() {
            @Override
            public void getInvoiceId(int pos) {
                invoiceId = String.valueOf(pos);
                if (CheckInternet.isInternetAvailable(MyContractsActivity.this)) {
                    dialog.show();
                    controller.setCompleteAgreement(sharedToken.getUserId(), invoiceId);
                } else {
                    startActivity(new Intent(MyContractsActivity.this, NoInternetActivity.class));
                }
            }
        });
    }

}
