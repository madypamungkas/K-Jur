package com.komsi.lab.kjur.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.komsi.lab.kjur.CartActivity;
import com.komsi.lab.kjur.R;
import com.komsi.lab.kjur.api.RetrofitClient;
import com.komsi.lab.kjur.model.CartProduct;
import com.komsi.lab.kjur.model.RemoveItemCartResponse;
import com.komsi.lab.kjur.model.User;
import com.komsi.lab.kjur.storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CustomViewHolder>{

    List<CartProduct> cartProducts;
    Context mContext;

    public CartAdapter(List<CartProduct> cartProducts, Context mContext) {
        this.cartProducts = cartProducts;
        this.mContext = mContext;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_cart, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        CartProduct cartProduct = cartProducts.get(position);
        holder.tvProductName.setText(cartProduct.getNameProduct());

        DecimalFormat fmt = new DecimalFormat();
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();

        fmts.setGroupingSeparator('.');
        fmt.setGroupingSize(3);
        fmt.setGroupingUsed(true);
        fmt.setDecimalFormatSymbols(fmts);
        holder.tvProductPrice.setText(String.valueOf(fmt.format(cartProduct.getPriceProduct())));
        holder.tvCurrentStock.setText(String.valueOf(cartProduct.getStockPoduct()));
        holder.tvQuantity.setText(String.valueOf(cartProduct.getQuantity()));
        holder.tvProductStore.setText(cartProduct.getStoreNameProduct());
        if(cartProduct.getPicProduct() != null && !cartProduct.getPicProduct().isEmpty()){
            Picasso.get()
                    .load(cartProduct.getPicProduct())
                    .placeholder(R.drawable.ic_snack)
                    .error(R.drawable.ic_close)
                    // To fit image into imageView
                    //.fit()
                    .resize(500, 500)
                    .centerInside()
                    // To prevent fade animation
                    .noFade()
                    .into(holder.ivProduct);
        } else{
            holder.ivProduct.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_snack));
        }
        holder.id = cartProducts.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return cartProducts.size();
    }

    public int grandTotal() {
        int totalPrice = 0;
        for (int i = 0; i < cartProducts.size(); i++) {
            totalPrice += (cartProducts.get(i).getPriceProduct() * cartProducts.get(i).getQuantity());
        }
        return totalPrice;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        private TextView tvProductName, tvProductPrice, tvQuantity, tvProductStore, tvCurrentStock;
        private ImageButton btnDelete;
        private ImageView ivProduct;
        private int id;

        public CustomViewHolder(View view) {
            super(view);
            tvProductName = (TextView)itemView.findViewById(R.id.tvProductName);
            tvProductPrice = (TextView)itemView.findViewById(R.id.tvProductPrice);
            tvProductStore = itemView.findViewById(R.id.tvProductStore);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvCurrentStock = itemView.findViewById(R.id.tvCurrentStock);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            btnDelete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    removeItem();
                }
            });
        }

        private void removeItem() {
            User user = SharedPrefManager.getInstance(mContext).getUser();
            String token = user.getToken();
            Call<RemoveItemCartResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .removeItem("Bearer " + token, id);

            call.enqueue(new Callback<RemoveItemCartResponse>() {

                @Override
                public void onResponse(Call<RemoveItemCartResponse> call, Response<RemoveItemCartResponse> response) {
                    RemoveItemCartResponse removeItemCartResponse = response.body();
                    if (response.isSuccessful()) {
                        if (removeItemCartResponse.getStatus().equals("success")) {
                            Toast.makeText(mContext, "Item was removed", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(itemView.getContext() , CartActivity.class);
                            itemView.getContext().startActivity(i);
                            //CartActivity cartActivity = (CartActivity)mContext;
                            //cartActivity.onRestart();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RemoveItemCartResponse> call, Throwable t) {
                    Toast.makeText(mContext, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                    Log.d("TAG", "Response = " + t.toString());
                }
            });
        }
    }

    public void refreshEvents(List<CartProduct> cartProducts) {
        this.cartProducts.clear();
        this.cartProducts.addAll(cartProducts);
        notifyDataSetChanged();
    }
}