package com.komsi.lab.kjur;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.komsi.lab.kjur.adapter.OrderDetailItemAdapter;
import com.komsi.lab.kjur.api.RetrofitClient;
import com.komsi.lab.kjur.model.OrderDetailItem;
import com.komsi.lab.kjur.model.OrderDetailResponse;
import com.komsi.lab.kjur.model.PayConfirmResponse;
import com.komsi.lab.kjur.model.User;
import com.komsi.lab.kjur.storage.SharedPrefManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {
    private LinearLayout layoutTimeRemain, layoutConfirmPay;
    private ImageView imgOrder;
    private ImageButton btnDownload;
    private TextView tvStatusOrder, tvTotalOrder, tvIdOrder, tvDateTimeOrder, tvPaymentType, tvTotalOrder2;
    private Button btnCancelOrder, btnConfirmPayment;
    private ArrayList<OrderDetailItem> orderDetailItemList;
    private RecyclerView recyclerView;
    private OrderDetailItemAdapter odiAdapter;
    private static final long START_TIME_IN_MILLIS = 600000;
    private TextView mTextViewCountDown;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    private long mEndTime;
    private int paymentId = 0;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        ImageButton btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnDownload = findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(OrderDetailActivity.this, null, "Saving image . . .", true, false);
                saveClick(v);
            }
        });

        layoutTimeRemain = findViewById(R.id.layoutTimeRemain);
        layoutConfirmPay = findViewById(R.id.layoutConfirmPay);
        imgOrder = findViewById(R.id.imgOrder);
        tvStatusOrder = findViewById(R.id.tvStatusOrder);
        tvTotalOrder = findViewById(R.id.tvTotalOrder);
        tvIdOrder = findViewById(R.id.tvIdOrder);
        tvDateTimeOrder = findViewById(R.id.tvDateTimeOrder);
        tvPaymentType = findViewById(R.id.tvPaymentType);
        tvTotalOrder2 = findViewById(R.id.tvTotalOrder2);
        mTextViewCountDown = findViewById(R.id.tvCountdown);

        btnCancelOrder = findViewById(R.id.btnCancelOrder);
        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(OrderDetailActivity.this, null, getString(R.string.please_wait), true, false);
                paymentFailed();
            }
        });

        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);
        btnConfirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(OrderDetailActivity.this, null, getString(R.string.please_wait), true, false);
                paymentSuccess();
            }
        });

        Intent intent = getIntent();
        int payMethodId = intent.getIntExtra("payMethodId", 0);
        if (payMethodId == 3) {
            resetTimer();
            startTimer();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loading = ProgressDialog.show(OrderDetailActivity.this, null, getString(R.string.please_wait), true, false);
        orderDetail();
    }

    @Override
    public void onBackPressed() {
        if (tvStatusOrder.getText().toString().equals("Waiting")) {
            Intent i = new Intent(OrderDetailActivity.this, WaitingPaymentActivity.class);
            finish();
            startActivity(i);
        } else {
            Intent i = new Intent(OrderDetailActivity.this, OrderHistoryActivity.class);
            finish();
            startActivity(i);
        }
    }

    private void orderDetail() {
        Intent intent = getIntent();
        String orderId = intent.getStringExtra("orderId");

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<OrderDetailResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .orderDetail("Bearer " + token, orderId);

        call.enqueue(new Callback<OrderDetailResponse>() {

            @Override
            public void onResponse(Call<OrderDetailResponse> call, Response<OrderDetailResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    OrderDetailResponse orderDetailResponse = response.body();
                    if (orderDetailResponse.getStatus().equals("success")) {
                        String status = orderDetailResponse.getOrderDetail().getStatus();
                        int paymentMethod = orderDetailResponse.getOrderDetail().getJenisBayarId();
                        if (status.equals("Success")) {
                            imgOrder.setImageResource(R.drawable.ic_order_success);
                            layoutTimeRemain.setVisibility(View.GONE);
                            layoutConfirmPay.setVisibility(View.GONE);
                        }
                        else if (status.equals("Waiting")) {
                            imgOrder.setImageResource(R.drawable.ic_order_waiting);
                            btnDownload.setVisibility(View.GONE);
                            if (paymentMethod == 2) {
                                layoutTimeRemain.setVisibility(View.GONE);
                                layoutConfirmPay.setVisibility(View.VISIBLE);
                            } else if (paymentMethod == 3) {
                                layoutTimeRemain.setVisibility(View.VISIBLE);
                                layoutConfirmPay.setVisibility(View.GONE);
                            } else {
                                layoutTimeRemain.setVisibility(View.GONE);
                                layoutConfirmPay.setVisibility(View.GONE);
                            }
                        }
                        else {
                            imgOrder.setImageResource(R.drawable.ic_order_failed);
                            layoutTimeRemain.setVisibility(View.GONE);
                            layoutConfirmPay.setVisibility(View.GONE);
                        }
                        DecimalFormat fmt = new DecimalFormat();
                        DecimalFormatSymbols fmts = new DecimalFormatSymbols();

                        fmts.setGroupingSeparator('.');
                        fmt.setGroupingSize(3);
                        fmt.setGroupingUsed(true);
                        fmt.setDecimalFormatSymbols(fmts);

                        tvStatusOrder.setText(status);
                        tvTotalOrder.setText(String.valueOf(fmt.format(orderDetailResponse.getOrderDetail().getJumlahBayar())));
                        tvIdOrder.setText(orderDetailResponse.getOrderDetail().getId());
                        tvDateTimeOrder.setText(orderDetailResponse.getOrderDetail().getDateCreate());
                        tvPaymentType.setText(orderDetailResponse.getOrderDetail().getJenisBayar());
                        tvTotalOrder2.setText(String.valueOf(fmt.format(orderDetailResponse.getOrderDetail().getJumlahBayar())));
                        paymentId = orderDetailResponse.getOrderDetail().getJenisBayarId();

                        orderDetailItemList = orderDetailResponse.getOrderDetailItem();
                        recyclerView = findViewById(R.id.rvItemOrder);
                        odiAdapter = new OrderDetailItemAdapter(orderDetailItemList);
                        RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(eLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(odiAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderDetailResponse> call, Throwable t) {
                loading.dismiss();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }

    private void paymentSuccess() {
        Intent intent = getIntent();
        String orderId = intent.getStringExtra("orderId");

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<PayConfirmResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .paymentSuccess("Bearer " + token, orderId);

        call.enqueue(new Callback<PayConfirmResponse>() {

            @Override
            public void onResponse(Call<PayConfirmResponse> call, Response<PayConfirmResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    PayConfirmResponse payConfirmResponse = response.body();
                    if (payConfirmResponse.getStatus().equals("success")) {
                        Toast.makeText(OrderDetailActivity.this, "Payment Confirmed", Toast.LENGTH_LONG).show();
                        recreate();
                    } else {
                        Toast.makeText(OrderDetailActivity.this, payConfirmResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PayConfirmResponse> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(OrderDetailActivity.this, getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }

    private void paymentFailed() {
        Intent intent = getIntent();
        String orderId = intent.getStringExtra("orderId");

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<PayConfirmResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .paymentFailed("Bearer " + token, orderId);

        call.enqueue(new Callback<PayConfirmResponse>() {

            @Override
            public void onResponse(Call<PayConfirmResponse> call, Response<PayConfirmResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    PayConfirmResponse payConfirmResponse = response.body();
                    if (payConfirmResponse.getStatus().equals("success")) {
                        Toast.makeText(OrderDetailActivity.this, "Order Canceled", Toast.LENGTH_LONG).show();
                        recreate();
                    } else {
                        Toast.makeText(OrderDetailActivity.this, payConfirmResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PayConfirmResponse> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(OrderDetailActivity.this, getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                Intent intent = getIntent();
                int payMethodId = intent.getIntExtra("paymentMethodId", 0);
                String statusOrder = tvStatusOrder.getText().toString();

                if (payMethodId == 3 && statusOrder.equals("Waiting")) {
                    paymentFailed();
                }
                mTimerRunning = false;
                mTextViewCountDown.setText("00:00");
            }
        }.start();

        mTimerRunning = true;
    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
            } else {
                startTimer();
            }
        }
    }

    public void saveClick(View view) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                LinearLayout savingLayout = findViewById(R.id.saveLayout);
                File file = saveBitMap(OrderDetailActivity.this, savingLayout);
                if (file != null) {
                    loading.dismiss();
                    Log.i("TAG", "Drawing saved to the gallery!");
                } else {
                    loading.dismiss();
                    Log.i("TAG", "Oops! Image could not be saved.");
                }
            }
        }, 1000);


    }

    private File saveBitMap(Context context, View drawView) {
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Ka-Jur");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.i("TAG", "Can't create directory to save the image");
            return null;
        }
        String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap = getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery(context, pictureFile.getAbsolutePath());
        return pictureFile;
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);

        return returnedBitmap;
    }

    private void scanGallery(Context ctx, String path) {
        try {
            MediaScannerConnection.scanFile(ctx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue scanning gallery.");
        }
    }
}
