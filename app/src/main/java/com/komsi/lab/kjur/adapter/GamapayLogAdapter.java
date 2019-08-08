package com.komsi.lab.kjur.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.komsi.lab.kjur.R;
import com.komsi.lab.kjur.model.GamapayLog;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

/**
 * Created by Afyad Kafa on 1/28/2019.
 */

public class GamapayLogAdapter extends RecyclerView.Adapter<GamapayLogAdapter.CustomViewHolder>{

    List<GamapayLog> gamapayLogs;

    public GamapayLogAdapter(List<GamapayLog> gamapayLogs) {
        this.gamapayLogs = gamapayLogs;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_log_balance, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        GamapayLog gamapayLog = gamapayLogs.get(position);

        DecimalFormat fmt = new DecimalFormat();
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();

        fmts.setGroupingSeparator('.');
        fmt.setGroupingSize(3);
        fmt.setGroupingUsed(true);
        fmt.setDecimalFormatSymbols(fmts);

        holder.tvRef.setText(String.valueOf(gamapayLog.getReferenceId()));
        holder.tvDateTime.setText(gamapayLog.getDateTime());
        holder.tvTotal.setText(String.valueOf(fmt.format(gamapayLog.getTotal())));
        holder.tvType.setText(gamapayLog.getType());
        holder.tvBalance.setText(String.valueOf(fmt.format(gamapayLog.getGrandTotal())));
    }

    @Override
    public int getItemCount() {
        return gamapayLogs.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        private TextView tvRef, tvDateTime, tvTotal, tvType, tvBalance;

        public CustomViewHolder(View view) {
            super(view);
            tvRef = itemView.findViewById(R.id.tvRef);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvType = itemView.findViewById(R.id.tvType);
            tvBalance = itemView.findViewById(R.id.tvBalance);
        }
    }
}