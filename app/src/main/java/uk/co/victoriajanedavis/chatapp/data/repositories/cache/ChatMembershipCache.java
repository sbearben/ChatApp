package uk.co.victoriajanedavis.chatapp.data.repositories.cache;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope;
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatMembershipDbModel;
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase;
import uk.co.victoriajanedavis.chatapp.data.room.daos.ChatMembershipDao;
import uk.co.victoriajanedavis.chatapp.domain.Cache.DiskCache;

@ApplicationScope
public class ChatMembershipCache implements DiskCache<UUID, ChatMembershipDbModel> {

    private final ChatMembershipDao dao;


    @Inject
    public ChatMembershipCache(ChatAppDatabase database) {
        this.dao = database.chatMembershipDao();
    }

    @Override
    public void putSingular(@NonNull ChatMembershipDbModel chatMembershipDbModel) {
        dao.insertChatMembership(chatMembershipDbModel);
    }

    @Override
    public void putAll(@NonNull List<ChatMembershipDbModel> chatMembershipDbModels) {
        dao.insertChatMemberships(chatMembershipDbModels);
    }

    @Override
    public void replaceAll(@Nullable UUID uuid,
                           @NonNull List<ChatMembershipDbModel> chatMembershipDbModels) {
        dao.replaceAll(chatMembershipDbModels);
    }

    @Override
    public void delete(@NonNull ChatMembershipDbModel chatMembershipDbModel) {
        dao.deleteChatMembership(chatMembershipDbModel);
    }

    @Override
    public void clear() {
        dao.clear();
    }

    @NonNull
    @Override
    public Observable<ChatMembershipDbModel> getSingular(@NonNull UUID uuid) {
        return dao.get(uuid).toObservable();
    }

    @NonNull
    @Override
    public Observable<List<ChatMembershipDbModel>> getAll(@Nullable UUID uuid) {
        return dao.getAll().toObservable();
    }
}
