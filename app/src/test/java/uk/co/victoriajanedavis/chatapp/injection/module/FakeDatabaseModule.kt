package uk.co.victoriajanedavis.chatapp.injection.module

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dagger.Module
import dagger.Provides
import uk.co.victoriajanedavis.chatapp.ChatApp
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase
import uk.co.victoriajanedavis.chatapp.data.room.daos.FriendshipDao
import uk.co.victoriajanedavis.chatapp.data.room.daos.MessageDao
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.ApplicationContext
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope

@Module
class FakeDatabaseModule {

    @Provides
    @ApplicationScope
    fun chatAppDatabase(@ApplicationContext context: Context): ChatAppDatabase {
        return Room.inMemoryDatabaseBuilder(context, ChatAppDatabase::class.java)
            // allowing main thread queries, just for testing
            .allowMainThreadQueries()
            .build();
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

    @Provides
    @ApplicationScope
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("chat_app_shared_pref", Context.MODE_PRIVATE)
}