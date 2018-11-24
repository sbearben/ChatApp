package uk.co.victoriajanedavis.chatapp.data.repositories;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope;
import uk.co.victoriajanedavis.chatapp.data.mappers.MessageDbEntityMapper;
import uk.co.victoriajanedavis.chatapp.data.mappers.MessageNwDbMapper;
import uk.co.victoriajanedavis.chatapp.data.repositories.store.MessageReactiveStore;
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService;
import uk.co.victoriajanedavis.chatapp.domain.entities.MessageEntity;

@ApplicationScope
public class MessageRepository {

    private static final int PAGE_SIZE = 30;

    private final MessageReactiveStore messageStore;
    private final ChatAppService chatService;

    private final MessageDbEntityMapper dbEntityMapper;
    private final MessageNwDbMapper nwDbMapper;

    @Inject
    public MessageRepository(@NonNull final MessageReactiveStore messageStore,
                             @NonNull final ChatAppService service) {
        this.messageStore = messageStore;
        this.chatService = service;

        dbEntityMapper = new MessageDbEntityMapper();
        nwDbMapper = new MessageNwDbMapper();
    }

    @NonNull
    public Observable<List<MessageEntity>> getAllMessagesInChat(UUID chatUuid) {
        return messageStore.getAll(chatUuid)
                .switchMapSingle(dbModels -> Observable.fromIterable(dbModels)
                    .map(dbEntityMapper::mapFrom)
                    .toList());
    }

    @NonNull
    public Completable fetchInitialMessagesInChat(UUID chatUuid) {
        return chatService.getNewestChatMessages(chatUuid.toString(), PAGE_SIZE)
                .flatMap(nwList -> Observable.fromIterable(nwList)
                    .map(nwDbMapper::mapFrom)
                    .toList())
                .flatMapCompletable(messages -> messageStore.replaceAll(chatUuid, messages));
    }

    // Maybe change this so that when we go to "load more messages", we query the backend for the
    // next X messages before the oldest message currently in the DB. If we copy the way iMessage
    // does this, we NEVER store messages returned to us through pagination.
    /*
    @NonNull
    public Completable loadMoreMessagesInChat(String nextUrl) {
        return chatService.getChatMessagesByUrl(nextUrl)
                .flatMap(messagesNwCollection -> Observable.fromIterable(messagesNwCollection.getData())
                        .map(nwDbMapper::mapFrom)
                        .toList())
                .flatMapCompletable(messageStore::storeAll);
    }
    */

    @NonNull
    public Completable fetchMoreMessagesInChatOlderThanOldestInDb(UUID chatUuid) {
        return messageStore.getDateOfOldestMessage(chatUuid)
                .flatMap(date -> chatService.getChatMessagesOlderThanGivenDate(chatUuid.toString(), date, PAGE_SIZE))
                .flatMap(nwList -> Observable.fromIterable(nwList)
                        .map(nwDbMapper::mapFrom)
                        .toList())
                .flatMapCompletable(messageStore::storeAll);
    }

    @NonNull
    public Single<MessageEntity> pushNewMessageToChat(UUID chatUuid, String message) {
        return chatService.postMessageToChat(chatUuid.toString(), message)
                .map(nwDbMapper::mapFrom)
                .flatMap(messageDbModel -> messageStore.storeSingular(messageDbModel)
                        .andThen(Single.just(messageDbModel)
                                .map(dbEntityMapper::mapFrom)));
    }
}
