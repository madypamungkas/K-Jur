package com.komsi.lab.kjur.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.komsi.lab.kjur.OrderDetailActivity;
import com.komsi.lab.kjur.PaymentActivity;
import com.komsi.lab.kjur.R;
import com.komsi.lab.kjur.model.PayConfirmResponse;
import com.komsi.lab.kjur.model.Payment;
import com.komsi.lab.kjur.model.User;
import com.komsi.lab.kjur.api.RetrofitClient;
import com.komsi.lab.kjur.storage.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Afyad Kafa on 1/28/2019.
 */

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.CustomViewHolder>{
    private int mSelectedItem = -1;
    List<Payment> payments;
    Context context;
    ProgressDialog loading;
    //private CompoundButton lastCheckedRB = null;

    public PaymentAdapter(Context context, List<Payment> payments) {
        this.context = context;
        this.payments = payments;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_payment, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Payment payment = payments.get(position);
        holder.tvPayName.setText(payment.getJenisBayar());
        holder.tvPayDesc.setText(payment.getDesc());
        holder.id = payments.get(position).getId();
        holder.rbPay.setChecked(position == mSelectedItem);
        if(position == mSelectedItem){
            holder.layoutDetail.setVisibility(View.VISIBLE);
        }
        else{
            holder.layoutDetail.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return payments.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPayName, tvPayDesc;
        private RadioButton rbPay;
        private int id;
        private Button btnConfirmPayment;
        private LinearLayout layoutDetail;

        public CustomViewHolder(View view) {
            super(view);
            tvPayName = itemView.findViewById(R.id.tvPayName);
            tvPayDesc = itemView.findViewById(R.id.tvPayDesc);
            rbPay = itemView.findViewById(R.id.rbPay);
            btnConfirmPayment = itemView.findViewById(R.id.btnConfirmPayment);
            layoutDetail = itemView.findViewById(R.id.layoutDetail);

            View.OnClickListener l = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0, payments.size());

                }
            };

            itemView.setOnClickListener(l);
            rbPay.setOnClickListener(l);

            btnConfirmPayment.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    loading = ProgressDialog.show(context, null, context.getResources().getString(R.string.please_wait), true, false);
                    paymentConfirm();
                }
            });
        }

        private void paymentConfirm() {
            Intent intent = ((PaymentActivity) context).getIntent();
            final String orderId = intent.getStringExtra("orderId");

            User user = SharedPrefManager.getInstance(context).getUser();
            String token = user.getToken();

            Call<PayConfirmResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .paymentConfirm("Bearer " + token, orderId, id);

            call.enqueue(new Callback<PayConfirmResponse>() {

                @Override
                public void onResponse(Call<PayConfirmResponse> call, Response<PayConfirmResponse> response) {
                    loading.dismiss();
                    if (response.isSuccessful()) {
                        PayConfirmResponse payConfirmResponse = response.body();
                        if (payConfirmResponse.getStatus().equals("success")) {
                            Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(itemView.getContext() , OrderDetailActivity.class);
                            intent.putExtra("orderId" , orderId);
                            intent.putExtra("payMethodId", id);
                            itemView.getContext().startActivity(intent);
                        } else {
                            Toast.makeText(context, payConfirmResponse.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<PayConfirmResponse> call, Throwable t) {
                    loading.dismiss();
                    Toast.makeText(context, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                    Log.d("TAG", "Response = " + t.toString());
                }
            });
        }
    }
}