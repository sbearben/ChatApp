package uk.co.victoriajanedavis.chatapp.data.model.websocket;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class Commands {

    public static final String SEND_MESSAGE = "send_message";
    public static final String ERROR = "error";

    @StringDef({SEND_MESSAGE, ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {}
}
