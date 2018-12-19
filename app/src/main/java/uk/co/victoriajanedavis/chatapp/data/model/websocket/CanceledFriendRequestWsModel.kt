package uk.co.victoriajanedavis.chatapp.data.model.websocket

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.UUID

data class CanceledFriendRequestWsModel (
    @SerializedName("type") @Expose val type: String = "canceled_friend_request",
    @SerializedName("canceler_uuid") @Expose var cancelerUuid: UUID,
    @SerializedName("canceler_email") @Expose var cancelerEmail: String,
    @SerializedName("canceler_username") @Expose var cancelerUsername: String
)
