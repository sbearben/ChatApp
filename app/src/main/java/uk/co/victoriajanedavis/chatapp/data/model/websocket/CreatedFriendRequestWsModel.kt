package uk.co.victoriajanedavis.chatapp.data.model.websocket

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.Date
import java.util.UUID

data class CreatedFriendRequestWsModel (
    @SerializedName("type") @Expose val type: String = "created_friend_request",
    @SerializedName("sender_uuid") @Expose var senderUuid: UUID,
    @SerializedName("sender_email") @Expose var senderEmail: String,
    @SerializedName("sender_username") @Expose var senderUsername: String,
    @SerializedName("date") @Expose var date: Date
)
