package uk.co.victoriajanedavis.chatapp.presentation.ui.main

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.domain.interactors.FullSync
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SyncService : Service() {

    @Inject lateinit var fullSync: FullSync
    private val disposables = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this);
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("SyncService", "onStartCommand() called")
        disposables.apply {
            clear()
            add(fullSync.getActionCompletable(0)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { stopSelf() },
                    { _ -> stopSelf() }
                )
            )
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SyncService::class.java)
        }
    }
}