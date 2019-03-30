package com.sellersync.jantz.sellersync;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.md5simply.MD5;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jantz on 5/9/2017.
 */

public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://jantz.000webhostapp.com/Register1.php";
    private Map<String, String> params;

    public RegisterRequest(String companyName, String username, String email, String password, int listingPermission, int inventoryPermission,
                           int shippingPermission, int purchasingPermission, int returnsPermission, int adminPermission, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("companyName", companyName);
        params.put("username", MD5.encrypt(username));
        params.put("email", email);
        params.put("password", MD5.encrypt(password));
        params.put("listingPermission", String.valueOf(listingPermission));
        params.put("inventoryPermission", String.valueOf(inventoryPermission));
        params.put("shippingPermission", String.valueOf(shippingPermission));
        params.put("purchasingPermission", String.valueOf(purchasingPermission));
        params.put("returnsPermission", String.valueOf(returnsPermission));
        params.put("adminPermission", String.valueOf(adminPermission));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
