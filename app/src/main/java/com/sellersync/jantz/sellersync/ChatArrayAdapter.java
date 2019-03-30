package com.sellersync.jantz.sellersync;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jantz on 8/3/2017.
 */

public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {
    private TextView chatText;
    private TextView username;
    private List<ChatMessage> MessageList = new ArrayList<ChatMessage>();
    private LinearLayout layout;


    public ChatArrayAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    public void add(ChatMessage object) {
        MessageList.add(object);
        super.add(object);
    }

    public int getCount() {
        return this.MessageList.size();
    }

    public ChatMessage getItem(int index) {

        return this.MessageList.get(index);
    }

    public View getView(int position, View ConvertView, ViewGroup parent) {
        View v = ConvertView;
        if(v==null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.chat, parent, false);
        }
        layout = (LinearLayout)v.findViewById(R.id.Message1);
        ChatMessage messageobj = getItem(position);
        chatText = (TextView) v.findViewById(R.id.SingleMessage);
        username = (TextView) v.findViewById(R.id.comUser);

        chatText.setText(messageobj.message);
        username.setText(messageobj.username);

        chatText.setBackgroundResource(messageobj.left ? R.drawable.sellersyncbubble2 :R.drawable.sellersyncbubble1);

        layout.setGravity(messageobj.left?Gravity.LEFT: Gravity.RIGHT);
        return v;
    }

    public Bitmap decodeToBitmap(byte[] decodeByte) {
        return BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);
    }
}
