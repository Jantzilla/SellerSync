package com.sellersync.jantz.sellersync;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class InventoryFragment extends Fragment {
    String productName,brandName,upc,quantity;
    EditText search;
    StringRequest request;
    private static final String INVENTORY_REQUEST_URL = "http://jantz.000webhostapp.com/QueryInventory.php";
    View rootView;
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;


    public InventoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_inventory, container, false);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ExpandableListView) view.findViewById(R.id.inventorylistview);
        search = (EditText) view.findViewById(R.id.etSearch);

            listDataHeader = new ArrayList<>();
            listHash = new HashMap<>();

            listDataHeader.add(getString(R.string.category));
            listDataHeader.add(getString(R.string.brand_name));
            listDataHeader.add(getString(R.string.upc));
            listDataHeader.add(getString(R.string.sku));
            listDataHeader.add(getString(R.string.quantity));
            listDataHeader.add(getString(R.string.retail_price));

            final List <String> category = new ArrayList<>();
            category.add("Product Category");

            final List <String> brand = new ArrayList<>();
            brand.add("Product Brand Name");

            final List <String> upc = new ArrayList<>();
            upc.add("Product UPC");

            final List <String> sku = new ArrayList<>();
            sku.add("Product SKU");

            final List <String> quantity = new ArrayList<>();
            quantity.add("Product Quantity");

            final List <String> price = new ArrayList<>();
            price.add("Product Retail Price");


            listHash.put(listDataHeader.get(0),category);
            listHash.put(listDataHeader.get(1),brand);
            listHash.put(listDataHeader.get(2),upc);
            listHash.put(listDataHeader.get(3),sku);
            listHash.put(listDataHeader.get(4),quantity);
            listHash.put(listDataHeader.get(5),price);

        listAdapter = new ExpandableListAdapter(getContext(),listDataHeader,listHash);
        listView.setAdapter(listAdapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (search.getText().toString().length() >=2) {
                    category.clear();
                    brand.clear();
                    upc.clear();
                    quantity.clear();
                    sku.clear();
                    price.clear();
                    queryCity(search.getText().toString());

                }
                if (search.getText().toString().isEmpty()) {
                    listView.collapseGroup(0);
                    listView.collapseGroup(1);
                    listView.collapseGroup(2);
                    listView.collapseGroup(3);
                    listView.collapseGroup(4);
                    listView.collapseGroup(5);

                    final List <String> category = new ArrayList<>();
                    category.add("Product Category");

                    final List <String> brand = new ArrayList<>();
                    brand.add("Product Brand Name");

                    final List <String> upc = new ArrayList<>();
                    upc.add("Product UPC");

                    final List <String> sku = new ArrayList<>();
                    sku.add("Product SKU");

                    final List <String> quantity = new ArrayList<>();
                    quantity.add("Product Quantity");

                    final List <String> price = new ArrayList<>();
                    price.add("Product Retail Price");


                    listHash.put(listDataHeader.get(0),category);
                    listHash.put(listDataHeader.get(1),brand);
                    listHash.put(listDataHeader.get(2),upc);
                    listHash.put(listDataHeader.get(3),sku);
                    listHash.put(listDataHeader.get(4),quantity);
                    listHash.put(listDataHeader.get(5),price);

                    listView.expandGroup(0);
                    listView.expandGroup(1);
                    listView.expandGroup(2);
                    listView.expandGroup(3);
                    listView.expandGroup(4);
                    listView.expandGroup(5);
                }
                else {
                    category.clear();
                    brand.clear();
                    upc.clear();
                    quantity.clear();
                    sku.clear();
                    price.clear();

                }
            }
        });


    }

    public void queryCity(final String query) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        request = new StringRequest(Request.Method.POST, INVENTORY_REQUEST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonResponse = new JSONObject(response);
                    productName = jsonResponse.getString("productName");
                    final String brandName = jsonResponse.getString("brandName");
                    final String upcResult = jsonResponse.getString("upc");
                    final String skuResult = jsonResponse.getString("sku");
                    final String quantityResult = jsonResponse.getString("quantity");
                    final String priceResult = jsonResponse.getString("retailPrice");

                    final List <String> category = new ArrayList<>();
                    category.add("Product Category");

                    final List <String> brand = new ArrayList<>();
                    brand.add(brandName);

                    final List <String> upc = new ArrayList<>();
                    upc.add(upcResult);

                    final List <String> sku = new ArrayList<>();
                    sku.add(skuResult);

                    final List <String> quantity = new ArrayList<>();
                    quantity.add(quantityResult);

                    final List <String> price = new ArrayList<>();
                    price.add(priceResult);


                    listHash.put(listDataHeader.get(0),category);
                    listHash.put(listDataHeader.get(1),brand);
                    listHash.put(listDataHeader.get(2),upc);
                    listHash.put(listDataHeader.get(3),sku);
                    listHash.put(listDataHeader.get(4),quantity);
                    listHash.put(listDataHeader.get(5),price);

                    listView.collapseGroup(0);
                    listView.collapseGroup(1);
                    listView.collapseGroup(2);
                    listView.collapseGroup(3);
                    listView.collapseGroup(4);
                    listView.collapseGroup(5);

                    listView.expandGroup(0);
                    listView.expandGroup(1);
                    listView.expandGroup(2);
                    listView.expandGroup(3);
                    listView.expandGroup(4);
                    listView.expandGroup(5);

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("searchQuery", query);
                return hashMap;
            }
        };
        queue.add(request);
    }




}
