package uk.co.victoriajanedavis.chatapp.domain.interactors

import javax.inject.Inject

import io.reactivex.Single
import uk.co.victoriajanedavis.chatapp.data.repositories.FirebaseTokenRepository
import uk.co.victoriajanedavis.chatapp.data.repositories.TokenRepository
import uk.co.victoriajanedavis.chatapp.domain.entities.TokenEntity
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.DeleteInteractor

class LogoutUser @Inject constructor(
    private val backendTokenRepo: TokenRepository,
    private val firebaseTokenRepo: FirebaseTokenRepository
) : DeleteInteractor<Void, TokenEntity> {

    override fun getSingle(aVoid: Void?): Single<TokenEntity> {
        return backendTokenRepo.requestTokenSingle()
            .flatMap { tokenEntity -> firebaseTokenRepo.deleteTokenFromBackend()
                .andThen(backendTokenRepo.deleteTokenViaLogout())
                .andThen(Single.just(tokenEntity))
            }
    }
}
