package uk.co.victoriajanedavis.chatapp.data.realtime.fcm

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class FirebaseMessageDataStream @Inject constructor() {

    private val stream : Subject<Map<String, String>> = PublishSubject.create<Map<String, String>>().toSerialized()
    private val backpressureStrategy = BackpressureStrategy.BUFFER

    fun push(jsonMap: Map<String, String>) {
        stream.onNext(jsonMap)
    }

    fun getStream(): Flowable<Map<String, String>> {
        return stream.toFlowable(backpressureStrategy)
    }
}