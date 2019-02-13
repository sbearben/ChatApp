package uk.co.victoriajanedavis.chatapp.domain.common

import java.util.ArrayList

import io.reactivex.Observable
import io.reactivex.annotations.NonNull

abstract class Mapper<E, T> {

    abstract fun mapFrom(from: E): T

    fun observable(from: E): Observable<T> {
        return Observable.fromCallable { mapFrom(from) }
    }

    fun observable(from: List<E>): Observable<List<T>> {
        return Observable.fromCallable { from.map { mapFrom(it) } }
    }
}
