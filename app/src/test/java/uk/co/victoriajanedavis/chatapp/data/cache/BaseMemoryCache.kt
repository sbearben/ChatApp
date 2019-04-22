package uk.co.victoriajanedavis.chatapp.data.cache

import io.reactivex.Observable
import uk.co.victoriajanedavis.chatapp.domain.Cache
import java.util.concurrent.ConcurrentHashMap

class BaseMemoryCache<Key, Value>(
    private val extractKeyFromValue: (Value) -> Key,
    private val extractParentKeyFromValue: ((Value) -> Key)? = null
) : Cache.MemoryCache<Key, Value> {

    private val cache = ConcurrentHashMap<Key, Value>()


    override fun putSingular(value: Value) {
        cache[extractKeyFromValue.invoke(value)] = value
    }

    override fun putAll(valuesList: List<Value>) {
        cache.putAll(valuesList.associateBy(extractKeyFromValue))
    }

    override fun replaceAll(key: Key?, valuesList: List<Value>) {
        if(key != null && extractParentKeyFromValue != null) {
            val oldValues = cache.values.toList().filter { value -> extractParentKeyFromValue.invoke(value) != key }
            clear()
            putAll(oldValues)
            putAll(valuesList)
        }
        else {
            clear()
            putAll(valuesList)
        }
    }

    override fun delete(value: Value) {
        cache.remove(extractKeyFromValue.invoke(value))
    }

    override fun clear() {
        cache.clear()
    }

    override fun getSingular(key: Key): Observable<Value> {
        return Observable.fromCallable { cache.containsKey(key) }
            .filter { containsKey -> containsKey }
            .map { cache[key] }
    }

    override fun getAll(key: Key?): Observable<List<Value>> {
        return if(key != null && extractParentKeyFromValue != null) {
            Observable.fromCallable{ cache.values }
                .map { values -> values.filter { value -> extractParentKeyFromValue.invoke(value) == key } }
                //.filter { value -> extractParentKeyFromValue.invoke(value) == key }
                //.toList()
                //.toObservable()
        }
        else {
            Observable.fromCallable { cache.values.toList() }

        }
    }
}