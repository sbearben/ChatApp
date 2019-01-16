package uk.co.victoriajanedavis.chatapp.data.model.websocket

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.Date
import java.util.UUID

data class MessageWsModel (
    @SerializedName("type") @Expose val type: String = RealtimeMessageTypes.CHAT_MESSAGE.type,
    @SerializedName("uuid") @Expose var uuid: UUID,
    @SerializedName("chat_uuid") @Expose var chatUuid: UUID,
    @SerializedName("sender_uuid") @Expose var senderUuid: UUID,
    @SerializedName("sender_username") @Expose var senderUsername: String,
    @SerializedName("date") @Expose var date: Date,
    @SerializedName("message") @Expose var message: String
) : RealtimeModel
