package uk.co.victoriajanedavis.chatapp.data.repositories;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BasePublishSubjectSingularStore;
import uk.co.victoriajanedavis.chatapp.domain.ReactiveSingularStore;
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope;
import uk.co.victoriajanedavis.chatapp.data.mappers.TokenNwSpMapper;
import uk.co.victoriajanedavis.chatapp.data.mappers.TokenSpEntityMapper;
import uk.co.victoriajanedavis.chatapp.data.model.sharedpref.TokenSpModel;
import uk.co.victoriajanedavis.chatapp.data.repositories.store.TokenReactiveStore;
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService;
import uk.co.victoriajanedavis.chatapp.domain.ReactiveStore;
import uk.co.victoriajanedavis.chatapp.domain.entities.TokenEntity;

@ApplicationScope
public class TokenRepository {

    private final ReactiveSingularStore<TokenSpModel> tokenStore;
    private final ChatAppService chatService;

    private final TokenSpEntityMapper spEntityMapper;
    private final TokenNwSpMapper nwSpMapper;


    @Inject
    public TokenRepository(@NonNull final BasePublishSubjectSingularStore<TokenSpModel> tokenStore,
                           @NonNull final ChatAppService service) {
        this.tokenStore = tokenStore;
        this.chatService = service;
        this.spEntityMapper = new TokenSpEntityMapper();
        this.nwSpMapper = new TokenNwSpMapper();
    }

    @NonNull
    public Single<TokenEntity> requestTokenSingle() {
        return tokenStore.getSingular()
                .map(spEntityMapper::mapFrom)
                .firstOrError();
    }

    @NonNull
    public Observable<TokenEntity> getTokenStream() {
        return tokenStore.getSingular()
                .map(spEntityMapper::mapFrom);
    }

    @NonNull
    public Completable deleteTokenViaLogout() {
        return deleteTokenFromStorage()
                .andThen(chatService.logout().onErrorResumeNext(e -> Completable.complete()));
    }

    @NonNull
    public Completable fetchTokenViaLogin(String username, String password) {
        return chatService.login(username, password)
                .map(nwSpMapper::mapFrom)
                .flatMapCompletable(tokenStore::storeSingular);
    }

    @NonNull
    public Completable fetchTokenViaRegister(String username, String email,
                                             String password1, String password2) {
        return chatService.register(username, email, password1, password2)
                .map(nwSpMapper::mapFrom)
                .flatMapCompletable(tokenStore::storeSingular);
    }

    @NonNull
    private Completable deleteTokenFromStorage() {
        return tokenStore.clear();
    }
}
