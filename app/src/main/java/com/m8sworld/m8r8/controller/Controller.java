package com.m8sworld.m8r8.controller;

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
import com.m8sworld.m8r8.retrofit.ApiInterface;
import com.m8sworld.m8r8.retrofit.ServiceGenerator;
import com.m8sworld.m8r8.signup.GetOtpApi;
import com.m8sworld.m8r8.signup.RegisterApi;
import com.m8sworld.m8r8.termsandcondition.TermsConditionApi;
import com.m8sworld.m8r8.videoupload.InvoiceApis;


import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Controller {

    public Signup signup;
    public TermsAndCondition termsAndCondition;
    public SignUpOtp signUpOtp;
    public Login login;
    public GetForgotOtp getForgotOtp;
    public SendOtpForget sendOtpForget;
    public ResetPassword resetPassword;
    public EditProfile editProfile;
    public GetProfileDetails getProfileDetails;
    public AddInvoiceText addInvoiceText;
    public AddInvoiceVideo addInvoiceVideo;
    public getVideo getVideos;
    public ProfileImage profileImage;
    public GetInvoice getInvoice;
    public AddMandate addMandate;
    public Agreement agreement;
    public ReciverData reciverData;
    public ChooseOne chooseOne;
    public ChooseAccept chooseAccept;
    public Adjust adjust;
    public GetMandateList getMandateList;
    public Forgive forgive;
    public CompleteAgreement completeAgreement;
    public InvoiceImage invoiceImage;
    public UpdateTranstion updateTranstion;


    /*++++++++++++++++++Get Video++++++++++++++++*/
    public Controller(getVideo getVideos1) {
        getVideos = getVideos1;
    }

    public void setGetVideos() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetVideo> call = apiInterface.video();
        call.enqueue(new Callback<GetVideo>() {
            @Override
            public void onResponse(Call<GetVideo> call, Response<GetVideo> response) {

                getVideos.onSuccesVideo(response);

            }

            @Override
            public void onFailure(Call<GetVideo> call, Throwable t) {
                getVideos.error(t.getMessage());
            }
        });
    }

    public interface getVideo {
        void onSuccesVideo(Response<GetVideo> response);

        void error(String error);
    }
    /*++++++++++++++ENd+++++++++++++++++*/

    /*++++++++++++++++++=Sing up++++++++++++++++++*/

    public Controller(Signup signup1, getVideo getVideos1) {
        signup = signup1;
        getVideos = getVideos1;
    }

    public void setSignup(String firstName, String lastName, String email, String password, String contact, String c_Password, String deviceToken) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<RegisterApi> call = apiInterface.register(firstName, lastName, email, password, contact, c_Password, deviceToken);
        //Progress bar

        call.enqueue(new Callback<RegisterApi>() {
            @Override
            public void onResponse(Call<RegisterApi> call, Response<RegisterApi> response) {
                signup.onSucess(response);

            }

            @Override
            public void onFailure(Call<RegisterApi> call, Throwable t) {
                signup.error(t.getMessage());
            }
        });
    }

    public interface Signup {
        void onSucess(Response<RegisterApi> response);

        void error(String error);
    }
    /*+++++++++++++++END++++++++++++++++++++*/

    /*++++++++++++++++++Terms and condition+++++++++++++*/

    public Controller(TermsAndCondition termsAndCondition1) {
        termsAndCondition = termsAndCondition1;
    }

    public void setTermsAndCondition() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<TermsConditionApi> call = apiInterface.getTermsCondition();
        call.enqueue(new Callback<TermsConditionApi>() {
            @Override
            public void onResponse(Call<TermsConditionApi> call, Response<TermsConditionApi> response) {

                termsAndCondition.onSuccess(response);

            }

            @Override
            public void onFailure(Call<TermsConditionApi> call, Throwable t) {
                termsAndCondition.error(t.getMessage());
            }
        });
    }

    public interface TermsAndCondition {
        void onSuccess(Response<TermsConditionApi> response);

        void error(String error);
    }
    /*++++++++++++++++++END++++++++++++++++++++*/

    /*++++++++++++++++++SingupOtp++++++++++++++++++++*/

    public Controller(SignUpOtp signUpOtp1) {
        signUpOtp = signUpOtp1;
    }

    public void setSignUpOtp(String otp, String userId) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetOtpApi> call = apiInterface.getOtp(otp, userId);
        call.enqueue(new Callback<GetOtpApi>() {
            @Override
            public void onResponse(Call<GetOtpApi> call, Response<GetOtpApi> response) {

                signUpOtp.onSucess(response);


            }

            @Override
            public void onFailure(Call<GetOtpApi> call, Throwable t) {
                signUpOtp.error(t.getMessage());
            }
        });

    }

    public interface SignUpOtp {
        void onSucess(Response<GetOtpApi> response);

        void error(String error);
    }
    /*++++++++++++++++++END++++++++++++++++++++*/


    /*++++++++++++++++LOgin Api+++++++++++++++*/

    public Controller(Login login1, getVideo getVideos1) {
        login = login1;
        getVideos = getVideos1;
    }

    public void setLogin(String email, String password, String token) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<LoginApi> call = apiInterface.login(email, password, token);
        call.enqueue(new Callback<LoginApi>() {
            @Override
            public void onResponse(Call<LoginApi> call, Response<LoginApi> response) {

                login.onSucess(response);


            }

            @Override
            public void onFailure(Call<LoginApi> call, Throwable t) {
                login.error(t.getMessage());
            }
        });
    }

    public interface Login {
        void onSucess(Response<LoginApi> response);

        void error(String error);
    }
    /*+++++++++++++++END++++++++++++++*/


    /*+++++++++++++++Forget Otp get++++++++++++++*/

    public Controller(GetForgotOtp getForgotOtp1, SendOtpForget sendOtpForget1, ResetPassword resetPassword1) {
        getForgotOtp = getForgotOtp1;
        sendOtpForget = sendOtpForget1;
        resetPassword = resetPassword1;
    }

    public void setGetForgotOtp(String email) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<PasswordOTP> call = apiInterface.passwrdOTP(email);

        call.enqueue(new Callback<PasswordOTP>() {
            @Override
            public void onResponse(Call<PasswordOTP> call, Response<PasswordOTP> response) {


                getForgotOtp.onSuccessOtp(response);


            }

            @Override
            public void onFailure(Call<PasswordOTP> call, Throwable t) {
                getForgotOtp.error(t.getMessage());
            }
        });

    }

    public interface GetForgotOtp {
        void onSuccessOtp(Response<PasswordOTP> response);

        void error(String error);
    }
    /*+++++++++++++++++++++END++++++++++++++++++++++++++*/

    /*+++++++++++++++++++++Send otp verification++++++++++++++++++++++++++*/

    public void setSendOtpForget(String email, String otp) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<PasswordOTP> call = apiInterface.sendPasswrodOtp(email, otp);
        call.enqueue(new Callback<PasswordOTP>() {
            @Override
            public void onResponse(Call<PasswordOTP> call, Response<PasswordOTP> response) {

                sendOtpForget.onSuccessSend(response);

            }

            @Override
            public void onFailure(Call<PasswordOTP> call, Throwable t) {
                sendOtpForget.error(t.getMessage());
            }
        });

    }

    public interface SendOtpForget {
        void onSuccessSend(Response<PasswordOTP> response);

        void error(String error);
    }
    /*++++++++++++++++++++++++++++++END+++++++++++++++++++++++++++*/

    /*++++++++++++++++++++++++++++++Reset Password+++++++++++++++++++++++++++*/

    public void setGetResetPassword(String email, String otp, String password, String cpassword) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<PasswordReset> call = apiInterface.passwordReset(email, otp, password, cpassword);
        call.enqueue(new Callback<PasswordReset>() {
            @Override
            public void onResponse(Call<PasswordReset> call, Response<PasswordReset> response) {

                resetPassword.onSuccessReset(response);

            }

            @Override
            public void onFailure(Call<PasswordReset> call, Throwable t) {
                resetPassword.error(t.getMessage());
            }
        });

    }

    public interface ResetPassword {
        void onSuccessReset(Response<PasswordReset> resetResponse);

        void error(String error);
    }
    /*++++++++++++++++++++++++++++++END+++++++++++++++++++++++++++*/

    /*++++++++++++++++++++++++++++++EDIT PROFILE+++++++++++++++++++++++++++*/
    public Controller(EditProfile editProfile1, GetProfileDetails getProfileDetails1) {
        editProfile = editProfile1;
        getProfileDetails = getProfileDetails1;
    }

    public void setEditProfile(String userid, String address1, String address2, String city, String postcode, String country, String name, String contact) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<EditProfileApi> call = apiInterface.editProfle(userid, name, contact, postcode, address1, address2, city, country);
        call.enqueue(new Callback<EditProfileApi>() {
            @Override
            public void onResponse(Call<EditProfileApi> call, Response<EditProfileApi> response) {

                editProfile.onSucessEdit(response);

            }

            @Override
            public void onFailure(Call<EditProfileApi> call, Throwable t) {
                editProfile.error(t.getMessage());
            }
        });

    }

    public interface EditProfile {
        void onSucessEdit(Response<EditProfileApi> response);

        void error(String error);
    }
    /*++++++++++++++++++++++++++++++END+++++++++++++++++++++++++++*/

    /************++++++++++++++GET PRofile detaisl+++++++++++++++++++**/
    public Controller(GetProfileDetails getProfileDetails1, ProfileImage profileImage1, GetMandateList getMandateList1, Forgive forgive1, CompleteAgreement completeAgreement1) {
        getProfileDetails = getProfileDetails1;
        profileImage = profileImage1;
        getMandateList = getMandateList1;
        forgive = forgive1;
        completeAgreement = completeAgreement1;

    }

    public void setGetProfileDetails(String userId) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetProfileDetailsApi> call = apiInterface.getProfileDetails(userId);
        call.enqueue(new Callback<GetProfileDetailsApi>() {
            @Override
            public void onResponse(Call<GetProfileDetailsApi> call, Response<GetProfileDetailsApi> response) {

                getProfileDetails.onSucessProfile(response);

            }

            @Override
            public void onFailure(Call<GetProfileDetailsApi> call, Throwable t) {
                getProfileDetails.error(t.getMessage());
            }
        });

    }


    public interface GetProfileDetails {
        void onSucessProfile(Response<GetProfileDetailsApi> response);

        void error(String error);
    }
    /*++++++++++++++++END++++++++++++++++++ */

    /*++++++++++++++++ADD invoice with++++++++++++++++++*/
    public Controller(AddInvoiceText addInvoiceText1, AddInvoiceVideo addInvoiceVideo1) {
        addInvoiceText = addInvoiceText1;
        addInvoiceVideo = addInvoiceVideo1;

    }

    public void setAddInvoiceText(String userId, String currency, String price, String commecnt, String type, MultipartBody.Part bil) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);

        Call<InvoiceApis> call = apiInterface.addInvoice(userId, currency, price, commecnt, type, bil);
        call.enqueue(new Callback<InvoiceApis>() {
            @Override
            public void onResponse(Call<InvoiceApis> call, Response<InvoiceApis> response) {

                addInvoiceText.onSucessText(response);

            }

            @Override
            public void onFailure(Call<InvoiceApis> call, Throwable t) {
                addInvoiceText.error(t.getMessage());
            }
        });
    }

    public interface AddInvoiceText {
        void onSucessText(Response<InvoiceApis> response);

        void error(String error);
    }
    /*++++++++++++++++++++END+++++++++++++++++++++*/

    /*+++++++++++++++++++++++Add invoice with video++++++++++++++++++++*/
    public void setAddInvoiceVideo(String userId, String currency, String price, String type, MultipartBody.Part bil, MultipartBody.Part video) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);

        Call<InvoiceApis> call = apiInterface.addInvoiceVideo(userId, currency, price, type, bil, video);
        call.enqueue(new Callback<InvoiceApis>() {
            @Override
            public void onResponse(Call<InvoiceApis> call, Response<InvoiceApis> response) {

                addInvoiceVideo.onSucessVideo(response);

            }

            @Override
            public void onFailure(Call<InvoiceApis> call, Throwable t) {
                addInvoiceVideo.error(t.getMessage());
            }
        });
    }

    public interface AddInvoiceVideo {
        void onSucessVideo(Response<InvoiceApis> response);

        void error(String error);
    }
    /*+++++++++++++++++++++++++END++++++++++++++++++++++++++++++*/

    /*++++++++++++++++++ADD Profile Picture++++++++++++++++++*/

    public void setProfileImage(String userId, MultipartBody.Part image) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<MessageApis> call = apiInterface.addProfileImage(userId, image);
        call.enqueue(new Callback<MessageApis>() {
            @Override
            public void onResponse(Call<MessageApis> call, Response<MessageApis> response) {

                profileImage.onSuccessADD(response);

            }

            @Override
            public void onFailure(Call<MessageApis> call, Throwable t) {
                profileImage.error(t.getMessage());
            }
        });

    }

    public interface ProfileImage {
        void onSuccessADD(Response<MessageApis> response);

        void error(String error);
    }
    /*++++++++++++++++++++++++END++++++++++++++++++++*/

    /*+++++++++++++++Get Invoice Data++++++++++++++++++++++*/

    public Controller(GetInvoice getInvoice1, AddMandate addMandate1, GetProfileDetails getProfileDetails1, UpdateTranstion updateTranstion1) {
        getInvoice = getInvoice1;
        addMandate = addMandate1;
        getProfileDetails = getProfileDetails1;
        updateTranstion = updateTranstion1;
    }

    public void setGetInvoice(String invoiceId) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetInvoiceApi> call = apiInterface.getPayScreen(invoiceId);
        call.enqueue(new Callback<GetInvoiceApi>() {
            @Override
            public void onResponse(Call<GetInvoiceApi> call, Response<GetInvoiceApi> response) {

                getInvoice.onSucess(response);

            }

            @Override
            public void onFailure(Call<GetInvoiceApi> call, Throwable t) {
                getInvoice.error(t.getMessage());
            }
        });
    }

    public interface GetInvoice {
        void onSucess(Response<GetInvoiceApi> response);

        void error(String error);
    }
    /*++++++++++++++++++++++++=END+++++++++++++++++++++++++++*/

    /*++++++++++++++++++++++++Add Mandate +++++++++++++++++++=*/

    public void setAddMandate(String invoiceId, String commisionType, String commission, String appMoney, String month, String transtionId, String moneyType, MultipartBody.Part sign) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<AddMandateApi> call = apiInterface.addMandate(invoiceId, commisionType, commission, appMoney, transtionId, month, moneyType, sign);
        call.enqueue(new Callback<AddMandateApi>() {
            @Override
            public void onResponse(Call<AddMandateApi> call, Response<AddMandateApi> response) {

                addMandate.onsuccesMandate(response);

            }

            @Override
            public void onFailure(Call<AddMandateApi> call, Throwable t) {
                addMandate.error(t.getMessage());
            }
        });
    }

    public interface AddMandate {
        void onsuccesMandate(Response<AddMandateApi> response);

        void error(String error);
    }
    /*+++++++++++++++++++++++END++++++++++++++++++++++++*/

    /*++++++++++++++++AGreement+++++++++++++++*/

    public Controller(Agreement agreement1) {
        agreement = agreement1;
    }

    public void setAgreement(String invoiceId) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<AgreeMentApi> call = apiInterface.agreement(invoiceId);
        call.enqueue(new Callback<AgreeMentApi>() {
            @Override
            public void onResponse(Call<AgreeMentApi> call, Response<AgreeMentApi> response) {
                agreement.onSucessA(response);

            }

            @Override
            public void onFailure(Call<AgreeMentApi> call, Throwable t) {
                agreement.error(t.getMessage());
            }
        });
    }

    public interface Agreement {
        void onSucessA(Response<AgreeMentApi> response);

        void error(String error);
    }
    /*+++++++++++++++END+++++++++++++++++++++*/

    /*+++++++++++++++++++++++Get the Reciver Data++++++++++++++=*/

    public Controller(ReciverData reciverData1, ChooseOne chooseOne1, ChooseAccept chooseAccept1, Agreement agreement1, UpdateTranstion updateTranstion1) {
        reciverData = reciverData1;
        chooseOne = chooseOne1;
        chooseAccept = chooseAccept1;
        agreement = agreement1;
        updateTranstion = updateTranstion1;
    }

    public void setReciverData(String invoiceId) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<ReciverApis> call = apiInterface.reciver(invoiceId);
        call.enqueue(new Callback<ReciverApis>() {
            @Override
            public void onResponse(Call<ReciverApis> call, Response<ReciverApis> response) {

                reciverData.onSucess(response);
            }

            @Override
            public void onFailure(Call<ReciverApis> call, Throwable t) {
                reciverData.error(t.getMessage());
            }
        });

    }

    public interface ReciverData {
        void onSucess(Response<ReciverApis> response);

        void error(String error);
    }
    /*++++++++++++++++++END+++++++++++++++++++++*/

    /*+++++++++++++CHooose One++++++++++++=*/
    public void setChooseOne(String invoiceId, String userId, String selctone) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<ChooseOneApis> call = apiInterface.chooseOne(invoiceId, userId, selctone);
        call.enqueue(new Callback<ChooseOneApis>() {
            @Override
            public void onResponse(Call<ChooseOneApis> call, Response<ChooseOneApis> response) {

                chooseOne.onSucessChoose(response);

            }

            @Override
            public void onFailure(Call<ChooseOneApis> call, Throwable t) {
                chooseOne.error(t.getMessage());
            }
        });
    }

    public interface ChooseOne {
        void onSucessChoose(Response<ChooseOneApis> response);

        void error(String error);
    }
    /*++++++END+++++++++++*/

    /*++++++++++++++++++++++++Choose one accept+++++++++++++*/
    public void setChooseAccept(String invoiceId, String userId, String selctone, MultipartBody.Part sign, String transation) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<ChooseOneApis> call = apiInterface.chooseAccept(invoiceId, userId, selctone, transation, sign);
        call.enqueue(new Callback<ChooseOneApis>() {
            @Override
            public void onResponse(Call<ChooseOneApis> call, Response<ChooseOneApis> response) {

                chooseAccept.onSucessAccept(response);

            }

            @Override
            public void onFailure(Call<ChooseOneApis> call, Throwable t) {
                chooseAccept.error(t.getMessage());
            }
        });
    }

    public interface ChooseAccept {
        void onSucessAccept(Response<ChooseOneApis> response);

        void error(String error);
    }
    /*+++++++++++++END++++++++++++++++*/
    /*++++++++++++++++++++++AdjustApi+++++++++++++++++++++*/

    public Controller(Adjust adjust1) {
        adjust = adjust1;
    }

    public void setAdjust(String invoiceId, String userId, String type, String amount, String month) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<AdjustApi> call = apiInterface.adjust(invoiceId, userId, type, amount, month);
        call.enqueue(new Callback<AdjustApi>() {
            @Override
            public void onResponse(Call<AdjustApi> call, Response<AdjustApi> response) {

                adjust.onSucessAdjust(response);

            }

            @Override
            public void onFailure(Call<AdjustApi> call, Throwable t) {
                adjust.error(t.getMessage());
            }
        });

    }

    public interface Adjust {
        void onSucessAdjust(Response<AdjustApi> response);

        void error(String error);

    }
    /*++++++++++++++++++++++END*/

    /*++++++++++++++++Mandate List ++++++++++++++++++++++*/

    public Controller(GetMandateList getMandateList1, Forgive forgive1, CompleteAgreement completeAgreement1) {
        getMandateList = getMandateList1;
        forgive = forgive1;
        completeAgreement = completeAgreement1;
    }

    public void setGetMandateList(String userId) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<MandateListApis> call = apiInterface.getMandateList(userId);
        call.enqueue(new Callback<MandateListApis>() {
            @Override
            public void onResponse(Call<MandateListApis> call, Response<MandateListApis> response) {

                getMandateList.onSucessMandate(response);

            }

            @Override
            public void onFailure(Call<MandateListApis> call, Throwable t) {
                getMandateList.error(t.getMessage());
            }
        });
    }

    public interface GetMandateList {
        void onSucessMandate(Response<MandateListApis> response);

        void error(String error);
    }
    /*++++++++++++++END+++++++++++++++++*/

    /*+++++++++++++++Forgive api++++++++++++++*/

    public void setForgive(String userId, String invoiceId) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<ForgiveApi> call = apiInterface.forgive(userId, invoiceId);
        call.enqueue(new Callback<ForgiveApi>() {
            @Override
            public void onResponse(Call<ForgiveApi> call, Response<ForgiveApi> response) {

                forgive.onSucessForgive(response);

            }

            @Override
            public void onFailure(Call<ForgiveApi> call, Throwable t) {
                forgive.error(t.getMessage());
            }
        });
    }

    public interface Forgive {
        void onSucessForgive(Response<ForgiveApi> response);

        void error(String error);
    }
    /*+++++++++++++END++++++++++++++*/

    /*+++++++++++++++COmpleted apis  api++++++++++++++*/

    public void setCompleteAgreement(String userId, String invoiceId) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<AgreementCompletedApi> call = apiInterface.completed(userId, invoiceId);
        call.enqueue(new Callback<AgreementCompletedApi>() {
            @Override
            public void onResponse(Call<AgreementCompletedApi> call, Response<AgreementCompletedApi> response) {

                completeAgreement.onSucessComplete(response);

            }

            @Override
            public void onFailure(Call<AgreementCompletedApi> call, Throwable t) {
                completeAgreement.error(t.getMessage());
            }
        });
    }

    public interface CompleteAgreement {
        void onSucessComplete(Response<AgreementCompletedApi> response);

        void error(String error);
    }
    /*+++++++++++++END++++++++++++++*/

    /*++++++++++++++++++++++=Get Invoce+++++++++++++*/

    public Controller(InvoiceImage invoiceImage1) {
        invoiceImage = invoiceImage1;

    }

    public void setInvoiceImage(String id) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<InvoiceApi> call = apiInterface.invoice(id);
        call.enqueue(new Callback<InvoiceApi>() {
            @Override
            public void onResponse(Call<InvoiceApi> call, Response<InvoiceApi> response) {
                invoiceImage.onSucess(response);
            }

            @Override
            public void onFailure(Call<InvoiceApi> call, Throwable t) {
                invoiceImage.error(t.getMessage());
            }
        });
    }

    public interface InvoiceImage {
        void onSucess(Response<InvoiceApi> response);

        void error(String error);
    }
    /*+++++++++++++++++END++++++++++++*/

    /*+++++++++++++++Update Transtion++++++++++++=*/

    public void setUpdateTranstion(String userId, String invoiceId, String trantion) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<MessageApis> call = apiInterface.updatePayment(userId, invoiceId, trantion);
        call.enqueue(new Callback<MessageApis>() {
            @Override
            public void onResponse(Call<MessageApis> call, Response<MessageApis> response) {
                updateTranstion.onSucessTranstion(response);
            }

            @Override
            public void onFailure(Call<MessageApis> call, Throwable t) {
                updateTranstion.error(t.getMessage());
            }
        });
    }

    public interface UpdateTranstion {
        void onSucessTranstion(Response<MessageApis> response);

        void error(String error);
    }
    /*+++++++++++++++++END+++++++++++*/
}
