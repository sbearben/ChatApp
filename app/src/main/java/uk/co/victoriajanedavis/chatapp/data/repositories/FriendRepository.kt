package uk.co.victoriajanedavis.chatapp.data.repositories

import javax.inject.Inject

import io.reactivex.Completable
import io.reactivex.Observable
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel
import uk.co.victoriajanedavis.chatapp.data.mappers.ChatMembershipNwDbMapper
import uk.co.victoriajanedavis.chatapp.data.mappers.FriendshipDbEntityMapper
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.repositories.store.RecentMessagesStore
import uk.co.victoriajanedavis.chatapp.data.repositories.store.FriendshipStore
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService
import uk.co.victoriajanedavis.chatapp.domain.ReactiveStore
import uk.co.victoriajanedavis.chatapp.domain.common.mapList
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import java.util.UUID
import javax.inject.Named

class FriendRepository @Inject constructor(
    @Named(RecentMessagesStore) private val messagesStore: ReactiveStore<UUID, MessageDbModel>,
    @Named(FriendshipStore) private val friendStore: ReactiveStore<UUID, FriendshipDbModel>,
    private val chatService: ChatAppService
) {
    private val friendDbEntityMapper = FriendshipDbEntityMapper()
    private val chatNwDbMapper = ChatMembershipNwDbMapper()


    fun allFriends(): Observable<List<FriendshipEntity>> {
        return friendStore.getAll(null)
            .mapList(friendDbEntityMapper::mapFrom)
    }

    fun fetchChatMemberships(): Completable {
        return chatService.chatMemberships
            .mapList(chatNwDbMapper::mapFrom)
            .flatMapCompletable(::replaceAllDeep)
    }

    private fun replaceAllDeep(chatModels: List<ChatDbModel>): Completable {
        return Completable.fromAction {
            val friendships = ArrayList<FriendshipDbModel>()
            val messages = ArrayList<MessageDbModel>()
            for (chatModel in chatModels) {
                friendships.add(chatModel.friendship)
                messages.addAll(chatModel.messages)
            }
            friendStore.replaceAll(null, friendships).subscribe()
            messagesStore.replaceAll(null, messages).subscribe()
        }
    }
}