package uk.co.victoriajanedavis.chatapp.data.repositories.store

import javax.inject.Inject

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.annotations.NonNull
import io.reactivex.annotations.Nullable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import uk.co.victoriajanedavis.chatapp.data.common.MethodInvalidForImplementationError
import uk.co.victoriajanedavis.chatapp.data.model.sharedpref.TokenSpModel
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.TokenCache
import uk.co.victoriajanedavis.chatapp.domain.Cache.DiskCache
import uk.co.victoriajanedavis.chatapp.domain.ReactiveSingularStore
import uk.co.victoriajanedavis.chatapp.domain.ReactiveStore

class TokenReactiveStore @Inject constructor(
    private val cache: TokenCache
) : ReactiveSingularStore<TokenSpModel> {

    private val singleSubject: Subject<TokenSpModel> =
        PublishSubject.create<TokenSpModel>().toSerialized()


    override fun storeSingular(value: TokenSpModel): Completable {
        return Completable.fromRunnable { cache.putSingular(value) }
            .andThen(Completable.fromRunnable { singleSubject.onNext(value) })
    }

    override fun getSingular(): Observable<TokenSpModel> {
        return Observable.defer { singleSubject.startWith(cache.getSingular().blockingFirst()) }
    }

    override fun clear(): Completable {
        return Completable.fromRunnable { cache.clear() }.andThen(cache.getSingular())
            .flatMapCompletable { tokenEntity -> Completable.fromRunnable { singleSubject.onNext(tokenEntity) } }
    }
}
