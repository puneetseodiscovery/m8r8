package com.m8sworld.m8r8.videoupload;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceApis {

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

        @SerializedName("created_by")
        @Expose
        private String createdBy;
        @SerializedName("currency")
        @Expose
        private String currency;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("bill_image")
        @Expose
        private String billImage;
        @SerializedName("video_text")
        @Expose
        private String videoText;
        @SerializedName("msg_type")
        @Expose
        private String msgType;
        @SerializedName("id")
        @Expose
        private Integer id;

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
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

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
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

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

    }

}
