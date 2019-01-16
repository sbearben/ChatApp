package uk.co.victoriajanedavis.chatapp.domain;


import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 Interface for any kind of reactive store.
 */
public interface ReactiveStore<Key, Value> {

    Completable storeSingular(@NonNull final Value model);

    Completable storeAll(@NonNull final List<Value> modelList);

    Completable replaceAll(@Nullable final Key key, @NonNull final List<Value> modelList);

    Completable delete(@NonNull final Value model);

    Observable<Value> getSingular(@Nullable final Key key);

    Observable<List<Value>> getAll(@Nullable final Key key);
}
