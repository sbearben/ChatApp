package uk.co.victoriajanedavis.chatapp.domain.common

import io.reactivex.Observable
import io.reactivex.Single
import uk.co.victoriajanedavis.chatapp.domain.common.StreamState.*

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


fun <T : Any, R : Any> Observable<List<T>>.mapList(block: (T) -> R) : Observable<List<R>> {
    return this.map { list -> list.map(block) }
}


fun <T : Any, R : Any> Single<List<T>>.mapList(block: (T) -> R) : Single<List<R>> {
    return this.map { list -> list.map(block) }
}
