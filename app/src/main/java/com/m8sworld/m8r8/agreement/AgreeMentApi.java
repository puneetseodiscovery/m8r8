package com.m8sworld.m8r8.agreement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AgreeMentApi {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public class UsersData {
        @SerializedName("borrowers")
        @Expose
        private Borrowers borrowers;
        @SerializedName("lenders")
        @Expose
        private Lenders lenders;

        public Borrowers getBorrowers() {
            return borrowers;
        }

        public void setBorrowers(Borrowers borrowers) {
            this.borrowers = borrowers;
        }

        public Lenders getLenders() {
            return lenders;
        }

        public void setLenders(Lenders lenders) {
            this.lenders = lenders;
        }
    }

    public class Lenders {

        @SerializedName("lenders_name")
        @Expose
        private String lendersName;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("address1")
        @Expose
        private String address1;
        @SerializedName("address2")
        @Expose
        private String address2;
        @SerializedName("post_code")
        @Expose
        private String postCode;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;
        @SerializedName("agreement_sign")
        @Expose
        private String agreementSign;

        public String getLendersName() {
            return lendersName;
        }

        public void setLendersName(String lendersName) {
            this.lendersName = lendersName;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }


        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getPostCode() {
            return postCode;
        }

        public void setPostCode(String postCode) {
            this.postCode = postCode;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public String getAgreementSign() {
            return agreementSign;
        }

        public void setAgreementSign(String agreementSign) {
            this.agreementSign = agreementSign;
        }
    }


    public class Borrowers {

        @SerializedName("borrowers_name")
        @Expose
        private String borrowersName;
        @SerializedName("address1")
        @Expose
        private String address1;
        @SerializedName("address2")
        @Expose
        private String address2;
        @SerializedName("post_code")
        @Expose
        private String postCode;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;
        @SerializedName("agreement_sign")
        @Expose
        private String agreementSign;
        @SerializedName("created_at")
        @Expose
        private String createdAt;

        public String getBorrowersName() {
            return borrowersName;
        }

        public void setBorrowersName(String borrowersName) {
            this.borrowersName = borrowersName;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getPostCode() {
            return postCode;
        }

        public void setPostCode(String postCode) {
            this.postCode = postCode;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public String getAgreementSign() {
            return agreementSign;
        }

        public void setAgreementSign(String agreementSign) {
            this.agreementSign = agreementSign;
        }

    }


    public class Data {

        @SerializedName("invoice_id")
        @Expose
        private Integer invoiceId;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("message_type")
        @Expose
        private String messageType;
        @SerializedName("request_content")
        @Expose
        private String requestContent;
        @SerializedName("users_data")
        @Expose
        private UsersData usersData;

        public Integer getInvoiceId() {
            return invoiceId;
        }

        public void setInvoiceId(Integer invoiceId) {
            this.invoiceId = invoiceId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessageType() {
            return messageType;
        }

        public void setMessageType(String messageType) {
            this.messageType = messageType;
        }

        public String getRequestContent() {
            return requestContent;
        }

        public void setRequestContent(String requestContent) {
            this.requestContent = requestContent;
        }

        public UsersData getUsersData() {
            return usersData;
        }

        public void setUsersData(UsersData usersData) {
            this.usersData = usersData;
        }

    }


}
