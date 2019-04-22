package uk.co.victoriajanedavis.chatapp.injection.module

import androidx.room.Room
import dagger.Module
import dagger.Provides
import uk.co.victoriajanedavis.chatapp.ChatApp
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase
import uk.co.victoriajanedavis.chatapp.data.room.daos.ChatMembershipDao
import uk.co.victoriajanedavis.chatapp.data.room.daos.FriendshipDao
import uk.co.victoriajanedavis.chatapp.data.room.daos.MessageDao
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope

@Module
class DatabaseModule {

    @Provides
    @ApplicationScope
    fun chatAppDatabase(app: ChatApp): ChatAppDatabase {
        return Room.databaseBuilder(app, ChatAppDatabase::class.java, "ChatApp.db")
                .fallbackToDestructiveMigration()
                .build()
    }

    @Provides
    @ApplicationScope
    fun friendshipDao(db: ChatAppDatabase): FriendshipDao {
        return db.friendshipDao()
    }

    @Provides
    @ApplicationScope
    fun messageDao(db: ChatAppDatabase): MessageDao {
        return db.messageDao()
    }
}