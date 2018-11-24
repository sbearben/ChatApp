package uk.co.victoriajanedavis.chatapp.data.model.websocket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorWsModel {

    @SerializedName("command") @Expose private @Commands.Type String command;
    @SerializedName("error_code") @Expose private @Errors.Code int error_code;
    @SerializedName("error_message") @Expose private @Errors.Message String error_message;


    public @Commands.Type
    String getCommand() {
        return Commands.ERROR;
    }

    public @Errors.Code int getErrorCode() {
        return error_code;
    }

    public void setErrorCode(int error_code) {
        this.error_code = error_code;
    }

    public @Errors.Message String getErrorMessage() {
        return error_message;
    }

    public void setErrorMessage(String error_message) {
        this.error_message = error_message;
    }
}
