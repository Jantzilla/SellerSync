package com.sellersync.jantz.sellersync;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.service.chooser.ChooserTarget;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.sellersync.jantz.sellersync.R.string.cancel;
import static com.sellersync.jantz.sellersync.R.string.profile_edit;
import static com.sellersync.jantz.sellersync.R.string.submit;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    ArrayAdapter<String> adapter;
    ImageView profile;
    TextView name,title,department,email,phone;
    ProgressDialog progressDialog;
    SharedPreferences.Editor editor;
    private EditText editTextName;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private String UPLOAD_URL ="http://jantz.000webhostapp.com/upload.php";
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "username";

    private boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE); // 1
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo(); // 2
        return networkInfo != null && networkInfo.isConnected(); // 3
    }


    public ProfileFragment() {
        // Required empty public constructor
    }
    public static Bitmap decodeToBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                profile.setImageBitmap(bitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                editor.putString("profileImage", encodedImage);
                editor.commit();
                uploadImage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    private void uploadImage(){
        final SharedPreferences pref = getActivity().getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        final String username = pref.getString("username","");
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(getContext(),getString(R.string.uploading),getString(R.string.please_wait),false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(getActivity(), R.string.image_uploaded, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name
                //String name = editTextName.getText().toString().trim();                                                    //// FIXME: 6/23/2017

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, username);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //ImageView checkMark = (ImageView) getView().findViewById(R.id.imageView);
        //checkMark.setVisibility(View.VISIBLE);
        profile  = (ImageView) getActivity().findViewById(R.id.profile_image);


        final SharedPreferences pref = getActivity().getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        final String username = pref.getString("username","");
        final String profileImage = pref.getString("profileImage","");
        editor = pref.edit();





            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {

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

                            editor.putInt("listingPermission", listingPermission);
                            editor.putInt("inventoryPermission", inventoryPermission);
                            editor.putInt("shippingPermission", shippingPermission);
                            editor.putInt("purchasingPermission", purchasingPermission);
                            editor.putInt("returnsPermission", returnsPermission);
                            editor.putInt("adminPermission", adminPermission);
                            editor.putString("employeeName", employeeName);
                            editor.putString("employeeTitle", employeeTitle);
                            editor.putString("employeeDepartment", employeeDepartment);
                            editor.putString("employeeEmail", employeeEmail);
                            editor.putString("employeePhone", employeePhone);
                            editor.apply();


                            name.setText(employeeName);
                            title.setText(employeeTitle);
                            department.setText(employeeDepartment);
                            email.setText(employeeEmail);
                            phone.setText(employeePhone);


                        } else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            ProfileInfoRequest profileInfoRequest = new ProfileInfoRequest(username, responseListener);
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(profileInfoRequest);


        View view =inflater.inflate(R.layout.fragment_profile,container,false);
        final ArrayList<String> reminders = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_checked, reminders);
        final ListView listview = (ListView)view.findViewById(R.id.profilelistview);
        listview.setAdapter(adapter);
        profile = (ImageView) view.findViewById(R.id.profile_image);
        name = (TextView) view.findViewById(R.id.user_name);
        title = (TextView) view.findViewById(R.id.user_title);
        department = (TextView) view.findViewById(R.id.user_dept);
        email = (TextView) view.findViewById(R.id.email);
        phone = (TextView) view.findViewById(R.id.phone);

        if(!profileImage.equals("")) {
            profile.setImageBitmap(decodeToBase64(profileImage));
        }

        if(!isNetworkConnected()){
            final String employeeName1 = pref.getString("employeeName", "");
            final String employeeTitle1 = pref.getString("employeeTitle", "");
            final String employeeDepartment1 = pref.getString("employeeDepartment", "");
            final String employeeEmail1 = pref.getString("employeeEmail", "");
            final String employeePhone1 = pref.getString("employeePhone", "");

            name.setText(employeeName1);
            title.setText(employeeTitle1);
            department.setText(employeeDepartment1);
            email.setText(employeeEmail1);
            phone.setText(employeePhone1);
        }


        final int listingPermission = pref.getInt("listingPermission", 0);
        final int inventoryPermission = pref.getInt("inventoryPermission", 0);
        final int shippingPermission = pref.getInt("shippingPermission", 0);
        final int purchasingPermission = pref.getInt("purchasingPermission", 0);
        final int returnsPermission = pref.getInt("returnsPermission", 0);
        final int adminPermission = pref.getInt("adminPermission", 0);


        final Response.Listener<String> responseListener2 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        };


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
                Toast.makeText(getContext(), R.string.choose_a_profile_image, Toast.LENGTH_LONG).show();

            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText newNameEditText = new EditText(getContext());
                AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle(R.string.profile_edit)
                        .setMessage(R.string.edit_name_to)
                        .setView(newNameEditText)
                        .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newName = String.valueOf(newNameEditText.getText());
                                if (newName.length() > 0 || !newName.equals("")) {

                                    name.setText(newName);
                                    String setTitle = title.getText().toString();
                                    String setDept = department.getText().toString();
                                    String setEmail = email.getText().toString();
                                    String setPhone = phone.getText().toString();
                                    ProfileInfoUpdateRequest profileInfoUpdateRequest = new ProfileInfoUpdateRequest(newName, setTitle, setDept, setEmail,
                                            setPhone, username, responseListener2);
                                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                                    queue.add(profileInfoUpdateRequest);

                                } else {
                                    Toast.makeText(getContext(), R.string.please_enter_a_name,
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(cancel, null)
                        .create();
                dialog.show();

            }
        });
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText newTitleEditText = new EditText(getContext());
                AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle(profile_edit)
                        .setMessage(R.string.edit_title_to)
                        .setView(newTitleEditText)
                        .setPositiveButton(submit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newTitle = String.valueOf(newTitleEditText.getText());
                                if (newTitle.length() > 0 || !newTitle.equals("")) {

                                    title.setText(newTitle);
                                    String setName = name.getText().toString();
                                    String setDept = department.getText().toString();
                                    String setEmail = email.getText().toString();
                                    String setPhone = phone.getText().toString();
                                    ProfileInfoUpdateRequest profileInfoUpdateRequest = new ProfileInfoUpdateRequest(setName, newTitle, setDept, setEmail,
                                            setPhone, username, responseListener2);
                                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                                    queue.add(profileInfoUpdateRequest);

                                } else {
                                    Toast.makeText(getContext(), R.string.please_enter_a_title,
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(cancel, null)
                        .create();
                dialog.show();

            }
        });
        department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText newDepartmentEditText = new EditText(getContext());
                AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle(profile_edit)
                        .setMessage(R.string.edit_dept_to)
                        .setView(newDepartmentEditText)
                        .setPositiveButton(submit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newDepartment = String.valueOf(newDepartmentEditText.getText());
                                if (newDepartment.length() > 0 || !newDepartment.equals("")) {

                                    department.setText(newDepartment);
                                    String setName = name.getText().toString();
                                    String setTitle = title.getText().toString();
                                    String setEmail = email.getText().toString();
                                    String setPhone = phone.getText().toString();
                                    ProfileInfoUpdateRequest profileInfoUpdateRequest = new ProfileInfoUpdateRequest(setName, setTitle, newDepartment, setEmail,
                                            setPhone, username, responseListener2);
                                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                                    queue.add(profileInfoUpdateRequest);

                                } else {
                                    Toast.makeText(getContext(), R.string.please_enter_a_dept,
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(cancel, null)
                        .create();
                dialog.show();

            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText newEmailEditText = new EditText(getContext());
                newEmailEditText.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle(profile_edit)
                        .setMessage(R.string.edit_email_to)
                        .setView(newEmailEditText)
                        .setPositiveButton(submit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newEmail = String.valueOf(newEmailEditText.getText());
                                if (newEmail.length() > 0 || !newEmail.equals("")) {

                                    email.setText(newEmail);
                                    String setName = name.getText().toString();
                                    String setTitle = title.getText().toString();
                                    String setDept = department.getText().toString();
                                    String setPhone = phone.getText().toString();
                                    ProfileInfoUpdateRequest profileInfoUpdateRequest = new ProfileInfoUpdateRequest(setName, setTitle, setDept, newEmail,
                                            setPhone, username, responseListener2);
                                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                                    queue.add(profileInfoUpdateRequest);

                                } else {
                                    Toast.makeText(getContext(), R.string.please_enter_an_email,
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(cancel, null)
                        .create();
                dialog.show();

            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText newPhoneEditText = new EditText(getContext());
                newPhoneEditText.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_CLASS_PHONE);
                AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle(profile_edit)
                        .setMessage(R.string.edit_phone_to)
                        .setView(newPhoneEditText)
                        .setPositiveButton(submit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newPhone = String.valueOf(newPhoneEditText.getText());
                                if (newPhone.length() > 0 || !newPhone.equals("")) {

                                    phone.setText(newPhone);
                                    String setName = name.getText().toString();
                                    String setTitle = title.getText().toString();
                                    String setDept = department.getText().toString();
                                    String setEmail = email.getText().toString();
                                    ProfileInfoUpdateRequest profileInfoUpdateRequest = new ProfileInfoUpdateRequest(setName, setTitle, setDept, setEmail,
                                            newPhone, username, responseListener2);
                                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                                    queue.add(profileInfoUpdateRequest);

                                } else {
                                    Toast.makeText(getContext(), R.string.please_enter_a_phone,
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(cancel, null)
                        .create();
                dialog.show();

            }
        });

        final String s1= new String(getString(R.string.listing));
        final String s2= new String(getString(R.string.inventory));
        final String s3= new String(getString(R.string.shipping));
        final String s4= new String(getString(R.string.purchasing));
        final String s5= new String(getString(R.string.returns));
        final String s6= new String(getString(R.string.admin));

        reminders.add(s1);
        reminders.add(s2);
        reminders.add(s3);
        reminders.add(s4);
        reminders.add(s5);
        reminders.add(s6);

        if(listingPermission == 1) {
            listview.setItemChecked(0, true);
        }
        if(inventoryPermission == 1) {
            listview.setItemChecked(1, true);
        }
        if(shippingPermission == 1) {
            listview.setItemChecked(2, true);
        }
        if(purchasingPermission == 1) {
            listview.setItemChecked(3, true);
        }
        if(returnsPermission == 1) {
            listview.setItemChecked(4, true);
        }
        if(adminPermission == 1) {
            listview.setItemChecked(5, true);
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selecteditem = reminders.get(position);
                if(selecteditem.equals(s1)){
                    if(listingPermission == 1) {
                        listview.setItemChecked(0, true);
                    } else {
                        listview.setItemChecked(0, false);
                    }
                    if(listview.isItemChecked(0)) {
                        Toast.makeText(getContext(), R.string.have_listing_permission, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), R.string.dont_have_listing_permission, Toast.LENGTH_SHORT).show();
                    }
                }
                if(selecteditem.equals(s2)){
                    if(inventoryPermission == 1) {
                        listview.setItemChecked(1, true);
                    } else {
                        listview.setItemChecked(1, false);
                    }
                    if(listview.isItemChecked(1)) {
                        Toast.makeText(getContext(), R.string.have_inventory_permission, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), R.string.dont_have_inventory_permission, Toast.LENGTH_SHORT).show();
                    }
                }
                if(selecteditem.equals(s3)){
                    if(shippingPermission == 1) {
                        listview.setItemChecked(2, true);
                    } else {
                        listview.setItemChecked(2, false);
                    }
                    if(listview.isItemChecked(2)) {
                        Toast.makeText(getContext(), R.string.have_shipping_permission, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), R.string.dont_have_shipping_permission, Toast.LENGTH_SHORT).show();
                    }
                }
                if(selecteditem.equals(s4)){
                    if(purchasingPermission == 1) {
                        listview.setItemChecked(3, true);
                    } else {
                        listview.setItemChecked(3, false);
                    }
                    if(listview.isItemChecked(3)) {
                        Toast.makeText(getContext(), R.string.have_purchasing_permission, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), R.string.dont_have_purchasing_permission, Toast.LENGTH_SHORT).show();
                    }
                }
                if(selecteditem.equals(s5)){
                    if(returnsPermission == 1) {
                        listview.setItemChecked(4, true);
                    } else {
                        listview.setItemChecked(4, false);
                    }
                    if(listview.isItemChecked(4)) {
                        Toast.makeText(getContext(), R.string.have_returns_permission, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), R.string.dont_have_returns_permission, Toast.LENGTH_SHORT).show();
                    }
                }
                if(selecteditem.equals(s6)){
                    if(adminPermission == 1) {
                        listview.setItemChecked(5, true);
                    } else {
                        listview.setItemChecked(5, false);
                    }
                    if(listview.isItemChecked(5)) {
                        Toast.makeText(getContext(), R.string.have_admin_permission, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), R.string.dont_have_admin_permission, Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });


        return view;
    }

}
