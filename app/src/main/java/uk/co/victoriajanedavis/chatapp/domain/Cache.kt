package uk.co.victoriajanedavis.chatapp.domain

import io.reactivex.Observable

/**
 * Interface for any type of store. Don't implement this directly,
 * use [MemoryCache] or [DiskCache] so it is more
 * descriptive.
 */
interface Cache<Key, Value> {

    fun putSingular(value: Value)

    fun putAll(valuesList: List<Value>)

    fun replaceAll(key: Key?, valuesList: List<Value>)

    fun delete(value: Value)

    fun clear()

    fun getSingular(key: Key?): Observable<Value>

    fun getAll(key: Key?): Observable<List<Value>>

    /**
     * More descriptive interface for memory based stores.
     */
    interface MemoryCache<Key, Value> : Cache<Key, Value>

    /**
     * More descriptive interface for disk based stores.
     */
    interface DiskCache<Key, Value> : Cache<Key, Value>
}
