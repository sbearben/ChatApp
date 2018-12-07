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

    //private var disposable: Disposable

    init {
        subscribeToIsUserLoggedInStream()
        //application.registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks())
    }

    private fun subscribeToIsUserLoggedInStream() {
        return isUserLoggedIn.getBehaviorStream(null)
            .toFlowable(BackpressureStrategy.LATEST)
            .map(::toLifecycleState)
            .subscribeOn(Schedulers.io())
            .subscribe(lifecycleRegistry)
        /*isUserLoggedIn.getBehaviorStream(null)
            .subscribe(object : ResourceObserver<Boolean>() {
                override fun onComplete() {}

                override fun onNext(isLoggedIn: Boolean) {
                    lifecycleRegistry.onNext(toLifecycleState(isLoggedIn))
                }

                override fun onError(e: Throwable) {}
            })
        */
    }

    private fun toLifecycleState(isLoggedIn: Boolean): LifecycleState = when (isLoggedIn) {
        true -> LifecycleState.Started
        false -> LifecycleState.Stopped
    }

    /*
    private inner class ActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity?) {
            if (!(activity?.isChangingConfigurations ?: false)) {
                disposable.dispose()
            }
        }

        override fun onActivityResumed(activity: Activity?)  {
            if (disposable.isDisposed) {
                disposable = subscribeToIsUserLoggedInStream()
            }
        }

        override fun onActivityStarted(activity: Activity?) {}

        override fun onActivityDestroyed(activity: Activity?) {}

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

        override fun onActivityStopped(activity: Activity?) {}

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {}
    }
    */
}