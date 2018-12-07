package uk.co.victoriajanedavis.chatapp.data.model.websocket;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class Errors {

    public static final int CLIENT_ERROR_CODE = 0;
    public static final String CLIENT_ERROR_MESSAGE = "Client error.";

    public static final int NOT_FRIENDS_ERROR_CODE = 1;
    public static final String NOT_FRIENDS_ERROR_MESSAGE = "Users are not friends.";

    public static final int NOT_LOGGED_IN_ERROR_CODE = 2;
    public static final String NOT_LOGGED_IN_ERROR_MESSAGE = "You are not logged in.";

    @IntDef({CLIENT_ERROR_CODE, NOT_FRIENDS_ERROR_CODE, NOT_LOGGED_IN_ERROR_CODE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Code {}

    @StringDef({CLIENT_ERROR_MESSAGE, NOT_FRIENDS_ERROR_MESSAGE, NOT_LOGGED_IN_ERROR_MESSAGE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Message {}

}
