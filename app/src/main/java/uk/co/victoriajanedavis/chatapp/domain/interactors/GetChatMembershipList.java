package uk.co.victoriajanedavis.chatapp.domain.interactors;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import uk.co.victoriajanedavis.chatapp.data.repositories.ChatRepository;
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatMembershipEntity;
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.RetrieveInteractor;

import static io.reactivex.Single.just;

public class GetChatMembershipList implements RetrieveInteractor<Void, List<ChatMembershipEntity>> {

    @NonNull
    private final ChatRepository chatRepository;

    @Inject
    public GetChatMembershipList(@NonNull final ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @NonNull
    @Override
    public Observable<List<ChatMembershipEntity>> getBehaviorStream(@Nullable final Void params) {
        return chatRepository.getAllChatMemberships()
                // fetch if emitted value is none
                .flatMapSingle(this::fetchWhenEmptyAndThenChatMemberships);
    }

    @NonNull
    private Single<List<ChatMembershipEntity>> fetchWhenEmptyAndThenChatMemberships(
            @NonNull final List<ChatMembershipEntity> chatMemberships) {
        return fetchWhenEmpty(chatMemberships).andThen(just(chatMemberships));//.filter(list -> !list.isEmpty());
    }

    @NonNull
    private Completable fetchWhenEmpty(@NonNull final List<ChatMembershipEntity> chatMemberships) {
        /* if chatMemberships !empty then returning complete() will cause the '.andThen(just(chatMemberships))'
         * above to trigger, passing the non-empty drafts down the stream.
         */
        return chatMemberships.isEmpty()
               ? chatRepository.fetchChatMemberships()
               : Completable.complete();
    }
}
