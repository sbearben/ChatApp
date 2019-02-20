package uk.co.victoriajanedavis.chatapp.data.repositories.store

import java.util.Date
import java.util.UUID

import io.reactivex.Single
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.MessageCache

class MessageReactiveStore constructor(
    private val cache: MessageCache
) : BaseReactiveStore<MessageDbModel>(cache) {

    fun getDateOfOldestMessage(chatUuid: UUID): Single<Date> {
        return cache.getDateOfOldestMessageByChat(chatUuid)
    }
}
