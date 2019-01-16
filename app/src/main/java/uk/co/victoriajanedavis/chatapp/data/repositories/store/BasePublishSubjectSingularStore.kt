package uk.co.victoriajanedavis.chatapp.data.repositories.store

import javax.inject.Inject

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import uk.co.victoriajanedavis.chatapp.domain.ReactiveSingularStore
import uk.co.victoriajanedavis.chatapp.domain.SingularCache

class BasePublishSubjectSingularStore<Value> @Inject constructor(
    private val cache: SingularCache<Value>
) : ReactiveSingularStore<Value> {

    private val singleSubject: Subject<Value> = PublishSubject.create<Value>().toSerialized()


    override fun storeSingular(value: Value): Completable {
        return Completable.fromRunnable { cache.putSingular(value) }
            .andThen(Completable.fromRunnable { singleSubject.onNext(value) })
    }

    override fun getSingular(): Observable<Value> {
        return Observable.defer { singleSubject.startWith(cache.getSingular().blockingFirst()) }
    }

    override fun clear(): Completable {
        return Completable.fromRunnable { cache.clear() }.andThen(cache.getSingular())
            .flatMapCompletable { tokenEntity -> Completable.fromRunnable { singleSubject.onNext(tokenEntity) } }
    }
}
