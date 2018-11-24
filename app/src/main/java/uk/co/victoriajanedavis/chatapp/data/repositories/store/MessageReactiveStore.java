package uk.co.victoriajanedavis.chatapp.data.repositories.store;

import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.Single;
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope;
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel;
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.MessageCache;

@ApplicationScope
public class MessageReactiveStore extends BaseReactiveStore<MessageDbModel> {

    private final MessageCache cache;

    @Inject
    public MessageReactiveStore(MessageCache cache) {
        super(cache);
        this.cache = cache;
    }

    public Single<Date> getDateOfOldestMessage(UUID chatUuid) {
        return cache.getDateOfOldestMessageByChat(chatUuid);
    }
}
