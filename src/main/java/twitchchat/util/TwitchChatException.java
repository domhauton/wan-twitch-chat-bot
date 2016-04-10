package twitchchat.util;

import java.io.IOException;

/**
 * Created by Dominic Hauton on 10/04/2016.
 *
 * General Exception while sending/receiving messages from TwitchChat
 */
public class TwitchChatException extends IOException{
    public TwitchChatException(String message) {
        super(message);
    }
}
