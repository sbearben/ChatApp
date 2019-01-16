package uk.co.victoriajanedavis.chatapp.data.model.websocket

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.Date
import java.util.UUID

data class AcceptedFriendRequestWsModel (
    @SerializedName("type") @Expose val type: String = RealtimeMessageTypes.ACCEPTED_FRIEND_REQUEST.type,
    @SerializedName("chat_uuid") @Expose var chatUuid: UUID,
    @SerializedName("acceptor_uuid") @Expose var acceptorUuid: UUID,
    @SerializedName("acceptor_email") @Expose var acceptorEmail: String,
    @SerializedName("acceptor_username") @Expose var acceptorUsername: String
) : RealtimeModel
