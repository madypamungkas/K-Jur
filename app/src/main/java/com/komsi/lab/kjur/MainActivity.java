package com.komsi.lab.kjur;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.komsi.lab.kjur.adapter.PagerAdapterMain;
import com.komsi.lab.kjur.api.RetrofitClient;
import com.komsi.lab.kjur.model.DetailUserResponse;
import com.komsi.lab.kjur.model.User;
import com.komsi.lab.kjur.storage.SharedPrefManager;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView tvName, tvEmail, tvBalance;
    private Context mContext;
    private SwipeRefreshLayout swipeContainer;
    private ProgressDialog loading;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvName = findViewById(R.id.tvResultName);
        tvEmail = findViewById(R.id.tvResultEmail);
        tvBalance = findViewById(R.id.tvBalance);

        mContext = this;

        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                detailUser();
            }
        });

        swipeContainer.setColorSchemeResources(R.color.light_green);

        ImageButton btnCart = this.findViewById(R.id.btnCart);
        btnCart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CartActivity.class);
                startActivity(i);
            }
        });

        ImageButton btnSettings = this.findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });

        Button btnGamapay = this.findViewById(R.id.btnGamapay);
        btnGamapay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GamapayActivity.class);
                startActivity(i);
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Home"));
        tabLayout.addTab(tabLayout.newTab().setText("Order"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        final PagerAdapterMain adapter = new PagerAdapterMain
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void onStart(){
        super.onStart();
        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    /*
    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }
    */

    @Override
    protected void onResume()
    {
        super.onResume();
        loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
        detailUser();
    }

    private void detailUser() {
        String accept = "application/json";

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<DetailUserResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .detailUser(accept,"Bearer " + token);

        call.enqueue(new Callback<DetailUserResponse>() {

            @Override
            public void onResponse(Call<DetailUserResponse> call, Response<DetailUserResponse> response) {
                loading.dismiss();
                DetailUserResponse detailUserResponse = response.body();
                if (response.isSuccessful()) {
                    if (detailUserResponse.getStatus().equals("success")) {
                        tvEmail.setText(detailUserResponse.getDetailUser().getEmail());
                        tvName.setText(detailUserResponse.getDetailUser().getName());

                        int balance = detailUserResponse.getDetailUser().getBalance();

                        DecimalFormat fmt = new DecimalFormat();
                        DecimalFormatSymbols fmts = new DecimalFormatSymbols();

                        fmts.setGroupingSeparator('.');
                        fmt.setGroupingSize(3);
                        fmt.setGroupingUsed(true);
                        fmt.setDecimalFormatSymbols(fmts);

                        tvBalance.setText(String.valueOf(fmt.format(balance)));
                    } else {
                        String emailSend = detailUserResponse.getEmail();
                        Intent intent = new Intent(MainActivity.this, VerifyEmailActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("email" , emailSend);
                        startActivity(intent);
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(mContext, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        if (jObjError.getString("message").equals("Unauthenticated.")) {
                            SharedPrefManager.getInstance(MainActivity.this).clear();
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<DetailUserResponse> call, Throwable t) {
                loading.dismiss();
                swipeContainer.setRefreshing(false);
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
                Intent intent = new Intent(MainActivity.this , ScanQrLoadingActivity.class);
                intent.putExtra("scanText" , scanText);
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
