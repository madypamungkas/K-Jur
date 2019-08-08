package com.komsi.lab.kjur.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateOrderResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("result")
    @Expose
    private OrderHistoryDetail orderDetail;

    public CreateOrderResponse(String status, String message, OrderHistoryDetail orderDetail) {
        this.status = status;
        this.message = message;
        this.orderDetail = orderDetail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OrderHistoryDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderHistoryDetail orderDetail) {
        this.orderDetail = orderDetail;
    }
}
