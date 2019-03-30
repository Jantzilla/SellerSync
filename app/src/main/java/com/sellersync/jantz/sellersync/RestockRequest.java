package com.sellersync.jantz.sellersync;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.md5simply.MD5;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jantz on 5/9/2017.
 */

public class RestockRequest extends StringRequest {

    private static final String RESTOCK_REQUEST_URL = "http://jantz.000webhostapp.com/Restock.php";
    private Map<String, String> params;

    public RestockRequest(String quantity, String productName, String brandName, String upc, Response.Listener<String> listener) {
        super(Method.POST, RESTOCK_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("quantity", quantity);
        params.put("productName", productName);
        params.put("brandName", brandName);
        params.put("upc", upc);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
