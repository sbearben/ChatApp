package uk.co.victoriajanedavis.chatapp.domain.interactors;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import uk.co.victoriajanedavis.chatapp.data.repositories.TokenRepository;
import uk.co.victoriajanedavis.chatapp.domain.entities.TokenEntity;
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.SendInteractor;

public class RegisterUser implements SendInteractor<RegisterUser.RegisterParams, TokenEntity> {

    @NonNull
    private final TokenRepository repository;


    @Inject
    public RegisterUser(@NonNull final TokenRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public Single<TokenEntity> getSingle(@NonNull RegisterParams registerParams) {
        return repository.fetchTokenViaRegister(registerParams.username, registerParams.email,
                                                registerParams.password1, registerParams.password2)
                .andThen(repository.requestTokenSingle());
    }

    public static final class RegisterParams {
        @NonNull private final String username;
        @NonNull private final String email;
        @NonNull private final String password1;
        @NonNull private final String password2;

        public RegisterParams(String username, String email, String password1, String password2) {
            this.username = username;
            this.email = email;
            this.password1 = password1;
            this.password2 = password2;
        }
    }
}
