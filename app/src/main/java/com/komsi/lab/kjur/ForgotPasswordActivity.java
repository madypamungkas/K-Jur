package com.komsi.lab.kjur;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.komsi.lab.kjur.api.RetrofitClient;
import com.komsi.lab.kjur.model.ForgotPasswordResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText etEmail;
    Context mContext;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.etEmail);

        ImageButton btnClose = findViewById(R.id.buttonClose);
        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ForgotPasswordActivity.this.finish();
            }
        });

        mContext = this;

        Button btnReset = this.findViewById(R.id.buttonReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Please wait...", true, false);
                fpUser();
            }
        });
    }

    private void fpUser() {
        String accept = "application/json";
        String email = etEmail.getText().toString().trim();

        if (email.isEmpty()) {
            loading.dismiss();
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loading.dismiss();
            etEmail.setError("Enter a valid Email");
            etEmail.requestFocus();
            return;
        }

        Call<ForgotPasswordResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .fpUser(accept, email);

        call.enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                ForgotPasswordResponse forgotPasswordResponse = response.body();
                if (response.isSuccessful()){
                    if (forgotPasswordResponse.getStatus().equals("Success")) {
                        Log.i("debug", "onResponse: SUCCESS");
                        loading.dismiss();
                        Toast.makeText(mContext, forgotPasswordResponse.getMessage(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(mContext, LoginActivity.class));
                    }
                } else {
                    Log.i("debug", "onResponse: FAILED");
                    loading.dismiss();
                    Toast.makeText(mContext, "Something wrong. Please try again later.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                loading.dismiss();
                Toast.makeText(mContext, "Something wrong. Please try again later.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
