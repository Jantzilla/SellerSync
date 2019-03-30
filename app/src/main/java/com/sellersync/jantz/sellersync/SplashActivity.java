package com.sellersync.jantz.sellersync;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {

    private boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE); // 1
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo(); // 2
        return networkInfo != null && networkInfo.isConnected(); // 3
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();
        final String prefUsername = pref.getString("username", "");
        String prefPassword = pref.getString("password", "");

        if (!(prefUsername.equals("") || prefPassword.equals(""))) {

            if (!isNetworkConnected()) {
                {
                    new AlertDialog.Builder(this)
                            .setTitle("No Internet Connection")
                            .setMessage("SellerSync could not establish an internet connection. Check if you have a network connection and try again.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).setIcon(android.R.drawable.ic_dialog_alert).show();
                }
            }


            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {

                            String employeeEmail = jsonResponse.getString("email");
                            String companyName = jsonResponse.getString("companyName");
                            int listingPermission = jsonResponse.getInt("listingPermission");
                            int inventoryPermission = jsonResponse.getInt("inventoryPermission");
                            int shippingPermission = jsonResponse.getInt("shippingPermission");
                            int purchasingPermission = jsonResponse.getInt("purchasingPermission");
                            int returnsPermission = jsonResponse.getInt("returnsPermission");
                            int adminPermission = jsonResponse.getInt("adminPermission");
                            String employeeName = jsonResponse.getString("employeeName");
                            String employeeTitle = jsonResponse.getString("employeeTitle");
                            String employeeDepartment = jsonResponse.getString("employeeDepartment");
                            String employeePhone = jsonResponse.getString("employeePhone");
                            String photo = jsonResponse.getString("photo");

                            editor.putString("employeeEmail", employeeEmail);
                            editor.putInt("listingPermission", listingPermission);
                            editor.putInt("inventoryPermission", inventoryPermission);
                            editor.putInt("shippingPermission", shippingPermission);
                            editor.putInt("purchasingPermission", purchasingPermission);
                            editor.putInt("returnsPermission", returnsPermission);
                            editor.putInt("adminPermission", adminPermission);
                            editor.putString("employeeName", employeeName);
                            editor.putString("employeeTitle", employeeTitle);
                            editor.putString("employeeDepartment", employeeDepartment);
                            editor.putString("employeePhone", employeePhone);
                            editor.putString("profileImage", photo);
                            editor.apply();

                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            intent.putExtra("companyName", companyName);
                            intent.putExtra("username", prefUsername);
                            SplashActivity.this.startActivity(intent);finish();finish();


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            LoginRequest loginRequest = new LoginRequest(prefUsername, prefPassword, responseListener);
            RequestQueue queue = Volley.newRequestQueue(SplashActivity.this);
            queue.add(loginRequest);
        }
        else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
