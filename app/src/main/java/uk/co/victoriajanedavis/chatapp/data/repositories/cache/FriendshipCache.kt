package uk.co.victoriajanedavis.chatapp.data.repositories.cache

import java.util.UUID

import javax.inject.Inject

import io.reactivex.Observable
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase
import uk.co.victoriajanedavis.chatapp.domain.Cache.DiskCache

@ApplicationScope
class FriendshipCache @Inject constructor(
    database: ChatAppDatabase
) : DiskCache<UUID, FriendshipDbModel> {

    private val dao = database.friendshipDao()


    override fun putSingular(value: FriendshipDbModel) {
        dao.insertFriendship(value)
    }

    override fun putAll(valuesList: List<FriendshipDbModel>) {
        dao.insertFriendships(valuesList)
    }

    override fun replaceAll(key: UUID?, valuesList: List<FriendshipDbModel>) {
        dao.replaceAllAcceptedFriendships(valuesList)
    }

    override fun delete(value: FriendshipDbModel) {
        dao.deleteFriendship(value)
    }

    override fun clear() {
        dao.clear()
    }

    // Key should be chatUuid
    override fun getSingular(key: UUID?): Observable<FriendshipDbModel> {
        return dao.getFriendshipByChatUuid(key!!).toObservable()
    }

    override fun getAll(key: UUID?): Observable<List<FriendshipDbModel>> {
        return dao.allAcceptedFriendships.toObservable()
    }
}
