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
            .flatMapCompletable(firebaseTokenRepo::postTokenToBackend)
    }

    private fun combineLatestFirebaseAndBackendTokenStreams() : Observable<String> {
        return Observable.combineLatest(
            getFirebaseToken().doOnNext { token ->  Log.d("FirebaseRepository1", "firebase: $token") },
            backendTokenRepo.tokenStream().filter { token -> !token.isEmpty }.doOnNext { token -> Log.d("FirebaseRepository2", "backend: $token") },
            BiFunction { firebaseToken, backendToken ->
                Log.d("FirebaseRepository3", "firebase: $firebaseToken, backend: ${backendToken.token}")
                return@BiFunction firebaseToken
            })
    }

    private fun getFirebaseToken() : Observable<String> {
        return firebaseTokenRepo.getTokenStream()
            .doOnNext { token -> Log.d("FirebaseRepository5", "getTokenStream.doOnNext: $token") }
            .switchMapSingle { token -> fetchWhenEmptyAndThenToken(token) }
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