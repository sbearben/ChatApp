package uk.co.victoriajanedavis.chatapp.data.store

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import uk.co.victoriajanedavis.chatapp.data.cache.BaseMemoryCache
import uk.co.victoriajanedavis.chatapp.domain.Cache
import uk.co.victoriajanedavis.chatapp.domain.ReactiveStore
import java.util.HashMap

class BaseMemoryReactiveStore<Key, Value>(
    private val extractKeyFromValue: (Value) -> Key,
    private val extractParentKeyFromValue: ((Value) -> Key)? = null
) : ReactiveStore<Key, Value> {

    private val cache: Cache.MemoryCache<Key, Value> = BaseMemoryCache(
        extractKeyFromValue,
        extractParentKeyFromValue
    )

    private val allSubject: Subject<List<Value>> = PublishSubject.create<List<Value>>().toSerialized()

    private val subjectMap = HashMap<Key, Subject<Value>>()


    override fun storeSingular(value: Value): Completable {
        return Completable.fromRunnable { cache.putSingular(value) }
            .andThen { Completable.fromRunnable {
                getOrCreateSubjectForKey(extractKeyFromValue.invoke(value)).onNext(value)
            } }
            .andThen(cache.getAll(null))
            .flatMapCompletable { all -> Completable.fromRunnable { allSubject.onNext(all) } }
    }

    override fun storeAll(valuesList: List<Value>): Completable {
        return Completable.fromRunnable { cache.putAll(valuesList) }
            .andThen(cache.getAll(null))
            .flatMapCompletable { all -> Completable.fromRunnable { allSubject.onNext(all) } }
            .andThen { Completable.fromRunnable { publishInEachKey() } }
    }

    override fun replaceAll(key: Key?, valuesList: List<Value>): Completable {
        return Completable.fromRunnable { cache.replaceAll(key, valuesList) }
            .andThen(cache.getAll(null))
            .flatMapCompletable { all -> Completable.fromRunnable { allSubject.onNext(all) } }
            .andThen { Completable.fromRunnable { publishInEachKey() } }
    }

    override fun delete(value: Value): Completable {
        return Completable.fromRunnable { cache.delete(value) }
            .andThen(cache.getAll(null))
            .flatMapCompletable { all -> Completable.fromRunnable { allSubject.onNext(all) } }
    }

    override fun getSingular(key: Key): Observable<Value> {
        return Observable.defer { getOrCreateSubjectForKey(key).startWith(getValue(key)) }
    }

    override fun getAll(key: Key?): Observable<List<Value>> {
        return Observable.defer { allSubject.startWith(getAllValues(key)) }
    }

    private fun getOrCreateSubjectForKey(key: Key): Subject<Value> {
        synchronized(subjectMap) {
            return subjectMap[key] ?: createAndStoreNewSubjectForKey(key)
        }
    }

    private fun createAndStoreNewSubjectForKey(key: Key): Subject<Value> {
        val processor = PublishSubject.create<Value>().toSerialized()
        synchronized(subjectMap) {
            subjectMap.put(key, processor)
        }
        return processor
    }

    private fun getValue(key: Key): Value {
        return cache.getSingular(key).blockingFirst()
    }

    private fun getAllValues(key: Key?): List<Value> {
        return cache.getAll(key).blockingFirst()
    }

    /**
     * Publishes the cached data in each independent stream only if it exists already.
     */
    private fun publishInEachKey() {
        val keySet: Set<Key>
        synchronized(subjectMap) {
            keySet = HashSet(subjectMap.keys)
        }
        for (key in keySet) {
            val value = cache.getSingular(key).blockingFirst()
            publishInKey(key, value)
        }
    }

    /**
     * Publishes the cached value if there is an already existing stream for the passed key. The case where there isn't a stream for the passed key
     * means that the data for this key is not being consumed and therefore there is no need to publish.
     */
    private fun publishInKey(key: Key, value: Value) {
        val processor: Subject<Value>?
        synchronized(subjectMap) {
            processor = subjectMap[key]
        }
        processor?.onNext(value)
    }
}