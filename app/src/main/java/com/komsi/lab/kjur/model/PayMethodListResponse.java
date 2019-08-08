package com.komsi.lab.kjur.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Afyad Kafa on 1/28/2019.
 */

public class PayMethodListResponse {
    @SerializedName("result")
    @Expose
    private ArrayList<Payment> payment = null;

    public PayMethodListResponse(ArrayList<Payment> payment) {
        this.payment = payment;
    }

    public ArrayList<Payment> getPayment() {
        return payment;
    }

    public void setPayment(ArrayList<Payment> payment) {
        this.payment = payment;
    }
}
