package uk.co.victoriajanedavis.chatapp.data.repositories

import javax.inject.Inject

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BasePublishSubjectSingularStore
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import uk.co.victoriajanedavis.chatapp.data.mappers.TokenNwSpMapper
import uk.co.victoriajanedavis.chatapp.data.mappers.TokenSpEntityMapper
import uk.co.victoriajanedavis.chatapp.data.model.sharedpref.TokenSpModel
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService
import uk.co.victoriajanedavis.chatapp.domain.entities.TokenEntity

@ApplicationScope
class TokenRepository @Inject constructor(
    private val tokenStore: BasePublishSubjectSingularStore<TokenSpModel>,
    private val chatService: ChatAppService,
    private val database: ChatAppDatabase
) {
    private val spEntityMapper = TokenSpEntityMapper()
    private val nwSpMapper = TokenNwSpMapper()


    fun tokenStream(): Observable<TokenEntity> {
        return tokenStore.getSingular()
            .map(spEntityMapper::mapFrom)
    }

    fun requestTokenSingle(): Single<TokenEntity> {
        return tokenStore.getSingular()
            .map(spEntityMapper::mapFrom)
            .firstOrError()
    }

    fun deleteTokenViaLogout(): Completable {
        return chatService.logout().onErrorResumeNext { _ -> Completable.complete() }
            .andThen(Completable.fromAction(database::clearAllTables))
            .andThen(deleteTokenFromStorage())
    }

    fun fetchTokenViaLoginAndStore(username: String, password: String): Completable {
        return chatService.login(username, password)
            .map(nwSpMapper::mapFrom)
            .flatMapCompletable(tokenStore::storeSingular)
    }

    fun fetchTokenViaRegisterAndStore(username: String, email: String,
                                      password1: String, password2: String): Completable {
        return chatService.register(username, email, password1, password2)
            .map(nwSpMapper::mapFrom)
            .flatMapCompletable(tokenStore::storeSingular)
    }

    fun deleteTokenFromStorage(): Completable {
        return tokenStore.clear()
    }
}
