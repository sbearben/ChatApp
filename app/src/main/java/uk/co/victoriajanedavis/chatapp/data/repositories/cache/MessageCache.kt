package uk.co.victoriajanedavis.chatapp.data.repositories.cache

import java.util.Date
import java.util.UUID

import javax.inject.Inject

import io.reactivex.Observable
import io.reactivex.Single
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase
import uk.co.victoriajanedavis.chatapp.data.room.daos.MessageDao
import uk.co.victoriajanedavis.chatapp.domain.Cache.DiskCache

@ApplicationScope
class MessageCache @Inject constructor(
    private val dao: MessageDao
) : DiskCache<UUID, MessageDbModel> {


    override fun putSingular(value: MessageDbModel) {
        dao.insertMessage(value)
    }

    override fun putAll(valuesList: List<MessageDbModel>) {
        dao.insertMessages(valuesList)
    }

    // Key should be chatUuid
    override fun replaceAll(key: UUID?, valuesList: List<MessageDbModel>) {
        if (key == null)
            dao.replaceAll(valuesList)
        else
            dao.replaceAllByChat(key, valuesList)
    }

    override fun delete(value: MessageDbModel) {
        dao.deleteMessage(value)
    }

    override fun clear() {
        dao.clear()
    }

    override fun getSingular(key: UUID): Observable<MessageDbModel> {
        return dao.get(key).toObservable()
    }

    // Key should be chatUuid
    override fun getAll(key: UUID?): Observable<List<MessageDbModel>> {
        return dao.getMessagesByChatUuid(key!!)
            .distinctUntilChanged()
            .toObservable()
    }

    fun getDateOfOldestMessageByChat(chatUuid: UUID): Single<Date> {
        return dao.getDateOfOldestMessageByChat(chatUuid)
    }
}
