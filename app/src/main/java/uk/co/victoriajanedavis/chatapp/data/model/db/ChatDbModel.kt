package uk.co.victoriajanedavis.chatapp.data.model.db

import kotlin.collections.ArrayList

data class ChatDbModel(
    val friendship: FriendshipDbModel,
    val messages: List<MessageDbModel> = ArrayList()
)
