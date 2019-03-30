package com.sellersync.jantz.sellersync;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by jantz on 5/9/2017.
 */

public class AdminCountRequest extends StringRequest {

    private static final String ADMIN_COUNT_REQUEST_URL = "http://jantz.000webhostapp.com/SellerSyncAdminCount.php";
    private Map<String, String> params;

    public AdminCountRequest(String username, Response.Listener<String> listener) {
        super(Method.POST, ADMIN_COUNT_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

