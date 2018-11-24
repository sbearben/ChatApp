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
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel;

@Dao
public abstract class FriendshipDao {

    @Query("SELECT * FROM friendships WHERE friendship_accepted = 1")
    public abstract Flowable<List<FriendshipDbModel>> getAllAcceptedFriendships();

    @Query("SELECT * FROM friendships WHERE uuid=:uuid")
    public abstract Flowable<FriendshipDbModel> get(UUID uuid);

    @Query("SELECT * FROM friendships WHERE chat_uuid=:chat_uuid")
    public abstract Flowable<FriendshipDbModel> getFriendshipByChatUuid(UUID chat_uuid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertFriendship(FriendshipDbModel friendship);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertFriendships(List<FriendshipDbModel> friendships);

    @Delete
    public abstract void deleteFriendship(FriendshipDbModel friendship);

    @Query("DELETE FROM friendships")
    public abstract void clear();

    @Query("DELETE FROM friendships WHERE friendship_accepted = 1")
    public abstract void clearAcceptedFriendships();

    @Transaction
    public void replaceAllAcceptedFriendships(List<FriendshipDbModel> friendships) {
        clearAcceptedFriendships();
        insertFriendships(friendships);
    }

    /***** Sent Friend Request Methods *****/
    @Query("SELECT * FROM friendships WHERE sent_from_current_user = 1 AND friendship_accepted = 0")
    public abstract Flowable<List<FriendshipDbModel>> getSentFriendRequests();

    @Query("DELETE FROM friendships WHERE sent_from_current_user = 1 AND friendship_accepted = 0")
    public abstract void clearSentFriendRequests();

    @Transaction
    public void replaceAllSentFriendRequests(List<FriendshipDbModel> friendships) {
        clearSentFriendRequests();
        insertFriendships(friendships);
    }

    /***** Received Friend Request Methods *****/
    @Query("SELECT * FROM friendships WHERE sent_from_current_user = 0 AND friendship_accepted = 0")
    public abstract Flowable<List<FriendshipDbModel>> getReceivedFriendRequests();

    @Query("DELETE FROM friendships WHERE sent_from_current_user = 0 AND friendship_accepted = 0")
    public abstract void clearReceivedFriendRequests();

    @Transaction
    public void replaceAllReceivedFriendRequests(List<FriendshipDbModel> friendships) {
        clearReceivedFriendRequests();
        insertFriendships(friendships);
    }


}
