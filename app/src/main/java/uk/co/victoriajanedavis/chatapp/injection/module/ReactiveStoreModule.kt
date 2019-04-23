package uk.co.victoriajanedavis.chatapp.injection.module

import dagger.Module
import dagger.Provides
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel
import uk.co.victoriajanedavis.chatapp.data.model.sharedpref.FirebaseTokenSpModel
import uk.co.victoriajanedavis.chatapp.data.model.sharedpref.TokenSpModel
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.*
import uk.co.victoriajanedavis.chatapp.data.repositories.store.*
import uk.co.victoriajanedavis.chatapp.domain.ReactiveSingularStore
import uk.co.victoriajanedavis.chatapp.domain.ReactiveStore
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import java.util.*
import javax.inject.Named

@Module
object ReactiveStoreModule {

    @Provides @JvmStatic
    @ApplicationScope
    @Named(RecentMessagesStore)
    fun chatMembershipReactiveStore(cache: RecentMessagesCache): ReactiveStore<UUID, MessageDbModel> {
        return BaseReactiveStore(cache)
    }

    @Provides @JvmStatic
    @ApplicationScope
    @Named(FriendshipStore)
    //@FriendshipStore
    fun friendshipReactiveStore(cache: FriendshipCache): ReactiveStore<UUID, FriendshipDbModel> {
        return BaseReactiveStore(cache)
    }

    @Provides @JvmStatic
    @ApplicationScope
    fun messageReactiveStore(cache: MessageCache): MessageReactiveStore {
        return MessageReactiveStore(cache)
    }

    @Provides @JvmStatic
    @ApplicationScope
    @Named(ReceivedFriendRequestStore)
    //@ReceivedFriendRequestStore
    fun receivedFriendRequestReactiveStore(cache: ReceivedFriendRequestCache): ReactiveStore<UUID, FriendshipDbModel> {
        return BaseReactiveStore(cache)
    }

    @Provides @JvmStatic
    @ApplicationScope
    @Named(SentFriendRequestStore)
    //@SentFriendRequestStore
    fun sentFriendRequestCache(cache: SentFriendRequestCache): ReactiveStore<UUID, FriendshipDbModel> {
        return BaseReactiveStore(cache)
    }

    @Provides @JvmStatic
    @ApplicationScope
    fun tokenReactiveStore(cache: TokenCache): ReactiveSingularStore<TokenSpModel> {
        return BasePublishSubjectSingularStore(cache)
    }

    @Provides @JvmStatic
    @ApplicationScope
    fun firebaseTokenReactiveStore(cache: FirebaseTokenCache): ReactiveSingularStore<FirebaseTokenSpModel> {
        return BasePublishSubjectSingularStore(cache)
    }
}