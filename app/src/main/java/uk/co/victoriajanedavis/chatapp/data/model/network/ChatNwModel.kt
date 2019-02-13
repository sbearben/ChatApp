package uk.co.victoriajanedavis.chatapp.data.model.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList
import java.util.UUID

data class ChatNwModel(
    @SerializedName("uuid") @Expose val uuid: UUID,
    @SerializedName("messages") @Expose val messages: List<MessageNwModel> = ArrayList()
)
