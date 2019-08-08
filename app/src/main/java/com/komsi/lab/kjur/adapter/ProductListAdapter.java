package com.komsi.lab.kjur.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.komsi.lab.kjur.AddCartActivity;
import com.komsi.lab.kjur.R;
import com.komsi.lab.kjur.model.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.CustomViewHolder> {

    List<Product> products;
    Context mContext;

    public ProductListAdapter(List<Product> products, Context mContext) {
        this.products = products;
        this.mContext = mContext;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_product_list, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Product product = products.get(position);

        DecimalFormat fmt = new DecimalFormat();
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();

        fmts.setGroupingSeparator('.');
        fmt.setGroupingSize(3);
        fmt.setGroupingUsed(true);
        fmt.setDecimalFormatSymbols(fmts);

        holder.tvProductName.setText(product.getNameProduct());
        holder.tvProductPrice.setText(String.valueOf(fmt.format(product.getPriceProduct())));
        holder.tvProductDesc.setText(product.getDescProduct());
        holder.tvProductStore.setText(product.getStoreProduct());
        holder.id = products.get(position).getId();
        holder.productName = products.get(position).getNameProduct();
        holder.productPrice = products.get(position).getPriceProduct();
        holder.productPic = products.get(position).getPicProduct();

        if(product.getPicProduct() != null && !product.getPicProduct().isEmpty()){
            Picasso.get()
                    .load(product.getPicProduct())
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
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvProductName, tvProductPrice, tvProductDesc, tvProductStore;
        private ImageView ivAddtoCart, ivProduct;
        private int productPrice;
        private String id, productName, productPic;

        public CustomViewHolder(View view) {
            super(view);
            tvProductName = (TextView)itemView.findViewById(R.id.tvProductName);
            tvProductPrice = (TextView)itemView.findViewById(R.id.tvProductPrice);
            tvProductDesc = (TextView)itemView.findViewById(R.id.tvProductDesc);
            tvProductStore = itemView.findViewById(R.id.tvProductStore);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            ivAddtoCart = itemView.findViewById(R.id.ivAddtoCart);
            ivAddtoCart.setOnClickListener(this);
        }

        public void onClick(View v) {
            Intent intent = new Intent(itemView.getContext() , AddCartActivity.class);
            // here pass id through intent
            intent.putExtra("productId" , id);
            intent.putExtra("productName" , productName);
            intent.putExtra("productPrice" , productPrice);
            intent.putExtra("productPic", productPic);
            itemView.getContext().startActivity(intent);
        }
    }

    public void refreshEvents(List<Product> products) {
        this.products.clear();
        this.products.addAll(products);
        notifyDataSetChanged();
    }
}