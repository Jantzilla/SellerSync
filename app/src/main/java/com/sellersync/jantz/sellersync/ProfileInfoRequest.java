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

public class ProfileInfoRequest extends StringRequest{

    private static final String PROFILE_INFO_REQUEST_URL = "http://jantz.000webhostapp.com/ProfileInfo.php";
    private Map<String, String> params;

    public ProfileInfoRequest(String username, Response.Listener<String> listener) {
        super(Request.Method.POST, PROFILE_INFO_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", MD5.encrypt(username));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
