package com.komsi.lab.kjur;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.komsi.lab.kjur.adapter.OrderWPayAdapter;
import com.komsi.lab.kjur.api.RetrofitClient;
import com.komsi.lab.kjur.model.OrderHistoryDetail;
import com.komsi.lab.kjur.model.OrderWPaymentListResponse;
import com.komsi.lab.kjur.model.User;
import com.komsi.lab.kjur.storage.SharedPrefManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaitingPaymentActivity extends AppCompatActivity {
    private ArrayList<OrderHistoryDetail> orderDetailList;
    private RecyclerView recyclerView;
    private OrderWPayAdapter owpAdapter;
    private SwipeRefreshLayout swipeContainer;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_payment);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (owpAdapter != null) {
                    owpAdapter.refreshEvents(orderDetailList);
                }
                orderWPay();
            }
        });

        swipeContainer.setColorSchemeResources(R.color.light_green);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loading = ProgressDialog.show(WaitingPaymentActivity.this, null, getString(R.string.please_wait), true, false);
        orderWPay();
    }

    @Override
    public void onBackPressed() {
        Intent i= new Intent(WaitingPaymentActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void orderWPay() {
        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<OrderWPaymentListResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .paymentWaiting("Bearer " + token);

        call.enqueue(new Callback<OrderWPaymentListResponse>() {

            @Override
            public void onResponse(Call<OrderWPaymentListResponse> call, Response<OrderWPaymentListResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    orderDetailList = response.body().getOrderDetails();
                    recyclerView = (RecyclerView) findViewById(R.id.rvOrder);
                    owpAdapter = new OrderWPayAdapter(orderDetailList);
                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(eLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(owpAdapter);
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<OrderWPaymentListResponse> call, Throwable t) {
                loading.dismiss();
                swipeContainer.setRefreshing(false);
                Log.d("TAG","Response = "+t.toString());
            }
        });
    }
}
