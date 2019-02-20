package uk.co.victoriajanedavis.chatapp.presentation.common.util

import io.reactivex.disposables.Disposable

fun dispose(disposable: Disposable?) {
    if (disposable != null && !disposable.isDisposed) {
        disposable.dispose()
    }
}
