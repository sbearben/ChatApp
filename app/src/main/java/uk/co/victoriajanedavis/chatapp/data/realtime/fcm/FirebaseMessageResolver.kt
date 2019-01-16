package uk.co.victoriajanedavis.chatapp.data.realtime.fcm

import com.google.gson.Gson
import uk.co.victoriajanedavis.chatapp.data.model.websocket.*
import uk.co.victoriajanedavis.chatapp.data.model.websocket.RealtimeMessageTypes.*
import javax.inject.Inject

class FirebaseMessageResolver @Inject constructor(
    private val gson: Gson,
    private val messageStreams: FirebaseMessagingStreams
) {

    fun resolveMessage(messageData: Map<String, String>) {
        val type: String = messageData["type"] ?: return
        val jsonString = gson.toJson(messageData)

        val realtimeModel: RealtimeModel = when(type) {
            ACCEPTED_FRIEND_REQUEST.type -> deserializeJson(jsonString, AcceptedFriendRequestWsModel::class.java)
            CANCELED_FRIEND_REQUEST.type -> deserializeJson(jsonString, CanceledFriendRequestWsModel::class.java)
            CREATED_FRIEND_REQUEST.type -> deserializeJson(jsonString, CreatedFriendRequestWsModel::class.java)
            REJECTED_FRIEND_REQUEST.type -> deserializeJson(jsonString, RejectedFriendRequestWsModel::class.java)
            CHAT_MESSAGE.type -> deserializeJson(jsonString, MessageWsModel::class.java)
            else -> return
        }

        messageStreams.push(realtimeModel)
    }

    private fun <T> deserializeJson(jsonString: String, classOfT: Class<T>): T {
        return gson.fromJson(jsonString, classOfT)
    }
}