package com.sellersync.jantz.sellersync;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class CommunicationFragment extends Fragment {

    private ChatArrayAdapter adapter;
    private ListView list;
    private EditText chatText;
    private ImageButton send;
    Intent intent;
    private boolean side = false;
    SharedPreferences pref;

    public CommunicationFragment() {
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
        return inflater.inflate(R.layout.fragment_communication, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent i = getActivity().getIntent();
        send = (ImageButton) getActivity().findViewById(R.id.btn);
        list = (ListView) getActivity().findViewById(R.id.listview);
        adapter = new ChatArrayAdapter(getContext(),R.layout.chat);
        chatText = (EditText) getActivity().findViewById(R.id.chat);
        pref = getActivity().getSharedPreferences("login.conf", Context.MODE_PRIVATE);

        chatText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction()==KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }

        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendChatMessage();
                InputMethodManager imm2 = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setAdapter(adapter);

        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

                list.setSelection(adapter.getCount() -1);
            }
        });
    }

    private boolean sendChatMessage () {

        adapter.add(new ChatMessage(side, pref.getString("username",""), chatText.getText().toString()));
        chatText.setText("");

        side =!side;

        return true;
    }
}
