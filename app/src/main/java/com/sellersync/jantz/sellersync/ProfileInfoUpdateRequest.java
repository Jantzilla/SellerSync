package com.sellersync.jantz.sellersync;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.md5simply.MD5;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jantz on 5/9/2017.
 */

public class ProfileInfoUpdateRequest extends StringRequest {

    private static final String PROFILE_EDIT_REQUEST_URL = "http://jantz.000webhostapp.com/EditProfile.php";
    private Map<String, String> params;

    public ProfileInfoUpdateRequest(String employeeName, String employeeTitle, String employeeDepartment, String employeeEmail, String employeePhone, String userName, Response.Listener<String> listener) {
        super(Method.POST, PROFILE_EDIT_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("employeeName", employeeName);
        params.put("employeeTitle", employeeTitle);
        params.put("employeeDepartment", employeeDepartment);
        params.put("email", employeeEmail);
        params.put("employeePhone", employeePhone);
        params.put ("userName", MD5.encrypt(userName));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
