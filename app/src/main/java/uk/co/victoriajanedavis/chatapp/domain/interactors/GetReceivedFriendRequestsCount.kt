package uk.co.victoriajanedavis.chatapp.domain.interactors

import io.reactivex.Observable
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.RetrieveInteractor
import uk.co.victoriajanedavis.chatapp.domain.common.StreamState
import uk.co.victoriajanedavis.chatapp.domain.common.StreamState.*

import javax.inject.Inject


class GetReceivedFriendRequestsCount @Inject constructor(
    private val requestsList: GetReceivedFriendRequestsList
) : RetrieveInteractor<Void, StreamState<Int>> {

    override fun getBehaviorStream(params: Void?): Observable<StreamState<Int>> {
        return requestsList.getBehaviorStream(null)
            .map { streamState -> when(streamState) {
                is OnNext -> OnNext(streamState.content.size)
                is OnError -> OnError(streamState.throwable)
            }}
    }
}
