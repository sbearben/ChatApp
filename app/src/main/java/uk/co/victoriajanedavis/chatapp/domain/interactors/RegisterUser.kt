package uk.co.victoriajanedavis.chatapp.domain.interactors

import io.reactivex.Completable
import javax.inject.Inject

import uk.co.victoriajanedavis.chatapp.data.repositories.FirebaseTokenRepository
import uk.co.victoriajanedavis.chatapp.data.repositories.TokenRepository
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.ActionInteractor

class RegisterUser @Inject constructor(
    private val backendTokenRepo: TokenRepository,
    private val firebaseTokenRepo: FirebaseTokenRepository
) : ActionInteractor<RegisterUser.RegisterParams> {

    // If POST of the Firebase registration token fails then whole registration process fails
    override fun getActionCompletable(params: RegisterParams): Completable {
        return backendTokenRepo.fetchTokenViaRegisterAndStore(
            params.username, params.email,
            params.password1, params.password2
        ).andThen(firebaseTokenRepo.postTokenToBackend()
            .onErrorResumeNext(::deleteTokenFromStorageThenError)
        )
    }

    private fun deleteTokenFromStorageThenError(e: Throwable): Completable {
        return backendTokenRepo.deleteTokenFromStorage()
            .andThen {
                Completable.error(Exception("Account created, but something went wrong logging in: ${e.message}"))
            }
    }

    class RegisterParams(
        val username: String,
        val email: String,
        val password1: String,
        val password2: String
    )
}
