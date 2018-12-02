package uk.co.victoriajanedavis.chatapp.data.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import uk.co.victoriajanedavis.chatapp.data.mappers.MessageDbEntityMapper;
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel;
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope;
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.FriendshipStore;
import uk.co.victoriajanedavis.chatapp.data.mappers.ChatMembershipDbEntityMapper;
import uk.co.victoriajanedavis.chatapp.data.mappers.ChatMembershipNwDbMapper;
import uk.co.victoriajanedavis.chatapp.data.mappers.FriendshipDbEntityMapper;
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel;
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore;
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService;
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatEntity;

@ApplicationScope
public class ChatRepository {

    private final BaseReactiveStore<ChatDbModel> chatStore;
    private final BaseReactiveStore<FriendshipDbModel> friendStore;
    private final BaseReactiveStore<MessageDbModel> messageStore;
    private final ChatAppService chatService;

    private final ChatMembershipDbEntityMapper chatDbEntityMapper;
    private final ChatMembershipNwDbMapper chatNwDbMapper;
    private final FriendshipDbEntityMapper friendDbEntityMapper;


    @Inject
    public ChatRepository(@NonNull BaseReactiveStore<ChatDbModel> chatStore,
                          @NonNull @FriendshipStore BaseReactiveStore<FriendshipDbModel> friendStore,
                          @NonNull BaseReactiveStore<MessageDbModel> messageStore,
                          @NonNull ChatAppService service) {
        this.chatStore = chatStore;
        this.friendStore = friendStore;
        this.messageStore = messageStore;
        this.chatService = service;

        this.chatDbEntityMapper = new ChatMembershipDbEntityMapper();
        this.chatNwDbMapper = new ChatMembershipNwDbMapper();
        this.friendDbEntityMapper = new FriendshipDbEntityMapper();
    }

    @NonNull
    public Observable<List<ChatEntity>> getAllChatMemberships() {
        return chatStore.getAll(null)
                .switchMapSingle(dbModels -> Observable.fromIterable(dbModels)
                        .map(chatDbEntityMapper::mapFrom)
                        .flatMap(entity -> Observable.zip(Observable.just(entity),
                                friendStore.getSingular(entity.getUuid()).map(friendDbEntityMapper::mapFrom),
                                (chatEntity, friendEntity) -> {
                                    chatEntity.setFriendship(friendEntity);
                                    return chatEntity;
                                }))
                        .toList());
    }

    @NonNull
    public Completable fetchChatMemberships() {
        return chatService.getChatMemberships()
                .flatMap(nwModels -> Observable.fromIterable(nwModels)
                    .map(chatNwDbMapper::mapFrom)
                    .toList())
                //.filter(dbModels -> !dbModels.isEmpty())
                .doOnSuccess(chatDbModels -> {
                    chatStore.replaceAll(null, chatDbModels).subscribe();
                    List<FriendshipDbModel> friendships = new ArrayList<>();
                    List<MessageDbModel> messages = new ArrayList<>();
                    for (ChatDbModel chatdbModel : chatDbModels) {
                        friendships.add(chatdbModel.getFriendship());
                        messages.addAll(chatdbModel.getMessages());
                    }
                    friendStore.replaceAll(null, friendships).subscribe();
                    messageStore.replaceAll(null, messages).subscribe();
                    //friendStore.replaceAll(chatEntities.map { it.getFriendship() }).subscribe()
                })
                .ignoreElement();
    }


}
