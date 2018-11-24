package uk.co.victoriajanedavis.chatapp.injection.module

import dagger.Module
import dagger.Provides
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatMembershipDbModel
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.*
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore
import uk.co.victoriajanedavis.chatapp.data.repositories.store.MessageReactiveStore
import uk.co.victoriajanedavis.chatapp.data.repositories.store.TokenReactiveStore
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.FriendshipStore
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.ReceivedFriendRequestStore
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.SentFriendRequestStore

@Module
class ReactiveStoreModule {

    @Provides
    @ApplicationScope
    fun chatMembershipReactiveStore(cache: ChatMembershipCache)
            : BaseReactiveStore<ChatMembershipDbModel> {
        return BaseReactiveStore(cache)
    }

    @Provides
    @ApplicationScope
    @FriendshipStore
    fun friendshipReactiveStore(cache: FriendshipCache): BaseReactiveStore<FriendshipDbModel> {
        return BaseReactiveStore(cache)
    }

    @Provides
    @ApplicationScope
    fun messageReactiveStore(cache: MessageCache): BaseReactiveStore<MessageDbModel> {
        return MessageReactiveStore(cache)
    }

    @Provides
    @ApplicationScope
    @ReceivedFriendRequestStore
    fun receivedFriendRequestReactiveStore(cache: ReceivedFriendRequestCache)
            : BaseReactiveStore<FriendshipDbModel> {
        return BaseReactiveStore(cache)
    }

    @Provides
    @ApplicationScope
    @SentFriendRequestStore
    fun sentFriendRequestCache(cache: SentFriendRequestCache)
            : BaseReactiveStore<FriendshipDbModel> {
        return BaseReactiveStore(cache)
    }

    @Provides
    @ApplicationScope
    fun tokenReactiveStore(cache: TokenCache): TokenReactiveStore {
        return TokenReactiveStore(cache)
    }
}