package com.example.ecom.ui.orders;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecom.R;
import com.example.ecom.ui.cart.CartAdapter;
import com.example.ecom.ui.products.ProductModel;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {

    ArrayList<ProductModel> productModelArrayList;
    RecyclerView orderRecyclerView;
    CartAdapter orderAdapter;

    TextView orderPrice, orderDate;

    String myId;

    JSONArray orderItems = null;
    int isSuccess = 0;

    View root;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_orders, container, false);

        myId = getActivity().getIntent().getStringExtra("myId");

        productModelArrayList = new ArrayList<>();
        orderRecyclerView = root.findViewById(R.id.orderRecyclerView);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        orderAdapter = new CartAdapter(productModelArrayList, root.getContext());
        orderRecyclerView.setAdapter(orderAdapter);
        orderPrice = root.findViewById(R.id.orderPrice);
        orderDate = root.findViewById(R.id.orderDate);


        String[] field = new String[1];
        String[] data = new String[1];

        field[0] = "personId";
        data[0] = myId;


        PutData putData = new PutData("http://192.168.43.115/ECOM/get_order_info.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    JSONArray arr = new JSONArray(jsonObject.getString("order"));

                    int success = jsonObject.getInt("success");
                    if (success == 1) {
                        JSONObject cartInfo = arr.getJSONObject(0);

                        orderPrice.setText("Total : " + cartInfo.getDouble("price"));
                        orderDate.setText("Date : " + cartInfo.getString("date"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        if (orderPrice.getText().toString().length() > 5) {
            data[0] = myId;
            field[0] = "personId";

            putData = new PutData("http://192.168.43.115/ECOM/get_order_products.php", "POST", field, data);
            if (putData.startPut()) {
                if (putData.onComplete()) {

                    String result = putData.getResult();
                    Log.e("PutData", result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        orderItems = new JSONArray(jsonObject.getString("orderItems"));
                        isSuccess = jsonObject.getInt("success");
                        if (isSuccess == 1) {
                            for (int i = 0; i < orderItems.length(); i++) {
                                JSONObject product = orderItems.getJSONObject(i);
                                productModelArrayList.add(new ProductModel(product.getInt("productId"), product.getString("productName"), "", product.getString("productImage")
                                        , "", 0, product.getDouble("productPrice"), 0, 0, null, null));
                            }


                        } else {
                            Toast.makeText(root.getContext(), "No products found!", Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
            orderAdapter.notifyDataSetChanged();
        }


        return root;
    }
}