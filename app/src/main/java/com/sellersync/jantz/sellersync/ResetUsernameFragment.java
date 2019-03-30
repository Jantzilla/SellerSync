package com.sellersync.jantz.sellersync;

import android.app.ProgressDialog;
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

public class ResetUsernameFragment extends Fragment {
    String employeeName,employeeTitle,employeeDepartment,employeeUsername;
    ProgressDialog progressDialog;
    String submitUsername;
    Button Submit;
    EditText etUsers;
    EditText newUsername;
    StringRequest request;
    private static final String INVENTORY_REQUEST_URL = "http://jantz.000webhostapp.com/QueryUsers.php";
    final ArrayList<Summarylist> summary= new ArrayList<>();
    final BindDictionary<Summarylist> dictionary= new BindDictionary<>();

    public void queryCity(final String query) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final FunDapter adapter= new FunDapter(ResetUsernameFragment.this.getActivity(),summary,R.layout.summary_layout,dictionary);
        final ListView listView = (ListView) getActivity().findViewById(R.id.lvUsers);
        Submit = (Button) getActivity().findViewById(R.id.btnSubmit);
        newUsername = (EditText) getActivity().findViewById(R.id.etNewUsername);
        dictionary.addStringField(R.id.tvsummary, new StringExtractor<Summarylist>() {
            @Override
            public String getStringValue(Summarylist summary1, int position) {
                return summary1.getName();
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

                    summary.add(new Summarylist("Employee Name: \n\t"+employeeName+"\nEmployee Title: \n\t"+employeeTitle+"\nEmployee Department: \n\t"+employeeDepartment));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            adapter.notifyDataSetChanged();
                            listView.getChildAt(position).setBackgroundColor(Color.LTGRAY);

                            etUsers.clearFocus();
                            InputMethodManager imm2 = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                            imm2.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            newUsername.requestFocus();
                            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);

                        }
                    });

                    Submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            submitUsername = newUsername.getText().toString();
                            if (MD5.encrypt(etUsers.getText().toString()).equals(employeeUsername) || etUsers.getText().toString().equalsIgnoreCase(employeeName) || etUsers.getText().toString().equalsIgnoreCase(employeeTitle) || etUsers.getText().toString().equalsIgnoreCase(employeeDepartment)) {
                                if (!submitUsername.equals("")) {
                                    progressDialog.setTitle("Updating Username");
                                    progressDialog.setMessage("Processing...");
                                    progressDialog.show();
                                    Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response2) {
                                            try {
                                                JSONObject jsonResponse2 = new JSONObject(response2);
                                                final boolean success = jsonResponse2.getBoolean("success");

                                                if (success) {
                                                    Toast.makeText(getContext(),"Selected user's username has been updated.", Toast.LENGTH_LONG).show();
                                                    progressDialog.cancel();
                                                } else {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                    builder.setMessage("Failed to update username.")
                                                            .setNegativeButton("Retry", null)
                                                            .create()
                                                            .show();
                                                }


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                    };
                                    ResetUserRequest resetUserRequest = new ResetUserRequest(employeeUsername, submitUsername, responseListener2);
                                    RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                                    queue.add(resetUserRequest);

                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setMessage("Please enter a username.")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("Search Users by Name, Username, Title, or Department.")
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


    public ResetUsernameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_reset_username, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etUsers = (EditText) view.findViewById(R.id.etUsers);
        progressDialog = new ProgressDialog(getContext());





        etUsers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etUsers.getText().toString().length() >=2) {
                    summary.clear();
                    queryCity(etUsers.getText().toString());
                    FunDapter adapter= new FunDapter(ResetUsernameFragment.this.getActivity(),summary,R.layout.summary_layout,dictionary);
                    final ListView listView = (ListView) getActivity().findViewById(R.id.lvUsers);
                    listView.setAdapter(adapter);//                                                                                     DO STUFF
                }
                else {
                    summary.clear();
                    FunDapter adapter= new FunDapter(ResetUsernameFragment.this.getActivity(),summary,R.layout.summary_layout,dictionary);
                    final ListView listView = (ListView) getActivity().findViewById(R.id.lvUsers);
                    listView.setAdapter(adapter);
                }
            }
        });

    }
}