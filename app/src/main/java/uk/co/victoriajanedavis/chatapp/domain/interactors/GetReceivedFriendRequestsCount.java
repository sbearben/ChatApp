package uk.co.victoriajanedavis.chatapp.domain.interactors;

import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.RetrieveInteractor;

import javax.inject.Inject;
import java.util.List;


public class GetReceivedFriendRequestsCount implements RetrieveInteractor<Void, Integer> {

    @NonNull
    private final GetReceivedFriendRequestsList requestsList;

    @Inject
    public GetReceivedFriendRequestsCount(@NonNull final GetReceivedFriendRequestsList requestsList) {
        this.requestsList = requestsList;
    }

    @NonNull
    @Override
    public Observable<Integer> getBehaviorStream(@NonNull final Void params) {
        return requestsList.getBehaviorStream(params)
                .map(List::size);
    }
}
