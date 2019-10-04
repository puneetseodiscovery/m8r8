package com.m8sworld.m8r8.reciver;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReciverApis {
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
        @SerializedName("fees_deposit_type")
        @Expose
        private String feesDepositType;
        @SerializedName("sign_image")
        @Expose
        private String signImage;


        public String getSignImage() {
            return signImage;
        }

        public void setSignImage(String signImage) {
            this.signImage = signImage;
        }

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

        public String getFeesDepositType() {
            return feesDepositType;
        }

        public void setFeesDepositType(String feesDepositType) {
            this.feesDepositType = feesDepositType;
        }


    }
}
