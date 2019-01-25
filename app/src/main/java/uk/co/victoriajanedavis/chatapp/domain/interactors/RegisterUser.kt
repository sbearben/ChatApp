package uk.co.victoriajanedavis.chatapp.domain.interactors

import javax.inject.Inject

import io.reactivex.Single
import io.reactivex.annotations.NonNull
import uk.co.victoriajanedavis.chatapp.data.repositories.TokenRepository
import uk.co.victoriajanedavis.chatapp.domain.entities.TokenEntity
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.SendInteractor

class RegisterUser @Inject constructor(
    private val repository: TokenRepository
) : SendInteractor<RegisterUser.RegisterParams, TokenEntity> {

    override fun getSingle(registerParams: RegisterParams): Single<TokenEntity> {
        return repository.fetchTokenViaRegister(
            registerParams.username, registerParams.email,
            registerParams.password1, registerParams.password2
        ).andThen(repository.requestTokenSingle())
    }

    class RegisterParams(
        val username: String,
        val email: String,
        val password1: String,
        val password2: String
    )
}
