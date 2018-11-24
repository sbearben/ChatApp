package uk.co.victoriajanedavis.chatapp

import android.app.Activity
import android.app.Application
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import uk.co.victoriajanedavis.chatapp.injection.component.ApplicationComponent
import uk.co.victoriajanedavis.chatapp.injection.component.DaggerApplicationComponent
import javax.inject.Inject

class ChatApp : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    lateinit var appComponent: ApplicationComponent
        private set

    lateinit var refWatcher: RefWatcher


    override fun activityInjector() = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()
        createAppComponent()
        appComponent.inject(this)

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        refWatcher = LeakCanary.install(this)
    }

    private fun createAppComponent() {
        appComponent = DaggerApplicationComponent.builder()
                .application(this)
                .build()
    }

    fun mustDie(`object`: Any) {
        refWatcher.watch(`object`)
    }
}