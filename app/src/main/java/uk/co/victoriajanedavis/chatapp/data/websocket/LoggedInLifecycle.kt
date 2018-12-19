package uk.co.victoriajanedavis.chatapp.data.websocket

import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.LifecycleState
import com.tinder.scarlet.lifecycle.LifecycleRegistry
import io.reactivex.BackpressureStrategy
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.domain.interactors.IsUserLoggedIn

class LoggedInLifecycle(
    private val isUserLoggedIn: IsUserLoggedIn,
    private val lifecycleRegistry: LifecycleRegistry
) : Lifecycle by lifecycleRegistry {

    constructor(isUserLoggedIn: IsUserLoggedIn) : this(isUserLoggedIn,
        LifecycleRegistry()
    )

    init {
        subscribeToIsUserLoggedInStream()
    }

    private fun subscribeToIsUserLoggedInStream() {
        return isUserLoggedIn.getBehaviorStream(null)
            .toFlowable(BackpressureStrategy.LATEST)
            .map(::toLifecycleState)
            .subscribeOn(Schedulers.io())
            .subscribe(lifecycleRegistry)
    }

    private fun toLifecycleState(isLoggedIn: Boolean): LifecycleState = when (isLoggedIn) {
        true -> LifecycleState.Started
        false -> LifecycleState.Stopped
    }
}