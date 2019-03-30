package com.sellersync.jantz.sellersync;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;

import java.util.ArrayList;

import static com.sellersync.jantz.sellersync.R.string.add_product;
import static com.sellersync.jantz.sellersync.R.string.new_listing;
import static com.sellersync.jantz.sellersync.R.string.purchases;
import static com.sellersync.jantz.sellersync.R.string.returns;
import static com.sellersync.jantz.sellersync.R.string.sales;
import static com.sellersync.jantz.sellersync.R.string.shipping;


/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryListFragment extends Fragment {


    public SummaryListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences pref = getActivity().getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        final int listingPermission = pref.getInt("listingPermission",0);
        final int inventoryPermission = pref.getInt("inventoryPermission", 0);
        final int shippingPermission = pref.getInt("shippingPermission", 0);
        final int purchasingPermission = pref.getInt("purchasingPermission", 0);
        final int returnsPermission = pref.getInt("returnsPermission", 0);
        final int adminPermission = pref.getInt("adminPermission", 0);

        // Inflate the layout for this fragment

        final FragmentManager fm = getActivity().getSupportFragmentManager();

        View view =inflater.inflate(R.layout.fragment_summary,container,false);
        final ArrayList<Summarylist> summary= new ArrayList<>();

        final Summarylist s1= new Summarylist(getString(new_listing));
        final Summarylist s2= new Summarylist(getString(add_product));
        final Summarylist s3= new Summarylist(getString(sales));
        final Summarylist s4= new Summarylist(getString(shipping));
        final Summarylist s5= new Summarylist(getString(purchases));
        final Summarylist s6= new Summarylist(getString(returns));

        summary.add(s1);
        summary.add(s2);
        summary.add(s3);
        summary.add(s4);
        summary.add(s5);
        summary.add(s6);

        BindDictionary<Summarylist> dictionary= new BindDictionary<>();
        dictionary.addStringField(R.id.tvsummary, new StringExtractor<Summarylist>() {
            @Override
            public String getStringValue(Summarylist summary1, int position) {
                return summary1.getName();
            }
        });

        FunDapter adapter= new FunDapter(SummaryListFragment.this.getActivity(),summary,R.layout.summary_layout,dictionary);
        ListView listview= (ListView)view.findViewById(R.id.summarylistview);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Summarylist selecteditem = summary.get(position);
                if (selecteditem == s1) {
                    if(listingPermission == 1) {
                        fm.beginTransaction().replace(R.id.containerView, new NewListingFragment()).commit();
                        MainActivity.tvPageTitle.setText("");
                    } else {
                        Toast.makeText(getContext(),"You are not authorized to access new listings", Toast.LENGTH_LONG).show();
                    }
                }
                else if (selecteditem == s2) {
                    if(inventoryPermission == 1) {
                        fm.beginTransaction().replace(R.id.containerView, new AddProductFragment()).commit();
                        MainActivity.tvPageTitle.setText("");
                    } else {
                        Toast.makeText(getContext(),"You are not authorized to add product", Toast.LENGTH_LONG).show();
                    }
                }
                else if (selecteditem == s3) {
                    if(adminPermission == 1) {
                        fm.beginTransaction().replace(R.id.containerView, new SalesFragment()).commit();
                        MainActivity.tvPageTitle.setText("");
                    } else {
                        Toast.makeText(getContext(),"You are not authorized to view sales", Toast.LENGTH_LONG).show();
                    }
                }
                else if (selecteditem == s4) {
                    if(shippingPermission == 1) {
                        fm.beginTransaction().replace(R.id.containerView, new ShippingFragment()).commit();
                        MainActivity.tvPageTitle.setText("");
                    } else {
                        Toast.makeText(getContext(),"You are not authorized to access shipping", Toast.LENGTH_LONG).show();
                    }
                }
                else if (selecteditem == s5) {
                    if(purchasingPermission == 1) {
                        fm.beginTransaction().replace(R.id.containerView, new AddProductFragment()).commit();
                        MainActivity.tvPageTitle.setText("");
                    } else {
                        Toast.makeText(getContext(),"You are not authorized to make purchases", Toast.LENGTH_LONG).show();
                    }
                }
                else if (selecteditem == s6) {
                    if(returnsPermission == 1) {
                        fm.beginTransaction().replace(R.id.containerView, new AddProductFragment()).commit();
                        MainActivity.tvPageTitle.setText("");
                    } else {
                        Toast.makeText(getContext(),"You are not authorized to access returns", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return view;
    }

}
