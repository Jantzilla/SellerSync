package com.sellersync.jantz.sellersync;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kosalgeek.android.md5simply.MD5;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */

public class SetPermissionsFragment extends Fragment {
    String employeeName,employeeTitle,employeeDepartment,employeeUsername,employeeListingPermission,employeeShippingPermission,employeePurchasingPermission,employeeInventoryPermission,employeeReturnsPermission,employeeAdminPermission;
    int listingPermission, inventoryPermission, shippingPermission, purchasingPermission, returnsPermission, adminPermission;
    CheckBox listingBox, inventoryBox, shippingBox, purchasingBox, returnsBox, adminBox;
    ProgressDialog progressDialog;
    String submitUsername;
    Button Submit;
    EditText newUsername;
    StringRequest request;
    private static final String INVENTORY_REQUEST_URL = "http://jantz.000webhostapp.com/QueryUsers.php";
    final ArrayList<Summarylist> summary= new ArrayList<>();
    final BindDictionary<Summarylist> dictionary= new BindDictionary<>();

    public void queryCity(final String query) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final FunDapter adapter= new FunDapter(SetPermissionsFragment.this.getActivity(),summary,R.layout.summary_layout,dictionary);
        final ListView listView = (ListView) getActivity().findViewById(R.id.lvUsers);
        Submit = (Button) getActivity().findViewById(R.id.btnSubmit);
        newUsername = (EditText) getActivity().findViewById(R.id.etUsers);
        dictionary.addStringField(R.id.tvsummary, new StringExtractor<Summarylist>() {
            @Override
            public String getStringValue(Summarylist summary1, int position) {
                return summary1.getName();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.notifyDataSetChanged();
                listView.getChildAt(position).setBackgroundColor(Color.LTGRAY);

                newUsername.clearFocus();
                InputMethodManager imm2 = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(view.getWindowToken(), 0);

            }
        });

        request = new StringRequest(Request.Method.POST, INVENTORY_REQUEST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    summary.clear();

                    JSONObject jsonResponse = new JSONObject(response);
                    employeeUsername = jsonResponse.getString("username");
                    employeeName = jsonResponse.getString("employeeName");
                    employeeTitle = jsonResponse.getString("employeeTitle");
                    employeeDepartment = jsonResponse.getString("employeeDepartment");
                    employeeListingPermission = jsonResponse.getString("listingPermission");
                    employeeInventoryPermission = jsonResponse.getString("inventoryPermission");
                    employeeShippingPermission = jsonResponse.getString("shippingPermission");
                    employeePurchasingPermission = jsonResponse.getString("purchasingPermission");
                    employeeReturnsPermission = jsonResponse.getString("returnsPermission");
                    employeeAdminPermission = jsonResponse.getString("adminPermission");

                    if(employeeListingPermission.equals("1")) {
                        listingBox.setChecked(true);
                    }
                    if(employeeListingPermission.equals("0")) {
                        listingBox.setChecked(false);
                    }
                    if(employeeInventoryPermission.equals("1")) {
                        inventoryBox.setChecked(true);
                    }
                    if(employeeInventoryPermission.equals("0")) {
                        inventoryBox.setChecked(false);
                    }
                    if(employeeShippingPermission.equals("1")) {
                        shippingBox.setChecked(true);
                    }
                    if(employeeShippingPermission.equals("0")) {
                        shippingBox.setChecked(false);
                    }
                    if(employeePurchasingPermission.equals("1")) {
                        purchasingBox.setChecked(true);
                    }
                    if(employeePurchasingPermission.equals("0")) {
                        purchasingBox.setChecked(false);
                    }
                    if(employeeReturnsPermission.equals("1")) {
                        returnsBox.setChecked(true);
                    }
                    if(employeeReturnsPermission.equals("0")) {
                        returnsBox.setChecked(false);
                    }
                    if(employeeAdminPermission.equals("1")) {
                        adminBox.setChecked(true);
                    }
                    if(employeeAdminPermission.equals("0")) {
                        adminBox.setChecked(false);
                    }

                    summary.add(new Summarylist("Employee Name: \n\t"+employeeName+"\nEmployee Title: \n\t"+employeeTitle+"\nEmployee Department: \n\t"+employeeDepartment));

                            submitUsername = newUsername.getText().toString();
                            Submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!submitUsername.equals("")) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle(R.string.app_name);
                                    builder.setIcon(R.mipmap.ic_launcher);
                                    builder.setMessage("Are you sure you want to update this user's permissions?")
                                            .setCancelable(false)
                                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {

                                                    final Response.Listener<String> responseListener3 = new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response2) {
                                                            try {
                                                                JSONObject jsonResponse2 = new JSONObject(response2);
                                                                final boolean success = jsonResponse2.getBoolean("success");

                                                                if (success) {
                                                                    newUsername.setText("");
                                                                    listingBox.setChecked(false);
                                                                    inventoryBox.setChecked(false);
                                                                    shippingBox.setChecked(false);
                                                                    purchasingBox.setChecked(false);
                                                                    returnsBox.setChecked(false);
                                                                    adminBox.setChecked(false);
                                                                    Toast.makeText(getContext(),"User permissions have been updated.", Toast.LENGTH_LONG).show();
                                                                    progressDialog.cancel();
                                                                } else {
                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                                    builder.setMessage("Failed to update user permissions.")
                                                                            .setNegativeButton("Retry", null)
                                                                            .create()
                                                                            .show();
                                                                }


                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }


                                                        }
                                                    };



                                    if (MD5.encrypt(newUsername.getText().toString()).equals(employeeUsername) || newUsername.getText().toString().equalsIgnoreCase(employeeName) || newUsername.getText().toString().equalsIgnoreCase(employeeTitle) || newUsername.getText().toString().equalsIgnoreCase(employeeDepartment)) {

                                            progressDialog.setTitle("Updating Permissions");
                                            progressDialog.setMessage("Processing...");
                                            progressDialog.show();
                                            if(listingBox.isChecked()) {
                                                listingPermission = 1;
                                            }
                                            if(inventoryBox.isChecked()) {
                                                inventoryPermission = 1;
                                            }
                                            if(shippingBox.isChecked()) {
                                                shippingPermission = 1;
                                            }
                                            if(purchasingBox.isChecked()) {
                                                purchasingPermission = 1;
                                            }
                                            if(returnsBox.isChecked()) {
                                                returnsPermission = 1;
                                            }
                                            if(adminBox.isChecked()) {
                                                adminPermission = 1;
                                            }
                                            if(adminBox.isChecked() == false && employeeAdminPermission.equals("1")) {

                                                Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response2) {
                                                        try {
                                                            JSONObject jsonResponse2 = new JSONObject(response2);
                                                            final boolean success = jsonResponse2.getBoolean("success");

                                                            if (success) {
                                                                final boolean multiAdmin = jsonResponse2.getBoolean("multiAdmin");
                                                                try {
                                                                    if(multiAdmin) {
                                                                        progressDialog.cancel();
                                                                        SetPermissionsRequest setPermissionsRequest = new SetPermissionsRequest(employeeUsername, listingPermission, inventoryPermission, shippingPermission, purchasingPermission, returnsPermission, adminPermission, responseListener3);
                                                                        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                                                                        queue.add(setPermissionsRequest);
                                                                        summary.clear();
                                                                        adapter.notifyDataSetChanged();
                                                                    }
                                                                    else {
                                                                        progressDialog.cancel();
                                                                        Toast.makeText(getContext(),"You cannot disable the administrator permission for this user.", Toast.LENGTH_LONG).show();
                                                                        newUsername.setText("");
                                                                        summary.clear();
                                                                        adapter.notifyDataSetChanged();
                                                                    }
                                                                } catch (Exception e) {}

                                                            } else {

                                                            }


                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }


                                                    }
                                                };
                                                AdminCountRequest adminCountRequest = new AdminCountRequest(employeeUsername, responseListener2);
                                                RequestQueue queue2 = Volley.newRequestQueue(getActivity().getApplicationContext());
                                                queue2.add(adminCountRequest);


                                            }
                                            else {
                                                SetPermissionsRequest setPermissionsRequest = new SetPermissionsRequest(employeeUsername, listingPermission, inventoryPermission, shippingPermission, purchasingPermission, returnsPermission, adminPermission, responseListener3);
                                                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                                                queue.add(setPermissionsRequest);
                                                summary.clear();
                                                adapter.notifyDataSetChanged();
                                            }


                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setMessage("Search Users by Name, Username, Title, or Department.")
                                                .setNegativeButton("Retry", null)
                                                .create()
                                                .show();
                                    }

                                                }
                                            })
                                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();

                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setMessage("Please select a user.")
                                                .setNegativeButton("Retry", null)
                                                .create()
                                                .show();
                                    }

                                }
                            });




                    if (summary.isEmpty()) {


                        listView.setAdapter(adapter);
                    }
                    else {

                        listView.setAdapter(adapter);
                    }


                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("restockQuery", query);
                return hashMap;
            }
        };
        queue.add(request);
    }


    public SetPermissionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_set_permissions, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newUsername = (EditText) view.findViewById(R.id.etUsers);
        progressDialog = new ProgressDialog(getContext());
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





        newUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (newUsername.getText().toString().length() >=2) {
                    summary.clear();
                    listingBox.setChecked(false);
                    inventoryBox.setChecked(false);
                    shippingBox.setChecked(false);
                    purchasingBox.setChecked(false);
                    returnsBox.setChecked(false);
                    adminBox.setChecked(false);
                    queryCity(newUsername.getText().toString());
                    FunDapter adapter= new FunDapter(SetPermissionsFragment.this.getActivity(),summary,R.layout.summary_layout,dictionary);
                    final ListView listView = (ListView) getActivity().findViewById(R.id.lvUsers);
                    listView.setAdapter(adapter);//                                                                                     DO STUFF
                }
                if(newUsername.getText().toString().isEmpty()) {
                    submitUsername = newUsername.getText().toString();
                }
                else {
                    summary.clear();
                    listingBox.setChecked(false);
                    inventoryBox.setChecked(false);
                    shippingBox.setChecked(false);
                    purchasingBox.setChecked(false);
                    returnsBox.setChecked(false);
                    adminBox.setChecked(false);
                    FunDapter adapter= new FunDapter(SetPermissionsFragment.this.getActivity(),summary,R.layout.summary_layout,dictionary);
                    final ListView listView = (ListView) getActivity().findViewById(R.id.lvUsers);
                    listView.setAdapter(adapter);
                }
            }
        });

    }
}