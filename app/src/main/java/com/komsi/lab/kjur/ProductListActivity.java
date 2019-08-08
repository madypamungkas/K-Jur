package com.komsi.lab.kjur;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Toast;

import com.komsi.lab.kjur.adapter.ProductListAdapter;
import com.komsi.lab.kjur.model.Product;
import com.komsi.lab.kjur.model.ProductListResponse;
import com.komsi.lab.kjur.model.User;
import com.komsi.lab.kjur.api.RetrofitClient;
import com.komsi.lab.kjur.storage.SharedPrefManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends AppCompatActivity {
    private ArrayList<Product> productList;
    private RecyclerView recyclerView;
    private ProductListAdapter plAdapter;
    private SwipeRefreshLayout swipeContainer;
    Context mContext;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        mContext = this;

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
                if (plAdapter != null) {
                    plAdapter.refreshEvents(productList);
                }
                listProduct();
            }
        });

        swipeContainer.setColorSchemeResources(R.color.light_green);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loading = ProgressDialog.show(ProductListActivity.this, null, getString(R.string.please_wait), true, false);
        listProduct();
    }

    private void listProduct() {
        Intent intent = getIntent();
        String storeId = intent.getStringExtra("storeId");

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<ProductListResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .productList("Bearer " + token, storeId);

        call.enqueue(new Callback<ProductListResponse>() {

            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    productList = response.body().getProduct();
                    recyclerView = (RecyclerView) findViewById(R.id.rvProduct);
                    plAdapter = new ProductListAdapter(productList, mContext);
                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(eLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(plAdapter);
                    if( eLayoutManager.getItemCount() == 0 ){
                        //Do something
                    }
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                loading.dismiss();
                swipeContainer.setRefreshing(false);
                Toast.makeText(ProductListActivity.this, "Something wrong. Please try again later.", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}