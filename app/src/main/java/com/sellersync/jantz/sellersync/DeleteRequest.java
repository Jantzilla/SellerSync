package com.sellersync.jantz.sellersync;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.md5simply.MD5;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jantz on 5/9/2017.
 */

public class DeleteRequest extends StringRequest {

    private static final String DELETE_REQUEST_URL = "http://jantz.000webhostapp.com/DeleteAccount.php";
    private Map<String, String> params;

    public DeleteRequest(String username, Response.Listener<String> listener) {
        super(Method.POST, DELETE_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
