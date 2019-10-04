package com.m8sworld.m8r8.mandate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetInvoiceApi {
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

        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("onePercent")
        @Expose
        private String onePercent;
        @SerializedName("twentyPercent")
        @Expose
        private String twentyPercent;
        @SerializedName("message_type")
        @Expose
        private String messageType;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getOnePercent() {
            return onePercent;
        }

        public void setOnePercent(String onePercent) {
            this.onePercent = onePercent;
        }

        public String getTwentyPercent() {
            return twentyPercent;
        }

        public void setTwentyPercent(String twentyPercent) {
            this.twentyPercent = twentyPercent;
        }

        public String getMessageType() {
            return messageType;
        }

        public void setMessageType(String messageType) {
            this.messageType = messageType;
        }

    }

}
