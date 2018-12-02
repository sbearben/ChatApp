package uk.co.victoriajanedavis.chatapp.domain.interactors;

import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import uk.co.victoriajanedavis.chatapp.data.repositories.MessageRepository;
import uk.co.victoriajanedavis.chatapp.domain.entities.MessageEntity;
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.SendInteractor;

public class SendChatMessage implements SendInteractor<SendChatMessage.MessageParams, MessageEntity> {

    @NonNull
    private final MessageRepository repository;


    @Inject
    public SendChatMessage(@NonNull final MessageRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public Single<MessageEntity> getSingle(@NonNull MessageParams messageParams) {
        return repository.pushNewMessageToChat(messageParams.chatUuid, messageParams.message);
    }

    public static final class MessageParams {
        @NonNull private final UUID chatUuid;
        @NonNull private final String message;

        public MessageParams(UUID chatUuid, String message) {
            this.chatUuid = chatUuid;
            this.message = message;
        }
    }
}
