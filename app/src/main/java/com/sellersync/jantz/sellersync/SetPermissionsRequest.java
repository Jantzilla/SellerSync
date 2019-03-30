package com.sellersync.jantz.sellersync;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.md5simply.MD5;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.md5simply.MD5;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jantz on 5/9/2017.
 */

public class SetPermissionsRequest extends StringRequest {

    private static final String SET_PERMISSIONS_REQUEST_URL = "http://jantz.000webhostapp.com/SellerSyncSetPermRequest.php";
    private Map<String, String> params;

    public SetPermissionsRequest(String username, int listingPermission, int inventoryPermission,
                           int shippingPermission, int purchasingPermission, int returnsPermission, int adminPermission, Response.Listener<String> listener) {
        super(Method.POST, SET_PERMISSIONS_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
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

