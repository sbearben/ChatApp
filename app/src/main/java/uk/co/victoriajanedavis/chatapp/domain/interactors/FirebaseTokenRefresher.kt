package uk.co.victoriajanedavis.chatapp.domain.interactors

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import uk.co.victoriajanedavis.chatapp.data.repositories.FirebaseTokenRepository
import uk.co.victoriajanedavis.chatapp.data.repositories.TokenRepository
import javax.inject.Inject

class FirebaseTokenRefresher @Inject constructor(
    private val firebaseTokenRepo: FirebaseTokenRepository,
    private val backendTokenRepo: TokenRepository
) : ReactiveInteractor.RefreshInteractor<Void> {

    override fun getRefreshSingle(params: Void?): Completable {
        return sendTokenToBackendIfLoggedIn()
    }

    fun storeToken(token: String) : Completable {
        return firebaseTokenRepo.storeToken(token)
    }

    private fun sendTokenToBackendIfLoggedIn() : Completable {
        return combineLatestFirebaseAndBackendTokenStreams()
            .flatMapCompletable { token -> firebaseTokenRepo.postTokenToBackend(token) }
    }

    private fun combineLatestFirebaseAndBackendTokenStreams() : Observable<String> {
        return Observable.combineLatest(
            getFirebaseToken().doOnNext { token ->  Log.d("FirebaseRepository", "firebase: $token") },
            backendTokenRepo.getTokenStream().filter { token -> !token.isEmpty }.doOnNext { token -> Log.d("FirebaseRepository", "backend: $token") },
            BiFunction { firebaseToken, backendToken ->
                Log.d("FirebaseRepository", "firebase: $firebaseToken, backend: ${backendToken.token}")
                firebaseToken
            })
    }

    private fun getFirebaseToken() : Observable<String> {
        return firebaseTokenRepo.getTokenStream()
            .flatMapSingle { token -> fetchWhenEmptyAndThenToken(token) }
    }

    private fun fetchWhenEmptyAndThenToken(token: String) : Single<String> {
        return fetchWhenEmpty(token).andThen(Single.just(token))
    }

    private fun fetchWhenEmpty(token: String) : Completable {
        return if (token.isEmpty()) {
            firebaseTokenRepo.fetchToken()
        } else {
            Completable.complete()
        }
    }
}