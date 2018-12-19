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
import androidx.room.Update



@Dao
abstract class ChatMembershipDao {

    @get:Query("SELECT * FROM chat_memberships")
    abstract val all: Flowable<List<ChatDbModel>>

    @Query("SELECT * FROM chat_memberships WHERE uuid=:uuid")
    abstract operator fun get(uuid: UUID): Flowable<ChatDbModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertChatMembership(chatMembership: ChatDbModel): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract fun updateChatMembership(chatMembership: ChatDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertChatMemberships(chatMemberships: List<ChatDbModel>)

    @Delete
    abstract fun deleteChatMembership(chatMembership: ChatDbModel)

    @Query("DELETE FROM chat_memberships")
    abstract fun clear()

    fun upsertChatMembership(chatMembership: ChatDbModel) {
        val id = insertChatMembership(chatMembership)
        if (id == -1L) {
            updateChatMembership(chatMembership)
        }
    }

    @Transaction
    open fun replaceAll(chatMemberships: List<ChatDbModel>) {
        clear()
        insertChatMemberships(chatMemberships)
    }
}
