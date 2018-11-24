package uk.co.victoriajanedavis.chatapp.data.room.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.reactivex.Flowable;
import io.reactivex.Single;
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel;

@Dao
public abstract class MessageDao {

    @Query("SELECT * FROM messages")
    public abstract Flowable<List<MessageDbModel>> getAll();

    @Query("SELECT * FROM messages WHERE uuid=:uuid")
    public abstract Flowable<MessageDbModel> get(UUID uuid);

    @Query("SELECT * FROM messages WHERE chat_uuid=:chatUuid ORDER BY created DESC")
    public abstract Flowable<List<MessageDbModel>> getMessagesByChatUuid(UUID chatUuid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertMessage(MessageDbModel message);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertMessages(List<MessageDbModel> messages);

    @Delete
    public abstract void deleteMessage(MessageDbModel message);

    @Query("DELETE FROM messages")
    public abstract void clear();

    @Query("DELETE FROM messages WHERE chat_uuid=:chatUuid")
    public abstract void clearByChat(UUID chatUuid);

    @Transaction
    public void replaceAll(List<MessageDbModel> messages) {
        clear();
        insertMessages(messages);
    }

    @Transaction
    public void replaceAllByChat(UUID chatUuid, List<MessageDbModel> messages) {
        clearByChat(chatUuid);
        insertMessages(messages);
    }

    @Query("SELECT MIN(created) FROM messages WHERE chat_uuid=:chatUuid")
    public abstract Single<Date> getDateOfOldestMessageByChat(UUID chatUuid);
}
