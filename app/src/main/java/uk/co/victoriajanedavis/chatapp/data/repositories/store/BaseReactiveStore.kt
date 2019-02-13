package uk.co.victoriajanedavis.chatapp.data.repositories.store

import java.util.UUID

import javax.inject.Inject

import io.reactivex.Completable
import io.reactivex.Observable
import uk.co.victoriajanedavis.chatapp.domain.Cache.DiskCache
import uk.co.victoriajanedavis.chatapp.domain.ReactiveStore

open class BaseReactiveStore<Value> @Inject constructor(
    private val cache: DiskCache<UUID, Value>
) : ReactiveStore<UUID, Value> {

    override fun storeSingular(value: Value): Completable {
        return Completable.fromRunnable { cache.putSingular(value) }
    }

    override fun storeAll(valuesList: List<Value>): Completable {
        return Completable.fromRunnable { cache.putAll(valuesList) }
    }

    override fun replaceAll(key: UUID?, valuesList: List<Value>): Completable {
        return Completable.fromRunnable { cache.replaceAll(key, valuesList) }
    }

    override fun delete(value: Value): Completable {
        return Completable.fromRunnable { cache.delete(value) }
    }

    override fun getSingular(key: UUID?): Observable<Value> {
        return cache.getSingular(key)
    }

    override fun getAll(key: UUID?): Observable<List<Value>> {
        return cache.getAll(key)
    }
}
