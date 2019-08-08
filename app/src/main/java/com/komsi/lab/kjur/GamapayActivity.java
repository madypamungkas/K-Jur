package com.komsi.lab.kjur;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.komsi.lab.kjur.adapter.GamapayLogAdapter;
import com.komsi.lab.kjur.api.RetrofitClient;
import com.komsi.lab.kjur.model.GamapayLog;
import com.komsi.lab.kjur.model.GamapayResponse;
import com.komsi.lab.kjur.model.User;
import com.komsi.lab.kjur.storage.SharedPrefManager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GamapayActivity extends AppCompatActivity {
    private ArrayList<GamapayLog> gamapayLogList;
    private RecyclerView recyclerView;
    private GamapayLogAdapter glAdapter;
    private TextView tvBalanceAll, tvAccNumber;
    private SwipeRefreshLayout swipeContainer;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamapay);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvBalanceAll = findViewById(R.id.tvBalanceAll);
        tvAccNumber = findViewById(R.id.tvAccNumber);

        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gamapay();
            }
        });

        swipeContainer.setColorSchemeResources(R.color.light_green);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loading = ProgressDialog.show(GamapayActivity.this, null, getString(R.string.please_wait), true, false);
        gamapay();
    }

    private void gamapay() {
        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<GamapayResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .gamapay("Bearer " + token);

        call.enqueue(new Callback<GamapayResponse>() {

            @Override
            public void onResponse(Call<GamapayResponse> call, Response<GamapayResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    GamapayResponse gamapayResponse = response.body();
                    if (gamapayResponse.getStatus().equals("success")) {
                        int balanceAll = gamapayResponse.getGamapayAccount().getBalance();

                        DecimalFormat fmt = new DecimalFormat();
                        DecimalFormatSymbols fmts = new DecimalFormatSymbols();

                        fmts.setGroupingSeparator('.');
                        fmt.setGroupingSize(3);
                        fmt.setGroupingUsed(true);
                        fmt.setDecimalFormatSymbols(fmts);
                        tvBalanceAll.setText(String.valueOf(fmt.format(balanceAll)));
                        tvAccNumber.setText(gamapayResponse.getGamapayAccount().getPhoneNumber());
                        gamapayLogList = gamapayResponse.getGamapayLog();
                        recyclerView = (RecyclerView) findViewById(R.id.rvHistory);
                        glAdapter = new GamapayLogAdapter(gamapayLogList);
                        RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(eLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(glAdapter);
                        if (eLayoutManager.getItemCount() == 0) {
                            //Do something
                        }
                    }
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<GamapayResponse> call, Throwable t) {
                loading.dismiss();
                swipeContainer.setRefreshing(false);
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}
