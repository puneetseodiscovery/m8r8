package com.m8sworld.m8r8.dashboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.m8sworld.m8r8.R;
import com.m8sworld.m8r8.login.LoginActivity;
import com.m8sworld.m8r8.mycontract.MyContractsActivity;
import com.m8sworld.m8r8.myprofile.MyProfileActivity;
import com.m8sworld.m8r8.termsandcondition.TermsActivity;
import com.m8sworld.m8r8.util.MultiPart;
import com.m8sworld.m8r8.util.SharedToken;
import com.m8sworld.m8r8.util.edit;
import com.m8sworld.m8r8.videoupload.UploadVideoActivity;
import com.m8sworld.m8r8.watchvideo.WatchVideoActivity;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;

public class Main2Activity extends AppCompatActivity {

    @BindView(R.id.tooolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbartext)
    TextView toolbartext;
    @BindView(R.id.imageDrawer)
    ImageView imageDrawer;
    @BindView(R.id.upload_currency)
    TextView uploadCurrency;
    @BindView(R.id.upload_price)
    EditText uploadPrice;
    @BindView(R.id.imageCamera)
    RoundedImageView imageCamera;
    @BindView(R.id.btnNext)
    Button btnNext;
    @BindView(R.id.Drawer_navigation)
    NavigationView navigation;
    @BindView(R.id.drawer)
    DrawerLayout drawer;

    public static int REQUEST_CAMERA = 122;
    public static int MY_PERMISSIONS_REQUEST_CAMERA = 555;

    ActionBarDrawerToggle mToggle;
    SharedToken sharedToken;
    String price, currency;
    Bitmap bitmap;
    public static MultipartBody.Part part;
    String abc = "1", msg;

    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        sharedToken = new SharedToken(this);


        edit.abc(uploadPrice, Main2Activity.this);

        setSupportActionBar(toolbar);
        NavigationMenuView navigationMenu = (NavigationMenuView) navigation.getChildAt(0);
        navigationMenu.addItemDecoration(new DividerItemDecoration(Main2Activity.this, DividerItemDecoration.VERTICAL));

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price = uploadPrice.getText().toString();
                currency = uploadCurrency.getText().toString();
                if (TextUtils.isEmpty(price)) {
                    uploadPrice.setError("Enter price");
                    uploadPrice.requestFocus();
                } else if (TextUtils.isEmpty(currency)) {
                    Toast.makeText(Main2Activity.this, "Choose the currency", Toast.LENGTH_LONG).show();
                    uploadCurrency.requestFocus();
                } else if (part == null) {
                    Toast.makeText(Main2Activity.this, "Select the image", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(Main2Activity.this, UploadVideoActivity.class);
                    intent.putExtra("price", price);
                    intent.putExtra("currency", currency);
                    startActivity(intent);

                }
            }
        });

        imageDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.closeDrawer(Gravity.RIGHT);
                } else
                    drawer.openDrawer(Gravity.RIGHT);
            }
        });


        imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Main2Activity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(Main2Activity.this, Manifest.permission.CAMERA)) {

                        ActivityCompat.requestPermissions(Main2Activity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                    } else {
                        ActivityCompat.requestPermissions(Main2Activity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }

                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
            }
        });

        uploadCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                final CurrencyPicker picker = CurrencyPicker.newInstance("Currency");
                Log.d("dadafadada", "" + picker);
                picker.setListener(new CurrencyPickerListener() {
                    @Override
                    public void onSelectCurrency(String s, String s1, String s2, int i) {
                        currency = s1;
                        uploadCurrency.setText(currency);
                        picker.dismiss();
                    }
                });
                picker.show(manager, "CURRENCY_PICKER");
            }
        });

        onCLick();
        checkPermissions();

    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void onCLick() {
        // set new title to the MenuIte
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.video) {
                    Intent intent = new Intent(Main2Activity.this, WatchVideoActivity.class);
                    startActivity(intent);
                    drawer.closeDrawer(Gravity.RIGHT);

                }

                if (id == R.id.myprofile) {
                    drawer.closeDrawer(Gravity.RIGHT);
                    Intent intent = new Intent(Main2Activity.this, MyProfileActivity.class);
                    startActivity(intent);

                }


                if (id == R.id.contract) {
                    drawer.closeDrawer(Gravity.RIGHT);
                    Intent intent = new Intent(Main2Activity.this, MyContractsActivity.class);
                    startActivity(intent);
                }

                if (id == R.id.terms) {
                    drawer.closeDrawer(Gravity.RIGHT);
                    Intent intent = new Intent(Main2Activity.this, TermsActivity.class);
                    intent.putExtra("terms", "Terms & Condition");
                    startActivity(intent);


                }
                if (id == R.id.privacy) {
                    drawer.closeDrawer(Gravity.RIGHT);
                    Intent intent = new Intent(Main2Activity.this, TermsActivity.class);
                    intent.putExtra("terms", "Privacy policy");
                    startActivity(intent);

                }
                if (id == R.id.art) {
                    drawer.closeDrawer(Gravity.RIGHT);
                    Intent intent = new Intent(Main2Activity.this, TermsActivity.class);
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
                    Intent logout = new Intent(Main2Activity.this, LoginActivity.class);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            bitmap = (Bitmap) bundle.get("data");
            try {
                MultiPart multi = new MultiPart(this, "bill_image");
                part = multi.sendImageFileToserver(bitmap);
                imageCamera.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    String permissionsDenied = "";
                    for (String per : permissions) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            permissionsDenied += "\n" + per;
                        }

                    }
                }
                return;
            }

        }
    }


}