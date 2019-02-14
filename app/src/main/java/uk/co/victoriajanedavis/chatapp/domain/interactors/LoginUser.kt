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

    // If POST of the Firebase registration token fails then whole login process fails
    override fun getActionCompletable(params: LoginParams): Completable {
        return backendTokenRepo.fetchTokenViaLoginAndStore(params.username, params.password)
            .andThen(firebaseTokenRepo.postTokenToBackend()
                .onErrorResumeNext(::deleteTokenFromStorageThenError)
            )
    }

    private fun deleteTokenFromStorageThenError(e: Throwable): Completable {
        return backendTokenRepo.deleteTokenFromStorage()
            .andThen { Completable.error(Exception("Login failed: ${e.message}")) }
    }

    class LoginParams(
        val username: String,
        val password: String
    )
}
