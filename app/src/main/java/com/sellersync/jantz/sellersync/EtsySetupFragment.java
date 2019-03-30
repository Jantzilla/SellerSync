package com.sellersync.jantz.sellersync;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.sellersync.jantz.sellersync.R.string.ok;


/**
 * A simple {@link Fragment} subclass.
 */
public class EtsySetupFragment extends Fragment {
    DbHelper dbHelper;
    EditText channelName;
    Button setup;
    String channelPrefix = "[ETSY]";


    public EtsySetupFragment() {
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
        return inflater.inflate(R.layout.fragment_etsy_setup, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        channelName = (EditText) view.findViewById(R.id.etListItem);
        setup = (Button) view.findViewById(R.id.btnSetup);
        dbHelper = new DbHelper(getContext());

        //HANDLE EVENTS
        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setup();
            }
        });
    }

    // ADD
    private void setup () {
        String channel = channelPrefix+" "+channelName.getText().toString();

        if(!channel.isEmpty() && channel.length() > 0) {

            if(!dbHelper.getChannelList().contains(channel)) {

                //ADD TO DATABASE
                dbHelper.insertNewChannel(channel);

                channelName.setText("");

                Toast.makeText(getContext(), channel+" "+getString(R.string.added_to_selling_channels), Toast.LENGTH_SHORT).show();
            } else {
                channelName.setText("");
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(channel + getString(R.string.please_choose_another_name))
                        .setNegativeButton(ok, null)
                        .create()
                        .show();
            }

        } else {

        }
    }



}
