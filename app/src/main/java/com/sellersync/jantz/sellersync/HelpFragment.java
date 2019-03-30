package com.sellersync.jantz.sellersync;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {
    View rootView;
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;




    public HelpFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_help, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ExpandableListView) view.findViewById(R.id.helplistview);
        initData();
        listAdapter = new ExpandableListAdapter(getContext(),listDataHeader,listHash);
        listView.setAdapter(listAdapter);

        listView.setChoiceMode(ExpandableListView.CHOICE_MODE_SINGLE);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition,long id) {
                if(groupPosition == 0) {
                    if(childPosition == 0) {
                        Toast.makeText(getContext(), R.string.how_do_I_add_product, Toast.LENGTH_SHORT).show();
                    }
                    if(childPosition == 1) {
                        Toast.makeText(getContext(), R.string.how_do_I_create_listing, Toast.LENGTH_SHORT).show();
                    }
                    if(childPosition == 2) {
                        Toast.makeText(getContext(), R.string.how_do_I_use_search_bar, Toast.LENGTH_SHORT).show();
                    }
                }
                if(groupPosition == 1) {
                    if(childPosition == 0) {
                        Toast.makeText(getContext(), R.string.how_to_set_up_selling_channel, Toast.LENGTH_SHORT).show();
                    }
                    if(childPosition == 1) {
                        Toast.makeText(getContext(), R.string.how_to_create_new_listing, Toast.LENGTH_SHORT).show();
                    }
                    if(childPosition == 2) {
                        Toast.makeText(getContext(), R.string.how_to_remove_listing, Toast.LENGTH_SHORT).show();
                    }
                    if(childPosition == 3) {
                        Toast.makeText(getContext(), R.string.how_to_edit_listing, Toast.LENGTH_SHORT).show();
                    }
                }
                if(groupPosition == 2) {
                        if (childPosition == 0) {
                            Toast.makeText(getContext(), R.string.how_to_add_new_inventory, Toast.LENGTH_SHORT).show();
                        }
                        if (childPosition == 1) {
                            Toast.makeText(getContext(), R.string.how_to_search_inventory, Toast.LENGTH_SHORT).show();
                        }
                        if (childPosition == 2) {
                            Toast.makeText(getContext(), R.string.how_to_edit_inventory, Toast.LENGTH_SHORT).show();
                        }
                }
                if(groupPosition == 3) {
                    if(childPosition == 0) {
                        Toast.makeText(getContext(), R.string.how_to_ship_product, Toast.LENGTH_SHORT).show();
                    }
                    if(childPosition == 1) {
                        Toast.makeText(getContext(), R.string.how_to_get_tracking, Toast.LENGTH_SHORT).show();
                    }
                    if(childPosition == 2) {
                        Toast.makeText(getContext(), R.string.how_to_compare_ship_costs, Toast.LENGTH_SHORT).show();
                    }
                }
                if(groupPosition == 4) {
                    if(childPosition == 0) {
                        Toast.makeText(getContext(), R.string.how_to_setup_payment, Toast.LENGTH_SHORT).show();
                    }
                    if(childPosition == 1) {
                        Toast.makeText(getContext(), R.string.how_to_initiate_a_purchase, Toast.LENGTH_SHORT).show();
                    }
                    if(childPosition == 2) {
                        Toast.makeText(getContext(), R.string.how_to_schedule_purchase, Toast.LENGTH_SHORT).show();
                    }
                }
                if(groupPosition == 5) {
                    if(childPosition == 0) {
                        Toast.makeText(getContext(), R.string.how_to_create_a_reminder, Toast.LENGTH_SHORT).show();
                    }
                    if(childPosition == 1) {
                        Toast.makeText(getContext(), R.string.how_to_edit_reminder, Toast.LENGTH_SHORT).show();
                    }
                    if(childPosition == 2) {
                        Toast.makeText(getContext(), R.string.how_to_receive_reminders, Toast.LENGTH_SHORT).show();
                    }
                }
                if(groupPosition == 6) {
                    if (childPosition == 0) {
                        Toast.makeText(getContext(), R.string.how_to_setup_contact_info, Toast.LENGTH_SHORT).show();
                    }
                    if (childPosition == 1) {
                        Toast.makeText(getContext(), R.string.how_to_add_contact, Toast.LENGTH_SHORT).show();
                    }
                    if (childPosition == 2) {
                        Toast.makeText(getContext(), R.string.how_to_sent_receive_communication, Toast.LENGTH_SHORT).show();
                    }
                }
                if(groupPosition == 7) {
                    if(childPosition == 0) {
                        Toast.makeText(getContext(), R.string.how_to_add_new_users, Toast.LENGTH_SHORT).show();
                    }
                    if(childPosition == 1) {
                        Toast.makeText(getContext(), R.string.how_to_edit_profiles, Toast.LENGTH_SHORT).show();
                    }
                    if(childPosition == 2) {
                        Toast.makeText(getContext(), R.string.how_to_reset_user_cred, Toast.LENGTH_SHORT).show();
                    }
                    if(childPosition == 3) {
                        Toast.makeText(getContext(), R.string.other_admin, Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });


    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();
        SharedPreferences pref = getActivity().getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        final int adminPermission = pref.getInt("adminPermission", 0);

        listDataHeader.add(getString(R.string.faqs));
        listDataHeader.add(getString(R.string.listings));
        listDataHeader.add(getString(R.string.inventory));
        listDataHeader.add(getString(R.string.shipping));
        listDataHeader.add(getString(R.string.purchases));
        listDataHeader.add(getString(R.string.reminders));
        listDataHeader.add(getString(R.string.Communication));
        if(adminPermission == 1) {
            listDataHeader.add(getString(R.string.admin_tasks));
        }
        List <String> faqs = new ArrayList<>();
        faqs.add(getString(R.string.how_do_I_add_product));
        faqs.add(getString(R.string.how_do_I_create_listing));
        faqs.add(getString(R.string.how_do_I_use_search_bar));

        List <String> listings = new ArrayList<>();
        listings.add(getString(R.string.how_to_set_up_selling_channel));
        listings.add(getString(R.string.how_to_create_a_reminder));
        listings.add(getString(R.string.how_to_remove_listing));
        listings.add(getString(R.string.how_to_edit_listing));

        List <String> inventory = new ArrayList<>();
        inventory.add(getString(R.string.how_to_add_new_inventory));
        inventory.add(getString(R.string.how_to_search_inventory));
        inventory.add(getString(R.string.how_to_edit_inventory));

        List <String> shipping = new ArrayList<>();
        shipping.add(getString(R.string.how_to_ship_product));
        shipping.add(getString(R.string.how_to_get_tracking));
        shipping.add(getString(R.string.how_to_compare_ship_costs));

        List <String> purchases = new ArrayList<>();
        purchases.add(getString(R.string.how_to_setup_payment));
        purchases.add(getString(R.string.how_to_initiate_a_purchase));
        purchases.add(getString(R.string.how_to_schedule_purchase));

        List <String> reminders = new ArrayList<>();
        reminders.add(getString(R.string.how_to_create_a_reminder));
        reminders.add(getString(R.string.how_to_edit_reminder));
        reminders.add(getString(R.string.how_to_receive_reminders));

        List <String> communication = new ArrayList<>();
        communication.add(getString(R.string.how_to_setup_contact_info));
        communication.add(getString(R.string.how_to_add_contact));
        communication.add(getString(R.string.how_to_sent_receive_communication));

        List <String> adminTasks = new ArrayList<>();
        adminTasks.add(getString(R.string.how_to_add_new_users));
        adminTasks.add(getString(R.string.how_to_edit_profiles));
        adminTasks.add(getString(R.string.how_to_reset_user_cred));
        adminTasks.add(getString(R.string.other_admin));

        listHash.put(listDataHeader.get(0),faqs);
        listHash.put(listDataHeader.get(1),listings);
        listHash.put(listDataHeader.get(2),inventory);
        listHash.put(listDataHeader.get(3),shipping);
        listHash.put(listDataHeader.get(4),purchases);
        listHash.put(listDataHeader.get(5),reminders);
        listHash.put(listDataHeader.get(6),communication);
        if(adminPermission == 1) {
            listHash.put(listDataHeader.get(7), adminTasks);
        }
    }

}
