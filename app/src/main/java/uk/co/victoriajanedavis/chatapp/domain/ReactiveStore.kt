package uk.co.victoriajanedavis.chatapp.domain


import io.reactivex.Completable
import io.reactivex.Observable

/**
 * Interface for any kind of reactive store.
 */
interface ReactiveStore<Key, Value> {

    fun storeSingular(value: Value): Completable

    fun storeAll(valuesList: List<Value>): Completable

    fun replaceAll(key: Key?, valuesList: List<Value>): Completable

    fun delete(value: Value): Completable

    fun getSingular(key: Key?): Observable<Value>

    fun getAll(key: Key?): Observable<List<Value>>
}
