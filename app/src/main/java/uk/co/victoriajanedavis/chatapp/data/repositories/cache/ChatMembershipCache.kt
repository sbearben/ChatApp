package uk.co.victoriajanedavis.chatapp.data.repositories.cache

import java.util.UUID

import javax.inject.Inject

import io.reactivex.Observable
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase
import uk.co.victoriajanedavis.chatapp.data.room.daos.ChatMembershipDao
import uk.co.victoriajanedavis.chatapp.domain.Cache.DiskCache

@ApplicationScope
class ChatMembershipCache @Inject constructor(
    database: ChatAppDatabase
) : DiskCache<UUID, ChatDbModel> {

    private val dao: ChatMembershipDao = database.chatMembershipDao()


    override fun putSingular(value: ChatDbModel) {
        //dao.insertChatMembership(chatDbModel);
        dao.upsertChatMembership(value)
    }

    override fun putAll(valuesList: List<ChatDbModel>) {
        dao.insertChatMemberships(valuesList)
    }

    override fun replaceAll(key: UUID?, valuesList: List<ChatDbModel>) {
        dao.replaceAll(valuesList)
    }

    override fun delete(value: ChatDbModel) {
        dao.deleteChatMembership(value)
    }

    override fun clear() {
        dao.clear()
    }

    override fun getSingular(key: UUID?): Observable<ChatDbModel> {
        return dao.get(key!!).toObservable()
    }

    override fun getAll(key: UUID?): Observable<List<ChatDbModel>> {
        return dao.all.toObservable()
    }
}
