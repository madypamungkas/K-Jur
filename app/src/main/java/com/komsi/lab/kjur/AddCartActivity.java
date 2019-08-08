package com.komsi.lab.kjur;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.komsi.lab.kjur.filter.InputFilterMinMax;
import com.komsi.lab.kjur.model.AddCartResponse;
import com.komsi.lab.kjur.model.User;
import com.komsi.lab.kjur.api.RetrofitClient;
import com.komsi.lab.kjur.storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCartActivity extends AppCompatActivity {
    Integer i = 1;
    Button btnConfirm;
    private EditText etQuantity;
    Context mContext;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cart);

        mContext = this;

        Intent intent = getIntent();
        String productName = intent.getStringExtra("productName");
        String productPic = intent.getStringExtra("productPic");
        int productPrice = intent.getIntExtra("productPrice", 0);

        ImageButton btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView ivProduct = findViewById(R.id.ivProduct);
        Picasso.get()
                .load(productPic)
                .placeholder(R.drawable.ic_snack)
                .error(R.drawable.ic_close)
                // To fit image into imageView
                //.fit()
                .resize(500, 500)
                .centerInside()
                // To prevent fade animation
                .noFade()
                .into(ivProduct);

        TextView tvProductName = this.findViewById(R.id.tvProductName);
        tvProductName.setText(productName);

        final TextView tvProductPrice = this.findViewById(R.id.tvProductPrice);
        tvProductPrice.setText(String.valueOf(productPrice));

        etQuantity = (EditText) findViewById(R.id.etQuantity);
        etQuantity.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "99")});

        final TextView tvTotalPrice = this.findViewById(R.id.tvTotalPrice);

        DecimalFormat fmt = new DecimalFormat();
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();

        fmts.setGroupingSeparator('.');
        fmt.setGroupingSize(3);
        fmt.setGroupingUsed(true);
        fmt.setDecimalFormatSymbols(fmts);
        tvTotalPrice.setText(String.valueOf(fmt.format(productPrice)));

        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, getString(R.string.please_wait), true, false);
                addCart();
            }
        });

        etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String qty = etQuantity.getText().toString();

                if (qty.isEmpty()) {
                    etQuantity.setError("Quantity required");
                    tvTotalPrice.setText(String.valueOf(0));
                    btnConfirm.setEnabled(false);
                } else {
                    etQuantity.setError(null);
                    int totalcost = Integer.parseInt(qty) * Integer.parseInt(tvProductPrice.getText().toString());
                    tvTotalPrice.setText(String.valueOf(totalcost));
                    btnConfirm.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final ImageButton decrease = this.findViewById(R.id.ivDecrease);
        final ImageButton increase = this.findViewById(R.id.ivIncrease);
        i = Integer.parseInt(etQuantity.getText().toString());

        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringVal;
                if (i > 1) {
                    i = i - 1;
                    stringVal = String.valueOf(i);
                    etQuantity.setText(stringVal);
                } else {
                    decrease.setEnabled(false);
                    decrease.setColorFilter(mContext.getResources().getColor(R.color.light_grey));
                }
            }
        });

        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringVal;
                if (i < 99) {
                    i = i + 1;
                    stringVal = String.valueOf(i);
                    etQuantity.setText(stringVal);
                } else {
                    increase.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        AddCartActivity.this.finish();
    }

    private void addCart() {
        int quantity = Integer.parseInt(etQuantity.getText().toString().trim());
        Intent intent = getIntent();
        String productId = intent.getStringExtra("productId");

        User user = SharedPrefManager.getInstance(this).getUser();
        String token = user.getToken();
        Call<AddCartResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .addCart("Bearer " + token, productId, quantity);

        call.enqueue(new Callback<AddCartResponse>() {

            @Override
            public void onResponse(Call<AddCartResponse> call, Response<AddCartResponse> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(mContext, "Added to Cart", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(AddCartActivity.this, CartActivity.class);
                    finish();
                    startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<AddCartResponse> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}
