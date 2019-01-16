package uk.co.victoriajanedavis.chatapp.data.repositories.store

import javax.inject.Inject

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import uk.co.victoriajanedavis.chatapp.data.model.sharedpref.FirebaseTokenSpModel
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.FirebaseTokenCache
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.TokenCache
import uk.co.victoriajanedavis.chatapp.domain.ReactiveSingularStore

class FirebaseTokenReactiveStore @Inject constructor(
    private val cache: FirebaseTokenCache
) : ReactiveSingularStore<FirebaseTokenSpModel> {

    private val singleSubject: Subject<FirebaseTokenSpModel> =
        PublishSubject.create<FirebaseTokenSpModel>().toSerialized()


    override fun storeSingular(value: FirebaseTokenSpModel): Completable {
        return Completable.fromRunnable { cache.putSingular(value) }
            .andThen(Completable.fromRunnable { singleSubject.onNext(value) })
    }

    override fun getSingular(): Observable<FirebaseTokenSpModel> {
        return Observable.defer { singleSubject.startWith(cache.getSingular().blockingFirst()) }
    }

    override fun clear(): Completable {
        return Completable.fromRunnable { cache.clear() }.andThen(cache.getSingular())
            .flatMapCompletable { tokenEntity -> Completable.fromRunnable { singleSubject.onNext(tokenEntity) } }
    }
}
