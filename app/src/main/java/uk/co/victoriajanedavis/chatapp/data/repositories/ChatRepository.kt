package uk.co.victoriajanedavis.chatapp.data.repositories

import javax.inject.Inject

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import uk.co.victoriajanedavis.chatapp.data.mappers.ChatMembershipNwDbMapper
import uk.co.victoriajanedavis.chatapp.data.mappers.FriendshipDbEntityMapper
import uk.co.victoriajanedavis.chatapp.data.mappers.MessageDbEntityMapper
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.repositories.store.RecentMessagesStore
import uk.co.victoriajanedavis.chatapp.data.repositories.store.FriendshipStore
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService
import uk.co.victoriajanedavis.chatapp.domain.ReactiveStore
import uk.co.victoriajanedavis.chatapp.domain.common.mapList
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatEntity
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity
import uk.co.victoriajanedavis.chatapp.domain.entities.MessageEntity
import java.util.UUID
import javax.inject.Named

@ApplicationScope
class ChatRepository @Inject constructor(
    @Named(RecentMessagesStore) private val messagesStore: ReactiveStore<UUID, MessageDbModel>,
    @Named(FriendshipStore) private val friendStore: ReactiveStore<UUID, FriendshipDbModel>,
    private val chatService: ChatAppService
) {
    private val messageDbEntityMapper = MessageDbEntityMapper()
    private val chatNwDbMapper = ChatMembershipNwDbMapper()
    private val friendDbEntityMapper = FriendshipDbEntityMapper()


    fun allChatMemberships(): Observable<List<ChatEntity>> {
        return messagesStore.getAll(null)
            .map { dbModels -> dbModels.map { messageDbEntityMapper.mapFrom(it) } }
            .switchMapSingle { messageEntities -> Observable.fromIterable(messageEntities)
                .flatMap { messageEntity -> Observable.zip(
                    Observable.just(messageEntity),
                    friendStore.getSingular(messageEntity.chatUuid).map(friendDbEntityMapper::mapFrom),
                    BiFunction { _: MessageEntity, friendEntity: FriendshipEntity ->
                        return@BiFunction ChatEntity (
                            uuid = messageEntity.chatUuid,
                            lastMessage = messageEntity,
                            friendship = friendEntity
                        )
                    })
                }
                .toSortedList()
            }
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