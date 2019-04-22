package uk.co.victoriajanedavis.chatapp.injection.module

import dagger.Module
import dagger.Provides
import uk.co.victoriajanedavis.chatapp.data.cache.FakeFirebaseTokenCache
import uk.co.victoriajanedavis.chatapp.data.cache.FakeTokenCache
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel
import uk.co.victoriajanedavis.chatapp.data.model.sharedpref.FirebaseTokenSpModel
import uk.co.victoriajanedavis.chatapp.data.model.sharedpref.TokenSpModel
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.*
import uk.co.victoriajanedavis.chatapp.data.repositories.store.*
import uk.co.victoriajanedavis.chatapp.data.store.*
import uk.co.victoriajanedavis.chatapp.domain.ReactiveSingularStore
import uk.co.victoriajanedavis.chatapp.domain.ReactiveStore
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import java.util.UUID
import javax.inject.Named

@Module
class MemoryReactiveStoreModule {

    @Provides
    @ApplicationScope
    @Named(RecentMessagesStore)
    fun fakeRecentMessagesStore(): ReactiveStore<UUID, MessageDbModel> {
        return BaseMemoryReactiveStore(
            extractKeyFromValue = { messageDbModel -> messageDbModel.uuid },
            extractParentKeyFromValue = { messageDbModel -> messageDbModel.chatUuid }
        )
    }


    /************* NEEDS TO CHANGE ************/
    @Provides
    @ApplicationScope
    @Named(FriendshipStore)
    fun fakeFriendshipReactiveStore(): ReactiveStore<UUID, FriendshipDbModel> {
        return BaseMemoryReactiveStore(
            extractKeyFromValue = { friendshipDbModel -> friendshipDbModel.uuid }
        )
    }

    /*
    @Provides
    @ApplicationScope
    fun messageReactiveStore(cache: MessageCache): MessageReactiveStore {
        return MessageReactiveStore(cache)
    }
    */
    /*****************************************/


    @Provides
    @ApplicationScope
    @Named(ReceivedFriendRequestStore)
    fun fakeReceivedFriendRequestReactiveStore(): ReactiveStore<UUID, FriendshipDbModel> {
        return BaseMemoryReactiveStore(
            extractKeyFromValue = { friendshipDbModel -> friendshipDbModel.uuid }
        )
    }

    @Provides
    @ApplicationScope
    @Named(SentFriendRequestStore)
    fun fakeSentFriendRequestCache(): ReactiveStore<UUID, FriendshipDbModel> {
        return BaseMemoryReactiveStore(
            extractKeyFromValue = { friendshipDbModel -> friendshipDbModel.uuid }
        )
    }

    @Provides
    @ApplicationScope
    fun tokenReactiveStore(cache: FakeTokenCache): ReactiveSingularStore<TokenSpModel> {
        return BasePublishSubjectSingularStore(cache)
    }

    @Provides
    @ApplicationScope
    fun firebaseTokenReactiveStore(cache: FakeFirebaseTokenCache): ReactiveSingularStore<FirebaseTokenSpModel> {
        return BasePublishSubjectSingularStore(cache)
    }
}