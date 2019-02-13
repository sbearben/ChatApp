package uk.co.victoriajanedavis.chatapp.domain.interactors

import io.reactivex.Completable
import javax.inject.Inject

import uk.co.victoriajanedavis.chatapp.data.repositories.FirebaseTokenRepository
import uk.co.victoriajanedavis.chatapp.data.repositories.TokenRepository
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.ActionInteractor

class LoginUser @Inject constructor(
    private val backendTokenRepo: TokenRepository,
    private val firebaseTokenRepo: FirebaseTokenRepository
) : ActionInteractor<LoginUser.LoginParams> {

    // Make the POST of the Firebase registration token first so that if that fails whole login process fails
    override fun getActionCompletable(params: LoginParams): Completable {
        return firebaseTokenRepo.requestTokenSingle()
            .flatMapCompletable(firebaseTokenRepo::postTokenToBackend)
            .andThen(backendTokenRepo.fetchTokenViaLogin(params.username, params.password))
    }

    class LoginParams(
        val username: String,
        val password: String
    )
}
