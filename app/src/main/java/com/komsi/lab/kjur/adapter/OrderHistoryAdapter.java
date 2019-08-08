package com.komsi.lab.kjur.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.komsi.lab.kjur.OrderDetailActivity;
import com.komsi.lab.kjur.R;
import com.komsi.lab.kjur.model.OrderHistoryDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.CustomViewHolder>{

    List<OrderHistoryDetail> orderDetails;
    List<String> colors;

    public OrderHistoryAdapter(List<OrderHistoryDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_order, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        OrderHistoryDetail orderDetail = orderDetails.get(position);
        holder.tvId.setText(orderDetail.getId());
        holder.tvDateTime.setText(orderDetail.getDateUpdate());
        holder.tvStatus.setText(orderDetail.getStatus());
        holder.tvTotal.setText(String.valueOf(orderDetail.getJumlahBayar()));
        holder.id = orderDetails.get(position).getId();
        holder.paymentMethodId = orderDetails.get(position).getJenisBayarId();

        colors = new ArrayList<String>();

        colors.add("#e51c23");
        colors.add("#e91e63");
        colors.add("#9c27b0");
        colors.add("#673ab7");
        colors.add("#3f51b5");
        colors.add("#5677fc");
        colors.add("#03a9f4");
        colors.add("#00bcd4");
        colors.add("#009688");
        colors.add("#259b24");
        colors.add("#8bc34a");
        colors.add("#cddc39");
        colors.add("#ffeb3b");
        colors.add("#ff9800");
        colors.add("#ff5722");
        colors.add("#795548");
        colors.add("#9e9e9e");
        colors.add("#607d8b");

        Random r = new Random();
        int i1 = r.nextInt(17- 0) + 0;

        holder.viewColor.setBackgroundColor(Color.parseColor(colors.get(i1)));
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvId, tvDateTime, tvStatus, tvTotal;
        private String id;
        private int paymentMethodId;
        private View viewColor;

        public CustomViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
            tvId = itemView.findViewById(R.id.tvId);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            viewColor = itemView.findViewById(R.id.viewColor);
        }

        public void onClick(View v) {
            Intent intent = new Intent(itemView.getContext() , OrderDetailActivity.class);
            intent.putExtra("orderId" , id);
            intent.putExtra("paymentMethodId", paymentMethodId);
            itemView.getContext().startActivity(intent);
        }
    }

    public void refreshEvents(List<OrderHistoryDetail> orderDetails) {
        this.orderDetails.clear();
        this.orderDetails.addAll(orderDetails);
        notifyDataSetChanged();
    }
}