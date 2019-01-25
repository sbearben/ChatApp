package uk.co.victoriajanedavis.chatapp.presentation.ext

import io.reactivex.Observable
import io.reactivex.Single
import uk.co.victoriajanedavis.chatapp.presentation.common.StreamState
import uk.co.victoriajanedavis.chatapp.presentation.common.StreamState.*

fun <T> Observable<T>.toStreamState() : Observable<StreamState<T>> {
    return this.map { emission -> OnNext(emission) as StreamState<T> }
        .onErrorResumeNext { throwable: Throwable ->
            Observable.just(OnError(throwable) as StreamState<T>)
        }
}

fun <T> Single<T>.toStreamState() : Single<StreamState<T>> {
    return this.map { emission -> OnNext(emission) as StreamState<T> }
        .onErrorResumeNext { throwable: Throwable ->
            Single.just(OnError(throwable) as StreamState<T>)
        }
}

fun <T> Single<T>.doOnErrorOrDispose(action: () -> Unit) : Single<T> {
    return this.doOnError { _ -> action.invoke() }
        .doOnDispose { action.invoke() }
}