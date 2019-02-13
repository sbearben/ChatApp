package uk.co.victoriajanedavis.chatapp.data.model.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.UUID


data class UserNwModel(
    @SerializedName("uuid") @Expose val uuid: UUID,
    @SerializedName("username") @Expose val username: String = "",
    @SerializedName("email") @Expose val email: String = ""
)
