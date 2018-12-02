package uk.co.victoriajanedavis.chatapp.data.repositories.cache;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope;
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel;
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase;
import uk.co.victoriajanedavis.chatapp.data.room.daos.MessageDao;
import uk.co.victoriajanedavis.chatapp.domain.Cache.DiskCache;

@ApplicationScope
public class MessageCache implements DiskCache<UUID, MessageDbModel> {

    private final MessageDao dao;


    @Inject
    public MessageCache(ChatAppDatabase database) {
        this.dao = database.messageDao();
    }

    @Override
    public void putSingular(@NonNull MessageDbModel messageDbModel) {
        dao.insertMessage(messageDbModel);
    }

    @Override
    public void putAll(@NonNull List<MessageDbModel> messageDbModels) {
        dao.insertMessages(messageDbModels);
    }

    @Override
    public void replaceAll(@Nullable UUID chatUuid, @NonNull List<MessageDbModel> messageDbModels) {
        if (chatUuid == null) dao.replaceAll(messageDbModels);
        else dao.replaceAllByChat(chatUuid, messageDbModels);
    }

    @Override
    public void delete(@NonNull MessageDbModel messageDbModel) {
        dao.deleteMessage(messageDbModel);
    }

    @Override
    public void clear() {
        dao.clear();
    }

    @NonNull
    @Override
    public Observable<MessageDbModel> getSingular(@NonNull UUID uuid) {
        return dao.get(uuid).toObservable();
    }

    @NonNull
    @Override
    public Observable<List<MessageDbModel>> getAll(@Nullable UUID chatUuid) {
        return dao.getMessagesByChatUuid(chatUuid).toObservable();
    }

    @NonNull
    public Single<Date> getDateOfOldestMessageByChat(@Nullable UUID chatUuid) {
        return dao.getDateOfOldestMessageByChat(chatUuid);
    }
}
