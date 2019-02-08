package uk.co.victoriajanedavis.chatapp.data.realtime.fcm

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import uk.co.victoriajanedavis.chatapp.data.model.websocket.*
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class FirebaseMessagingStreams @Inject constructor() {

    private val stream : Subject<RealtimeModel> = PublishSubject.create<RealtimeModel>().toSerialized()
    private val backpressureStrategy = BackpressureStrategy.BUFFER


    internal fun push(model : RealtimeModel) {
        stream.onNext(model)
    }

    fun chatMessageStream() : Flowable<MessageWsModel> {
        return stream.ofType(MessageWsModel::class.java)
            .toFlowable(backpressureStrategy)
    }

    fun acceptedFriendRequestStream() : Flowable<AcceptedFriendRequestWsModel> {
        return stream.ofType(AcceptedFriendRequestWsModel::class.java)
            .toFlowable(backpressureStrategy)
    }

    fun canceledFriendRequestStream() : Flowable<CanceledFriendRequestWsModel> {
        return stream.ofType(CanceledFriendRequestWsModel::class.java)
            .toFlowable(backpressureStrategy)
    }

    fun createdFriendRequestStream() : Flowable<CreatedFriendRequestWsModel> {
        return stream.ofType(CreatedFriendRequestWsModel::class.java)
            .toFlowable(backpressureStrategy)
    }

    fun rejectedFriendRequestStream() : Flowable<RejectedFriendRequestWsModel> {
        return stream.ofType(RejectedFriendRequestWsModel::class.java)
            .toFlowable(backpressureStrategy)
    }
}