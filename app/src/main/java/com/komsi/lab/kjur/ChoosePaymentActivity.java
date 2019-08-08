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

import com.komsi.lab.kjur.adapter.OrderCPayAdapter;
import com.komsi.lab.kjur.api.RetrofitClient;
import com.komsi.lab.kjur.model.OrderCPaymentListResponse;
import com.komsi.lab.kjur.model.OrderHistoryDetail;
import com.komsi.lab.kjur.model.User;
import com.komsi.lab.kjur.storage.SharedPrefManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoosePaymentActivity extends AppCompatActivity {
    private ArrayList<OrderHistoryDetail> orderDetailList;
    private RecyclerView recyclerView;
    private OrderCPayAdapter ocpAdapter;
    private SwipeRefreshLayout swipeContainer;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_payment);

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
                if (ocpAdapter != null) {
                    ocpAdapter.refreshEvents(orderDetailList);
                }
                orderCPay();
            }
        });

        swipeContainer.setColorSchemeResources(R.color.light_green);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loading = ProgressDialog.show(ChoosePaymentActivity.this, null, getString(R.string.please_wait), true, false);
        orderCPay();
    }

    @Override
    public void onBackPressed() {
        Intent i= new Intent(ChoosePaymentActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void orderCPay() {
        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<OrderCPaymentListResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .paymentChoose("Bearer " + token);

        call.enqueue(new Callback<OrderCPaymentListResponse>() {

            @Override
            public void onResponse(Call<OrderCPaymentListResponse> call, Response<OrderCPaymentListResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    orderDetailList = response.body().getOrderDetails();
                    recyclerView = (RecyclerView) findViewById(R.id.rvOrder);
                    ocpAdapter = new OrderCPayAdapter(orderDetailList);
                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(eLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(ocpAdapter);
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<OrderCPaymentListResponse> call, Throwable t) {
                loading.dismiss();
                swipeContainer.setRefreshing(false);
                Log.d("TAG","Response = "+t.toString());
            }
        });
    }
}
