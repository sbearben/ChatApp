package uk.co.victoriajanedavis.chatapp.data.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import java.util.UUID

import io.reactivex.Flowable
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel

@Dao
abstract class FriendshipDao {

    @get:Query("SELECT * FROM friendships WHERE friendship_accepted = 1")
    abstract val allAcceptedFriendships: Flowable<List<FriendshipDbModel>>

    /***** Sent Friend Request Methods  */
    @get:Query("SELECT * FROM friendships WHERE sent_from_current_user = 1 AND friendship_accepted = 0")
    abstract val sentFriendRequests: Flowable<List<FriendshipDbModel>>

    /***** Received Friend Request Methods  */
    @get:Query("SELECT * FROM friendships WHERE sent_from_current_user = 0 AND friendship_accepted = 0")
    abstract val receivedFriendRequests: Flowable<List<FriendshipDbModel>>

    @Query("SELECT * FROM friendships WHERE uuid=:uuid")
    abstract operator fun get(uuid: UUID): Flowable<FriendshipDbModel>

    @Query("SELECT * FROM friendships WHERE chat_uuid=:chat_uuid")
    abstract fun getFriendshipByChatUuid(chat_uuid: UUID): Flowable<FriendshipDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertFriendship(friendship: FriendshipDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertFriendships(friendships: List<FriendshipDbModel>)

    @Delete
    abstract fun deleteFriendship(friendship: FriendshipDbModel)

    @Query("DELETE FROM friendships")
    abstract fun clear()

    @Query("DELETE FROM friendships WHERE friendship_accepted = 1")
    abstract fun clearAcceptedFriendships()

    @Transaction
    open fun replaceAllAcceptedFriendships(friendships: List<FriendshipDbModel>) {
        clearAcceptedFriendships()
        insertFriendships(friendships)
    }

    @Query("DELETE FROM friendships WHERE sent_from_current_user = 1 AND friendship_accepted = 0")
    abstract fun clearSentFriendRequests()

    @Transaction
    open fun replaceAllSentFriendRequests(friendships: List<FriendshipDbModel>) {
        clearSentFriendRequests()
        insertFriendships(friendships)
    }

    @Query("DELETE FROM friendships WHERE sent_from_current_user = 0 AND friendship_accepted = 0")
    abstract fun clearReceivedFriendRequests()

    @Transaction
    open fun replaceAllReceivedFriendRequests(friendships: List<FriendshipDbModel>) {
        clearReceivedFriendRequests()
        insertFriendships(friendships)
    }


}
