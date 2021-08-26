package com.example.ecom.ui.cart;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecom.R;
import com.example.ecom.ui.products.ProductModel;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class CartFragment extends Fragment {

    ArrayList<ProductModel> productModelArrayList;
    RecyclerView cartRecyclerView;
    CartAdapter cartAdapter;

    TextView cartPrice, cartQuantity;
    Button cartOrder;

    String myId, price;

    JSONArray cartItems = null;
    int isSuccess = 0;

    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_cart, container, false);

        myId = getActivity().getIntent().getStringExtra("myId");

        productModelArrayList = new ArrayList<>();
        cartRecyclerView = root.findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        cartAdapter = new CartAdapter(productModelArrayList, root.getContext());
        cartRecyclerView.setAdapter(cartAdapter);

        cartPrice = root.findViewById(R.id.cartPrice);
        cartQuantity = root.findViewById(R.id.cartQuantity);
        cartOrder = root.findViewById(R.id.cartOrder);


        String[] field = new String[1];
        String[] data = new String[1];

        field[0] = "personId";
        data[0] = myId;


        PutData putData = new PutData("http://192.168.43.115/ECOM/get_cart_info.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    JSONArray arr = new JSONArray(jsonObject.getString("cart"));

                    int success = jsonObject.getInt("success");
                    if (success == 1) {

                        JSONObject cartInfo = arr.getJSONObject(0);
                        price = cartInfo.getString("price");
                        cartPrice.setText("Total : " + cartInfo.getDouble("price"));
                        cartQuantity.setText("Quantity : " + cartInfo.getInt("quantity") + "x");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        getCartProducts();

        cartOrder.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String[] field1 = new String[3];
                String[] data1 = new String[3];
                data1[0] = myId;
                field1[0] = "personId";
                data1[1] = LocalDate.now().toString();
                field1[1] = "date";
                data1[2] = price;
                field1[2] = "price";

                PutData putData = new PutData("http://192.168.43.115/ECOM/add_order.php", "POST", field1, data1);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        Log.e("PutData", result);
                        if (result.equals("Success")) {
                            Toast.makeText(root.getContext(), "Order Placed", Toast.LENGTH_SHORT).show();
                            getCartProducts();
                        }
                        else{
                            Toast.makeText(root.getContext(), "Order already exists.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


            }

        });

        return root;
    }

    void getCartProducts(){
        String[] field = new String[1];
        String[] data = new String[1];

        data[0] = myId;
        field[0] = "personId";

        PutData putData = new PutData("http://192.168.43.115/ECOM/get_cart_products.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {

                String result = putData.getResult();
                Log.e("PutData", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    cartItems = new JSONArray(jsonObject.getString("cartItems"));
                    isSuccess = jsonObject.getInt("success");
                    if (isSuccess == 1) {
                        for (int i = 0; i < cartItems.length(); i++) {
                            JSONObject product = cartItems.getJSONObject(i);
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
        cartAdapter.notifyDataSetChanged();
    }
}