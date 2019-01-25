package uk.co.victoriajanedavis.chatapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel
import uk.co.victoriajanedavis.chatapp.data.model.db.converter.BooleanConverter
import uk.co.victoriajanedavis.chatapp.data.model.db.converter.DateConverter
import uk.co.victoriajanedavis.chatapp.data.model.db.converter.FriendshipLoadingStateConverter
import uk.co.victoriajanedavis.chatapp.data.model.db.converter.UuidConverter
import uk.co.victoriajanedavis.chatapp.data.room.daos.ChatMembershipDao
import uk.co.victoriajanedavis.chatapp.data.room.daos.FriendshipDao
import uk.co.victoriajanedavis.chatapp.data.room.daos.MessageDao


@Database(
    entities = arrayOf(
        ChatDbModel::class,
        FriendshipDbModel::class,
        MessageDbModel::class
    ),
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DateConverter::class,
    BooleanConverter::class,
    UuidConverter::class,
    FriendshipLoadingStateConverter::class
)
abstract class ChatAppDatabase : RoomDatabase() {
    abstract fun chatMembershipDao(): ChatMembershipDao
    abstract fun friendshipDao(): FriendshipDao
    abstract fun messageDao(): MessageDao

}
