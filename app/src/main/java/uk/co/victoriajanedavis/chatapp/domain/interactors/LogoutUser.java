package uk.co.victoriajanedavis.chatapp.domain.interactors;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import uk.co.victoriajanedavis.chatapp.data.repositories.FirebaseTokenRepository;
import uk.co.victoriajanedavis.chatapp.data.repositories.TokenRepository;
import uk.co.victoriajanedavis.chatapp.domain.entities.TokenEntity;
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.DeleteInteractor;

public class LogoutUser implements DeleteInteractor<Void, TokenEntity> {

    @NonNull private final TokenRepository backendTokenRepo;
    @NonNull private final FirebaseTokenRepository firebaseTokenRepo;


    @Inject
    public LogoutUser(@NonNull final TokenRepository backendTokenRepo,
                      @NonNull final FirebaseTokenRepository firebaseTokenRepo) {
        this.backendTokenRepo = backendTokenRepo;
        this.firebaseTokenRepo = firebaseTokenRepo;
    }

    @NonNull
    @Override
    public Single<TokenEntity> getSingle(@NonNull Void aVoid) {
        return backendTokenRepo.requestTokenSingle()
                .flatMap(tokenEntity -> backendTokenRepo.deleteTokenViaLogout()
                        .andThen(firebaseTokenRepo.deleteTokenFromBackend())
                        .andThen(Single.just(tokenEntity)));
    }
}
