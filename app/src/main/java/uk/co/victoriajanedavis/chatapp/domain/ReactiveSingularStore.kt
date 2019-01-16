package uk.co.victoriajanedavis.chatapp.domain

import io.reactivex.Completable
import io.reactivex.Observable

interface ReactiveSingularStore<Value> {

    fun storeSingular(value: Value) : Completable

    fun getSingular() : Observable<Value>

    fun clear() : Completable
}