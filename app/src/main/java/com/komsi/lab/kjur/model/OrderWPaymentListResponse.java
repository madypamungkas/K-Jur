package com.komsi.lab.kjur.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Afyad Kafa on 1/28/2019.
 */

public class OrderWPaymentListResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("result")
    @Expose
    private ArrayList<OrderHistoryDetail> orderDetails = null;

    public OrderWPaymentListResponse(String status, ArrayList<OrderHistoryDetail> orderDetails) {
        this.status = status;
        this.orderDetails = orderDetails;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<OrderHistoryDetail> getOrderDetails() {
        return orderDetails;
    }
}
