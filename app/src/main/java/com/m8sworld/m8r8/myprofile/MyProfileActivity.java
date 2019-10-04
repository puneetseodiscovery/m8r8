package com.m8sworld.m8r8.myprofile;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.m8sworld.m8r8.MessageApis;
import com.m8sworld.m8r8.R;
import com.m8sworld.m8r8.controller.Controller;
import com.m8sworld.m8r8.dashboard.Main2Activity;
import com.m8sworld.m8r8.login.LoginActivity;
import com.m8sworld.m8r8.mycontract.MyContractsActivity;
import com.m8sworld.m8r8.myprofile.adapter.GetForgive;
import com.m8sworld.m8r8.myprofile.adapter.GetId;
import com.m8sworld.m8r8.myprofile.adapter.MandateAdapter;
import com.m8sworld.m8r8.myprofile.adapter.MandateListApis;
import com.m8sworld.m8r8.myprofile.apis.AgreementCompletedApi;
import com.m8sworld.m8r8.myprofile.apis.ForgiveApi;
import com.m8sworld.m8r8.myprofile.apis.GetProfileDetailsApi;
import com.m8sworld.m8r8.nointernet.NoInternetActivity;
import com.m8sworld.m8r8.termsandcondition.TermsActivity;
import com.m8sworld.m8r8.util.CheckInternet;
import com.m8sworld.m8r8.util.Config;
import com.m8sworld.m8r8.util.MultiPart;
import com.m8sworld.m8r8.util.ProgressBarClass;
import com.m8sworld.m8r8.util.SharedToken;
import com.m8sworld.m8r8.util.SnackBar;
import com.m8sworld.m8r8.util.SpacesItemDecoration;
import com.m8sworld.m8r8.watchvideo.WatchVideoActivity;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import retrofit2.Response;

public class MyProfileActivity extends AppCompatActivity implements Controller.GetProfileDetails, Controller.ProfileImage, Controller.GetMandateList, Controller.Forgive, Controller.CompleteAgreement {

    @BindView(R.id.tooolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbartext)
    TextView toolbartext;
    @BindView(R.id.profile_image)
    RoundedImageView profileImage;
    @BindView(R.id.editProfile)
    TextView editProfile;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_phone)
    TextView userPhone;
    @BindView(R.id.user_email)
    TextView userEmail;
    @BindView(R.id.user_address)
    TextView userAddress;
    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.linear2)
    LinearLayout linear2;
    @BindView(R.id.txtContractNumber)
    TextView txtContractNumber;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    public static int RESULT_LOAD_IMAGE = 121;
    public static int REQUEST_CAMERA = 122;
    public static int MY_PERMISSIONS_REQUEST_CAMERA = 555;
    String[] dialogOptions = new String[]{"Camera", "Gallery", "Cancel"};
    MultipartBody.Part part;

    Bitmap bitmap;
    ProgressDialog dialog;
    Controller controller;
    SharedToken sharedToken;
    MandateAdapter adapter;

    @BindView(R.id.imageDrawer)
    ImageView imageDrawer;
    @BindView(R.id.Drawer_navigation)
    NavigationView navigationView;
    @BindView(R.id.drawer)
    DrawerLayout drawer;
    ActionBarDrawerToggle mToggle;

    String invoiceId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        ButterKnife.bind(this);
        sharedToken = new SharedToken(this);
        dialog = ProgressBarClass.showProgressDialog(this);
        controller = new Controller((Controller.GetProfileDetails) this, (Controller.ProfileImage) this, (Controller.GetMandateList) this, (Controller.Forgive) this, (Controller.CompleteAgreement) this);

        setSupportActionBar(toolbar);
        NavigationMenuView navigationMenu = (NavigationMenuView) navigationView.getChildAt(0);
        navigationMenu.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        toolbartext.setText("My Profile");

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
            controller.setGetProfileDetails(sharedToken.getUserId());
            controller.setGetMandateList(sharedToken.getUserId());
        }


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("abc", "2");
                intent.putExtra("data", "0");
                startActivity(intent);
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialg();
            }
        });

        onCLick();
    }


    private void onCLick() {
        // set new title to the MenuIte
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.video) {

                    Intent intent = new Intent(MyProfileActivity.this, WatchVideoActivity.class);
                    startActivity(intent);
                    drawer.closeDrawer(Gravity.RIGHT);

                }

                if (id == R.id.home) {
                    drawer.closeDrawer(Gravity.RIGHT);
                    Intent intent = new Intent(MyProfileActivity.this, Main2Activity.class);
                    startActivity(intent);
                }

                if (id == R.id.myprofile) {
                    drawer.closeDrawer(Gravity.RIGHT);
                    finish();
                    startActivity(getIntent());

                }


                if (id == R.id.contract) {
                    drawer.closeDrawer(Gravity.RIGHT);
                    Intent intent = new Intent(MyProfileActivity.this, MyContractsActivity.class);
                    startActivity(intent);
                }


                if (id == R.id.terms) {
                    drawer.closeDrawer(Gravity.RIGHT);
                    Intent intent = new Intent(MyProfileActivity.this, TermsActivity.class);
                    intent.putExtra("terms", "Terms & Condition");
                    startActivity(intent);


                }
                if (id == R.id.privacy) {
                    drawer.closeDrawer(Gravity.RIGHT);
                    Intent intent = new Intent(MyProfileActivity.this, TermsActivity.class);
                    intent.putExtra("terms", "Privacy policy");
                    startActivity(intent);

                }
                if (id == R.id.art) {
                    drawer.closeDrawer(Gravity.RIGHT);
                    Intent intent = new Intent(MyProfileActivity.this, TermsActivity.class);
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
                    Intent logout = new Intent(MyProfileActivity.this, LoginActivity.class);
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
    public void onSucessProfile(Response<GetProfileDetailsApi> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                GetProfileDetailsApi.Data data = response.body().getData();
                userName.setText(getError(data.getName().toString()));
                userEmail.setText(getError(data.getEmail()));
                userPhone.setText(getError(data.getContactNo().toString()));
                userAddress.setText(data.getAddress1() + " " + getError(data.getPostCode()) + " " + getError(data.getCountry()));
                Glide.with(this).load(Config.PROFILE_IMAGE_URL + response.body().getData().getProfileImage()).dontAnimate().dontTransform().into(profileImage);

            } else {
                Toast.makeText(this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "" + response.message(), Toast.LENGTH_LONG).show();
        }
    }

    //    chose image from camera and gallery
    private void showDialg() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
        builder.setTitle("Select Images");
        builder.setCancelable(false);
        //builder.setPositiveButton("OK", null);

        builder.setItems(dialogOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if ("Camera".equals(dialogOptions[which])) {

                    if (ContextCompat.checkSelfPermission(MyProfileActivity.this,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(MyProfileActivity.this, Manifest.permission.CAMERA)) {

                            ActivityCompat.requestPermissions(MyProfileActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                        } else {
                            ActivityCompat.requestPermissions(MyProfileActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    MY_PERMISSIONS_REQUEST_CAMERA);
                        }

                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    }


                } else if ("Gallery".equals(dialogOptions[which])) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);

                } else if ("Cancel".equals(dialogOptions[which])) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog dialog = builder.create();
//        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {

            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                MultiPart multi = new MultiPart(this, "image");
                part = multi.sendImageFileToserver(bitmap);
                if (CheckInternet.isInternetAvailable(this)) {
                    dialog.show();
                    controller.setProfileImage(sharedToken.getUserId(), part);
                } else {
                    Toast.makeText(MyProfileActivity.this, "" + getResources().getString(R.string.noInternet), Toast.LENGTH_LONG).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            bitmap = (Bitmap) bundle.get("data");
            try {
                MultiPart multi = new MultiPart(this, "image");
                part = multi.sendImageFileToserver(bitmap);
                if (CheckInternet.isInternetAvailable(this)) {
                    dialog.show();
                    controller.setProfileImage(sharedToken.getUserId(), part);
                } else {
                    Toast.makeText(MyProfileActivity.this, "" + getResources().getString(R.string.noInternet), Toast.LENGTH_LONG).show();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onSuccessADD(Response<MessageApis> response) {
        dialog.dismiss();
        if (response.body().getStatus() == 200) {
            finish();
            startActivity(getIntent());
        } else {
            Toast.makeText(this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSucessMandate(Response<MandateListApis> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                ArrayList<MandateListApis.Datum> arrayList = new ArrayList<>();
                txtContractNumber.setText(response.body().getMessage().toString());
                for (int i = 0; i < response.body().getData().size(); i++) {
                    arrayList.add(response.body().getData().get(i));
                    setReyclerView(arrayList);
                }
            } else {

            }
        } else {

        }
    }


    @Override
    public void onSucessForgive(Response<ForgiveApi> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                Toast.makeText(this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                finish();
                startActivity(getIntent());
            } else {
                Toast.makeText(this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "" + response.message(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSucessComplete(Response<AgreementCompletedApi> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                Toast.makeText(this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                finish();
                startActivity(getIntent());
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
        SnackBar.snackbar(MyProfileActivity.this, error);
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
                if (CheckInternet.isInternetAvailable(MyProfileActivity.this)) {
                    dialog.show();
                    controller.setForgive(sharedToken.getUserId(), invoiceId);
                } else {
                    startActivity(new Intent(MyProfileActivity.this, NoInternetActivity.class));
                }

            }
        });


        adapter.setGetHand(new GetId() {
            @Override
            public void getInvoiceId(int pos) {
                invoiceId = String.valueOf(pos);
                if (CheckInternet.isInternetAvailable(MyProfileActivity.this)) {
                    dialog.show();
                    controller.setCompleteAgreement(sharedToken.getUserId(), invoiceId);
                } else {
                    startActivity(new Intent(MyProfileActivity.this, NoInternetActivity.class));
                }
            }
        });
    }


    // for check the string is null or not
    public String getError(String message) {
        if (message == null || message.equals("")) {
            message = "";
            return message;
        } else {
            return message;
        }

    }
}
