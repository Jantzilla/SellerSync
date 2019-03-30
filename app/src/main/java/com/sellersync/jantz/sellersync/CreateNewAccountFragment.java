package com.sellersync.jantz.sellersync;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static com.sellersync.jantz.sellersync.R.string.retry;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateNewAccountFragment extends Fragment {
    private EditText etUsername, etPassword, etRePassword;
    private String username, password, rePassword;
    int listingPermission, inventoryPermission, shippingPermission, purchasingPermission, returnsPermission, adminPermission;
    CheckBox listingBox, inventoryBox, shippingBox, purchasingBox, returnsBox, adminBox;
    Button create;

    private ProgressDialog progressDialog;

    public CreateNewAccountFragment() {
        // Required empty public constructor
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE); // 1
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo(); // 2
        return networkInfo != null && networkInfo.isConnected(); // 3
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void register(){
        initialize();
        if (!validate()) {
            Toast.makeText(getContext(), R.string.failed_create_new_account, Toast.LENGTH_SHORT).show();
        } else {
            onRegistrationSuccess();
        }
    }
    public void onRegistrationSuccess() {

        if (isNetworkConnected()) {
            final SharedPreferences pref = getActivity().getSharedPreferences("login.conf", Context.MODE_PRIVATE);
            final String companyName = pref.getString("companyName","");

            progressDialog.setTitle(getString(R.string.create_account));
            progressDialog.setMessage(getString(R.string.processing));
            progressDialog.show();

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            Toast.makeText(getContext(), R.string.new_account_created, Toast.LENGTH_LONG).show();
                            etUsername.setText("");
                            etPassword.setText("");
                            etRePassword.setText("");
                            listingBox.setChecked(false);
                            inventoryBox.setChecked(false);
                            shippingBox.setChecked(false);
                            purchasingBox.setChecked(false);
                            returnsBox.setChecked(false);
                            adminBox.setChecked(false);
                            progressDialog.cancel();
                        } else if (!success) {
                            boolean reuser = jsonResponse.getBoolean("reuser");
                            if(reuser) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(R.string.username_in_use)
                                        .setNegativeButton(retry, null)
                                        .create()
                                        .show();

                                progressDialog.cancel();
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage(getString(R.string.failed_create_new_account))
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
            RegisterRequest registerRequest = new RegisterRequest(companyName, username, "", password, listingPermission, inventoryPermission,
                    shippingPermission, purchasingPermission, returnsPermission, adminPermission, responseListener);
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(registerRequest);

        } else {
            new AlertDialog.Builder(getContext())
                    .setTitle(getString(R.string.no_connection))
                    .setMessage(getString(R.string.no_connection_direction))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();
        }
    }
    public boolean validate() {
        boolean valid = true;
        if(username.isEmpty() || username.length() > 8) {
            etUsername.setError(getString(R.string.please_enter_valid_username) );
            valid = false;
        }
        if(password.isEmpty() || rePassword.isEmpty() || !password.equals(rePassword)) {
            valid = false;
            if (password.isEmpty() || rePassword.isEmpty()){
                etPassword.setError(getString(R.string.please_complete_password_fields) );
            } else if(!password.equals(rePassword)) {
                etPassword.setError(getString(R.string.password_doesnt_match));
            }
        }
        return valid;
    }
    public void initialize(){
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();
        rePassword = etRePassword.getText().toString();
        if(listingBox.isChecked() == true) {
            listingPermission = 1;
        }
        if(inventoryBox.isChecked() == true) {
            inventoryPermission = 1;
        }
        if(shippingBox.isChecked() == true) {
            shippingPermission = 1;
        }
        if(purchasingBox.isChecked() == true) {
            purchasingPermission = 1;
        }
        if(returnsBox.isChecked() == true) {
            returnsPermission = 1;
        }
        if(adminBox.isChecked() == true) {
            adminPermission = 1;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_create_new_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(getContext());
        etUsername = (EditText) getActivity().findViewById(R.id.etUsername);
        etPassword = (EditText) getActivity().findViewById(R.id.etPassword);
        etRePassword = (EditText) getActivity().findViewById(R.id.etRePassword);
        create = (Button) getActivity().findViewById(R.id.btnCreate);
        listingBox = (CheckBox) getActivity().findViewById(R.id.cbListing);
        inventoryBox = (CheckBox) getActivity().findViewById(R.id.cbInventory);
        shippingBox = (CheckBox) getActivity().findViewById(R.id.cbShipping);
        purchasingBox = (CheckBox) getActivity().findViewById(R.id.cbPurchasing);
        returnsBox = (CheckBox) getActivity().findViewById(R.id.cbReturns);
        adminBox = (CheckBox) getActivity().findViewById(R.id.cbAdmin);
        listingPermission = 0;
        inventoryPermission = 0;
        shippingPermission = 0;
        purchasingPermission = 0;
        returnsPermission = 0;
        adminPermission = 0;

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }
}
