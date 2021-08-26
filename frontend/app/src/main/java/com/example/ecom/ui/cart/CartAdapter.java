package com.example.ecom.ui.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecom.R;
import com.example.ecom.ui.products.ProductModel;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    ArrayList<ProductModel> cartModelArrayList;
    Context context;

    public CartAdapter(ArrayList<ProductModel> cartModelArrayList, Context context) {
        this.cartModelArrayList = cartModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel productModel = cartModelArrayList.get(position);
        holder.cartItemName.setText(productModel.getProductName());
        holder.cartItemPrice.setText(String.valueOf(productModel.getProductPrice()));
        Glide.with(context).load(productModel.getProductImage()).into(holder.cartItemImage);
    }

    @Override
    public int getItemCount() {
        return cartModelArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView cartItemName, cartItemPrice;
        ImageView cartItemImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartItemName = itemView.findViewById(R.id.cartItemName);
            cartItemPrice = itemView.findViewById(R.id.cartItemPrice);
            cartItemImage = itemView.findViewById(R.id.cartItemImage);
        }
    }
}
