package com.m8sworld.m8r8.myprofile.adapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MandateListApis {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private Integer message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getMessage() {
        return message;
    }

    public void setMessage(Integer message) {
        this.message = message;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("lender_id")
        @Expose
        private Integer lenderId;
        @SerializedName("borrower_id")
        @Expose
        private Integer borrowerId;
        @SerializedName("invoice_id")
        @Expose
        private Integer invoiceId;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("pdf_invoice")
        @Expose
        private String pdfInvoice;
        @SerializedName("user_type")
        @Expose
        private String userType;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getLenderId() {
            return lenderId;
        }

        public void setLenderId(Integer lenderId) {
            this.lenderId = lenderId;
        }

        public Integer getBorrowerId() {
            return borrowerId;
        }

        public void setBorrowerId(Integer borrowerId) {
            this.borrowerId = borrowerId;
        }

        public Integer getInvoiceId() {
            return invoiceId;
        }

        public void setInvoiceId(Integer invoiceId) {
            this.invoiceId = invoiceId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getPdfInvoice() {
            return pdfInvoice;
        }

        public void setPdfInvoice(String pdfInvoice) {
            this.pdfInvoice = pdfInvoice;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

    }
}
