package com.sellersync.jantz.sellersync;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    View rootView;
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor settingsEditor;




    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        return rootView;
    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();
        SharedPreferences pref = getActivity().getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        SharedPreferences preference = getActivity().getSharedPreferences("settings.conf", Context.MODE_PRIVATE);
        settingsEditor = preference.edit();
        editor = pref.edit();
        final int adminPermission = pref.getInt("adminPermission", 0);

        listDataHeader.add(getString(R.string.notifications));
        listDataHeader.add(getString(R.string.lang));
        listDataHeader.add(getString(R.string.account));
        if(adminPermission == 1) {
            listDataHeader.add(getString(R.string.username_password));
        }
        List <String> notifications = new ArrayList<>();
        notifications.add(getString(R.string.sales_notifications));
        notifications.add(getString(R.string.mess_notifications));
        notifications.add(getString(R.string.rem_notifications));
        notifications.add(getString(R.string.all_activity_notifications));

        List<String> userpass = new ArrayList<>();
        userpass.add(getString(R.string.reset_username));
        userpass.add(getString(R.string.reset_password));

        List <String> language = new ArrayList<>();
        language.add(getString(R.string.English));
        language.add(getString(R.string.Spanish));
        language.add(getString(R.string.French));
        language.add(getString(R.string.Portuguese));

        List <String> account = new ArrayList<>();
        if(adminPermission == 1) {
            account.add(getString(R.string.set_permissions));
            account.add(getString(R.string.create_new_account));
        }
        account.add(getString(R.string.close_account));

        listHash.put(listDataHeader.get(0),notifications);
        listHash.put(listDataHeader.get(1),language);
        listHash.put(listDataHeader.get(2),account);
        if(adminPermission == 1) {
            listHash.put(listDataHeader.get(3), userpass);
        }
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SharedPreferences pref = getActivity().getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        final SharedPreferences preference = getActivity().getSharedPreferences("settings.conf", Context.MODE_PRIVATE);
        final int adminPermission = pref.getInt("adminPermission", 0);
        final FragmentManager fm = getActivity().getSupportFragmentManager();


        listView = (ExpandableListView) view.findViewById(R.id.settingslistview);
        initData();
        listAdapter = new ExpandableListAdapter(getContext(),listDataHeader,listHash);
        listView.setAdapter(listAdapter);

        listView.setChoiceMode(ExpandableListView.CHOICE_MODE_MULTIPLE);

        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForGroup(0));
                parent.isGroupExpanded(index);
                if(groupPosition == 0) {
                    parent.collapseGroup(1);
                    listView.clearChoices();
                }


                return false;
            }
        });

        listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                if(groupPosition == 1) {
                    listView.collapseGroup(0);
                    listView.clearChoices();
                }
            }
        });

        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                final int salesNotPref = preference.getInt("salesNotPref",1);
                final int messNotPref = preference.getInt("messNotPref", 1);
                final int remNotPref = preference.getInt("remNotPref", 1);
                final int allNotPref = preference.getInt("allNotPref", 1);
                final String langPref = preference.getString("langPref", "en");
                if(groupPosition == 0) {
                    if(salesNotPref == 1) {
                        listView.setItemChecked(1,true);
                    }
                    if(messNotPref == 1) {
                        listView.setItemChecked(2,true);
                    }
                    if(remNotPref == 1) {
                        listView.setItemChecked(3,true);
                    }
                    if(allNotPref == 1) {
                        listView.setItemChecked(4,true);
                    }
                }
                if(groupPosition == 1) {
                    listView.collapseGroup(0);
                    listView.clearChoices();
                    if(langPref.equals("en")) {
                        listView.setItemChecked(2,true);
                    }
                    if(langPref.equals("es")) {
                        listView.setItemChecked(3,true);
                    }
                    if(langPref.equals("fr")) {
                        listView.setItemChecked(4,true);
                    }
                    if(langPref.equals("pt")) {
                        listView.setItemChecked(5,true);
                    }
                }
            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition,long id) {
                if(groupPosition == 0) {
                    int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,childPosition));
                    int index1 = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,0));
                    int index2 = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,1));
                    int index3 = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,2));
                    int index4 = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,3));
                    if(childPosition == 0) {
                        if(parent.isItemChecked(1)) {
                            parent.setItemChecked(index,false);
                            settingsEditor.putInt("salesNotPref", 0);
                            settingsEditor.apply();
                            Toast.makeText(getContext(), R.string.sales_notifications_disabled, Toast.LENGTH_SHORT).show();
                            if(!parent.isItemChecked(2) && !parent.isItemChecked(3)) {
                                Toast.makeText(getContext(),getString(R.string.all_notifications_disabled), Toast.LENGTH_SHORT).show();
                            }
                            if(parent.isItemChecked(2) && parent.isItemChecked(3)){
                                parent.setItemChecked(index4,false);
                                settingsEditor.putInt("allNotPref", 0);
                                settingsEditor.apply();

                            }
                        } else {
                            parent.setItemChecked(index,true);
                            settingsEditor.putInt("salesNotPref", 1);
                            settingsEditor.apply();
                            Toast.makeText(getContext(), R.string.sales_notifications_enabled, Toast.LENGTH_SHORT).show();
                            if(parent.isItemChecked(2) && parent.isItemChecked(3)){
                                parent.setItemChecked(index4,true);
                                settingsEditor.putInt("allNotPref", 1);
                                settingsEditor.apply();
                                Toast.makeText(getContext(),getString(R.string.all_notifications_enabled), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                    if(childPosition == 1) {
                        if(parent.isItemChecked(2)) {
                            parent.setItemChecked(index,false);
                            settingsEditor.putInt("messNotPref", 0);
                            settingsEditor.apply();
                            Toast.makeText(getContext(), R.string.mess_notifications_disabled, Toast.LENGTH_SHORT).show();
                            if(!parent.isItemChecked(1) && !parent.isItemChecked(3)) {
                                Toast.makeText(getContext(),getString(R.string.all_notifications_disabled), Toast.LENGTH_SHORT).show();
                            }
                            if(parent.isItemChecked(1) && parent.isItemChecked(3)){
                                parent.setItemChecked(index4,false);
                                settingsEditor.putInt("allNotPref", 0);
                                settingsEditor.apply();
                            }
                        } else {
                            parent.setItemChecked(index,true);
                            settingsEditor.putInt("messNotPref", 1);
                            settingsEditor.apply();
                            Toast.makeText(getContext(), R.string.mess_notifications_enabled, Toast.LENGTH_SHORT).show();
                            if(parent.isItemChecked(1) && parent.isItemChecked(3)){
                                parent.setItemChecked(index4,true);
                                settingsEditor.putInt("allNotPref", 1);
                                settingsEditor.apply();
                                Toast.makeText(getContext(),getString(R.string.all_notifications_enabled), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    if(childPosition == 2) {
                        if(parent.isItemChecked(3)) {
                            parent.setItemChecked(index,false);
                            settingsEditor.putInt("remNotPref", 0);
                            settingsEditor.apply();
                            Toast.makeText(getContext(), R.string.rem_notifications_disabled, Toast.LENGTH_SHORT).show();
                            if(!parent.isItemChecked(2) && !parent.isItemChecked(1)) {
                                Toast.makeText(getContext(),getString(R.string.all_notifications_disabled), Toast.LENGTH_SHORT).show();
                            }
                            if(parent.isItemChecked(1) && parent.isItemChecked(2)){
                                parent.setItemChecked(index4,false);
                                settingsEditor.putInt("allNotPref", 0);
                                settingsEditor.apply();
                            }
                        } else {
                            parent.setItemChecked(index,true);
                            settingsEditor.putInt("remNotPref", 1);
                            settingsEditor.apply();
                            Toast.makeText(getContext(), R.string.rem_notifications_enabled, Toast.LENGTH_SHORT).show();
                            if(parent.isItemChecked(1) && parent.isItemChecked(2)){
                                parent.setItemChecked(index4,true);
                                settingsEditor.putInt("allNotPref", 1);
                                settingsEditor.apply();
                                Toast.makeText(getContext(),getString(R.string.all_notifications_enabled), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    if(childPosition == 3) {
                        if(parent.isItemChecked(4)){
                            parent.setItemChecked(index,false);
                            settingsEditor.putInt("allNotPref", 0);
                            settingsEditor.apply();
                            if(parent.isItemChecked(1) && parent.isItemChecked(2) && parent.isItemChecked(3)) {
                                Toast.makeText(getContext(), R.string.all_notifications_disabled, Toast.LENGTH_SHORT).show();
                                parent.setItemChecked(index1, false);
                                settingsEditor.putInt("salesNotPref", 0);
                                settingsEditor.apply();
                                parent.setItemChecked(index2, false);
                                settingsEditor.putInt("messNotPref", 0);
                                settingsEditor.apply();
                                parent.setItemChecked(index3, false);
                                settingsEditor.putInt("remNotPref", 0);
                                settingsEditor.apply();
                            }
                        } else {
                            parent.setItemChecked(index, true);
                            settingsEditor.putInt("allNotPref", 1);
                            settingsEditor.apply();
                            Toast.makeText(getContext(), R.string.all_notifications_enabled, Toast.LENGTH_SHORT).show();
                            parent.setItemChecked(index1, true);
                            settingsEditor.putInt("salesNotPref", 1);
                            settingsEditor.apply();
                            parent.setItemChecked(index2, true);
                            settingsEditor.putInt("messNotPref", 1);
                            settingsEditor.apply();
                            parent.setItemChecked(index3, true);
                            settingsEditor.putInt("remNotPref", 1);
                            settingsEditor.apply();
                        }
                    }
                }
                if(groupPosition == 1) {
                    if(childPosition == 0) {
                        int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,childPosition));
                        int index1 = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,1));
                        int index2 = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,2));
                        int index3 = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,3));
                        parent.setItemChecked(index1,false);
                        parent.setItemChecked(index2,false);
                        parent.setItemChecked(index3,false);
                        parent.setItemChecked(index,true);
                        settingsEditor.putString("langPref", "en");
                        settingsEditor.apply();
                        ((App)getActivity().getApplicationContext()).changeLang(preference.getString("langPref", "en"));
                        Intent refresh = new Intent(getContext(), MainActivity.class);
                        startActivity(refresh);
                        getActivity().finish();
                        Toast.makeText(getContext(), R.string.English, Toast.LENGTH_SHORT).show();
                    }
                    if(childPosition == 1) {
                        int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,childPosition));
                        int index1 = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,0));
                        int index2 = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,2));
                        int index3 = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,3));
                        parent.setItemChecked(index1,false);
                        parent.setItemChecked(index2,false);
                        parent.setItemChecked(index3,false);
                        parent.setItemChecked(index,true);
                        settingsEditor.putString("langPref", "es");
                        settingsEditor.apply();
                        ((App)getActivity().getApplicationContext()).changeLang(preference.getString("langPref", "en"));
                        Intent refresh = new Intent(getContext(), MainActivity.class);
                        startActivity(refresh);
                        getActivity().finish();
                        Toast.makeText(getContext(), R.string.Spanish, Toast.LENGTH_SHORT).show();
                    }
                    if(childPosition == 2) {
                        int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,childPosition));
                        int index1 = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,0));
                        int index2 = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,1));
                        int index3 = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,3));
                        parent.setItemChecked(index1,false);
                        parent.setItemChecked(index2,false);
                        parent.setItemChecked(index3,false);
                        parent.setItemChecked(index,true);
                        settingsEditor.putString("langPref", "fr");
                        settingsEditor.apply();
                        ((App)getActivity().getApplicationContext()).changeLang(preference.getString("langPref", "en"));
                        Intent refresh = new Intent(getContext(), MainActivity.class);
                        startActivity(refresh);
                        getActivity().finish();
                        Toast.makeText(getContext(), R.string.French, Toast.LENGTH_SHORT).show();
                    }
                    if(childPosition == 3) {
                        int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,childPosition));
                        int index1 = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,1));
                        int index2 = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,2));
                        int index3 = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,0));
                        parent.setItemChecked(index1,false);
                        parent.setItemChecked(index2,false);
                        parent.setItemChecked(index3,false);
                        parent.setItemChecked(index,true);
                        settingsEditor.putString("langPref", "pt");
                        settingsEditor.apply();
                        ((App)getActivity().getApplicationContext()).changeLang(preference.getString("langPref", "en"));
                        Intent refresh = new Intent(getContext(), MainActivity.class);
                        startActivity(refresh);
                        getActivity().finish();
                        Toast.makeText(getContext(), R.string.Portuguese, Toast.LENGTH_SHORT).show();
                    }
                }
                if(groupPosition == 2) {
                    if(adminPermission == 1) {
                        if (childPosition == 0) {
                            Toast.makeText(getContext(), "Set Permissions", Toast.LENGTH_SHORT).show();
                            fm.beginTransaction().replace(R.id.containerView, new SetPermissionsFragment(),"SETPERMISSIONS").commit();
                            MainActivity.tvPageTitle.setText(R.string.set_permissions);
                        }
                        if (childPosition == 1) {
                            Toast.makeText(getContext(), "create new account", Toast.LENGTH_SHORT).show();
                            fm.beginTransaction().replace(R.id.containerView, new CreateNewAccountFragment(),"CREATENEWACCOUNT").commit();
                            MainActivity.tvPageTitle.setText(R.string.create_new_account);
                        }
                        if (childPosition == 2) {
                            Toast.makeText(getContext(), "close account", Toast.LENGTH_SHORT).show();
                            fm.beginTransaction().replace(R.id.containerView, new CloseAccountFragment(),"CLOSEACCOUNT").commit();
                            MainActivity.tvPageTitle.setText(R.string.close_account);
                        }
                    } else {
                        if (childPosition == 0) {
                            Toast.makeText(getContext(), "close account", Toast.LENGTH_SHORT).show();
                            fm.beginTransaction().replace(R.id.containerView, new CloseAccountFragment(),"CLOSEACCOUNT").commit();
                            MainActivity.tvPageTitle.setText(R.string.close_account);
                        }
                    }
                }
                if(groupPosition == 3) {
                    if(childPosition == 0) {
                        Toast.makeText(getContext(),"reset username", Toast.LENGTH_SHORT).show();
                        fm.beginTransaction().replace(R.id.containerView, new ResetUsernameFragment(),"RESETUSERNAME").commit();
                        MainActivity.tvPageTitle.setText(R.string.reset_username);
                    }
                    if(childPosition == 1) {
                        Toast.makeText(getContext(),"reset password", Toast.LENGTH_SHORT).show();
                        fm.beginTransaction().replace(R.id.containerView, new ResetPasswordFragment(),"RESETPASSWORD").commit();
                        MainActivity.tvPageTitle.setText(R.string.reset_password);
                    }
                }

                return false;
            }
        });


    }


}
