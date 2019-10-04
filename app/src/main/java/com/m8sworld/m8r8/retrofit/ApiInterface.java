package com.m8sworld.m8r8.retrofit;

import com.m8sworld.m8r8.MessageApis;
import com.m8sworld.m8r8.adjust.AdjustApi;
import com.m8sworld.m8r8.agreement.AgreeMentApi;
import com.m8sworld.m8r8.forgotpssword.PasswordOTP;
import com.m8sworld.m8r8.forgotpssword.PasswordReset;
import com.m8sworld.m8r8.invoice.InvoiceApi;
import com.m8sworld.m8r8.login.GetVideo;
import com.m8sworld.m8r8.login.LoginApi;
import com.m8sworld.m8r8.mandate.AddMandateApi;
import com.m8sworld.m8r8.mandate.GetInvoiceApi;
import com.m8sworld.m8r8.myprofile.apis.AgreementCompletedApi;
import com.m8sworld.m8r8.myprofile.apis.EditProfileApi;
import com.m8sworld.m8r8.myprofile.apis.ForgiveApi;
import com.m8sworld.m8r8.myprofile.apis.GetProfileDetailsApi;
import com.m8sworld.m8r8.myprofile.adapter.MandateListApis;
import com.m8sworld.m8r8.reciver.ChooseOneApis;
import com.m8sworld.m8r8.reciver.ReciverApis;
import com.m8sworld.m8r8.signup.GetOtpApi;
import com.m8sworld.m8r8.signup.RegisterApi;
import com.m8sworld.m8r8.termsandcondition.TermsConditionApi;
import com.m8sworld.m8r8.videoupload.InvoiceApis;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("get_video")
    Call<GetVideo> video();

    // sing up
    @FormUrlEncoded
    @POST("user_register")
    Call<RegisterApi> register(
            @Field("first_name") String firstName,
            @Field("last_name") String lastName,
            @Field("email") String email,
            @Field("password") String password,
            @Field("contact_no") String contact,
            @Field("c_password") String name,
            @Field("device_token") String deviceToken
    );

    //get the otp for registeration
    @POST("varify_otp")
    Call<GetOtpApi> getOtp(
            @Query("otp") String otp,
            @Query("user_id") String userId
    );

    //get terms and condition and privecy
    @GET("pagecontent")
    Call<TermsConditionApi> getTermsCondition();

    @FormUrlEncoded
    @POST("login")
    Call<LoginApi> login(
            @Field("email") String email,
            @Field("password") String password,
            @Field("device_token") String token
    );

    //get the otp for password reset
    @POST("password/create")
    Call<PasswordOTP> passwrdOTP(
            @Query("email") String email
    );

    //send otp for verfication
    @POST("password/find")
    Call<PasswordOTP> sendPasswrodOtp(
            @Query("email") String email,
            @Query("otp") String Otp
    );

    //set a new password
    @POST("password/reset")
    Call<PasswordReset> passwordReset(
            @Query("email") String email,
            @Query("otp") String Otp,
            @Query("password") String password,
            @Query("password_confirmation") String cPassword

    );

    //edit profile
    @POST("edit_profile")
    Call<EditProfileApi> editProfle(
            @Query("user_id") String user_id,
            @Query("name") String name,
            @Query("contact_no") String contact_no,
            @Query("post_code") String post_code,
            @Query("address1") String address1,
            @Query("address2") String address2,
            @Query("city") String city,
            @Query("country") String country

    );

    //get the user details
    @POST("userdetails")
    Call<GetProfileDetailsApi> getProfileDetails(
            @Query("user_id") String userId
    );


    //make a invoice with msg
    @Multipart
    @POST("create_invoice")
    Call<InvoiceApis> addInvoice(
            @Query("user_id") String userId,
            @Query("currency") String currency,
            @Query("price") String price,
            @Query("text_msg") String comment,
            @Query("msg_type") String type,
            @Part MultipartBody.Part bil
    );

    //make a invoice with msg
    @Multipart
    @POST("create_invoice")
    Call<InvoiceApis> addInvoiceVideo(
            @Query("user_id") String userId,
            @Query("currency") String currency,
            @Query("price") String price,
            @Query("msg_type") String type,
            @Part MultipartBody.Part bil,
            @Part MultipartBody.Part video
    );

    // upload profile image
    @Multipart
    @POST("ProfileImage")
    Call<MessageApis> addProfileImage(
            @Query("user_id") String userId,
            @Part MultipartBody.Part image
    );


    @POST("get_pay_screen")
    Call<GetInvoiceApi> getPayScreen(
            @Query("invoice_id") String invoideId
    );

    @Multipart
    @POST("send_invoice")
    Call<AddMandateApi> addMandate(
            @Query("invoice_id") String invoice_id,
            @Query("interest_type") String interest_type,
            @Query("interest") String interest,
            @Query("app_fees") String app_fees,
            @Query("transaction_id") String transaction_id,
            @Query("months") String month,
            @Query("fees_deposit_type") String fees_deposit_type,
            @Part MultipartBody.Part sign
    );


    //get pdf data
    @POST("get_agreement_screen")
    Call<AgreeMentApi> agreement(
            @Query("invoice_id") String invoice_id);

    //Reciver api
    @POST("select_options_screen")
    Call<ReciverApis> reciver(
            @Query("invoice_id") String id
    );

    // choose one
    @POST("choose_one_option")
    Call<ChooseOneApis> chooseOne(
            @Query("invoice_id") String inVOiceid,
            @Query("user_id") String userId,
            @Query("selected_option") String select

    );

    //choose one agree
    @Multipart
    @POST("choose_one_option")
    Call<ChooseOneApis> chooseAccept(
            @Query("invoice_id") String inVOiceid,
            @Query("user_id") String userId,
            @Query("selected_option") String select,
            @Query("transaction_id") String transation,
            @Part MultipartBody.Part sign

    );


    //adjust api
    @POST("adjust_agreement_details")
    Call<AdjustApi> adjust(
            @Query("invoice_id") String inVOiceid,
            @Query("user_id") String userId,
            @Query("interest_type") String interest_type,
            @Query("interest") String interest,
            @Query("months") String months

    );

    //get the mandate list
    @POST("contract_lists")
    Call<MandateListApis> getMandateList(
            @Query("user_id") String userId

    );

    // for forgive btn
    @POST("forgive_payment")
    Call<ForgiveApi> forgive(
            @Query("user_id") String userId,
            @Query("invoice_id") String landerId
    );

    // for forgive btn
    @POST("agreement_complete")
    Call<AgreementCompletedApi> completed(
            @Query("user_id") String userId,
            @Query("invoice_id") String landerId
    );

    // get invoice image
    @POST("getInvoiceImage")
    Call<InvoiceApi> invoice(
            @Query("invoice_id") String id
    );

    //get the payment
    @POST("updateAppFeesStatus")
    Call<MessageApis> updatePayment(
            @Query("user_id") String userId,
            @Query("invoice_id") String invoiceId,
            @Query("transaction_id") String transaction

    );

}
