package uk.co.victoriajanedavis.chatapp.data.model.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ChatMembershipNwModel(
    @SerializedName("chat") @Expose val chat: ChatNwModel,
    @SerializedName("other_user") @Expose val otherUser: UserNwModel
)
