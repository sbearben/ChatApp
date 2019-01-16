package uk.co.victoriajanedavis.chatapp.data.realtime.fcm

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class FirebaseTokenStream @Inject constructor() {

    private val stream : Subject<String> = BehaviorSubject.create<String>().toSerialized();

    internal fun push(token: String) {
        stream.onNext(token)
    }

    fun get() : Observable<String> {
        return stream.defaultIfEmpty("")
    }
}