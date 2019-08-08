package com.komsi.lab.kjur;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.komsi.lab.kjur.adapter.CartAdapter;
import com.komsi.lab.kjur.model.CartListResponse;
import com.komsi.lab.kjur.model.CartProduct;
import com.komsi.lab.kjur.model.CreateOrderResponse;
import com.komsi.lab.kjur.model.RemoveAllCartResponse;
import com.komsi.lab.kjur.model.User;
import com.komsi.lab.kjur.api.RetrofitClient;
import com.komsi.lab.kjur.storage.SharedPrefManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    private ArrayList<CartProduct> cartProductList;
    private TextView tvTotalPrice;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager eLayoutManager;
    private CartAdapter cAdapter;
    private SwipeRefreshLayout swipeContainer;
    private LinearLayout layoutCart, layoutNoData;
    private int mSubTotal = 0;
    private IntentIntegrator qrScan;
    ProgressDialog loading;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        loading = ProgressDialog.show(CartActivity.this, null, getString(R.string.please_wait), true, false);

        mContext = this;
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        layoutCart = findViewById(R.id.layoutCart);
        layoutNoData = findViewById(R.id.layoutNoData);

        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cAdapter != null) {
                    cAdapter.refreshEvents(cartProductList);
                }
                listCart();
            }
        });

        swipeContainer.setColorSchemeResources(R.color.light_green);

        TextView tvDeleteAll = findViewById(R.id.tvRemoveAll);
        tvDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(R.string.confirmation);
                builder.setMessage(R.string.are_you_sure_del_cart);
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
                        removeAll();
                    }
                });

                builder.show();
            }
        });

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button btnConfirmOrder = findViewById(R.id.btnConfirmOrder);
        btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
                createOrder();
            }
        });

        Button btnAddMoreItem = findViewById(R.id.btnAddMoreItem);
        btnAddMoreItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] addFrom = {getString(R.string.stores), getString(R.string.scan_qr)};

                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                builder.setTitle("Add Item from");
                builder.setItems(addFrom, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (getString(R.string.stores).equals(addFrom[which])){
                            startActivity(new Intent(mContext, LocationActivity.class));
                        }
                        else if (getString(R.string.scan_qr).equals(addFrom[which])){
                            qrScan = new IntentIntegrator(CartActivity.this).setCaptureActivity(ScanQrActivity.class);
                            qrScan.setDesiredBarcodeFormats(qrScan.QR_CODE_TYPES);

                            qrScan.initiateScan();
                        }
                    }
                });
                builder.show();
            }
        });


    }

    @Override
    protected void onResume()
    {
        super.onResume();
        listCart();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CartActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void listCart() {
        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<CartListResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .cartList("Bearer " + token);

        call.enqueue(new Callback<CartListResponse>() {

            @Override
            public void onResponse(Call<CartListResponse> call, Response<CartListResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    cartProductList = response.body().getCartProduct();
                    recyclerView = (RecyclerView) findViewById(R.id.rvCart);
                    cAdapter = new CartAdapter(cartProductList, CartActivity.this);
                    eLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(eLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(cAdapter);
                    mSubTotal = cAdapter.grandTotal();
                    tvTotalPrice.setText(String.valueOf(mSubTotal));
                }
                checkList();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<CartListResponse> call, Throwable t) {
                loading.dismiss();
                swipeContainer.setRefreshing(false);
                Log.d("TAG","Response = "+t.toString());
            }
        });
    }

    private void checkList(){
        if (eLayoutManager.getItemCount() == 0) {
            layoutCart.setVisibility(View.GONE);
            layoutNoData.setVisibility(View.VISIBLE);
        }
        else {
            layoutNoData.setVisibility(View.GONE);
            layoutCart.setVisibility(View.VISIBLE);
        }
    }

    private void createOrder() {
        int totalPrice = Integer.parseInt(tvTotalPrice.getText().toString());
        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<CreateOrderResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .createOrder("Bearer " + token, totalPrice);

        call.enqueue(new Callback<CreateOrderResponse>() {

            @Override
            public void onResponse(Call<CreateOrderResponse> call, Response<CreateOrderResponse> response) {
                CreateOrderResponse createOrderResponse = response.body();
                loading.dismiss();
                if (response.isSuccessful()) {
                    if (createOrderResponse.getStatus().equals("success")) {
                        Toast.makeText(mContext, "Order Created", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                        intent.putExtra("orderId", createOrderResponse.getOrderDetail().getId());
                        startActivity(intent);
                    } else {
                        Toast.makeText(mContext, createOrderResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateOrderResponse> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }

    private void removeAll() {
        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<RemoveAllCartResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .removeAll("Bearer " + token);

        call.enqueue(new Callback<RemoveAllCartResponse>() {

            @Override
            public void onResponse(Call<RemoveAllCartResponse> call, Response<RemoveAllCartResponse> response) {
                RemoveAllCartResponse removeAllCartResponse = response.body();
                if (response.isSuccessful()) {
                    Toast.makeText(mContext, "All item was removed", Toast.LENGTH_LONG).show();
                    recreate();
                }
            }

            @Override
            public void onFailure(Call<RemoveAllCartResponse> call, Throwable t) {
                Toast.makeText(mContext, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(mContext, "Scan Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String scanText = result.getContents();
                Intent intent = new Intent(CartActivity.this , ScanQrLoadingActivity.class);
                intent.putExtra("scanText" , scanText);
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
