package com.komsi.lab.kjur.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.komsi.lab.kjur.ProductListActivity;
import com.komsi.lab.kjur.R;
import com.komsi.lab.kjur.model.Location;
import com.komsi.lab.kjur.model.Stores;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StoresAdapter extends RecyclerView.Adapter<StoresAdapter.CustomViewHolder>{

    List<Stores> storesList;

    public StoresAdapter(List<Stores> storesList) {
        this.storesList = storesList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_stores, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Stores stores = storesList.get(position);
        holder.tvStoreName.setText(stores.getStoreName());
        holder.id = storesList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return storesList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvStoreName;
        private String id;

        public CustomViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
            tvStoreName = itemView.findViewById(R.id.tvStoreName);
        }

        public void onClick(View v) {
            Intent intent = new Intent(itemView.getContext() , ProductListActivity.class);
            intent.putExtra("storeId" , id);
            itemView.getContext().startActivity(intent);
        }
    }

    public void refreshEvents(List<Stores> storesList) {
        this.storesList.clear();
        this.storesList.addAll(storesList);
        notifyDataSetChanged();
    }
}