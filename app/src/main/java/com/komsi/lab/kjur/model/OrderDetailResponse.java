package com.komsi.lab.kjur.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderDetailResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("result")
    @Expose
    private OrderDetail orderDetail;

    @SerializedName("detail_transaksi")
    @Expose
    private ArrayList<OrderDetailItem> orderDetailItem = null;

    public OrderDetailResponse(String status, OrderDetail orderDetail, ArrayList<OrderDetailItem> orderDetailItem) {
        this.status = status;
        this.orderDetail = orderDetail;
        this.orderDetailItem = orderDetailItem;
    }

    public String getStatus() {
        return status;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public ArrayList<OrderDetailItem> getOrderDetailItem() {
        return orderDetailItem;
    }
}
