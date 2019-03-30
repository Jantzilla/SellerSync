package com.sellersync.jantz.sellersync;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import static com.sellersync.jantz.sellersync.R.string.retry;

/**
 * A simple {@link Fragment} subclass.
 */

public class CloseAccountFragment extends Fragment {
    String employeeName,employeeTitle,employeeDepartment,employeeUsername,employeeAdminPermission;
    String submitUser;
    ProgressDialog progressDialog;
    Button Delete, DeleteMe;
    EditText etUsers;
    StringRequest request;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private static final String USER_REQUEST_URL = "http://jantz.000webhostapp.com/QueryUsers.php";
    final ArrayList<Summarylist> accounts = new ArrayList<>();
    final BindDictionary<Summarylist> dictionary= new BindDictionary<>();

    public void queryCity(final String query) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final ListView listView = (ListView) getActivity().findViewById(R.id.lvUsers);
        etUsers = (EditText) getActivity().findViewById(R.id.etUsers);
        Delete = (Button) getActivity().findViewById(R.id.btnSubmit);
        final FunDapter adapter= new FunDapter(CloseAccountFragment.this.getActivity(),accounts,R.layout.summary_layout,dictionary);
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

                etUsers.clearFocus();
                InputMethodManager imm2 = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(view.getWindowToken(), 0);

            }
        });

        request = new StringRequest(Request.Method.POST, USER_REQUEST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    accounts.clear();

                    JSONObject jsonResponse = new JSONObject(response);
                    employeeUsername = jsonResponse.getString("username");
                    employeeName = jsonResponse.getString("employeeName");
                    employeeTitle = jsonResponse.getString("employeeTitle");
                    employeeDepartment = jsonResponse.getString("employeeDepartment");
                    employeeAdminPermission = jsonResponse.getString("adminPermission");

                    accounts.add(new Summarylist(getString(R.string.employee_name)+"\t\t\t"+employeeName+"\n"+getString(R.string.employee_title)+"\t\t\t"+employeeTitle+"\n"+getString(R.string.department)+"\t\t\t"+employeeDepartment));

                    if (accounts.isEmpty()) {


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


    public CloseAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_close_account, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etUsers = (EditText) view.findViewById(R.id.etUsers);
        progressDialog = new ProgressDialog(getContext());
        Delete = (Button) getActivity().findViewById(R.id.btnSubmit);
        DeleteMe = (Button) getActivity().findViewById(R.id.btnDeleteMe);
        final FunDapter adapter= new FunDapter(CloseAccountFragment.this.getActivity(),accounts,R.layout.summary_layout,dictionary);

        final SharedPreferences pref = getActivity().getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        editor = pref.edit();
        final Integer adminPer = pref.getInt("adminPermission",0);

        if(adminPer == 0) {
            Delete.setVisibility(View.GONE);
            etUsers.setVisibility(View.GONE);
            DeleteMe.setVisibility(View.VISIBLE);
        }

        submitUser = etUsers.getText().toString();
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etUsers.getText().toString().equals("") && !accounts.isEmpty()) {

                    if (MD5.encrypt(etUsers.getText().toString()).equals(employeeUsername) || etUsers.getText().toString().equalsIgnoreCase(employeeName) || etUsers.getText().toString().equalsIgnoreCase(employeeTitle) || etUsers.getText().toString().equalsIgnoreCase(employeeDepartment)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle(R.string.app_name);
                        builder.setIcon(R.mipmap.ic_launcher);
                        builder.setMessage(R.string.confirm_account_deletion)
                                .setCancelable(false)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        progressDialog.setTitle("Deleting Selected User");
                                        progressDialog.setMessage(getString(R.string.processing));
                                        progressDialog.show();

                                        final Response.Listener<String> responseListener3 = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response2) {
                                                try {
                                                    JSONObject jsonResponse2 = new JSONObject(response2);
                                                    final boolean success = jsonResponse2.getBoolean("success");

                                                    if (success) {
                                                        progressDialog.cancel();
                                                        if (employeeUsername.equals(MD5.encrypt(pref.getString("username", "")))) {
                                                            editor.putString("username", "");
                                                            editor.putString("password", "");
                                                            editor.apply();

                                                            Toast.makeText(getContext(), "User has successfully been deleted.", Toast.LENGTH_LONG).show();
                                                            new Handler().postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                                                                    getActivity().getApplication().startActivity(intent);
                                                                    getActivity().finish();
                                                                    accounts.clear();
                                                                    adapter.notifyDataSetChanged();
                                                                }
                                                            }, 2200);

                                                        } else {
                                                            progressDialog.cancel();
                                                            Toast.makeText(getContext(), "User has successfully been deleted.", Toast.LENGTH_LONG).show();
                                                            etUsers.setText("");
                                                            accounts.clear();
                                                            adapter.notifyDataSetChanged();
                                                        }

                                                    } else {
                                                        progressDialog.cancel();
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                        builder.setMessage(R.string.failed_to_delete_account)
                                                                .setNegativeButton(retry, null)
                                                                .create()
                                                                .show();
                                                    }


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }


                                            }
                                        };

                                        Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response2) {
                                                try {
                                                    JSONObject jsonResponse2 = new JSONObject(response2);
                                                    final boolean success = jsonResponse2.getBoolean("success");

                                                    if (success) {
                                                        final boolean multiAdmin = jsonResponse2.getBoolean("multiAdmin");
                                                        try {
                                                            if (multiAdmin) {
                                                                progressDialog.cancel();
                                                                DeleteRequest deleteRequest = new DeleteRequest(employeeUsername, responseListener3);
                                                                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                                                                queue.add(deleteRequest);
                                                            } else {
                                                                progressDialog.cancel();
                                                                Toast.makeText(getContext(), "You cannot delete the only administrator account.", Toast.LENGTH_LONG).show();
                                                            }
                                                        } catch (Exception e) {
                                                        }

                                                    } else {

                                                    }


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }


                                            }
                                        };

                                        if (employeeAdminPermission.equals("1")) {
                                            AdminCountRequest adminCountRequest = new AdminCountRequest(employeeUsername, responseListener2);
                                            RequestQueue queue2 = Volley.newRequestQueue(getActivity().getApplicationContext());
                                            queue2.add(adminCountRequest);
                                        } else {
                                            DeleteRequest deleteRequest = new DeleteRequest(employeeUsername, responseListener3);
                                            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                                            queue.add(deleteRequest);
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
                        builder.setMessage("Search for users by Username, Employee Name, Employee Title, or Employee Department.")
                                .setNegativeButton(retry, null)
                                .create()
                                .show();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Please select a user.")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                }

            }
        });


        DeleteMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.app_name);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage(R.string.confirm_account_deletion)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                final SharedPreferences pref = getActivity().getSharedPreferences("login.conf", Context.MODE_PRIVATE);
                                final String userName = pref.getString("username","");

                                progressDialog.setTitle("Deleting Selected User");
                                progressDialog.setMessage(getString(R.string.processing));
                                progressDialog.show();
                                Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response2) {
                                        try {
                                            JSONObject jsonResponse2 = new JSONObject(response2);
                                            final boolean success = jsonResponse2.getBoolean("success");

                                            if (success) {
                                                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                                                getActivity().getApplication().startActivity(intent);
                                                getActivity().finish();
                                                progressDialog.cancel();
                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                builder.setMessage(R.string.failed_to_delete_account)
                                                        .setNegativeButton(retry, null)
                                                        .create()
                                                        .show();
                                            }


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                };
                                DeleteRequest deleteRequest = new DeleteRequest(MD5.encrypt(userName), responseListener2);
                                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                                queue.add(deleteRequest);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();



            }
        });



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
                    accounts.clear();
                    queryCity(etUsers.getText().toString());
                    FunDapter adapter= new FunDapter(CloseAccountFragment.this.getActivity(),accounts,R.layout.summary_layout,dictionary);
                    final ListView listView = (ListView) getActivity().findViewById(R.id.lvUsers);
                    listView.setAdapter(adapter);
                }
                if(etUsers.getText().toString().isEmpty()) {
                    submitUser = etUsers.getText().toString();
                }
                else {
                    accounts.clear();
                    FunDapter adapter= new FunDapter(CloseAccountFragment.this.getActivity(),accounts,R.layout.summary_layout,dictionary);
                    final ListView listView = (ListView) getActivity().findViewById(R.id.lvUsers);
                    listView.setAdapter(adapter);
                }
            }
        });

    }
}