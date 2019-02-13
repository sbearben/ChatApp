package uk.co.victoriajanedavis.chatapp.data.model.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokenNwModel(
    @SerializedName("key") @Expose val token: String,
    @SerializedName("user") @Expose val user: UserNwModel
)
