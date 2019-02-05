package uk.co.victoriajanedavis.chatapp

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import uk.co.victoriajanedavis.chatapp.injection.component.ApplicationComponent
import uk.co.victoriajanedavis.chatapp.injection.component.DaggerApplicationComponent
import javax.inject.Inject
import android.content.Intent



class ChatApp : Application(), HasActivityInjector, HasServiceInjector {

    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    @Inject lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

    lateinit var appComponent: ApplicationComponent
        private set

    lateinit var refWatcher: RefWatcher


    override fun activityInjector() = dispatchingAndroidInjector

    override fun serviceInjector() = dispatchingServiceInjector

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

        registerConnectivityNetworkMonitor()
    }

    private fun createAppComponent() {
        appComponent = DaggerApplicationComponent.builder()
                .application(this)
                .build()
    }

    private fun registerConnectivityNetworkMonitor() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(),
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    sendBroadcast(getConnectivityIntent(false), BuildConfig.APP_PRIVATE_PREMISSION)
                }

                override fun onLost(network: Network) {
                    sendBroadcast(getConnectivityIntent(true), BuildConfig.APP_PRIVATE_PREMISSION)
                }
            })
    }

    private fun getConnectivityIntent(noConnection: Boolean): Intent {
        Intent().apply {
            action = ACTION_CONNECTIVITY_CHANGE
            putExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, noConnection)
            return this
        }
    }

    fun mustDie(`object`: Any) {
        refWatcher.watch(`object`)
    }

    companion object {
        const val ACTION_CONNECTIVITY_CHANGE = "uk.co.victoriajanedavis.chatapp.ACTION_CONNECTIVITY_CHANGE"
    }
}