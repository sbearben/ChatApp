package uk.co.victoriajanedavis.chatapp.data.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatMembershipDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.db.converter.BooleanConverter;
import uk.co.victoriajanedavis.chatapp.data.model.db.converter.DateConverter;
import uk.co.victoriajanedavis.chatapp.data.model.db.converter.UuidConverter;
import uk.co.victoriajanedavis.chatapp.data.room.daos.ChatMembershipDao;
import uk.co.victoriajanedavis.chatapp.data.room.daos.FriendshipDao;
import uk.co.victoriajanedavis.chatapp.data.room.daos.MessageDao;


@Database(entities = {
        ChatMembershipDbModel.class,
        FriendshipDbModel.class,
        MessageDbModel.class},
    version = 1,
    exportSchema = false)
@TypeConverters({DateConverter.class, BooleanConverter.class, UuidConverter.class})
public abstract class ChatAppDatabase extends RoomDatabase {
    public abstract ChatMembershipDao chatMembershipDao();
    public abstract FriendshipDao friendshipDao();
    public abstract MessageDao messageDao();

}
