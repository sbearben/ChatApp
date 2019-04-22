package uk.co.victoriajanedavis.chatapp.data.cache

import io.reactivex.Observable
import uk.co.victoriajanedavis.chatapp.domain.SingularCache

abstract class BaseSingularMemoryCache<Value> : SingularCache.SingularMemoryCache<Value> {

    private var value: Value? = null


    override fun putSingular(value: Value) {
        this.value = value
    }

    override fun getSingular(): Observable<Value> {
        if(value == null) value = createDefaultValue()
        return Observable.fromCallable { value }
    }

    override fun clear() {
        value = null
    }

    abstract fun createDefaultValue(): Value
}