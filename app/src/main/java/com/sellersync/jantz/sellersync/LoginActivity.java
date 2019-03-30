package com.sellersync.jantz.sellersync;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.kosalgeek.android.md5simply.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import static com.sellersync.jantz.sellersync.R.string.login_failed;
import static com.sellersync.jantz.sellersync.R.string.no;
import static com.sellersync.jantz.sellersync.R.string.no_connection;
import static com.sellersync.jantz.sellersync.R.string.no_connection_direction;
import static com.sellersync.jantz.sellersync.R.string.retry;
import static com.sellersync.jantz.sellersync.R.string.yes;


public class LoginActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private EditText etUsername, etPassword;
    private String username, password;
    private String titleUsername, titlePassword;
    private ProgressDialog progressDialog;

    SharedPreferences.Editor editor;
    SharedPreferences pref;
    CheckBox cbRemember;

    boolean checkFlag;

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(R.string.confirm_app_close)
                .setCancelable(false)
                .setPositiveButton(yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        finish();
                    }
                })
                .setNegativeButton(no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

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
        setContentView(R.layout.activity_login);

        cbRemember = (CheckBox) findViewById(R.id.cbRemember);
        cbRemember.setOnCheckedChangeListener(this);
        checkFlag = cbRemember.isChecked();

        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        editor = pref.edit();

        progressDialog = new ProgressDialog(this);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bLogin = (Button) findViewById(R.id.bLogin);
        final TextView registerLink = (TextView) findViewById(R.id.tvRegister);
        final TextView forgotLogin = (TextView) findViewById(R.id.tvForgotLogin);


        forgotLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, ForgotLoginActivity.class);
                LoginActivity.this.startActivity(registerIntent);
                finish();

            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
                finish();

            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                login();

            }
        });
    }
    public void login(){
        initialize();
        if (!validate()) {
            Toast.makeText(this, R.string.login_failed, Toast.LENGTH_SHORT).show();
        } else {
            onRegistrationSuccess();
        }
    }
    public void onRegistrationSuccess() {

        if (isNetworkConnected()) {

            progressDialog.setTitle(getString(R.string.login));
            progressDialog.setMessage(getString(R.string.processing));
            progressDialog.show();

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {

                            progressDialog.cancel();

                            String companyName = jsonResponse.getString("companyName");
                            String employeeEmail = jsonResponse.getString("email");
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

                            editor.putString("username", titleUsername);
                            editor.putString("companyName", companyName);
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

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("companyName", companyName);
                            intent.putExtra("username", titleUsername);

                            if (checkFlag) {
                                editor.putString("username", titleUsername);
                                editor.putString("password", titlePassword);
                                editor.putString("companyName", companyName);
                                editor.apply();
                            }

                            LoginActivity.this.startActivity(intent);
                            finish();

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage(login_failed)
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
            LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(loginRequest);

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
        if(username.isEmpty()) {
            etUsername.setError(getString(R.string.username_is_blank) );
            valid = false;
        }
        if(password.isEmpty()) {
            valid = false;
                etPassword.setError(getString(R.string.password_is_blank));
        }
        return valid;
    }
    public void initialize(){
        titleUsername = etUsername.getText().toString();
        titlePassword = etPassword.getText().toString();
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        checkFlag = isChecked;
    }
}

