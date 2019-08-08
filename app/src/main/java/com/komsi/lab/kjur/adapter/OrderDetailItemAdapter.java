package com.komsi.lab.kjur.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.komsi.lab.kjur.R;
import com.komsi.lab.kjur.model.OrderDetailItem;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class OrderDetailItemAdapter extends RecyclerView.Adapter<OrderDetailItemAdapter.CustomViewHolder>{

    List<OrderDetailItem> orderDetailItems;

    public OrderDetailItemAdapter(List<OrderDetailItem> orderDetailItems) {
        this.orderDetailItems = orderDetailItems;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_order_detail_item, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        OrderDetailItem orderDetailItem = orderDetailItems.get(position);

        DecimalFormat fmt = new DecimalFormat();
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();

        fmts.setGroupingSeparator('.');
        fmt.setGroupingSize(3);
        fmt.setGroupingUsed(true);
        fmt.setDecimalFormatSymbols(fmts);
        holder.itemName.setText(orderDetailItem.getProductName());
        holder.itemStoreName.setText(orderDetailItem.getProductStoreName());
        holder.itemQty.setText(String.valueOf(orderDetailItem.getQty()));
        holder.itemPrice.setText(orderDetailItem.getProductPrice());
        holder.itemSubTotal.setText(String.valueOf(fmt.format(orderDetailItem.getTotalBayar())));
    }

    @Override
    public int getItemCount() {
        return orderDetailItems.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        private TextView itemName, itemStoreName, itemQty, itemPrice, itemSubTotal;

        public CustomViewHolder(View view) {
            super(view);
            itemName = itemView.findViewById(R.id.itemName);
            itemStoreName = itemView.findViewById(R.id.itemStoreName);
            itemQty = itemView.findViewById(R.id.itemQty);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemSubTotal = itemView.findViewById(R.id.itemSubTotal);
        }
    }
}