package uk.co.victoriajanedavis.chatapp.data.repositories.cache;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel;
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope;
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase;
import uk.co.victoriajanedavis.chatapp.data.room.daos.ChatMembershipDao;
import uk.co.victoriajanedavis.chatapp.domain.Cache.DiskCache;

@ApplicationScope
public class ChatMembershipCache implements DiskCache<UUID, ChatDbModel> {

    private final ChatMembershipDao dao;


    @Inject
    public ChatMembershipCache(ChatAppDatabase database) {
        this.dao = database.chatMembershipDao();
    }

    @Override
    public void putSingular(@NonNull ChatDbModel chatDbModel) {
        //dao.insertChatMembership(chatDbModel);
        dao.upsertChatMembership(chatDbModel);
    }

    @Override
    public void putAll(@NonNull List<ChatDbModel> chatDbModels) {
        dao.insertChatMemberships(chatDbModels);
    }

    @Override
    public void replaceAll(@Nullable UUID uuid,
                           @NonNull List<ChatDbModel> chatDbModels) {
        dao.replaceAll(chatDbModels);
    }

    @Override
    public void delete(@NonNull ChatDbModel chatDbModel) {
        dao.deleteChatMembership(chatDbModel);
    }

    @Override
    public void clear() {
        dao.clear();
    }

    @NonNull
    @Override
    public Observable<ChatDbModel> getSingular(@NonNull UUID uuid) {
        return dao.get(uuid).toObservable();
    }

    @NonNull
    @Override
    public Observable<List<ChatDbModel>> getAll(@Nullable UUID uuid) {
        return dao.getAll().toObservable();
    }
}
