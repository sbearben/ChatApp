package uk.co.victoriajanedavis.chatapp.domain

import io.reactivex.Observable

interface SingularCache<Value> {

    fun putSingular(value: Value)

    fun getSingular() : Observable<Value>

    fun clear()

    interface SingularMemoryCache<Value> : SingularCache<Value>

    interface SingularDiskCache<Value> : SingularCache<Value>
}