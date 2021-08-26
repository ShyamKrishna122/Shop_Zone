package com.example.ecom.ui.products;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecom.ClickInterface;
import com.example.ecom.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    List<ProductModel> productModelArrayList;
    ClickInterface clickInterface;
    Context context;


    public ProductAdapter(ArrayList<ProductModel> productModelArrayList, ClickInterface clickInterface, Context context) {
        this.productModelArrayList = productModelArrayList;
        this.clickInterface = clickInterface;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_view, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel product = productModelArrayList.get(position);
        holder.productName.setText(product.getProductName());
        holder.productCategory.setText(product.getProductCategory());
        holder.productDescription.setText("Description : "+product.getProductDescription());
        holder.productRating.setText("Rating : "+String.valueOf(product.getProductRating()));
        holder.productPrice.setText("Price(Rs) : "+String.valueOf(product.getProductPrice()));
        holder.productDiscount.setText("Discount % : "+String.valueOf(product.getProductDiscount()));
        holder.productWeight.setText("Weight (Kg) : "+String.valueOf(product.getProductWeight()));
        holder.productAvailableColors.setText("Colours : "+Arrays.toString(product.getProductAvailableColors()));
        holder.productAvailableSizes.setText("Sizes : "+Arrays.toString(product.getProductAvailableSizes()));
        Glide.with(context).load(product.getProductImage()).into(holder.productImage);

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickInterface.onItemClick(product.getProductId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName,productCategory, productDescription, productRating, productPrice, productDiscount, productWeight, productAvailableColors, productAvailableSizes;
        ImageView productImage;
        ImageButton addToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productCategory = itemView.findViewById(R.id.productCategory);
            productDescription = itemView.findViewById(R.id.productDescription);
            productRating = itemView.findViewById(R.id.productRating);
            productPrice = itemView.findViewById(R.id.productPrice);
            productDiscount = itemView.findViewById(R.id.productDiscount);
            productWeight = itemView.findViewById(R.id.productWeight);
            productAvailableColors = itemView.findViewById(R.id.productAvailableColors);
            productAvailableSizes = itemView.findViewById(R.id.productAvailableSizes);
            productImage = itemView.findViewById(R.id.productImage);
            addToCart = itemView.findViewById(R.id.addToCart);



        }
    }
}
