package com.sellersync.jantz.sellersync;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static com.sellersync.jantz.sellersync.R.string.could_not_add_new_product;
import static com.sellersync.jantz.sellersync.R.string.enter_quantity;
import static com.sellersync.jantz.sellersync.R.string.home;
import static com.sellersync.jantz.sellersync.R.string.no_connection;
import static com.sellersync.jantz.sellersync.R.string.no_connection_direction;
import static com.sellersync.jantz.sellersync.R.string.processing;
import static com.sellersync.jantz.sellersync.R.string.retry;
import static com.sellersync.jantz.sellersync.R.string.storing_new_product;
import static com.sellersync.jantz.sellersync.R.string.upc_taken;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewProductFragment extends Fragment {

    ProgressDialog progressDialog;
    EditText etQuantity, etRetailPrice, etSKU, etUPC, etPurchPrice, etBrandName, etProductName;
    String productName, brandName, upc, sku, purchasePrice, retailPrice, quantity;





    public NewProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_product, container, false);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new ProgressDialog(getContext());

        etQuantity = (EditText) view.findViewById(R.id.etQuantity);
        etRetailPrice = (EditText) view.findViewById(R.id.etRetailPrice);
        etSKU = (EditText) view.findViewById(R.id.etSKU);
        etUPC = (EditText) view.findViewById(R.id.etUPC);
        etPurchPrice = (EditText) view.findViewById(R.id.etPurchPrice);
        etBrandName = (EditText) view.findViewById(R.id.etBrandName);
        etProductName = (EditText) view.findViewById(R.id.etProductName);
        final Button btnAddProduct =(Button) view.findViewById(R.id.btnAddProduct);
        final TextView tvAddProdList = (TextView) view.findViewById(R.id.tvAddProdList);
        final TextView tvAnotherProduct = (TextView) view.findViewById(R.id.tvAnotherProduct);


        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                register ();
            }
        });
        tvAnotherProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                tvregister ();

            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getContext().getSystemService(Context.CONNECTIVITY_SERVICE); // 1
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo(); // 2
        return networkInfo != null && networkInfo.isConnected(); // 3
    }

    public void register(){
        initialize();
        if (!validate()) {
            Toast.makeText(getContext(), R.string.could_not_add_new_product, Toast.LENGTH_SHORT).show();
        } else {
            onRegistrationSuccess();
        }
    }
    public void onRegistrationSuccess() {

        if (isNetworkConnected()) {

            progressDialog.setTitle(getString(R.string.storing_new_product));
            progressDialog.setMessage(getString(R.string.processing));
            progressDialog.show();
            final FragmentManager fm = getActivity().getSupportFragmentManager();

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            fm.beginTransaction().replace(R.id.containerView, new HomeFragment()).commit();
                            MainActivity.tvPageTitle.setText(home);
                            progressDialog.cancel();
                        } else if (!success) {
                            boolean reuser = jsonResponse.getBoolean("reuser");
                            if(reuser) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage(R.string.upc_taken)
                                        .setNegativeButton(R.string.retry, null)
                                        .create()
                                        .show();

                                progressDialog.cancel();
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage(could_not_add_new_product)
                                    .setNegativeButton(retry, null)
                                    .create()
                                    .show();

                            progressDialog.cancel();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            };
            NewProductRequest newProductRequest = new NewProductRequest(productName, brandName, upc, sku, purchasePrice, retailPrice, quantity, responseListener);
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(newProductRequest);

        } else {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.no_connection)
                    .setMessage(R.string.no_connection_direction)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();
        }
    }
    public void tvregister(){
        initialize();
        if (!validate()) {
            Toast.makeText(getContext(),could_not_add_new_product, Toast.LENGTH_SHORT).show();
        } else {
            onTVRegistrationSuccess();
        }
    }
    public void onTVRegistrationSuccess() {

        if (isNetworkConnected()) {

            progressDialog.setTitle(storing_new_product);
            progressDialog.setMessage(getString(R.string.processing));
            progressDialog.show();
            final FragmentManager fm = getActivity().getSupportFragmentManager();

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            fm.beginTransaction().replace(R.id.containerView, new NewProductFragment()).commit();
                            progressDialog.cancel();
                        } else if (!success) {
                            boolean reuser = jsonResponse.getBoolean("reuser");
                            if(reuser) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage(upc_taken)
                                        .setNegativeButton(retry, null)
                                        .create()
                                        .show();

                                progressDialog.cancel();
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage(could_not_add_new_product)
                                    .setNegativeButton(retry, null)
                                    .create()
                                    .show();

                            progressDialog.cancel();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            };
            NewProductRequest newProductRequest = new NewProductRequest(productName, brandName, upc, sku, purchasePrice, retailPrice, quantity, responseListener);
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(newProductRequest);

        } else {
            new AlertDialog.Builder(getContext())
                    .setTitle(no_connection)
                    .setMessage(no_connection_direction)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();
        }
    }
    public boolean validate() {
        boolean valid = true;
        int indexOFdecP =  purchasePrice.indexOf(".");
        int indexOFdecR = retailPrice.indexOf(".");
        if(productName.isEmpty()) {
            etProductName.setError(getString(R.string.enter_product_name));
            valid = false;
        }
        if(brandName.isEmpty()) {
            etBrandName.setError(getString(R.string.enter_brand_name));
            valid = false;
        }
        if(upc.isEmpty()) {
            etUPC.setError(getString(R.string.please_enter_or_create_upc) );
            valid = false;
        }
        if(indexOFdecP >=0) {
            if(purchasePrice.substring(indexOFdecP).length() >3) {
                etPurchPrice.setError(getString(R.string.please_enter_a_currency));
                valid = false;
            }
        }
        if(indexOFdecR >=0) {
            if(retailPrice.substring(indexOFdecR).length() >3) {
                etRetailPrice.setError(getString(R.string.please_enter_a_currency));
                valid = false;
            }
        }
        if(quantity.isEmpty()) {
            etQuantity.setError(getString(R.string.enter_quantity));
            valid = false;
        }
        return valid;
    }
    public void initialize(){

        productName = etProductName.getText().toString();
        brandName = etBrandName.getText().toString();
        upc = etUPC.getText().toString();
        sku = etSKU.getText().toString();
        purchasePrice = etPurchPrice.getText().toString();
        retailPrice = etRetailPrice.getText().toString();
        quantity = etQuantity.getText().toString();



    }

    }
