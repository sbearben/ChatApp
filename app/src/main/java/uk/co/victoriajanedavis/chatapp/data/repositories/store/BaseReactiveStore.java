package uk.co.victoriajanedavis.chatapp.data.repositories.store;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import uk.co.victoriajanedavis.chatapp.domain.Cache.DiskCache;
import uk.co.victoriajanedavis.chatapp.domain.ReactiveStore;

public class BaseReactiveStore<Value> implements ReactiveStore<UUID, Value> {

    private final DiskCache<UUID, Value> cache;

    @Inject
    public BaseReactiveStore(DiskCache<UUID, Value> cache) {
        this.cache = cache;
    }

    @Override
    public Completable storeSingular(@NonNull Value value) {
        return Completable.fromRunnable(() -> cache.putSingular(value));
    }

    @Override
    public Completable storeAll(@NonNull List<Value> valuesList) {
        return Completable.fromRunnable(() -> cache.putAll(valuesList));
    }

    @Override
    public Completable replaceAll(@Nullable UUID uuid, @NonNull List<Value> valuesList) {
        return Completable.fromRunnable(() -> cache.replaceAll(uuid, valuesList));
    }

    @Override
    public Completable delete(Value value) {
        return Completable.fromRunnable(() -> cache.delete(value));
    }

    @Override
    public Observable<Value> getSingular(@Nullable UUID uuid) {
        return cache.getSingular(uuid);
    }

    @Override
    public Observable<List<Value>> getAll(@Nullable UUID uuid) {
        return cache.getAll(uuid);
    }
}
