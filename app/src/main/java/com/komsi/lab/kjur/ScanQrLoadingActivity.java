package com.komsi.lab.kjur;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.komsi.lab.kjur.model.ProductDetailResponse;
import com.komsi.lab.kjur.model.User;
import com.komsi.lab.kjur.storage.SharedPrefManager;
import com.komsi.lab.kjur.api.RetrofitClient;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanQrLoadingActivity extends AppCompatActivity {
    private IntentIntegrator qrScan;
    private ImageButton btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_loading);

        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final ProgressBar loading = findViewById(R.id.loading);
        final TextView tvSearch = findViewById(R.id.tvSearch);
        final TextView tvPWait = findViewById(R.id.tvPWait);
        final TextView tvNotFound = findViewById(R.id.tvNotFound);
        final ImageView ivNoItem = findViewById(R.id.ivNoItem);
        final Button btnTryAgain = findViewById(R.id.btnTryAgain);

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan = new IntentIntegrator(ScanQrLoadingActivity.this).setCaptureActivity(ScanQrActivity.class);
                qrScan.setDesiredBarcodeFormats(qrScan.QR_CODE_TYPES);

                qrScan.initiateScan();
            }
        });

        Intent intent = getIntent();
        String scanText = intent.getStringExtra("scanText");

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<ProductDetailResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .productDetail("Bearer " + token, scanText);

        call.enqueue(new Callback<ProductDetailResponse>() {

            @Override
            public void onResponse(Call<ProductDetailResponse> call, Response<ProductDetailResponse> response) {
                ProductDetailResponse productDetail = response.body();
                if (response.isSuccessful()) {
                    if (productDetail.getStatus().equals("failed")) {
                        loading.setVisibility(View.GONE);
                        tvSearch.setVisibility(View.GONE);
                        tvPWait.setVisibility(View.GONE);

                        ivNoItem.setVisibility(View.VISIBLE);
                        tvNotFound.setVisibility(View.VISIBLE);
                        btnClose.setVisibility(View.VISIBLE);
                        btnTryAgain.setVisibility(View.VISIBLE);
                        //Toast.makeText(getActivity(), "Result Not Found", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Intent intent = new Intent(ScanQrLoadingActivity.this , AddCartActivity.class);
                        intent.putExtra("productId" , productDetail.getProduct().getId());
                        intent.putExtra("productName" , productDetail.getProduct().getNameProduct());
                        intent.putExtra("productPrice" , productDetail.getProduct().getPriceProduct());
                        ScanQrLoadingActivity.this.finish();
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductDetailResponse> call, Throwable t) {
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i= new Intent(ScanQrLoadingActivity.this, MainActivity.class);
        finish();
        startActivity(i);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(ScanQrLoadingActivity.this, "Scan Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String scanText = result.getContents();
                Intent intent = new Intent(ScanQrLoadingActivity.this , ScanQrLoadingActivity.class);
                intent.putExtra("scanText" , scanText);
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
