package uk.co.victoriajanedavis.chatapp.domain.interactors

import android.util.Log
import javax.inject.Inject

import io.reactivex.Observable
import uk.co.victoriajanedavis.chatapp.data.repositories.TokenRepository
import uk.co.victoriajanedavis.chatapp.domain.entities.TokenEntityHolder
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope

@ApplicationScope
class IsUserLoggedIn @Inject constructor(
    private val repository: TokenRepository,
    private val holder: TokenEntityHolder
) : ReactiveInteractor.RetrieveInteractor<Void, Boolean> {

    override fun getBehaviorStream(params: Void?): Observable<Boolean> {
        return repository.tokenStream()
            .doOnNext { token -> holder.tokenEntity = token } // TODO: I don't love this way of setting the TokenHolder
            .map { token -> !token.isEmpty }
    }
}
