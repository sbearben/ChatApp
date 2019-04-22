package uk.co.victoriajanedavis.chatapp.data.repositories.cache

import java.util.UUID

import javax.inject.Inject

import io.reactivex.Observable
import io.reactivex.annotations.NonNull
import io.reactivex.annotations.Nullable
import uk.co.victoriajanedavis.chatapp.data.common.TimestampProvider
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase
import uk.co.victoriajanedavis.chatapp.data.room.daos.FriendshipDao
import uk.co.victoriajanedavis.chatapp.domain.Cache.DiskCache

@ApplicationScope
class SentFriendRequestCache @Inject constructor(
    database: ChatAppDatabase
) : DiskCache<UUID, FriendshipDbModel> {

    private val dao: FriendshipDao = database.friendshipDao()


    override fun putSingular(value: FriendshipDbModel) {
        dao.insertFriendship(value)
    }

    override fun putAll(valuesList: List<FriendshipDbModel>) {
        dao.insertFriendships(valuesList)
    }

    override fun replaceAll(key: UUID?, valuesList: List<FriendshipDbModel>) {
        dao.replaceAllSentFriendRequests(valuesList)
    }

    override fun delete(value: FriendshipDbModel) {
        dao.deleteFriendship(value)
    }

    override fun clear() {}

    override fun getSingular(key: UUID): Observable<FriendshipDbModel> {
        return dao.get(key)
            //.filter(this::notExpired)
            .toObservable()
    }

    override fun getAll(key: UUID?): Observable<List<FriendshipDbModel>> {
        return dao.sentFriendRequests
            .distinctUntilChanged()
            //.flatMap(Flowable::fromIterable)
            //.filter(this::notExpired)
            //.toList()
            .toObservable()
        //.defaultIfEmpty(new ArrayList<>());
    }

    private fun notExpired(friendModel: FriendshipDbModel): Boolean {
        return friendModel.timestamp + CACHE_MAX_AGE > TimestampProvider.currentTimeMillis()
    }

    companion object {
        private const val CACHE_MAX_AGE = (5 * 60 * 1000).toLong() // 5 minutes
    }
}
