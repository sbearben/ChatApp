package uk.co.victoriajanedavis.chatapp.data.model.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList

data class CollectionNwModel<T> (
    @SerializedName("count") @Expose val total: Int = 0,
    @SerializedName("next") @Expose val next: String = "",
    @SerializedName("previous") @Expose val previous: String = "",
    @SerializedName("results") @Expose val data: List<T> = ArrayList()
)

