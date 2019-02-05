package uk.co.victoriajanedavis.chatapp.domain.interactors;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 Interfaces for Interactors. This interfaces represent use cases (this means any use case in the application should implement this contract).
 */
public interface ReactiveInteractor {

    /**
     Sends changes to data layer.
     It returns a {@link Single} that will emit the result of the send operation.

     @param <Result> the type of the send operation result.
     @param <Params> required parameters for the send.
     */
    interface SendInteractor<Params, Result> extends ReactiveInteractor {

        @NonNull
        Single<Result> getSingle(@NonNull final Params params);
    }

    /**
     Sends changes to data layer but ignores the result.
     It returns a {@link Completable} that tells whether the action completed or not.

     @param <Params> required parameters for the action.
     */
    interface ActionInteractor<Params> extends ReactiveInteractor {

        @NonNull
        Completable getActionCompletable(@NonNull final Params params);
    }

    /**
     Retrieves changes from the data layer.
     It returns an {@link Flowable} that emits updates for the retrieved object. The returned {@link Flowable} will never complete, but it can
     error if there are any problems performing the required actions to serve the data.

     @param <Object> the type of the retrieved object.
     @param <Params> required parameters for the retrieve operation.
     */
    interface RetrieveInteractor<Params, Object> extends ReactiveInteractor {

        @NonNull
        Observable<Object> getBehaviorStream(@Nullable final Params params);
    }

    /**
     The request interactor is used to request some result once. The returned observable is a single, emits once and then completes or errors.

     @param <Params> the type of the returned data.
     @param <Result> required parameters for the request.
     */
    interface RequestInteractor<Params, Result> extends ReactiveInteractor {

        @NonNull
        Single<Result> getSingle(@Nullable final Params params);
    }

    /**
     The delete interactor is used to delete entities from data layer. The response for the delete operation comes as onNext
     event in the returned observable.

     @param <Result> the type of the delete response.
     @param <Params>   required parameters for the delete.
     */
    interface DeleteInteractor<Params, Result> extends ReactiveInteractor {

        @NonNull
        Single<Result> getSingle(@Nullable final Params params);
    }

    /**
     The refreshItems interactor is used to refreshItems the reactive store with new data. Typically calling this interactor will trigger events in its
     get interactor counterpart. The returned observable will complete when the refreshItems is finished or error if there was any problem in the process.

     @param <Params> required parameters for the refreshItems.
     */
    interface RefreshInteractor<Params> extends ReactiveInteractor {

        @NonNull
        Completable getRefreshSingle(@Nullable final Params params);
    }

    interface PaginatedRetrieveInteractor<Params, Result> extends RetrieveInteractor<Params, Result> {

        @NonNull
        Completable fetchMoreItems(@Nullable final Params params);
    }
}

