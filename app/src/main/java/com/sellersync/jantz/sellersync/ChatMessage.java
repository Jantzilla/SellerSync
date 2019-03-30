package com.sellersync.jantz.sellersync;

/**
 * Created by jantz on 8/4/2017.
 */

public class ChatMessage {
    boolean left;
    String username;
    String message;

    public ChatMessage(boolean left, String username, String message) {

        super();
        this.left = left;
        this.username = username;
        this.message = message;
    }
}
