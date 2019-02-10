package uk.co.victoriajanedavis.chatapp.domain.interactors

import javax.inject.Inject

import io.reactivex.Single
import uk.co.victoriajanedavis.chatapp.data.repositories.FirebaseTokenRepository
import uk.co.victoriajanedavis.chatapp.data.repositories.TokenRepository
import uk.co.victoriajanedavis.chatapp.domain.entities.TokenEntity
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.SendInteractor

class LoginUser @Inject constructor(
    private val backendTokenRepo: TokenRepository,
    private val firebaseTokenRepo: FirebaseTokenRepository
) : SendInteractor<LoginUser.LoginParams, TokenEntity> {

    override fun getSingle(loginParams: LoginParams): Single<TokenEntity> {
        return backendTokenRepo.fetchTokenViaLogin(loginParams.username, loginParams.password)
            .andThen(firebaseTokenRepo.requestTokenSingle())
            .flatMapCompletable(firebaseTokenRepo::postTokenToBackend)
            .andThen(backendTokenRepo.requestTokenSingle())
    }

    class LoginParams(
        val username: String,
        val password: String
    )
}
