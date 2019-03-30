package com.sellersync.jantz.sellersync;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SelfResetUserActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;

    private AdView mAdView, mAdView2;
    private static final String ADMOB_APP_ID = "ca-app-pub-5985384760144093~3080645763";
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    @Override
    public void onBackPressed()
    {
        Intent homeIntent = new Intent(SelfResetUserActivity.this, ForgotLoginActivity.class);
        SelfResetUserActivity.this.startActivity(homeIntent);
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
        setContentView(R.layout.activity_self_reset_user);
        mAdView = (AdView) findViewById(R.id.adView);
        mAdView2 = (AdView) findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView2.loadAd(adRequest);
        MobileAds.initialize(this, ADMOB_APP_ID);
        final EditText newUsername = (EditText) findViewById(R.id.et_username_one);
        final EditText reUsername = (EditText) findViewById(R.id.et_username_two);
        Button submitChange = (Button) findViewById(R.id.btn_submit_username);

        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        editor = pref.edit();

        progressDialog = new ProgressDialog(this);

        submitChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newUsername.getText().toString().equals("") && reUsername.getText().toString().equals("")) {
                    Toast.makeText(SelfResetUserActivity.this, "Please choose a new password.", Toast.LENGTH_LONG).show();
                }
                else {
                    if (!reUsername.getText().toString().equals(newUsername.getText().toString())) {
                        Toast.makeText(SelfResetUserActivity.this, getString(R.string.username_doesnt_match), Toast.LENGTH_LONG).show();
                    }
                    if (reUsername.getText().toString().equals(newUsername.getText().toString())) {
                        if(reUsername.getText().toString().length() < 8) {
                            Toast.makeText(SelfResetUserActivity.this, getString(R.string.your_username_must_be_eight), Toast.LENGTH_LONG).show();
                        }
                        else {
                            if (isNetworkConnected()) {

                                String typePassword = newUsername.getText().toString();

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
                                                Toast.makeText(SelfResetUserActivity.this, R.string.username_successfully_updated, Toast.LENGTH_LONG).show();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Intent intent = new Intent(SelfResetUserActivity.this, LoginActivity.class);
                                                        SelfResetUserActivity.this.startActivity(intent);
                                                        finish();
                                                    }
                                                }, 2200);

                                            } else {
                                                progressDialog.cancel();
                                                Toast.makeText(SelfResetUserActivity.this, R.string.unable_to_update_username, Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                };

                                String storedUsername = pref.getString("username","");

                                ResetUserRequest resetUserRequest = new ResetUserRequest(storedUsername, typePassword, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(SelfResetUserActivity.this);
                                queue.add(resetUserRequest);

                            } else {
                                new AlertDialog.Builder(SelfResetUserActivity.this)
                                        .setTitle(no_connection)
                                        .setMessage(no_connection_direction)
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        }).setIcon(android.R.drawable.ic_dialog_alert).show();
                            }

                        }
                    }
                }

            }
        });

    }
}
