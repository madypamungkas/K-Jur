package com.komsi.lab.kjur.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Afyad Kafa on 1/14/2019.
 */

public class DetailUserResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("waiting-payment")
    @Expose
    private String waitPay;

    @SerializedName("choose-method")
    @Expose
    private String choosePay;

    @SerializedName("result")
    @Expose
    private DetailUser detailUser;

    public DetailUserResponse(String status, String message, String email, String waitPay, String choosePay, DetailUser detailUser) {
        this.status = status;
        this.message = message;
        this.email = email;
        this.waitPay = waitPay;
        this.choosePay = choosePay;
        this.detailUser = detailUser;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return email;
    }

    public String getWaitPay() {
        return waitPay;
    }

    public String getChoosePay() {
        return choosePay;
    }

    public DetailUser getDetailUser() {
        return detailUser;
    }
}
