package uk.co.victoriajanedavis.chatapp.data.model.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.Date
import java.util.UUID

data class MessageNwModel(
    @SerializedName("uuid") @Expose val uuid: UUID,
    @SerializedName("text") @Expose val text: String = "",
    @SerializedName("created") @Expose val created: Date,
    @SerializedName("chat_uuid") @Expose val chatUuid: UUID,
    @SerializedName("user_uuid") @Expose val userUuid: UUID,
    @SerializedName("user_username") @Expose val userUsername: String = "",
    @SerializedName("from_current_user") @Expose val isFromCurrentUser: Boolean = false
)
