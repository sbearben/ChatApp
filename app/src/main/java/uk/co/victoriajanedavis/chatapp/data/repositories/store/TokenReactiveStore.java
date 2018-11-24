package uk.co.victoriajanedavis.chatapp.data.repositories.store;

import java.util.List;

import javax.inject.Inject;

import android.util.Log;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope;
import uk.co.victoriajanedavis.chatapp.data.common.MethodInvalidForImplementationError;
import uk.co.victoriajanedavis.chatapp.data.model.sharedpref.TokenSpModel;
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.TokenCache;
import uk.co.victoriajanedavis.chatapp.domain.Cache.DiskCache;
import uk.co.victoriajanedavis.chatapp.domain.ReactiveStore;

@ApplicationScope
public class TokenReactiveStore implements ReactiveStore<Void, TokenSpModel> {

    private final DiskCache<Void, TokenSpModel> cache;

    @NonNull
    private final Subject<TokenSpModel> singleSubject;


    @Inject
    public TokenReactiveStore(TokenCache cache) {
        this.cache = cache;
        this.singleSubject = PublishSubject.<TokenSpModel>create().toSerialized();
    }

    @Override
    public Completable storeSingular(@NonNull TokenSpModel model) {
        return Completable.fromRunnable(() -> cache.putSingular(model))
                .doOnComplete(() -> Log.d("TokenReactiveSore", "STORE SINGULAR"))
                .andThen(Completable.fromRunnable(() -> singleSubject.onNext(model)));
    }

    @Override
    public Completable storeAll(@NonNull List<TokenSpModel> modelList) {
        return Completable.error(new MethodInvalidForImplementationError());
    }

    @Override
    public Completable replaceAll(@Nullable Void aVoid, @NonNull List<TokenSpModel> modelList) {
        return Completable.error(new MethodInvalidForImplementationError());
    }

    @Override
    public Completable delete(TokenSpModel model) {
        return Completable.fromRunnable(cache::clear).andThen(cache.getSingular(null))
                .flatMapCompletable(tokenEntity -> Completable.fromRunnable(() -> singleSubject.onNext(tokenEntity)));
    }

    @Override
    public Observable<TokenSpModel> getSingular(@NonNull Void aVoid) {
        return Observable.defer(() -> singleSubject.startWith(cache.getSingular(aVoid).blockingFirst()));
    }

    @Override
    public Observable<List<TokenSpModel>> getAll(@Nullable Void aVoid) {
        return Observable.error(new MethodInvalidForImplementationError());
    }
}
