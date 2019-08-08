package com.komsi.lab.kjur;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.komsi.lab.kjur.api.RetrofitClient;
import com.komsi.lab.kjur.model.ResendVerifyEmailResponse;
import com.komsi.lab.kjur.model.User;
import com.komsi.lab.kjur.model.VerifyEmailResponse;
import com.komsi.lab.kjur.storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyEmailActivity extends AppCompatActivity {
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;
    private TextView tvEmail, btnResend, tvTime;
    private EditText etOTP;
    private Button btnSubmit;
    private LinearLayout timer;
    private Context mContext;
    ProgressDialog loading;
    CountDownTimer cTimer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        tvEmail = findViewById(R.id.tvEmail);
        etOTP = findViewById(R.id.etOTP);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnResend = findViewById(R.id.btnResend);
        timer = findViewById(R.id.timer);
        tvTime = findViewById(R.id.tvTime);

        mContext = this;

        Intent intent = getIntent();
        String emailSend = intent.getStringExtra("email");
        String emailCensoredFirst =  emailSend.substring(0, 4);
        String emailCensoredLast = emailSend.substring(emailSend.length()-4);
        tvEmail.setText(emailCensoredFirst + "***@***" + emailCensoredLast);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
                verifyUser();
            }
        });

        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
                resendVerifyUser();
            }
        });

        loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
        resendVerifyUser();
        startTimer();
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }

        cancelTimer();
        mBackPressed = System.currentTimeMillis();
    }

    private void startTimer() {
        cTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setVisibility(View.VISIBLE);
                tvTime.setText("" + millisUntilFinished / 1000);
                btnResend.setTextColor(getResources().getColor(R.color.light_grey));
                btnResend.setClickable(false);
            }
            public void onFinish() {
                timer.setVisibility(View.GONE);
                btnResend.setTextColor(getResources().getColor(R.color.white));
                btnResend.setClickable(true);
            }
        };
        cTimer.start();
    }

    private void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }

    private void verifyUser() {
        String codeOTP = etOTP.getText().toString().trim();

        if (codeOTP.isEmpty()) {
            loading.dismiss();
            etOTP.setError("OTP is required");
            etOTP.requestFocus();
            return;
        }

        if (codeOTP.length() < 6) {
            loading.dismiss();
            etOTP.setError("OTP should be 6 characters long.");
            etOTP.requestFocus();
            return;
        }

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<VerifyEmailResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .verifyUser("Bearer " + token, codeOTP);

        call.enqueue(new Callback<VerifyEmailResponse>() {

            @Override
            public void onResponse(Call<VerifyEmailResponse> call, Response<VerifyEmailResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    VerifyEmailResponse verifyEmailResponse = response.body();
                    if (verifyEmailResponse.getStatus().equals("success")) {
                        Toast.makeText(mContext, verifyEmailResponse.getMessage(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(VerifyEmailActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(mContext, verifyEmailResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<VerifyEmailResponse> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }

    private void resendVerifyUser() {
        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<ResendVerifyEmailResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .resendVerifyUser("Bearer " + token);

        call.enqueue(new Callback<ResendVerifyEmailResponse>() {

            @Override
            public void onResponse(Call<ResendVerifyEmailResponse> call, Response<ResendVerifyEmailResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    ResendVerifyEmailResponse resendVerifyEmailResponse = response.body();
                    if (resendVerifyEmailResponse.getStatus().equals("success")) {
                        Toast.makeText(mContext, "Please check your email.", Toast.LENGTH_LONG).show();
                        startTimer();
                    } else {
                        Toast.makeText(mContext, resendVerifyEmailResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResendVerifyEmailResponse> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}
