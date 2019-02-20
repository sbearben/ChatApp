package uk.co.victoriajanedavis.chatapp.data.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

import java.util.Date
import java.util.UUID

import io.reactivex.Flowable
import io.reactivex.Single
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel

@Dao
abstract class MessageDao {

    @get:Query("SELECT * FROM messages")
    abstract val all: Flowable<List<MessageDbModel>>

    @Query("SELECT * FROM messages WHERE uuid=:uuid")
    abstract fun get(uuid: UUID): Flowable<MessageDbModel>

    @Query("SELECT * FROM messages WHERE chat_uuid=:chatUuid ORDER BY created DESC")
    abstract fun getMessagesByChatUuid(chatUuid: UUID): Flowable<List<MessageDbModel>>

    @Query("SELECT m.* " +
            "FROM messages m " +
            "   LEFT JOIN messages b " +
            "       ON m.chat_uuid = b.chat_uuid " +
            "       AND m.created < b.created " +
            "WHERE b.created IS NULL ")
    abstract fun getNewestMessagePerUniqueChat(): Flowable<List<MessageDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMessage(message: MessageDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMessages(messages: List<MessageDbModel>)

    @Delete
    abstract fun deleteMessage(message: MessageDbModel)

    @Query("DELETE FROM messages")
    abstract fun clear()

    @Query("DELETE FROM messages WHERE chat_uuid=:chatUuid")
    abstract fun clearByChat(chatUuid: UUID)

    @Transaction
    open fun replaceAll(messages: List<MessageDbModel>) {
        clear()
        insertMessages(messages)
    }

    @Transaction
    open fun replaceAllByChat(chatUuid: UUID, messages: List<MessageDbModel>) {
        clearByChat(chatUuid)
        insertMessages(messages)
    }

    @Query("SELECT MIN(created) FROM messages WHERE chat_uuid=:chatUuid")
    abstract fun getDateOfOldestMessageByChat(chatUuid: UUID): Single<Date>
}
