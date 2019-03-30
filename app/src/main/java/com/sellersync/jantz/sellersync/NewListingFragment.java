package com.sellersync.jantz.sellersync;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.sellersync.jantz.sellersync.MainActivity.tvPageTitle;
import static com.sellersync.jantz.sellersync.R.string.ok;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewListingFragment extends Fragment {
    ListView channels;
    EditText addEntry;
    Button Ebay,Etsy,Magento,Rakuten,Bigcommerce,Shopify,Amazon;
    ArrayList<String> Arraychannels = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    DbHelper dbHelper;
    FragmentManager fm;

    public NewListingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_listing, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvPageTitle.setText(R.string.where_do_you_want_to_list);

        channels = (ListView) view.findViewById(R.id.lvChannels);
        Ebay = (Button) view.findViewById(R.id.btnEbay);
        Amazon = (Button) view.findViewById(R.id.btnAmazon);
        Etsy = (Button) view.findViewById(R.id.btnEtsy);
        Bigcommerce = (Button) view.findViewById(R.id.btnBigcommerce);
        Magento = (Button) view.findViewById(R.id.btnMagento);
        Rakuten = (Button) view.findViewById(R.id.btnRakuten);
        Shopify = (Button) view.findViewById(R.id.btnShopify);
        fm = getActivity().getSupportFragmentManager();
        dbHelper = new DbHelper(getContext());

        // ADAPTER
        loadChannelList();




        // SET SELECTED ITEM
        channels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {

                //addEntry.setText(Arraychannels.get(pos));

            }
        });
        channels.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.app_name);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage("Are you sure you want to delete this channel?")
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                delete((Arraychannels.get(position)));
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
        });

        // SET CHANNEL SETUP BUTTONS
        Ebay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.containerView, new EbaySetupFragment(),"EBAYSETUP").commit();
                tvPageTitle.setText(R.string.ebay_seller_setup);
            }
        });
        Amazon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.containerView, new AmazonSetupFragment(),"AMAZONSETUP").commit();
                tvPageTitle.setText(R.string.amazon_seller_setup);
            }
        });
        Bigcommerce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.containerView, new BigcommerceSetupFragment(),"BIGCOMMERCESETUP").commit();
                tvPageTitle.setText(R.string.bigcommerce_seller_setup);
            }
        });
        Etsy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.containerView, new EtsySetupFragment(),"ETSYSETUP").commit();
                tvPageTitle.setText(R.string.etsy_seller_setup);
            }
        });
        Magento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.containerView, new MagentoSetupFragment(),"MAGENTOSETUP").commit();
                tvPageTitle.setText(R.string.magento_seller_setup);
            }
        });
        Rakuten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.containerView, new RakutenSetupFragment(),"RAKUTENSETUP").commit();
                tvPageTitle.setText(R.string.rakuten_seller_setup);
            }
        });
        Shopify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.containerView, new ShopifySetupFragment(),"SHOPIFYSETUP").commit();
                tvPageTitle.setText(R.string.shopify_seller_setup);
            }
        });


        // LOAD LISTVIEW

        //HANDLE EVENTS
        /*
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
*/
    }

    private void loadChannelList() {

        if(adapter==null) {
            adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_single_choice, Arraychannels);
            channels.setAdapter(adapter);
            adapter.addAll(dbHelper.getChannelList());
        }
        else {

            adapter.notifyDataSetChanged();
        }
    }


    //UPDATE
    private void update () {
        String channel = addEntry.getText().toString();

        // GET POS OF SELECTED ITEM
        int pos = channels.getCheckedItemPosition();

        if (pos > -1) {

            if (!channel.isEmpty() && channel.length() > 0) {

                //REMOVE
                adapter.remove(Arraychannels.get(pos));

                //INSERT
                adapter.insert(channel, pos);

                //REFRESH

                adapter.notifyDataSetChanged();

                Toast.makeText(getContext(),  getString(R.string.your)+ channel +getString(R.string.has_been_updated) , Toast.LENGTH_SHORT).show();

            } else {

            }
        } else {

        }
    }

    //DELETE
    private void delete(String position) {
        //int pos = channels.getCheckedItemPosition();
        //String channel = addEntry.getText().toString();
        //if(pos > -1) {
            //REMOVE
            adapter.remove(position);
            dbHelper.deleteChannel(position);

            //REFRESH
            adapter.notifyDataSetChanged();

            //addEntry.setText("");
            Toast.makeText(getContext(),  position+" "+getString(R.string.has_been_deleted_from_channels), Toast.LENGTH_SHORT).show();

        //} else {
            //Toast.makeText(getContext(), "no deletion", Toast.LENGTH_SHORT).show();

        //}
    }
}