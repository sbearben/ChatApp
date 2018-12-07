package uk.co.victoriajanedavis.chatapp.data.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import java.util.UUID

import io.reactivex.Flowable
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel

@Dao
abstract class ChatMembershipDao {

    @get:Query("SELECT * FROM chat_memberships")
    abstract val all: Flowable<List<ChatDbModel>>

    @Query("SELECT * FROM chat_memberships WHERE chat_uuid=:chat_uuid")
    abstract operator fun get(chat_uuid: UUID): Flowable<ChatDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertChatMembership(chatMembership: ChatDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertChatMemberships(chatMemberships: List<ChatDbModel>)

    @Delete
    abstract fun deleteChatMembership(chatMembership: ChatDbModel)

    @Query("DELETE FROM chat_memberships")
    abstract fun clear()

    @Transaction
    open fun replaceAll(chatMemberships: List<ChatDbModel>) {
        clear()
        insertChatMemberships(chatMemberships)
    }
}
