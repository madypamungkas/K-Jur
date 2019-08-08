package com.komsi.lab.kjur;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.komsi.lab.kjur.adapter.PaymentAdapter;
import com.komsi.lab.kjur.model.PayMethodListResponse;
import com.komsi.lab.kjur.model.Payment;
import com.komsi.lab.kjur.model.User;
import com.komsi.lab.kjur.api.RetrofitClient;
import com.komsi.lab.kjur.storage.SharedPrefManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
    private ArrayList<Payment> paymentList;
    private RecyclerView recyclerView;
    private PaymentAdapter pAdapter;
    ProgressDialog loading;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mContext = this;

        loading = ProgressDialog.show(PaymentActivity.this, null, getString(R.string.please_wait), true, false);

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<PayMethodListResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .paymentList("Bearer " + token);

        call.enqueue(new Callback<PayMethodListResponse>() {

            @Override
            public void onResponse(Call<PayMethodListResponse> call, Response<PayMethodListResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    paymentList = response.body().getPayment();
                    recyclerView = (RecyclerView) findViewById(R.id.rvPayment);
                    pAdapter = new PaymentAdapter(mContext, paymentList);
                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(eLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(pAdapter);
                }
            }

            @Override
            public void onFailure(Call<PayMethodListResponse> call, Throwable t) {
                loading.dismiss();
                Log.d("TAG","Response = "+t.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        confirmOnBackPressed();
    }

    private void confirmOnBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.warning);
        builder.setMessage(R.string.are_you_sure_payment);
        builder.setCancelable(false);
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i= new Intent(PaymentActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        builder.show();
    }
}