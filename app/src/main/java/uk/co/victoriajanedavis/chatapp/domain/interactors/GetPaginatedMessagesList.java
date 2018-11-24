package uk.co.victoriajanedavis.chatapp.domain.interactors;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import uk.co.victoriajanedavis.chatapp.data.repositories.MessageRepository;
import uk.co.victoriajanedavis.chatapp.domain.entities.MessageEntity;
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.PaginatedRetrieveInteractor;

import static io.reactivex.Single.just;

public class GetPaginatedMessagesList implements PaginatedRetrieveInteractor<UUID, List<MessageEntity>> {

    @NonNull
    private final MessageRepository messageRepository;

    @Inject
    public GetPaginatedMessagesList(@NonNull final MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @NonNull
    @Override
    public Observable<List<MessageEntity>> getBehaviorStream(@NonNull final UUID chatUuid) {
        return messageRepository.getAllMessagesInChat(chatUuid)
                .flatMapSingle(messages -> fetchWhenEmptyAndThenMessages(messages, chatUuid));
    }

    @NonNull
    @Override
    public Completable fetchMoreItems(@NonNull final UUID chatUuid) {
        return messageRepository.fetchMoreMessagesInChatOlderThanOldestInDb(chatUuid);
    }

    @NonNull
    private Single<List<MessageEntity>> fetchWhenEmptyAndThenMessages(
            @NonNull final List<MessageEntity> messages, @NonNull final UUID chatUuid) {
        return fetchWhenEmpty(messages, chatUuid).andThen(just(messages));
    }

    @NonNull
    private Completable fetchWhenEmpty(@NonNull final List<MessageEntity> messages,
                                       @NonNull final UUID chatUuid) {
        return messages.isEmpty()
               ? messageRepository.fetchInitialMessagesInChat(chatUuid)
               : Completable.complete();
    }
}
