package com.example.ecom.ui.orders;

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

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    ArrayList<ProductModel> orderModelArrayList;
    Context context;

    public OrderAdapter(ArrayList<ProductModel> orderModelArrayList, Context context) {
        this.orderModelArrayList = orderModelArrayList;
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
        ProductModel productModel = orderModelArrayList.get(position);
        holder.orderItemName.setText(productModel.getProductName());
        holder.orderItemPrice.setText(String.valueOf(productModel.getProductPrice()));
        Glide.with(context).load(productModel.getProductImage()).into(holder.orderItemImage);
    }

    @Override
    public int getItemCount() {
        return orderModelArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView orderItemName, orderItemPrice;
        ImageView orderItemImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderItemName = itemView.findViewById(R.id.orderItemName);
            orderItemPrice = itemView.findViewById(R.id.orderItemPrice);
            orderItemImage = itemView.findViewById(R.id.orderItemImage);
        }
    }
}
