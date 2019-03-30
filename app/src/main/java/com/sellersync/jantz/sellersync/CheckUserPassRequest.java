package com.sellersync.jantz.sellersync;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.md5simply.MD5;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jantz on 5/9/2017.
 */

public class CheckUserPassRequest extends StringRequest{

    private static final String LOGIN_REQUEST_URL = "http://jantz.000webhostapp.com/SellerSyncCheckUser.php";
    private Map<String, String> params;

    public CheckUserPassRequest(String password, String email, Response.Listener<String> listener) {
        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("password", MD5.encrypt(password));
        params.put("email", email);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

