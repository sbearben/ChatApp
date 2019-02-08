package uk.co.victoriajanedavis.chatapp.data.realtime

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uk.co.victoriajanedavis.chatapp.data.model.websocket.*
import javax.inject.Inject

class RealtimeEventResolver @Inject constructor(
    private val gson: Gson
) {

    fun resolveEventFromMap(jsonMap: Map<String, String>): RealtimeModel {
        return resolveEvent(mapToJson(jsonMap), jsonMap)
    }

    fun resolveEventFromJson(json: String): RealtimeModel {
        return resolveEvent(json, jsonToMap(json))
    }

    private fun resolveEvent(json: String, jsonMap: Map<String, String>): RealtimeModel {
        val type: String = jsonMap["type"] ?: throw Exception("Realtime event error: event json missing type key")

        return when(type) {
            RealtimeMessageTypes.ACCEPTED_FRIEND_REQUEST.type -> deserializeJson(json, AcceptedFriendRequestWsModel::class.java)
            RealtimeMessageTypes.CANCELED_FRIEND_REQUEST.type -> deserializeJson(json, CanceledFriendRequestWsModel::class.java)
            RealtimeMessageTypes.CREATED_FRIEND_REQUEST.type -> deserializeJson(json, CreatedFriendRequestWsModel::class.java)
            RealtimeMessageTypes.REJECTED_FRIEND_REQUEST.type -> deserializeJson(json, RejectedFriendRequestWsModel::class.java)
            RealtimeMessageTypes.CHAT_MESSAGE.type -> deserializeJson(json, MessageWsModel::class.java)
            else -> throw Exception("Realtime event error: unable to deserialize json")
        }
    }

    private fun jsonToMap(json: String): HashMap<String, String> {
        val type = object: TypeToken<HashMap<String, String>>(){}.type
        return gson.fromJson(json, type)
    }

    private fun mapToJson(messageData: Map<String, String>): String {
        return gson.toJson(messageData)
    }

    private fun <T> deserializeJson(jsonString: String, classOfT: Class<T>): T {
        return gson.fromJson(jsonString, classOfT)
    }
}