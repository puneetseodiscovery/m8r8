package com.m8sworld.m8r8.mandate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddMandateApi {
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

    public class Data {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("created_by")
        @Expose
        private Integer createdBy;
        @SerializedName("currency")
        @Expose
        private String currency;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("bill_image")
        @Expose
        private String billImage;
        @SerializedName("video_text")
        @Expose
        private String videoText;
        @SerializedName("msg_type")
        @Expose
        private String msgType;
        @SerializedName("interest_type")
        @Expose
        private String interestType;
        @SerializedName("interest")
        @Expose
        private String interest;
        @SerializedName("app_fees")
        @Expose
        private String appFees;
        @SerializedName("transaction_id")
        @Expose
        private String transactionId;
        @SerializedName("fees_deposit_type")
        @Expose
        private String feesDepositType;
        @SerializedName("created_by_sign")
        @Expose
        private String createdBySign;
        @SerializedName("pdf")
        @Expose
        private String pdf;
        @SerializedName("months")
        @Expose
        private String months;
        @SerializedName("total_payable_amount")
        @Expose
        private String totalPayableAmount;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Integer createdBy) {
            this.createdBy = createdBy;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getBillImage() {
            return billImage;
        }

        public void setBillImage(String billImage) {
            this.billImage = billImage;
        }

        public String getVideoText() {
            return videoText;
        }

        public void setVideoText(String videoText) {
            this.videoText = videoText;
        }

        public String getMsgType() {
            return msgType;
        }

        public void setMsgType(String msgType) {
            this.msgType = msgType;
        }

        public String getInterestType() {
            return interestType;
        }

        public void setInterestType(String interestType) {
            this.interestType = interestType;
        }

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public String getAppFees() {
            return appFees;
        }

        public void setAppFees(String appFees) {
            this.appFees = appFees;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getFeesDepositType() {
            return feesDepositType;
        }

        public void setFeesDepositType(String feesDepositType) {
            this.feesDepositType = feesDepositType;
        }

        public String getCreatedBySign() {
            return createdBySign;
        }

        public void setCreatedBySign(String createdBySign) {
            this.createdBySign = createdBySign;
        }

        public String getPdf() {
            return pdf;
        }

        public void setPdf(String pdf) {
            this.pdf = pdf;
        }

        public String getMonths() {
            return months;
        }

        public void setMonths(String months) {
            this.months = months;
        }

        public String getTotalPayableAmount() {
            return totalPayableAmount;
        }

        public void setTotalPayableAmount(String totalPayableAmount) {
            this.totalPayableAmount = totalPayableAmount;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }
}
