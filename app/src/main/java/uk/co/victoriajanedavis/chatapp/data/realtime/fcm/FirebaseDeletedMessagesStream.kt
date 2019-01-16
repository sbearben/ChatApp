package uk.co.victoriajanedavis.chatapp.data.realtime.fcm

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class FirebaseDeletedMessagesStream @Inject constructor() {

    private val stream : Subject<Boolean> = PublishSubject.create<Boolean>().toSerialized();

    internal fun push(newDeletedMessages: Boolean) {
        stream.onNext(newDeletedMessages)
    }

    fun get() : Observable<Boolean> {
        return stream.filter { b -> b }  // only want true values
    }
}