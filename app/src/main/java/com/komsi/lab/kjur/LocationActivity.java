package com.komsi.lab.kjur;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
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

import com.komsi.lab.kjur.adapter.LocationAdapter;
import com.komsi.lab.kjur.model.Location;
import com.komsi.lab.kjur.model.LocationListResponse;
import com.komsi.lab.kjur.model.User;
import com.komsi.lab.kjur.api.RetrofitClient;
import com.komsi.lab.kjur.storage.SharedPrefManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationActivity extends AppCompatActivity {
    private ArrayList<Location> locationList;
    private RecyclerView recyclerView;
    private LocationAdapter lAdapter;
    private SwipeRefreshLayout swipeContainer;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

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
                if (lAdapter != null) {
                    lAdapter.refreshEvents(locationList);
                }
                listLocation();
            }
        });

        swipeContainer.setColorSchemeResources(R.color.light_green);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loading = ProgressDialog.show(LocationActivity.this, null, getString(R.string.please_wait), true, false);
        listLocation();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(LocationActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void listLocation() {
        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<LocationListResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .locStore("Bearer " + token);

        call.enqueue(new Callback<LocationListResponse>() {

            @Override
            public void onResponse(Call<LocationListResponse> call, Response<LocationListResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    locationList = response.body().getLocation();
                    recyclerView = (RecyclerView) findViewById(R.id.rvLoc);
                    lAdapter = new LocationAdapter(locationList);
                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(eLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(lAdapter);
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<LocationListResponse> call, Throwable t) {
                loading.dismiss();
                swipeContainer.setRefreshing(false);
                Toast.makeText(LocationActivity.this, "Something wrong. Please try again later.", Toast.LENGTH_LONG).show();
                Log.d("TAG","Response = "+t.toString());
            }
        });
    }
}