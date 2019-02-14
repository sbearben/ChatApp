package uk.co.victoriajanedavis.chatapp.data.repositories

import java.util.ArrayList

import javax.inject.Inject

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.FriendshipStore
import uk.co.victoriajanedavis.chatapp.data.mappers.ChatMembershipDbEntityMapper
import uk.co.victoriajanedavis.chatapp.data.mappers.ChatMembershipNwDbMapper
import uk.co.victoriajanedavis.chatapp.data.mappers.FriendshipDbEntityMapper
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService
import uk.co.victoriajanedavis.chatapp.domain.common.mapList
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatEntity
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity

@ApplicationScope
class ChatRepository @Inject constructor(
    private val chatStore: BaseReactiveStore<ChatDbModel>,
    @FriendshipStore private val friendStore: BaseReactiveStore<FriendshipDbModel>,
    private val messageStore: BaseReactiveStore<MessageDbModel>,
    private val chatService: ChatAppService
) {
    private val chatDbEntityMapper = ChatMembershipDbEntityMapper()
    private val chatNwDbMapper = ChatMembershipNwDbMapper()
    private val friendDbEntityMapper = FriendshipDbEntityMapper()


    fun allChatMemberships(): Observable<List<ChatEntity>> {
        return chatStore.getAll(null)
            .switchMapSingle { dbModels -> Observable.fromIterable(dbModels)
                .map(chatDbEntityMapper::mapFrom)
                .flatMap { entity -> Observable.zip(
                    Observable.just(entity),
                    friendStore.getSingular(entity.uuid).map(friendDbEntityMapper::mapFrom),
                    BiFunction { chatEntity: ChatEntity, friendEntity: FriendshipEntity ->
                        chatEntity.friendship = friendEntity
                        return@BiFunction chatEntity
                    })
                }
                .toList()
            }
    }

    fun fetchChatMemberships(): Completable {
        return chatService.chatMemberships
            .mapList(chatNwDbMapper::mapFrom)
            .flatMapCompletable(::replaceAllDeep)
    }

    private fun replaceAllDeep(chatModels: List<ChatDbModel>): Completable {
        return Completable.fromAction {
            chatStore.replaceAll(null, chatModels).subscribe()
            val friendships = ArrayList<FriendshipDbModel>()
            val messages = ArrayList<MessageDbModel>()
            for (chatModel in chatModels) {
                friendships.add(chatModel.friendship!!)
                messages.addAll(chatModel.messages!!)
            }

            friendStore.replaceAll(null, friendships).subscribe()
            messageStore.replaceAll(null, messages).subscribe()
        }
    }
}