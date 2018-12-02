package uk.co.victoriajanedavis.chatapp.data.room.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;
import java.util.UUID;

import io.reactivex.Flowable;
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel;

@Dao
public abstract class ChatMembershipDao {

    @Query("SELECT * FROM chat_memberships")
    public abstract Flowable<List<ChatDbModel>> getAll();

    @Query("SELECT * FROM chat_memberships WHERE chat_uuid=:chat_uuid")
    public abstract Flowable<ChatDbModel> get(UUID chat_uuid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertChatMembership(ChatDbModel chatMembership);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertChatMemberships(List<ChatDbModel> chatMemberships);

    @Delete
    public abstract void deleteChatMembership(ChatDbModel chatMembership);

    @Query("DELETE FROM chat_memberships")
    public abstract void clear();

    @Transaction
    public void replaceAll(List<ChatDbModel> chatMemberships) {
        clear();
        insertChatMemberships(chatMemberships);
    }
}
