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

import com.komsi.lab.kjur.adapter.StoresAdapter;
import com.komsi.lab.kjur.api.RetrofitClient;
import com.komsi.lab.kjur.model.Stores;
import com.komsi.lab.kjur.model.StoresListResponse;
import com.komsi.lab.kjur.model.User;
import com.komsi.lab.kjur.storage.SharedPrefManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoresListActivity extends AppCompatActivity {
    private ArrayList<Stores> storesArrayList;
    private RecyclerView recyclerView;
    private StoresAdapter sAdapter;
    private SwipeRefreshLayout swipeContainer;
    Context mContext;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores_list);

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
                if (sAdapter != null) {
                    sAdapter.refreshEvents(storesArrayList);
                }
                listStores();
            }
        });

        swipeContainer.setColorSchemeResources(R.color.colorPrimary);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loading = ProgressDialog.show(StoresListActivity.this, null, getString(R.string.please_wait), true, false);
        listStores();
    }

    private void listStores() {
        Intent intent = getIntent();
        int locId = intent.getIntExtra("locId", 0);

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<StoresListResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .storeList("Bearer " + token, locId);

        call.enqueue(new Callback<StoresListResponse>() {

            @Override
            public void onResponse(Call<StoresListResponse> call, Response<StoresListResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    storesArrayList = response.body().getStores();
                    recyclerView = (RecyclerView) findViewById(R.id.rvStores);
                    sAdapter = new StoresAdapter(storesArrayList);
                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(eLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(sAdapter);
                    if( eLayoutManager.getItemCount() == 0 ){
                        //Do something
                    }
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<StoresListResponse> call, Throwable t) {
                loading.dismiss();
                swipeContainer.setRefreshing(false);
                Toast.makeText(StoresListActivity.this, "Something wrong. Please try again later.", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}
