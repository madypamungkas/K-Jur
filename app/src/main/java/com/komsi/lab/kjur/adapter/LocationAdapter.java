package com.komsi.lab.kjur.adapter;

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
import com.komsi.lab.kjur.StoresListActivity;
import com.komsi.lab.kjur.model.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.CustomViewHolder>{

    List<Location> locations;
    List<String> colors;

    public LocationAdapter(List<Location> locations) {
        this.locations = locations;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_location, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Location location = locations.get(position);
        holder.tvLocName.setText(location.getNama());
        holder.tvLocDesc.setText(location.getDesc());
        holder.id = locations.get(position).getId();
        holder.circleText.setText(location.getNama().substring(0, 1));

        //Random r = new Random();
        //int red=r.nextInt(255 - 0 + 1)+0;
        //int green=r.nextInt(255 - 0 + 1)+0;
        //int blue=r.nextInt(255 - 0 + 1)+0;

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

        GradientDrawable draw = new GradientDrawable();
        draw.setShape(GradientDrawable.OVAL);
        draw.setColor(Color.parseColor(colors.get(i1)));
        holder.circleText.setBackground(draw);
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvLocName, tvLocDesc, circleText;
        private int id;

        public CustomViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
            tvLocName = (TextView)itemView.findViewById(R.id.tvLocName);
            tvLocDesc = (TextView)itemView.findViewById(R.id.tvLocDesc);
            circleText = itemView.findViewById(R.id.circleText);
        }

        public void onClick(View v) {
            Intent intent = new Intent(itemView.getContext() , StoresListActivity.class);
            intent.putExtra("locId" , id);
            itemView.getContext().startActivity(intent);
        }
    }

    public void refreshEvents(List<Location> locations) {
        this.locations.clear();
        this.locations.addAll(locations);
        notifyDataSetChanged();
    }
}