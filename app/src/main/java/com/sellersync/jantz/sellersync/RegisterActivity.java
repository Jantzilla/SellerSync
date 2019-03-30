package com.sellersync.jantz.sellersync;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static com.sellersync.jantz.sellersync.R.string.no_connection;
import static com.sellersync.jantz.sellersync.R.string.no_connection_direction;
import static com.sellersync.jantz.sellersync.R.string.registration_failed;
import static com.sellersync.jantz.sellersync.R.string.retry;

public class RegisterActivity extends AppCompatActivity {
    private EditText etCompanyName, etUsername, etEmail, etPassword, etRePassword;
    private String companyName, username, email, password, rePassword;
    int listingPermission, inventoryPermission, shippingPermission, purchasingPermission, returnsPermission, adminPermission;

    private ProgressDialog progressDialog;

    @Override
    public void onBackPressed()
    {
        Intent logoutIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        RegisterActivity.this.startActivity(logoutIntent);
        finish();

    }

    private boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE); // 1
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo(); // 2
        return networkInfo != null && networkInfo.isConnected(); // 3
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

         progressDialog = new ProgressDialog(this);

        etCompanyName = (EditText) findViewById(R.id.etCompanyName);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRePassword = (EditText) findViewById(R.id.etRePassword);
        final Button bRegister =(Button) findViewById(R.id.bRegister);
        final TextView loginLink = (TextView) findViewById(R.id.tvLogin);
        listingPermission = 1;
        inventoryPermission = 1;
        shippingPermission = 1;
        purchasingPermission = 1;
        returnsPermission = 1;
        adminPermission = 1;

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(loginIntent);
                finish();

            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                register ();
            }
        });
    }
    public void register(){
        initialize();
        if (!validate()) {
            Toast.makeText(this, R.string.registration_failed, Toast.LENGTH_SHORT).show();
        } else {
            onRegistrationSuccess();
        }
    }
    public void onRegistrationSuccess() {

        if (isNetworkConnected()) {

            progressDialog.setTitle(getString(R.string.register));
            progressDialog.setMessage(getString(R.string.processing));
            progressDialog.show();

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            RegisterActivity.this.startActivity(intent);
                            finish();
                        } else if (!success) {
                            boolean reuser = jsonResponse.getBoolean("reuser");
                            if(reuser) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage(getString(R.string.username_in_use))
                                        .setNegativeButton(retry, null)
                                        .create()
                                        .show();

                                progressDialog.cancel();
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setMessage(registration_failed)
                                    .setNegativeButton(retry, null)
                                    .create()
                                    .show();

                            progressDialog.cancel();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            };
            RegisterRequest registerRequest = new RegisterRequest(companyName, username, email, password, listingPermission, inventoryPermission,
             shippingPermission, purchasingPermission, returnsPermission, adminPermission, responseListener);
            RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
            queue.add(registerRequest);

        } else {
            new AlertDialog.Builder(this)
                    .setTitle(no_connection)
                    .setMessage(no_connection_direction)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();
        }
    }
    public boolean validate() {
        boolean valid = true;
        if(companyName.isEmpty() || companyName.length() > 10) {
            etCompanyName.setError(getString(R.string.please_enter_valid_company_name));
            valid = false;
        }
        if(username.isEmpty() || username.length() > 8) {
            etUsername.setError(getString(R.string.please_enter_valid_username) );
            valid = false;
        }
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.please_enter_valid_email));
            valid = false;
        }
        if(password.isEmpty() || rePassword.isEmpty() || !password.equals(rePassword)) {
            valid = false;
            if (password.isEmpty() || rePassword.isEmpty()){
                etPassword.setError(getString(R.string.please_complete_password_fields));
            } else if(!password.equals(rePassword)) {
                etPassword.setError(getString(R.string.password_doesnt_match));
            }
        }
        return valid;
    }
    public void initialize(){
        companyName = etCompanyName.getText().toString();
        username = etUsername.getText().toString();
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        rePassword = etRePassword.getText().toString();

    }
}
