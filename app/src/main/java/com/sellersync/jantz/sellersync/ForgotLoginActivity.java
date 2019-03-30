package com.sellersync.jantz.sellersync;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONException;
import org.json.JSONObject;

import static com.sellersync.jantz.sellersync.R.string.no_connection;
import static com.sellersync.jantz.sellersync.R.string.no_connection_direction;

public class ForgotLoginActivity extends AppCompatActivity {
    private AdView mAdView;
    private static final String ADMOB_APP_ID = "ca-app-pub-5985384760144093~3080645763";
    private ProgressDialog progressDialog;

    SharedPreferences.Editor editor;
    SharedPreferences pref;

    @Override
    public void onBackPressed()
    {
        Intent homeIntent = new Intent(ForgotLoginActivity.this, LoginActivity.class);
        ForgotLoginActivity.this.startActivity(homeIntent);
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
        setContentView(R.layout.activity_forgot_login);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        MobileAds.initialize(this, ADMOB_APP_ID);
        final LinearLayout forgotUsernameLayout = (LinearLayout) findViewById(R.id.forgot_user);
        final LinearLayout forgotPasswordLayout = (LinearLayout) findViewById(R.id.forgot_pass);
        final LinearLayout submitLayout = (LinearLayout) findViewById(R.id.ll_button);
        final CheckBox checkPass = (CheckBox) findViewById(R.id.cb_password);
        final CheckBox checkUser = (CheckBox) findViewById(R.id.cb_username);
        final TextView forgotBothView = (TextView) findViewById(R.id.forgot_pass_user);
        final TextView forgotEmail = (TextView) findViewById(R.id.forgot_email);
        final EditText passEmailEdit = (EditText) findViewById(R.id.et_pass_email);
        final EditText passUserEdit = (EditText) findViewById(R.id.et_pass_user);
        final EditText userPassEmail = (EditText) findViewById(R.id.et_user_email);
        final EditText userPassEdit = (EditText) findViewById(R.id.et_user_pass);
        final Button submit = (Button) findViewById(R.id.btn_submit);

        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        editor = pref.edit();

        progressDialog = new ProgressDialog(this);

        checkPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                passEmailEdit.setText("");
                passUserEdit.setText("");
                userPassEdit.setText("");
                userPassEmail.setText("");

                if(checkUser.isChecked() && checkPass.isChecked()) {
                    forgotBothView.setVisibility(View.VISIBLE);
                    forgotEmail.setVisibility(View.GONE);
                    forgotPasswordLayout.setVisibility(View.GONE);
                    forgotUsernameLayout.setVisibility(View.GONE);
                    submitLayout.setVisibility(View.GONE);
                }
                else if(checkPass.isChecked()) {
                    forgotEmail.setVisibility(View.VISIBLE);
                    forgotPasswordLayout.setVisibility(View.VISIBLE);
                    forgotUsernameLayout.setVisibility(View.GONE);
                    submitLayout.setVisibility(View.VISIBLE);
                    forgotBothView.setVisibility(View.GONE);
                }
                else if(checkUser.isChecked()) {
                    forgotEmail.setVisibility(View.VISIBLE);
                    forgotPasswordLayout.setVisibility(View.GONE);
                    forgotUsernameLayout.setVisibility(View.VISIBLE);
                    submitLayout.setVisibility(View.VISIBLE);
                    forgotBothView.setVisibility(View.GONE);
                }
                else if(!checkPass.isChecked()) {
                    forgotBothView.setVisibility(View.GONE);
                    forgotEmail.setVisibility(View.GONE);
                    forgotPasswordLayout.setVisibility(View.GONE);
                    forgotUsernameLayout.setVisibility(View.GONE);
                    submitLayout.setVisibility(View.GONE);
                }
            }
        });
        checkUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                passEmailEdit.setText("");
                passUserEdit.setText("");
                userPassEdit.setText("");
                userPassEmail.setText("");

                if(checkUser.isChecked() && checkPass.isChecked()) {
                    forgotBothView.setVisibility(View.VISIBLE);
                    forgotEmail.setVisibility(View.GONE);
                    forgotPasswordLayout.setVisibility(View.GONE);
                    forgotUsernameLayout.setVisibility(View.GONE);
                    submitLayout.setVisibility(View.GONE);
                }
                else if(checkUser.isChecked()) {
                    forgotEmail.setVisibility(View.VISIBLE);
                    forgotPasswordLayout.setVisibility(View.GONE);
                    forgotUsernameLayout.setVisibility(View.VISIBLE);
                    submitLayout.setVisibility(View.VISIBLE);
                    forgotBothView.setVisibility(View.GONE);
                }
                else if(checkPass.isChecked()) {
                    forgotEmail.setVisibility(View.VISIBLE);
                    forgotPasswordLayout.setVisibility(View.VISIBLE);
                    forgotUsernameLayout.setVisibility(View.GONE);
                    submitLayout.setVisibility(View.VISIBLE);
                    forgotBothView.setVisibility(View.GONE);
                }
                else if(!checkUser.isChecked()) {
                    forgotBothView.setVisibility(View.GONE);
                    forgotEmail.setVisibility(View.GONE);
                    forgotPasswordLayout.setVisibility(View.GONE);
                    forgotUsernameLayout.setVisibility(View.GONE);
                    submitLayout.setVisibility(View.GONE);
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passEmailEdit.getText().toString().equals("")&& passUserEdit.getText().toString().equals("") && userPassEdit.getText().toString().equals("") && userPassEmail.getText().toString().equals("")) {
                    Toast.makeText(ForgotLoginActivity.this, R.string.no_information_entered, Toast.LENGTH_LONG).show();
                }
                else if (!passUserEdit.getText().toString().equals("") && passEmailEdit.getText().toString().equals("")) {
                    Toast.makeText(ForgotLoginActivity.this, R.string.enter_email, Toast.LENGTH_LONG).show();
                }
                else if (!passEmailEdit.getText().toString().equals("") && passUserEdit.getText().toString().equals("")) {
                    Toast.makeText(ForgotLoginActivity.this, R.string.enter_username, Toast.LENGTH_LONG).show();
                }
                else if (!passEmailEdit.getText().toString().equals("")) {

                    if (isNetworkConnected()) {

                        String typeUsername = passUserEdit.getText().toString();
                        String typeEmail = passEmailEdit.getText().toString();

                        progressDialog.setMessage(getString(R.string.processing));
                        progressDialog.show();

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");

                                    if (success) {

                                        String username = jsonResponse.getString("username");
                                        String email = jsonResponse.getString("email");

                                        editor.putString("email", email);
                                        editor.putString("username", username);
                                        editor.apply();

                                        Intent intent = new Intent(ForgotLoginActivity.this, SelfResetPassActivity.class);
                                        ForgotLoginActivity.this.startActivity(intent);
                                        finish();

                                    } else {
                                        progressDialog.cancel();
                                        Toast.makeText(ForgotLoginActivity.this, R.string.invalid_information, Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        };

                        CheckPassUserRequest checkPassUserRequest = new CheckPassUserRequest(typeUsername, typeEmail, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(ForgotLoginActivity.this);
                        queue.add(checkPassUserRequest);

                    } else {
                        new AlertDialog.Builder(ForgotLoginActivity.this)
                                .setTitle(no_connection)
                                .setMessage(no_connection_direction)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).setIcon(android.R.drawable.ic_dialog_alert).show();
                    }
                }
                else if (!userPassEdit.getText().toString().equals("") && userPassEmail.getText().toString().equals("")) {
                    Toast.makeText(ForgotLoginActivity.this, R.string.enter_email, Toast.LENGTH_LONG).show();
                }
                else if (!userPassEmail.getText().toString().equals("") && userPassEdit.getText().toString().equals("")) {
                    Toast.makeText(ForgotLoginActivity.this, R.string.enter_password, Toast.LENGTH_LONG).show();
                }
                else if (!userPassEmail.getText().toString().equals("")) {

                    if (isNetworkConnected()) {

                        String typePassword = userPassEdit.getText().toString();
                        String typeEmail = userPassEmail.getText().toString();

                        progressDialog.setMessage(getString(R.string.processing));
                        progressDialog.show();

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");

                                    if (success) {

                                        String username = jsonResponse.getString("username");
                                        String email = jsonResponse.getString("email");

                                        editor.putString("email", email);
                                        editor.putString("username", username);
                                        editor.apply();

                                        Intent intent = new Intent(ForgotLoginActivity.this, SelfResetUserActivity.class);
                                        ForgotLoginActivity.this.startActivity(intent);
                                        finish();

                                    } else {
                                        progressDialog.cancel();
                                        Toast.makeText(ForgotLoginActivity.this, getString(R.string.invalid_information), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        };

                        CheckUserPassRequest checkUserPassRequest = new CheckUserPassRequest(typePassword, typeEmail, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(ForgotLoginActivity.this);
                        queue.add(checkUserPassRequest);

                    } else {
                        new AlertDialog.Builder(ForgotLoginActivity.this)
                                .setTitle(no_connection)
                                .setMessage(no_connection_direction)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).setIcon(android.R.drawable.ic_dialog_alert).show();
                    }
                }

            }
        });
    }
}

