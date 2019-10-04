package com.m8sworld.m8r8.myprofile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.m8sworld.m8r8.R;
import com.m8sworld.m8r8.controller.Controller;
import com.m8sworld.m8r8.myprofile.apis.EditProfileApi;
import com.m8sworld.m8r8.myprofile.apis.GetProfileDetailsApi;
import com.m8sworld.m8r8.nointernet.NoInternetActivity;
import com.m8sworld.m8r8.reciver.ReciverActivity;
import com.m8sworld.m8r8.util.CheckInternet;
import com.m8sworld.m8r8.util.ProgressBarClass;
import com.m8sworld.m8r8.util.SharedToken;
import com.m8sworld.m8r8.util.SnackBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity implements Controller.EditProfile, Controller.GetProfileDetails {
    @BindView(R.id.tooolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarImage)
    ImageView toolbarImage;
    @BindView(R.id.toolbartext)
    TextView toolbartext;
    @BindView(R.id.firstName)
    EditText firstName;
    @BindView(R.id.LastName)
    EditText lastName;
    @BindView(R.id.Email)
    EditText Email;
    @BindView(R.id.Phone)
    EditText Phone;
    @BindView(R.id.Address1)
    EditText Address1;
    @BindView(R.id.Address2)
    EditText Address2;
    @BindView(R.id.Town)
    EditText Town;
    @BindView(R.id.Postcode)
    EditText Postcode;
    @BindView(R.id.business_country)
    CountryCodePicker businessCountry;
    @BindView(R.id.button_next)
    Button buttonNext;

    Controller controller;
    ProgressDialog dialog;
    String Fname, Lname, email, phone, address1, address2, town, postcode, country;

    SharedToken sharedToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        dialog = ProgressBarClass.showProgressDialog(this);
        controller = new Controller((Controller.EditProfile) this, (Controller.GetProfileDetails) this);
        sharedToken = new SharedToken(this);

        setSupportActionBar(toolbar);
        toolbarImage.setVisibility(View.GONE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        toolbartext.setText("Edit profile");

        if (CheckInternet.isInternetAvailable(this)) {
            controller.setGetProfileDetails(sharedToken.getUserId());

        } else {
            startActivity(new Intent(EditProfileActivity.this, NoInternetActivity.class));
        }

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fname = firstName.getText().toString();
                Lname = lastName.getText().toString();
                email = Email.getText().toString();
                phone = Phone.getText().toString();
                address1 = Address1.getText().toString();
                address2 = Address2.getText().toString();
                town = Town.getText().toString();
                postcode = Postcode.getText().toString();
                country = businessCountry.getSelectedCountryName();

                if (TextUtils.isEmpty(Fname) || TextUtils.isEmpty(Lname) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address1) || TextUtils.isEmpty(address2) || TextUtils.isEmpty(postcode)) {
                    error(firstName);
                    error(lastName);
                    error(Email);
                    error(Phone);
                    error(Address1);
                    error(Address2);
                    error(Town);
                    error(Postcode);
                } else {
                    if (CheckInternet.isInternetAvailable(EditProfileActivity.this)) {
                        dialog.show();
                        controller.setEditProfile(sharedToken.getUserId(), address1, address2, town, postcode, country, Fname + " " + Lname, phone);
                    } else {
                        startActivity(new Intent(EditProfileActivity.this, NoInternetActivity.class));
                    }
                }
            }
        });

    }


    private void error(EditText editText) {
        if (TextUtils.isEmpty(editText.getText().toString()) || editText.getText().toString().length() > 50) {
            editText.requestFocus();
            editText.setError("Invalid Data");
        }
    }

    @Override
    public void onSucessEdit(Response<EditProfileApi> response) {
        dialog.dismiss();
        if (response.body().getStatus() == 200) {
            SnackBar.snackbar(EditProfileActivity.this, response.body().getMessage());

            if (!getIntent().getStringExtra("data").equals("0")) {
                Intent intent = new Intent(EditProfileActivity.this, ReciverActivity.class);
                intent.putExtra("data", getIntent().getStringExtra("data"));
                startActivity(intent);
            } else {
                Intent intent = new Intent(EditProfileActivity.this, MyProfileActivity.class);
                startActivity(intent);
            }
        } else {
            SnackBar.snackbar(EditProfileActivity.this, response.body().getMessage());
        }
    }

    @Override
    public void onSucessProfile(Response<GetProfileDetailsApi> response) {
        dialog.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getStatus() == 200) {
                GetProfileDetailsApi.Data data = response.body().getData();
                String[] spt = data.getName().split(" ");
                firstName.setText(spt[0]);
                lastName.setText(spt[1]);
                Phone.setText(getError(data.getContactNo()));
                Email.setText(getError(data.getEmail()));
                Address1.setText(getError(data.getAddress1()));
                Address2.setText(getError(data.getAddress2()));
                Town.setText(getError(data.getCity()));
                Postcode.setText(getError(data.getPostCode()));

            } else {
                SnackBar.snackbar(EditProfileActivity.this, response.body().getMessage());
            }
        } else {
            Toast.makeText(this, "" + response.message(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void error(String error) {
        dialog.dismiss();
        SnackBar.snackbar(EditProfileActivity.this, error);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (getIntent().getStringExtra("abc").equals("1")) {
            Toast.makeText(this, "Please complete this form to continue", Toast.LENGTH_LONG).show();
        } else {
            onBackPressed();
        }
        return true;
    }

    public String getError(String message) {
        if (message == null) {
            message = "";
            return message;
        } else {
            return message;
        }

    }
}
