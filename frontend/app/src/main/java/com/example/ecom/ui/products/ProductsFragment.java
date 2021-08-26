package com.example.ecom.ui.products;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecom.ClickInterface;
import com.example.ecom.R;

import com.vishnusivadas.advanced_httpurlconnection.FetchData;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ProductsFragment extends Fragment implements ClickInterface {

    ArrayList<ProductModel> productModelArrayList;
    RecyclerView productRecyclerView;
    ProductAdapter productAdapter;
    View root;


    JSONArray products = null;
    int isSuccess = 0;

    String myId;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);

        myId = getActivity().getIntent().getStringExtra("myId");

        productModelArrayList = new ArrayList<>();
        productRecyclerView = root.findViewById(R.id.productsRecyclerView);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        productAdapter = new ProductAdapter(productModelArrayList, this, root.getContext());

        productRecyclerView.setAdapter(productAdapter);

        FetchData fetchData = new FetchData("http://192.168.43.115/ECOM/products.php");
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                String result = fetchData.getResult();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    products = new JSONArray(jsonObject.getString("products"));
                    isSuccess = jsonObject.getInt("success");
                    if (isSuccess == 1) {
                        for (int i = 0; i < products.length(); i++) {

                            JSONObject product = (JSONObject) products.getJSONObject(i);

                            productModelArrayList.add(new ProductModel(product.getInt("productId"),
                                    product.getString("productName"), product.getString("productDescription"),
                                    product.getString("productImage"), product.getString("productCategory"),
                                    product.getDouble("productRating"), product.getDouble("productPrice"),
                                    product.getDouble("productDiscount"), product.getDouble("productWeight"),
                                    getColors(product.getString("productId")), getSizes(product.getString("productId"))
                            ));
                        }
                    } else {
                        Toast.makeText(root.getContext(), "No products found!", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        productAdapter.notifyDataSetChanged();

        return root;
    }

    String[] getColors(String id) {
        String[] field = new String[1];
        String[] data = new String[1];

        field[0] = "productId";
        data[0] = id;

        PutData putData = new PutData("http://192.168.43.115/ECOM/product_colors.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray arr = new JSONArray(jsonObject.getString("productColors"));
                    int success = jsonObject.getInt("success");
                    if (success == 1) {
                        String[] colors = new String[arr.length()];
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject productColor = (JSONObject) arr.getJSONObject(i);
                            colors[i] = (productColor.getString("color"));

                        }
                        return colors;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        return null;
    }

    double[] getSizes(String id) {
        String[] field = new String[1];
        String[] data = new String[1];

        field[0] = "productId";
        data[0] = id;

        PutData putData = new PutData("http://192.168.43.115/ECOM/product_sizes.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray arr = new JSONArray(jsonObject.getString("productSizes"));
                    int success = jsonObject.getInt("success");
                    if (success == 1) {
                        double[] sizes = new double[arr.length()];
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject productColor = (JSONObject) arr.getJSONObject(i);
                            sizes[i] = (productColor.getDouble("size"));
                        }
                        return sizes;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        return null;
    }

    @Override
    public void onItemClick(int productId) {
        String[] field = new String[2];
        String[] data = new String[2];

        field[0] = "personId";
        data[0] = myId;
        field[1] = "productId";
        data[1] = String.valueOf(productId);

        PutData putData = new PutData("http://192.168.43.115/ECOM/add_to_cart.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
//                Toast.makeText(root.getContext(), result, Toast.LENGTH_SHORT).show();
                Log.e("qwert",result);
            }
        }
        Toast.makeText(root.getContext(), "Product has been added to cart.", Toast.LENGTH_SHORT).show();
    }

}