package com.sellersync.jantz.sellersync;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.md5simply.MD5;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jantz on 5/9/2017.
 */

public class NewProductRequest extends StringRequest {

    private static final String NEW_PRODUCT_REQUEST_URL = "http://jantz.000webhostapp.com/AddNewProduct.php";
    private Map<String, String> params;

    public NewProductRequest(String productName, String brandName, String upc, String sku, String purchasePrice, String retailPrice, String quantity, Response.Listener<String> listener) {
        super(Method.POST, NEW_PRODUCT_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("productName", productName);
        params.put("brandName", brandName);
        params.put("upc", upc);
        params.put("sku", sku);
        params.put("purchasePrice", purchasePrice);
        params.put("retailPrice", retailPrice);
        params.put("quantity", quantity);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
