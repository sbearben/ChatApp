package uk.co.victoriajanedavis.chatapp.domain;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;


/**
 * Interface for any type of store. Don't implement this directly,
 * use {@link MemoryCache} or {@link DiskCache} so it is more
 * descriptive.
 */
public interface Cache<Key, Value> {

    void putSingular(@NonNull final Value value);

    void putAll(@NonNull final List<Value> valueList);

    void replaceAll(@Nullable final Key key, @NonNull final List<Value> valueList);

    void delete(@NonNull final Value value);

    void clear();

    @NonNull
    Observable<Value> getSingular(@Nullable final Key key);

    @NonNull
    Observable<List<Value>> getAll(@Nullable final Key key);

    /**
     * More descriptive interface for memory based stores.
     */
    interface MemoryCache<Key, Value> extends Cache<Key, Value> {
    }

    /**
     * More descriptive interface for disk based stores.
     */
    interface DiskCache<Key, Value> extends Cache<Key, Value> {
    }
}
